import org.apollo.game.message.impl.decode.IfActionMessage

on { IfActionMessage::class }
    .where { componentId in Emote.MAP }
    .then { player ->
        player.playAnimation(Emote.fromButton(componentId)!!.animation)
    }