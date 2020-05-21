import com.google.common.primitives.Ints
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.setting.PrivilegeLevel

/**
 * An array of npcs that cannot be spawned.
 */
val blacklist: IntArray = intArrayOf()

/**
 * Spawns a non-blacklisted npc in the specified position, or the player's position if both 'x' and
 * 'y' are not supplied.
 */
on_command("spawn", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        val invalidSyntax = "Invalid syntax - ::spawn [npc id] [optional-x] [optional-y] [optional-z]"
        if (arguments.size !in intArrayOf(1, 3, 4)) {
            player.sendMessage(invalidSyntax)
            return@then
        }

        val id = Ints.tryParse(arguments[0])
        if (id == null) {
            player.sendMessage(invalidSyntax)
            return@then
        }

        if (id in blacklist) {
            player.sendMessage("Sorry, npc $id is blacklisted!")
            return@then
        }

        val position: Position?
        if (arguments.size == 1) {
            position = player.position
        } else {
            val x = Ints.tryParse(arguments[1])
            val y = Ints.tryParse(arguments[2])

            if (x == null || y == null) {
                player.sendMessage(invalidSyntax)
                return@then
            }

            val height = if (arguments.size == 4) {
                val h = Ints.tryParse(arguments[3])
                if (h == null) {
                    player.sendMessage(invalidSyntax)
                    return@then
                }

                h
            } else {
                player.position.height
            }

            position = Position(x, y, height)
        }

        player.world.register(Npc(player.world, id, position))
    }

/**
 * Mass spawns npcs around the player.
 */
on_command("mass", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        val invalidSyntax = "Invalid syntax - ::mass [npc id] [range (1-5)]"
        if (arguments.size != 2) {
            player.sendMessage(invalidSyntax)
            return@then
        }

        val id = Ints.tryParse(arguments[0])
        if (id == null) {
            player.sendMessage(invalidSyntax)
            return@then
        }

        val range = Ints.tryParse(arguments[1])
        if (range == null) {
            player.sendMessage(invalidSyntax)
            return@then
        }

        if (id < 0 || range !in 1..5) {
            player.sendMessage(invalidSyntax)
            return@then
        }

        if (id in blacklist) {
            player.sendMessage("Sorry, npc $id is blacklisted!")
            return@then
        }

        val centerPosition = player.position

        val minX = centerPosition.x - range
        val minY = centerPosition.y - range
        val maxX = centerPosition.x + range
        val maxY = centerPosition.y + range
        val z = centerPosition.height

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                player.world.register(Npc(player.world, id, Position(x, y, z)))
            }
        }

        player.sendMessage("Mass spawning npcs with id $id.")
    }

/**
 * Unregisters all npcs from the world npc repository.
 */
on_command("clearnpcs", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        player.world.npcRepository.forEach { npc -> player.world.unregister(npc) }
        player.sendMessage("Unregistered all npcs from the world.")
    }