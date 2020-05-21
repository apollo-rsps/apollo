package org.apollo.game.action

interface AsyncActionTrait {
    /**
     * The continuation that this `Action` is executing.  May be `null` if this action hasn't started yet.
     */
    abstract var continuation: ActionCoroutine?

    /**
     * Update this action, initializing the continuation if not already initialized.
     *
     * @return `true` if this `Action` has completed execution.
     */
    fun update(): Boolean {
        val continuation = this.continuation
        if (continuation == null) {
            this.continuation = ActionCoroutine.start(action())
            return false
        }

        continuation.pulse()
        return continuation.stopped()
    }

    /**
     * Create a new `ActionBlock` to execute.
     */
    fun action(): ActionBlock
}