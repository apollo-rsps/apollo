package org.apollo.game.plugin.kotlin.message.action.obj

import org.apollo.game.plugin.kotlin.message.action.ActionPredicateContext

data class ObjectActionPredicateContext<T : InteractiveObject>(
    override val option: String,
    val objects: List<T>
) : ActionPredicateContext(option)