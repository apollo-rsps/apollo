package org.apollo.game.action

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ActionCoroutineTest {
    @Test
    fun `Coroutine execution resumes after a pulse() call`() {
        val coroutine = ActionCoroutine.start {
            wait(1)
        }

        coroutine.pulse()
        assertTrue(coroutine.stopped())
    }

    @Test
    fun `Coroutine suspends on wait() calls`() {
        val coroutine = ActionCoroutine.start {
            wait(1)
        }

        // Check that the continuation is still waiting on a pulse.
        assertFalse(coroutine.stopped())
    }

    @Test
    fun `Coroutine cancels on stop() calls`() {
        val coroutine = ActionCoroutine.start {
            stop()
        }

        assertTrue(coroutine.stopped())
        coroutine.pulse()
        assertTrue(coroutine.stopped())
    }
}