import org.apollo.game.message.impl.ForwardPrivateChatMessage
import org.apollo.game.message.impl.PrivateChatMessage
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.setting.PrivacyState.OFF
import org.apollo.game.model.entity.setting.PrivacyState.ON

on { PrivateChatMessage::class }
        .then {
            val friend = it.world.getPlayer(username)

            if (interactionPermitted(it, friend)) {
                friend.send(ForwardPrivateChatMessage(it.username, it.privilegeLevel, compressedMessage))
            }
        }

fun interactionPermitted(player: Player, friend: Player?): Boolean {
    val username = player.username
    val privacy = friend?.friendPrivacy

    if (friend == null || friend.hasIgnored(username)) {
        return false
    } else {
        return if (friend.friendsWith(username)) privacy != OFF else privacy == ON
    }
}
