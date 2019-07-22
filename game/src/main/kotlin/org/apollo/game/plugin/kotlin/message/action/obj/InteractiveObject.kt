package org.apollo.game.plugin.kotlin.message.action.obj

import org.apollo.game.model.entity.obj.GameObject

/**
 * An object that can be interacted with.
 */
interface InteractiveObject {

    val id: Int

    fun instanceOf(other: GameObject): Boolean // TODO alternative name?

}