package org.apollo.game.plugin.testing.junit.api.annotations

import org.apollo.game.action.Action
import kotlin.reflect.KClass

annotation class ActionTest(val value: KClass<out Action<*>> = Action::class)