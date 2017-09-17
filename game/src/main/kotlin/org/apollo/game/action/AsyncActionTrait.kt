package org.apollo.game.action

interface AsyncActionTrait {
    val runner: AsyncActionRunner

    suspend fun wait(pulses: Int = 1, pulseCallback: (() -> Unit)? = null) {
        runner.wait(pulses, pulseCallback)
    }
}