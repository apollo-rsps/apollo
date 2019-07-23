package org.apollo.game.plugin.kotlin.message

import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.message.impl.ButtonMessage
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
    listenable: ButtonClick.Companion,
    button: Int,
    callback: ButtonClick.() -> Unit
) {
    registerListener(listenable, ButtonPredicateContext(button), callback)
}

class ButtonClick(override val player: Player, val button: Int) : PlayerContext {

    companion object : MessageListenable<ButtonMessage, ButtonClick, ButtonPredicateContext>() {

        override val type = ButtonMessage::class
        
        override fun createHandler(
            world: World,
            predicateContext: ButtonPredicateContext?,
            callback: ButtonClick.() -> Unit
        ): MessageHandler<ButtonMessage> {
            return object : MessageHandler<ButtonMessage>(world) {

                override fun handle(player: Player, message: ButtonMessage) {
                    if (predicateContext == null || predicateContext.button == message.widgetId) {
                        val context = ButtonClick(player, message.widgetId)
                        context.callback()
                    }
                }

            }
        }

    }

}

class ButtonPredicateContext(val button: Int) : PredicateContext