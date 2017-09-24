import org.apollo.game.plugin.entity.player_action.PlayerActionType
import org.apollo.plugin.entity.following.FollowAction

on_player_event { org.apollo.game.plugin.entity.player_action.PlayerActionEvent::class }
    .where { action == PlayerActionType.FOLLOW }
    .then {
        FollowAction.start(it, target)
        terminate()
    }