package org.apollo.game.plugin.testing.junit.mocking

import kotlin.reflect.KClass

data class StubPrototype<T : Any>(val type: KClass<T>, val annotations: Collection<Annotation>)
