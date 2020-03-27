import org.apollo.game.message.impl.decode.AddIgnoreMessage
import org.apollo.game.message.impl.decode.RemoveIgnoreMessage

on { AddIgnoreMessage::class }
        .then { it.addIgnore(username) }

on { RemoveIgnoreMessage::class }
        .then { it.removeIgnore(username) }