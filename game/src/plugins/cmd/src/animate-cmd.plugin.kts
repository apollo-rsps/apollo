import com.google.common.primitives.Ints
import org.apollo.game.model.Animation
import org.apollo.game.model.entity.setting.PrivilegeLevel

on_command("animate", PrivilegeLevel.MODERATOR)
        .then { player ->
            val invalidSyntax = "Invalid syntax - ::animate [animation-id]"
            if(valid_arg_length(arguments, 1, player, invalidSyntax)) {
                val id = Ints.tryParse(arguments[0])
                if (id == null) {
                    player.sendMessage(invalidSyntax)
                    return@then
                }

                player.playAnimation(Animation(id))
            }
        }