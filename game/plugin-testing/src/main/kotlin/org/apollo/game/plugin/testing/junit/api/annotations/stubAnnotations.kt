package org.apollo.game.plugin.testing.junit.api.annotations

annotation class Id(val value: Int)
annotation class Pos(val x: Int, val y: Int, val height: Int = 0)
annotation class Name(val value: String)

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestMock