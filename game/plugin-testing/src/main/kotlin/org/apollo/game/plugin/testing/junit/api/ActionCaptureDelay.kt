package org.apollo.game.plugin.testing.junit.api

sealed class ActionCaptureDelay {
    data class Ticks(val count: Int) : ActionCaptureDelay()
    object Completed : ActionCaptureDelay()
}
