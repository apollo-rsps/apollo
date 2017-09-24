import org.apollo.game.model.entity.setting.PrivilegeLevel

on_command("broadcast", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        val message = arguments.joinToString(" ")
        val broadcast = "[Broadcast] ${player.username.capitalize()}: $message"

        player.world.playerRepository.forEach { other ->
            other.sendMessage(broadcast)
        }
    }
