package org.apollo.game.plugin.kotlin.message.action.obj

import org.apollo.cache.def.ObjectDefinition
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.entity.EntityType
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.plugin.kotlin.message.action.ActionContext

/**
 * An interaction between a [Player] and an [interactive] [GameObject].
 */
class ObjectAction<T : InteractiveObject?>( // TODO split into two classes, one with T and one without?
    override val player: Player,
    override val option: String,
    val target: GameObject,
    val interactive: T
) : ActionContext {

    companion object : ObjectActionListenable() {

        override val type = ObjectActionMessage::class

        override fun createContext(player: Player, message: ObjectActionMessage): ObjectAction<*>? {
            return create<InteractiveObject>(player, message, objects = null)
        }

        override fun <T : InteractiveObject> createContext(
            player: Player,
            other: ObjectActionMessage,
            objects: List<T>
        ): ObjectAction<T>? {
            @Suppress("UNCHECKED_CAST")
            return create(player, other, objects) as ObjectAction<T>
        }

        private fun <T : InteractiveObject> create(
            player: Player,
            other: ObjectActionMessage,
            objects: List<T>?
        ): ObjectAction<*>? {
            val def = ObjectDefinition.lookup(other.id)
            val selectedAction = def.menuActions[other.option]

            val obj = player.world.regionRepository
                .fromPosition(other.position)
                .getEntities<GameObject>(other.position, EntityType.DYNAMIC_OBJECT, EntityType.STATIC_OBJECT)
                .find { it.definition == def }
                ?: return null

            if (objects == null) {
                return ObjectAction(player, selectedAction, obj, null)
            } else {
                val interactive = objects.find { it.instanceOf(obj) } ?: return null
                return ObjectAction(player, selectedAction, obj, interactive)
            }
        }
    }

}
