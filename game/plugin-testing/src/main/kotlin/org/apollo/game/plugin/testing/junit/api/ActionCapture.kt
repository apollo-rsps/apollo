package org.apollo.game.plugin.testing.junit.api

import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf
import org.apollo.game.action.Action
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class ActionCapture(val type: KClass<out Action<*>>) {
    private var action: Action<*>? = null
    private val callbacks = mutableListOf<ActionCaptureCallback>()
    private var lastTicks: Int = 0

    fun capture(captured: Action<*>) {
        assertTrue(type.isSuperclassOf(captured::class)) {
            "${captured::class.simpleName} is not an instance of ${type.simpleName}"
        }

        this.action = captured
    }

    private fun callback(delay: ActionCaptureDelay): ActionCaptureCallbackRegistration {
        val registration = ActionCaptureCallbackRegistration()
        val callback = ActionCaptureCallback(delay, registration)

        callbacks.add(callback)
        return registration
    }

    fun runAction(timeout: Int = 50) {
        action?.let {
            var pulses = 0

            do {
                it.pulse()
                pulses++

                val tickCallbacks = callbacks.filter { it.delay == ActionCaptureDelay.Ticks(pulses) }
                tickCallbacks.forEach { it.invoke() }

                callbacks.removeAll(tickCallbacks)
            } while (it.isRunning && pulses < timeout)

            val completionCallbacks = callbacks.filter { it.delay == ActionCaptureDelay.Completed }
            completionCallbacks.forEach { it.invoke() }

            callbacks.removeAll(completionCallbacks)
        }

        assertEquals(0, callbacks.size, {
            "untriggered callbacks:\n" + callbacks
                .map {
                    val delayDescription = when (it.delay) {
                        is ActionCaptureDelay.Ticks -> "${it.delay.count} ticks"
                        is ActionCaptureDelay.Completed -> "action completion"
                    }

                    "$delayDescription (${it.callbackRegistration.description ?: ""})"
                }
                .joinToString("\n")
                .prependIndent("    ")
        })
    }

    /**
     * Create a callback registration that triggers after exactly [count] ticks.
     */
    fun exactTicks(count: Int) = callback(ActionCaptureDelay.Ticks(count))

    /**
     * Create a callback registration that triggers after [count] ticks.  This method is cumulative,
     * and will take into account previous calls to [ticks] when creating new callbacks.
     *
     * To run a callback after an exact number of ticks use [exactTicks].
     */
    fun ticks(count: Int): ActionCaptureCallbackRegistration {
        lastTicks += count

        return exactTicks(lastTicks)
    }

    /**
     * Create a callback registration that triggers when an [Action] completes.
     */
    fun complete() = callback(ActionCaptureDelay.Completed)

    /**
     * Check if this capture has a pending [Action] to run.
     */
    fun isPending(): Boolean {
        return action?.isRunning ?: false
    }
}