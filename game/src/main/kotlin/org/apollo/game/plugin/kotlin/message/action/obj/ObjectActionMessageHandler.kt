package org.apollo.game.plugin.kotlin.message.action.obj

import org.apollo.cache.def.ObjectDefinition
import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.World
import org.apollo.game.model.entity.EntityType
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.plugin.kotlin.KotlinPluginScript

/**
 * Registers a listener for [ObjectActionMessage]s that occur on any of the given [InteractiveObject]s using the
 * given [option] (case-insensitive).
 *
 * ```
 * on(ObjectAction, option = "Open", objects = DOORS.toList()) {
 *     player.sendMessage("You open the door.")
 * }
 * ```
 */
fun <T : InteractiveObject> KotlinPluginScript.on(
    listenable: ObjectActionListenable,
    option: String,
    objects: List<T>,
    callback: ObjectAction<T>.() -> Unit
) {
    if (objects.isEmpty()) {
        on(listenable) {
            @Suppress("UNCHECKED_CAST") (callback as ObjectAction<*>.() -> Unit)
            callback(this)
        }
    } else {
        val handler = ObjectActionMessageHandler(world, listenable, objects, option, callback)
        context.addMessageHandler(listenable.type.java, handler)
    }
}

class ObjectActionMessageHandler<T : InteractiveObject>(
    world: World,
    private val listenable: ObjectActionListenable,
    private val objects: List<T>,
    private val option: String,
    private val callback: ObjectAction<T>.() -> Unit
) : MessageHandler<ObjectActionMessage>(world) {

    override fun handle(player: Player, message: ObjectActionMessage) {
        val def = ObjectDefinition.lookup(message.id)
        val selectedAction = def.menuActions[message.option]

        val obj = player.world.regionRepository
            .fromPosition(message.position)
            .getEntities<GameObject>(message.position, EntityType.DYNAMIC_OBJECT, EntityType.STATIC_OBJECT)
            .first { it.definition == def }

        if (option.equals(selectedAction, ignoreCase = true) && objects.any { it.instanceOf(obj) }) {
            val context = listenable.createContext(player, message, objects)
            context?.callback()
        }
    }

}