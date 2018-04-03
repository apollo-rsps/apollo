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

var TELEPORT_DESTINATIONS = listOf<Pair<String, Position>>(
    Pair("ardougne", Position(2662, 3304, 0)),
    Pair("barrows", Position(3565, 3314, 0)),
    Pair("brimhaven", Position(2802, 3179, 0)),
    Pair("burthorpe", Position(2898, 3545, 0)),
    Pair("camelot", Position(2757, 3478, 0)),
    Pair("canifis", Position(3493, 3489, 0)),
    Pair("cw", Position(2442, 3090, 0)),
    Pair("draynor", Position(3082, 3249, 0)),
    Pair("duelarena", Position(3370, 3267, 0)),
    Pair("edgeville", Position(3087, 3504, 0)),
    Pair("entrana", Position(2827, 3343, 0)),
    Pair("falador", Position(2965, 3379, 0)),
    Pair("ge", Position(3164, 3476, 0)),
    Pair("gwd", Position(2881, 5310, 2)),
    Pair("kbd", Position(2273, 4680, 0)),
    Pair("keldagrim", Position(2845, 10210, 0)),
    Pair("kq", Position(3507, 9494, 0)),
    Pair("lumbridge", Position(3222, 3219, 0)),
    Pair("lunar", Position(2113, 3915, 0)),
    Pair("misc", Position(2515, 3866, 0)),
    Pair("neit", Position(2332, 3804, 0)),
    Pair("pc", Position(2658, 2660, 0)),
    Pair("rellekka", Position(2660, 3657, 0)),
    Pair("shilo", Position(2852, 2955, 0)),
    Pair("taverley", Position(2895, 3443, 0)),
    Pair("tutorial", Position(3094, 3107, 0)),
    Pair("tzhaar", Position(2480, 5175, 0)),
    Pair("varrock", Position(3212, 3423, 0)),
    Pair("yanille", Position(2605, 3096, 0)),
    Pair("zanaris", Position(2452, 4473, 0))
)

/**
 * Teleports the player to the specified position.
 */
on_command("tele", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        val invalidSyntax = "Invalid syntax - ::tele [x] [y] [optional-z] or ::tele [place name]"

        if (arguments.size == 1) {
            val query = arguments[0]

            for (target in TELEPORT_DESTINATIONS) {
                val (dest, name) = target

                if (!name.startsWith(query)) {
                    continue
                }

                player.sendMessage("Teleporting to $name.")
                player.teleport(dest)
                return@then
            }

            player.sendMessage("No destinations matching '$query'.")

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
