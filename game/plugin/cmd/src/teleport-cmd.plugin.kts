
import com.google.common.primitives.Ints
import org.apollo.game.model.Position
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.plugins.api.Position.component1
import org.apollo.game.plugins.api.Position.component2
import org.apollo.game.plugins.api.Position.component3

/**
 * Sends the player's position.
 */
on_command("pos", PrivilegeLevel.MODERATOR)
    .then { player ->
        val (x, y, z) = player.position
        val region = player.position.regionCoordinates

        player.sendMessage("You are at: ($x, $y, $z) in region (${region.x}, ${region.y}).")
    }

/**
 * Teleports the player to the specified position.
 */
on_command("tele", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        val invalidSyntax = "Invalid syntax - ::tele [x] [y] [optional-z] or ::tele [place name]"

        if (arguments.size == 1) {
            val query = arguments[0]
            val results = TELEPORT_DESTINATIONS.filter { (name) -> name.startsWith(query) }

            if (results.isEmpty()) {
                player.sendMessage("No destinations matching '$query'.")
                return@then
            } else if (results.size > 1) {
                player.sendMessage("Ambiguous query '$query' (could be $results). Please disambiguate.")
                return@then
            }

            val (name, dest) = results[0]
            player.sendMessage("Teleporting to $name.")
            player.teleport(dest)

            return@then
        }

        if (arguments.size !in 2..3) {
            player.sendMessage(invalidSyntax)
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

val TELEPORT_DESTINATIONS = listOf(
    "alkharid" to Position(3292, 3171, 0),
    "ardougne" to Position(2662, 3304, 0),
    "barrows" to Position(3565, 3314, 0),
    "brimhaven" to Position(2802, 3179, 0),
    "burthorpe" to Position(2898, 3545, 0),
    "camelot" to Position(2757, 3478, 0),
    "canifis" to Position(3493, 3489, 0),
    "cw" to Position(2442, 3090, 0),
    "draynor" to Position(3082, 3249, 0),
    "duelarena" to Position(3370, 3267, 0),
    "edgeville" to Position(3087, 3504, 0),
    "entrana" to Position(2827, 3343, 0),
    "falador" to Position(2965, 3379, 0),
    "ge" to Position(3164, 3476, 0),
    "kbd" to Position(2273, 4680, 0),
    "keldagrim" to Position(2845, 10210, 0),
    "kq" to Position(3507, 9494, 0),
    "lumbridge" to Position(3222, 3219, 0),
    "lunar" to Position(2113, 3915, 0),
    "misc" to Position(2515, 3866, 0),
    "neit" to Position(2332, 3804, 0),
    "pc" to Position(2658, 2660, 0),
    "rellekka" to Position(2660, 3657, 0),
    "shilo" to Position(2852, 2955, 0),
    "taverley" to Position(2895, 3443, 0),
    "tutorial" to Position(3094, 3107, 0),
    "tzhaar" to Position(2480, 5175, 0),
    "varrock" to Position(3212, 3423, 0),
    "yanille" to Position(2605, 3096, 0),
    "zanaris" to Position(2452, 4473, 0)
)