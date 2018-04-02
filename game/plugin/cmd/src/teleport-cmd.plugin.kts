import com.google.common.primitives.Ints
import java.io.File
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
        var z             = player.position.height

        // Quickly jump to a location by any prefix of a name in a file.
        if (arguments.size == 1) {
            val query        = arguments[0]
            val destinations = mutableListOf<String>()

            File("data/teleports.txt").useLines {
                lines -> lines.forEach { destinations.add(it) }
            }

            // For now we do a linear search through the list, but we could
            // make this smarter in the future.
            for (target in destinations) {
                val parts = target.split(' ')
                val name  = parts[0]

                if (!name.startsWith(query)) {
                    continue
                }

                // Avoid the '='
                val x = Ints.tryParse(parts[2])
                val y = Ints.tryParse(parts[3])

                if (x == null || y == null) {
                    player.sendMessage("Line for entry '" +
                        name +
                        "' had invalid coords.")
                }

                player.sendMessage("Teleporting to " + name + ".")
                player.teleport(Position(x!!, y!!, z))
                return@then
            }

            player.sendMessage("No destinations matching '" + query + "'.")

            return@then
        }

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
