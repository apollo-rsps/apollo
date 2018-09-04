import org.apollo.game.message.impl.ButtonMessage

on { ButtonMessage::class }
    .where { widgetId in Emote.MAP }
    .then { player ->
        player.playAnimation(Emote.fromButton(widgetId)!!.animation)
    }