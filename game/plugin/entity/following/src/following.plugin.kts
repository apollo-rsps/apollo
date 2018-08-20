import org.apollo.game.plugin.entity.player_action.PlayerActionEvent
import org.apollo.game.plugin.entity.player_action.PlayerActionType
import org.apollo.plugin.entity.following.FollowAction

on_player_event { PlayerActionEvent::class }
    .where { action == PlayerActionType.FOLLOW }
    .then {
        FollowAction.start(it, target)
        terminate()
    }