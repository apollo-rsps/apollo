import org.apollo.game.model.inter.TopLevelPosition
import org.apollo.game.plugin.kotlin.message.IfAction
import org.apollo.game.plugin.kotlin.message.on

val LOGOUT_COMPONENT = 8

on(IfAction, inter = TopLevelPosition.LOGOUT_TAB.interfaceId, comp = LOGOUT_COMPONENT) {
    player.logout()
}