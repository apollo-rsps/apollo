import org.apollo.game.model.Animation
import org.apollo.game.model.entity.setting.PrivilegeLevel

on_command("animate", PrivilegeLevel.MODERATOR)
    .then { player ->
        arguments.firstOrNull()
            ?.let(String::toIntOrNull)
            ?.let(::Animation)
            ?.let {
                player.playAnimation(it)
                return@then
            }

        player.sendMessage("Invalid syntax - ::animate [animation-id]")
    }