import org.apollo.game.model.Animation
import org.apollo.game.model.entity.setting.PrivilegeLevel

on_command("animate", PrivilegeLevel.MODERATOR)
        .then { player ->
            if(valid_arg_length(arguments, 1, player, "Invalid syntax - ::animate [animation-id]")) {
                player.playAnimation(Animation(arguments[0].toInt()))
            }
        }