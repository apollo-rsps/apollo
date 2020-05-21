package org.apollo.game.plugin.kotlin.message

import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.message.impl.decode.ButtonMessage
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.kotlin.KotlinPluginScript
import org.apollo.game.plugin.kotlin.MessageListenable
import org.apollo.game.plugin.kotlin.PlayerContext
import org.apollo.game.plugin.kotlin.PredicateContext

/**
 * Registers a listener for [ButtonMessage]s that occur on the given [button] id.
 *
 * ```
 * on(ButtonClick, button = 416) {
 *     player.sendMessage("You click the button.")
 * }
 * ```
 */
fun KotlinPluginScript.on(
    listenable: IfAction.Companion,
    inter: Int,
    comp: Int,
    action: Int = 1,
    callback: IfAction.() -> Unit
) {
    registerListener(listenable, IfActionPredicateContext(inter, comp, action), callback)
}

class IfAction(override val player: Player) : PlayerContext {

    companion object : MessageListenable<ButtonMessage, IfAction, IfActionPredicateContext>() {

        override val type = ButtonMessage::class

        override fun createHandler(
            world: World,
            predicateContext: IfActionPredicateContext?,
            callback: IfAction.() -> Unit
        ): MessageHandler<ButtonMessage> {
            return object : MessageHandler<ButtonMessage>(world) {

                override fun handle(player: Player, message: ButtonMessage) {
                    if (predicateContext == null || predicateContext.inter != message.interfaceId ||
                        predicateContext.comp != message.componentId || predicateContext.action != message.option) {
                        return;
                    }

                    val context = IfAction(player)
                    context.callback()
                }

            }
        }

    }

}

class IfActionPredicateContext(val inter: Int, val comp: Int, val action: Int) : PredicateContext