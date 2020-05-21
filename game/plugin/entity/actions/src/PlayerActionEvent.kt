package org.apollo.game.plugin.entity.actions

import org.apollo.game.model.entity.Player
import org.apollo.game.model.event.PlayerEvent

class PlayerActionEvent(player: Player, val target: Player, val action: PlayerActionType) : PlayerEvent(player)