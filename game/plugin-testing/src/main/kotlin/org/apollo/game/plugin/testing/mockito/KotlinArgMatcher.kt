package org.apollo.game.plugin.testing.mockito

import org.mockito.ArgumentMatcher
import java.lang.AssertionError
import java.util.function.Consumer

class KotlinArgMatcher<T>(val consumer: Consumer<T>) : ArgumentMatcher<T>() {
    private var error: String? = null

    override fun matches(argument: Any?): Boolean {
        try {
            consumer.accept(argument as T)
            return true
        } catch (err: AssertionError) {
            error = err.message
            println(error)
            return false
        }
    }

    override fun toString(): String {
        return error ?: ""
    }
}

