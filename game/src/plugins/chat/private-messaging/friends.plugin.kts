import org.apollo.game.message.impl.AddFriendMessage
import org.apollo.game.message.impl.SendFriendMessage
import org.apollo.game.model.entity.setting.PrivacyState

on { AddFriendMessage::class }
        .then { player ->
            player.addFriend(username)

            val playerUsername = player.username
            val friend = world.getPlayer(username)

            if (friend == null) {
                player.send(SendFriendMessage(username, 0))
            } else if (friend.friendsWith(playerUsername) || friend.friendPrivacy == PrivacyState.ON)  {
                if (player.friendPrivacy != PrivacyState.OFF) {
                    friend.send(SendFriendMessage(playerUsername, player.worldId))
                }

                if (friend.friendPrivacy != PrivacyState.OFF) {
                    player.send(SendFriendMessage(username, friend.worldId))
                }
            }
        }