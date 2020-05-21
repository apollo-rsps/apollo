package org.apollo.game.plugin.kotlin

import org.apollo.game.model.entity.Player

/**
 * Contextual information for a [Listenable].
 */
interface ListenableContext

/**
 * Contextual information for a [Listenable] involving a specific [Player].
 */
interface PlayerContext : ListenableContext {
    val player: Player
}

interface PredicateContext