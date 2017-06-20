import org.apollo.game.message.impl.AddFriendMessage
import org.apollo.game.message.impl.SendFriendMessage
import org.apollo.game.model.entity.setting.PrivacyState

on { AddFriendMessage::class }
    .then {
        it.addFriend(username)

        val friend = it.world.getPlayer(username)

        if (friend == null || friend.friendPrivacy == PrivacyState.OFF) {
            it.send(SendFriendMessage(username, 0))
            return@then
        } else {
            it.send(SendFriendMessage(username, friend.worldId))
        }

        if (friend.friendsWith(it.username) && it.friendPrivacy != PrivacyState.OFF) {
            friend.send(SendFriendMessage(it.username, it.worldId))
        }
    }
