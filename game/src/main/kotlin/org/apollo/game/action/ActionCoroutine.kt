package org.apollo.game.action

import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.CancellationException
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

typealias ActionPredicate = () -> Boolean
typealias ActionBlock = suspend ActionCoroutine.() -> Unit

interface ActionCoroutineCondition {
    /**
     * Called once every tick to check if `Continuation` associated with this condition should be resumed.
     */
    fun resume(): Boolean
}

/**
 * A continuation condition that waits on a given number of pulses.
 */
class AwaitPulses(pulses: Int) : ActionCoroutineCondition {
    val remainingPulses: AtomicInteger = AtomicInteger(pulses)

    override fun resume(): Boolean {
        return remainingPulses.decrementAndGet() <= 0
    }
}

/**
 * A continuation condition that waits until a predicate is fufilled.
 */
class AwaitPredicate(val predicate: ActionPredicate) : ActionCoroutineCondition {
    override fun resume(): Boolean {
        return predicate.invoke()
    }
}

/**
 * A suspend point in an `ActionCoroutine` that has its `continuation` resumed whenever the `condition` evaluates
 * to `true`.
 */
data class ActionCoroutineStep(val condition: ActionCoroutineCondition, internal val continuation: Continuation<Unit>)

@RestrictsSuspension
class ActionCoroutine : Continuation<Unit> {
    companion object {
        /**
         * Create a new `ActionCoroutine` and immediately execute the given `block`, returning a continuation that
         * can be resumed.
         */
        fun start(block: ActionBlock): ActionCoroutine {
            val coroutine = ActionCoroutine()
            val continuation = block.createCoroutine(coroutine, coroutine)

            coroutine.resumeContinuation(continuation)

            return coroutine
        }
    }

    override val context: CoroutineContext = EmptyCoroutineContext
    override fun resumeWith(result: Result<Unit>) {
        if (result.isFailure) {
            throw result.exceptionOrNull()!!
        }
    }

    private fun resumeContinuation(continuation: Continuation<Unit>, allowCancellation: Boolean = true) {
        try {
            continuation.resume(Unit)
        } catch (ex: CancellationException) {
            if (!allowCancellation) {
                throw ex
            }
        }
    }

    /**
     * The next `step` in this `ActionCoroutine` saved as a resume point.
     */
    private var next = AtomicReference<ActionCoroutineStep>()

    /**
     * Check if this continuation has no more steps to execute.
     */
    fun stopped(): Boolean {
        return next.get() == null
    }

    /**
     * Update this continuation and check if the condition for the next step to be resumed is satisfied.
     */
    fun pulse() {
        val nextStep = next.getAndSet(null) ?: return

        val condition = nextStep.condition
        val continuation = nextStep.continuation

        if (condition.resume()) {
            resumeContinuation(continuation)
        } else {
            next.compareAndSet(null, nextStep)
        }
    }

    private suspend fun awaitCondition(condition: ActionCoroutineCondition) {
        return suspendCoroutineUninterceptedOrReturn { cont ->
            next.compareAndSet(null, ActionCoroutineStep(condition, cont))
            COROUTINE_SUSPENDED
        }
    }

    /**
     * Stop execution of this continuation.
     */
    suspend fun stop(): Nothing {
        suspendCancellableCoroutine<Unit> { cont ->
            next.set(null)
            cont.cancel()
        }

        error("Tried to resume execution a coroutine that should have been cancelled.")
    }

    /**
     * Wait `pulses` game updates before resuming this continuation.
     */
    suspend fun wait(pulses: Int = 1) = awaitCondition(AwaitPulses(pulses))

    /**
     * Wait until the `predicate` returns `true` before resuming this continuation.
     */
    suspend fun wait(predicate: ActionPredicate) = awaitCondition(AwaitPredicate(predicate))
}