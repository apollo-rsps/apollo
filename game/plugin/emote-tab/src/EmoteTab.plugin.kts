import org.apollo.game.message.impl.decode.ButtonMessage

on { ButtonMessage::class }
    .where { componentId in Emote.MAP }
    .then { player ->
        player.playAnimation(Emote.fromButton(componentId)!!.animation)
    }