import org.apollo.game.message.impl.UpdateIgnoreListMessage
import org.apollo.game.message.impl.decode.AddIgnoreMessage
import org.apollo.game.message.impl.decode.RemoveIgnoreMessage
import org.apollo.game.model.inter.TopLevelPosition
import org.apollo.game.plugin.kotlin.message.IfAction
import org.apollo.game.plugin.kotlin.message.on

on(IfAction, inter = 432, comp = 1) {
    player.interfaceSet.openTopLevel(TopLevelPosition.FRIENDS_TAB)
}

on { AddIgnoreMessage::class }.then {
    it.addIgnore(username)
    it.send(UpdateIgnoreListMessage(UpdateIgnoreListMessage.IgnoreMessageComponent(username)))
}

on { RemoveIgnoreMessage::class }.then {
    it.removeIgnore(username)
}