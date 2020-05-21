import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.setting.PrivilegeLevel

/**
 * Adds a command to mute a player. Admins cannot be muted.
 */
on_command("mute", PrivilegeLevel.MODERATOR)
    .then { player ->
        val name = arguments.joinToString(" ")
        val targetPlayer = player.world.getPlayer(name)

        if (validate(player, targetPlayer)) {
            targetPlayer.isMuted = true
            player.sendMessage("You have just unmuted ${targetPlayer.username}.")
        }
    }

/**
 * Adds a command to unmute a player.
 */
on_command("unmute", PrivilegeLevel.MODERATOR)
    .then { player ->
        val name = arguments.joinToString(" ")
        val targetPlayer = player.world.getPlayer(name)

        if (validate(player, targetPlayer)) {
            targetPlayer.isMuted = false
            player.sendMessage("You have just unmuted ${targetPlayer.username}.")
        }
    }

/**
 * Adds a command to ban a player. Admins cannot be banned.
 */
on_command("ban", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        val name = arguments.joinToString(" ")
        val targetPlayer = player.world.getPlayer(name)

        if (validate(player, targetPlayer)) {
            targetPlayer.ban()
            targetPlayer.logout() // TODO force logout
            player.sendMessage("You have just banned ${targetPlayer.username}.")
        }
    }

/**
 * Ensures the player isn't null, and that they aren't an Administrator.
 */
fun validate(player: Player, targetPlayer: Player?): Boolean {
    if (targetPlayer == null) {
        player.sendMessage("That player does not exist.")
        return false
    } else if (targetPlayer.privilegeLevel == PrivilegeLevel.ADMINISTRATOR) {
        player.sendMessage("You cannot perform this action on Administrators.")
        return false
    }
    return true
}