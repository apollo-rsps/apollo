import org.apollo.game.message.impl.SendFriendMessage
import org.apollo.game.message.impl.SendFriendMessage.FriendMessageComponent
import org.apollo.game.message.impl.decode.AddFriendMessage
import org.apollo.game.message.impl.decode.RemoveFriendMessage
import org.apollo.game.model.entity.setting.PrivacyState
import org.apollo.game.model.event.impl.LoginEvent
import org.apollo.game.model.inter.TopLevelPosition
import org.apollo.game.plugin.kotlin.message.IfAction
import org.apollo.game.plugin.kotlin.message.on

on(IfAction, inter = TopLevelPosition.FRIENDS_TAB.interfaceId, comp = 1) {
    player.interfaceSet.openTopLevel(432, TopLevelPosition.FRIENDS_TAB)
}


on_player_event { LoginEvent::class }.then {
    player.sendUserLists();
}

on { AddFriendMessage::class }.then {
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
on { RemoveFriendMessage::class }.then {
    it.removeFriend(username)
}

