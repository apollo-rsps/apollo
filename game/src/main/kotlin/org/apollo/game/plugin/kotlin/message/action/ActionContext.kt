package org.apollo.game.plugin.kotlin.message.action

import org.apollo.game.plugin.kotlin.KotlinPluginScript
import org.apollo.game.plugin.kotlin.MessageListenable
import org.apollo.game.plugin.kotlin.PlayerContext
import org.apollo.net.message.Message

/**
 * Registers a listener for an action event that uses the given [option] (case-insensitive).
 *
 * ```
 * on(PlayerAction, option = "Trade") {
 *     player.sendMessage("Sending trade request...")
 * }
 * ```
 */
inline fun <T : ActionContext, reified F : Message> KotlinPluginScript.on(
    listenable: MessageListenable<F, T>,
    option: String,
    crossinline callback: T.() -> Unit
) {
    on(listenable) {
        if (this.option.equals(option, ignoreCase = true)) {
            callback()
        }
    }
}

interface ActionContext : PlayerContext {
    val option: String
}
