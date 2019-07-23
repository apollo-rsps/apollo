package org.apollo.game.plugin.kotlin.message.action

import org.apollo.game.plugin.kotlin.KotlinPluginScript
import org.apollo.game.plugin.kotlin.MessageListenable
import org.apollo.game.plugin.kotlin.PlayerContext
import org.apollo.game.plugin.kotlin.PredicateContext
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
fun <T : ActionContext, F : Message> KotlinPluginScript.on(
    listenable: MessageListenable<F, T, ActionPredicateContext>,
    option: String,
    callback: T.() -> Unit
) {
    registerListener(listenable, ActionPredicateContext(option), callback)
}

interface ActionContext : PlayerContext {
    val option: String
}

open class ActionPredicateContext(open val option: String) : PredicateContext