package org.apollo.game.plugin.kotlin.message

import org.apollo.game.message.impl.ButtonMessage
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.kotlin.KotlinPluginScript
import org.apollo.game.plugin.kotlin.MessageListenable
import org.apollo.game.plugin.kotlin.PlayerContext

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
    on(listenable) {
        if (this.button == button) {
            callback()
        }
    }
}

class ButtonClick(override val player: Player, val button: Int) : PlayerContext {

    companion object : MessageListenable<ButtonMessage, ButtonClick>() {
        override val type = ButtonMessage::class

        override fun createContext(player: Player, message: ButtonMessage): ButtonClick {
            return ButtonClick(player, message.widgetId)
        }
    }

}