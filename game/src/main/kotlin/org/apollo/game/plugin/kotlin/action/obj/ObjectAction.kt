package org.apollo.game.plugin.kotlin.action.obj

import org.apollo.cache.def.ObjectDefinition
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.entity.EntityType
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.plugin.kotlin.action.ActionListenableContext

/**
 * An interaction between a [Player] and an [interactive] [GameObject].
 */
class ObjectAction<T : InteractiveObject?>(
    override val option: String,
    override val player: Player,
    val target: GameObject,
    val interactive: T
) : ActionListenableContext {

    companion object : ObjectActionListenable() {

        override fun from(player: Player, message: ObjectActionMessage): ObjectAction<*> {
            val def = ObjectDefinition.lookup(message.id)
            val selectedAction = def.menuActions[message.option]

            val obj = player.world.regionRepository
                .fromPosition(message.position)
                .getEntities<GameObject>(message.position, EntityType.DYNAMIC_OBJECT, EntityType.STATIC_OBJECT)
                .first { it.definition == def }

            return ObjectAction(selectedAction, player, obj, null)
        }

        override val type = ObjectActionMessage::class

        override fun <T : InteractiveObject> from(
            player: Player,
            other: ObjectActionMessage,
            objects: List<T>
        ): ObjectAction<T> {
            val def = ObjectDefinition.lookup(other.id)
            val selectedAction = def.menuActions[other.option]

            val obj = player.world.regionRepository
                .fromPosition(other.position)
                .getEntities<GameObject>(other.position, EntityType.DYNAMIC_OBJECT, EntityType.STATIC_OBJECT)
                .first { it.definition == def }

            return ObjectAction(selectedAction, player, obj, objects.first { it.instanceOf(obj) })
        }
    }

}
