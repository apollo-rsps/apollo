import org.apollo.game.message.impl.SendFriendMessage
import org.apollo.game.message.impl.SendFriendMessage.FriendMessageComponent
import org.apollo.game.message.impl.decode.AddFriendMessage
import org.apollo.game.model.entity.setting.PrivacyState
import org.apollo.game.model.event.impl.LoginEvent

on_player_event { LoginEvent::class }.then {
    player.sendUserLists();
}

on { AddFriendMessage::class }
    .then {
        it.addFriend(username)

        val friend = it.world.getPlayer(username)

        if (friend == null || friend.friendPrivacy == PrivacyState.OFF) {
            it.send(SendFriendMessage(FriendMessageComponent(username, 0)))
            return@then
        } else {
            it.send(SendFriendMessage(FriendMessageComponent(username, friend.worldId)))
        }

        if (friend.friendsWith(it.username) && it.friendPrivacy != PrivacyState.OFF) {
            friend.send(SendFriendMessage(FriendMessageComponent(it.username, it.worldId)))
        }
    }
