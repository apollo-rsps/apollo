package org.apollo.game.plugin.testing.assertions

import io.mockk.MockKVerificationScope
import io.mockk.verify
import org.apollo.game.plugin.testing.junit.api.ActionCaptureCallbackRegistration

/**
 * Verify some expectations on a [mock] after a delayed event (specified by [DelayMode]).
 */
fun verifyAfter(registration: ActionCaptureCallbackRegistration, description: String? = null, verifier: MockKVerificationScope.() -> Unit) {
    after(registration, description) { verify(verifyBlock = verifier) }
}

/**
 * Run a [callback] after a given delay, specified by [DelayMode].
 */
fun after(registration: ActionCaptureCallbackRegistration, description: String? = null, callback: () -> Unit) {
    registration.function = callback
    registration.description = description
}