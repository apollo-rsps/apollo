import org.apollo.game.message.impl.IfActionMessage

on { IfActionMessage::class }
    .where { componentId in Emote.MAP }
    .then { player ->
        player.playAnimation(Emote.fromButton(componentId)!!.animation)
    }