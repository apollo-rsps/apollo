import org.apollo.game.model.Position
import org.apollo.game.model.entity.setting.PrivilegeLevel

/**
 * Sends the player's position.
 */
on_command("pos", PrivilegeLevel.MODERATOR)
        .then { player ->
            player.sendMessage("You are at: ${player.position}.")
        }

/**
 * Teleports the player to the specified position.
 */
on_command("tele", PrivilegeLevel.ADMINISTRATOR)
        .then { player ->
            if (!valid_arg_length(arguments, 2..3, player, "Invalid syntax - ::tele [x] [y] [optional-z]")) {
                return@then
            }

            val x = arguments[0].toInt()
            val y = arguments[1].toInt()
            val z = if (arguments.size == 3) arguments[2].toInt() else player.position.height

            if (z in 0..4) {
                player.teleport(Position(x, y, z))
            }
        }