import org.apollo.game.model.Graphic
import org.apollo.game.model.entity.setting.PrivilegeLevel

on_command("graphic", PrivilegeLevel.MODERATOR)
    .then { player ->
        arguments.firstOrNull()
            ?.let(String::toIntOrNull)
            ?.let(::Graphic)
            ?.let {
                player.playGraphic(it)
                return@then
            }

        player.sendMessage("Invalid syntax - ::graphic [graphic-id]")
    }