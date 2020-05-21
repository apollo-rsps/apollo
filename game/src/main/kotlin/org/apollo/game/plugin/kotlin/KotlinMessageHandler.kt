package org.apollo.game.plugin.kotlin

import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.PluginContext
import org.apollo.net.message.Message
import kotlin.reflect.KClass

/**
 * A handler for [Message]s.
 */
@Deprecated("To be removed")
class OldKotlinMessageHandler<T : Message>(val world: World, val context: PluginContext, val type: KClass<T>) :
    KotlinPlayerHandlerProxyTrait<T>, MessageHandler<T>(world) {

    override var callback: T.(Player) -> Unit = {}
    override var predicate: T.() -> Boolean = { true }

    override fun handle(player: Player, message: T) = handleProxy(player, message)
    override fun register() = context.addMessageHandler(type.java, this)
}