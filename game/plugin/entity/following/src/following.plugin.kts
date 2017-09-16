import com.google.common.primitives.Ints
import org.apollo.game.message.impl.PlayerActionMessage
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.plugin.entity.following.FollowAction

on_player_event { PlayerActionEvent::class }
        .where { action == PlayerActionType.FOLLOW }
        .then {
            FollowAction.start(it, target)
            terminate()
        }