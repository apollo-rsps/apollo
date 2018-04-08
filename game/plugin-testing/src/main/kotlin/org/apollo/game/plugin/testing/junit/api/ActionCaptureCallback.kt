package org.apollo.game.plugin.testing.junit.api

data class ActionCaptureCallback(val delay: ActionCaptureDelay, val callbackRegistration: ActionCaptureCallbackRegistration) {
    fun invoke() {
        callbackRegistration.function?.invoke()
    }
}