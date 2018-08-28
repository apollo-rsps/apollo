import org.apollo.game.plugin.entity.actions.PlayerActionEvent
import org.apollo.game.plugin.entity.actions.PlayerActionType
import org.apollo.plugin.entity.following.FollowAction

on_player_event { PlayerActionEvent::class }
    .where { action == PlayerActionType.FOLLOW }
    .then {
        FollowAction.start(it, target)
        terminate()
    }