package org.apollo.game.plugin.testing.mockito

import org.mockito.Mockito
import java.util.function.Consumer


object KotlinMockitoExtensions {
    inline fun <reified T> matches(crossinline callback: T.() -> Unit): T {
        val consumer = Consumer<T> { it.callback() }
        val matcher = KotlinArgMatcher(consumer)

        return Mockito.argThat(matcher)
    }
}
