package org.apollo.game.plugin.kotlin.message.action.obj

import org.apollo.cache.def.ObjectDefinition
import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.World
import org.apollo.game.model.entity.EntityType
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.plugin.kotlin.KotlinPluginScript
import org.apollo.game.plugin.kotlin.MessageListenable
import org.apollo.game.plugin.kotlin.message.action.ActionContext

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
    listenable: ObjectAction.Companion,
    option: String,
    objects: List<T>,
    callback: ObjectAction<T>.() -> Unit
) {
    @Suppress("UNCHECKED_CAST") (callback as ObjectAction<*>.() -> Unit)
    registerListener(listenable, ObjectActionPredicateContext(option, objects), callback)
}

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
fun KotlinPluginScript.on(
    listenable: ObjectAction.Companion,
    option: String,
    callback: ObjectAction<*>.() -> Unit
) {
    registerListener(listenable, ObjectActionPredicateContext(option, emptyList()), callback)
}

fun KotlinPluginScript.x() {
    on(ObjectAction, "walk") {

    }
}

/**
 * An interaction between a [Player] and an [interactive] [GameObject].
 */
class ObjectAction<T : InteractiveObject?>( // TODO split into two classes, one with T and one without?
    override val player: Player,
    override val option: String,
    val target: GameObject,
    val interactive: T
) : ActionContext {

    companion object : MessageListenable<ObjectActionMessage, ObjectAction<*>, ObjectActionPredicateContext<*>>() {

        override val type = ObjectActionMessage::class

        override fun createHandler(
            world: World,
            predicateContext: ObjectActionPredicateContext<*>?,
            callback: ObjectAction<*>.() -> Unit
        ): MessageHandler<ObjectActionMessage> {
            return object : MessageHandler<ObjectActionMessage>(world) {

                override fun handle(player: Player, message: ObjectActionMessage) {
                    val def = ObjectDefinition.lookup(message.id)
                    val option = def.menuActions[message.option]

                    val target = world.regionRepository
                        .fromPosition(message.position)
                        .getEntities<GameObject>(message.position, EntityType.DYNAMIC_OBJECT, EntityType.STATIC_OBJECT)
                        .find { it.definition == def }
                        ?: return // Could happen if object was despawned this tick, before calling this handle function

                    val context = when { // Evaluation-order matters here.
                        predicateContext == null -> ObjectAction<InteractiveObject?>(player, option, target, null)
                        !predicateContext.option.equals(option, ignoreCase = true) -> return
                        predicateContext.objects.isEmpty() -> ObjectAction(player, option, target, null)
                        predicateContext.objects.any { it.instanceOf(target) } -> {
                            val interactive = predicateContext.objects.find { it.instanceOf(target) } ?: return
                            ObjectAction(player, option, target, interactive)
                        }
                        else -> return
                    }

                    context.callback()
                }

            }
        }

    }

}
