package org.apollo.game.plugin.kotlin.message

import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.message.impl.IfActionMessage
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.kotlin.KotlinPluginScript
import org.apollo.game.plugin.kotlin.MessageListenable
import org.apollo.game.plugin.kotlin.PlayerContext
import org.apollo.game.plugin.kotlin.PredicateContext

/**
 * Registers a listener for [IfActionMessage]s that occur on the given [button] id.
 *
 * ```
 * on(ButtonClick, button = 416) {
 *     player.sendMessage("You click the button.")
 * }
 * ```
 */
fun KotlinPluginScript.on(
    listenable: ButtonClick.Companion,
    button: Int,
    callback: ButtonClick.() -> Unit
) {
    registerListener(listenable, ButtonPredicateContext(button), callback)
}

class ButtonClick(override val player: Player, val button: Int) : PlayerContext {

    companion object : MessageListenable<IfActionMessage, ButtonClick, ButtonPredicateContext>() {

        override val type = IfActionMessage::class

        override fun createHandler(
            world: World,
            predicateContext: ButtonPredicateContext?,
            callback: ButtonClick.() -> Unit
        ): MessageHandler<IfActionMessage> {
            return object : MessageHandler<IfActionMessage>(world) {

                override fun handle(player: Player, message: IfActionMessage) {
                    if (predicateContext == null || predicateContext.button == message.componentId) {
                        val context = ButtonClick(player, message.componentId)
                        context.callback()
                    }
                }

            }
        }

    }

}

class ButtonPredicateContext(val button: Int) : PredicateContext