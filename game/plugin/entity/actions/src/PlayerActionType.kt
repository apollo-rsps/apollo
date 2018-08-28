package org.apollo.game.plugin.entity.actions

enum class PlayerActionType(val displayName: String, val slot: Int, val primary: Boolean = true) {
    ATTACK("Attack", 2),
    CHALLENGE("Challenge", 2),
    FOLLOW("Follow", 4),
    TRADE("Trade with", 5)
}