import com.google.common.primitives.Ints
import org.apollo.game.model.Position
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.plugin.util.command.valid_arg_length

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
        val invalidSyntax = "Invalid syntax - ::tele [x] [y] [optional-z]"
        if (!valid_arg_length(arguments, 2..3, player, invalidSyntax)) {
            return@then
        }

        val x = Ints.tryParse(arguments[0])
        if (x == null) {
            player.sendMessage(invalidSyntax)
            return@then
        }

        val y = Ints.tryParse(arguments[1])
        if (y == null) {
            player.sendMessage(invalidSyntax)
            return@then
        }

        var z = player.position.height
        if (arguments.size == 3) {
            val plane = Ints.tryParse(arguments[2])
            if (plane == null) {
                player.sendMessage(invalidSyntax)
                return@then
            }
            z = plane
        }

        if (z in 0..4) {
            player.teleport(Position(x, y, z))
        }
    }