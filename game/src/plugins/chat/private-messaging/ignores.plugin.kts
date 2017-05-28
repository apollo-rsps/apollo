import org.apollo.game.message.impl.AddIgnoreMessage
import org.apollo.game.message.impl.RemoveIgnoreMessage

on { AddIgnoreMessage::class }
        .then { player -> player.addIgnore(username) }

on { RemoveIgnoreMessage::class }
        .then { player -> player.removeIgnore(username) }