package org.apollo.game.plugin.kotlin.action

import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.kotlin.KotlinPluginScript
import org.apollo.game.plugin.kotlin.ListenableContext
import org.apollo.game.plugin.kotlin.MessageListenable
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
inline fun <T : ActionListenableContext, reified F : Message> KotlinPluginScript.on(
    listenable: MessageListenable<T, F>,
    option: String,
    crossinline callback: T.() -> Unit
) {
    on(listenable) {
        if (this.option.equals(option, ignoreCase = true)) {
            callback()
        }
    }
}

interface ActionListenableContext : ListenableContext {
    val option: String
    val player: Player
}
