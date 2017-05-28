import org.apollo.game.message.impl.AddFriendMessage
import org.apollo.game.message.impl.SendFriendMessage
import org.apollo.game.model.entity.setting.PrivacyState

on { AddFriendMessage::class }
        .then {
            it.addFriend(username)

            val playerUsername = it.username
            val friend = it.world.getPlayer(username)

            if (friend == null) {
                it.send(SendFriendMessage(username, 0))
            } else if (friend.friendsWith(playerUsername) || friend.friendPrivacy == PrivacyState.ON)  {
                if (it.friendPrivacy != PrivacyState.OFF) {
                    friend.send(SendFriendMessage(playerUsername, it.worldId))
                }

                if (friend.friendPrivacy != PrivacyState.OFF) {
                    it.send(SendFriendMessage(username, friend.worldId))
                }
            }
        }
