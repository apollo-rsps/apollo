import org.apollo.game.message.impl.IfActionMessage

val LOGOUT_COMPONENT = 8
val LOGOUT_INTERFACE = 182

on { IfActionMessage::class }
    .where { interfaceId == LOGOUT_INTERFACE && componentId == LOGOUT_COMPONENT }
    .then { player ->
        player.logout()
    }