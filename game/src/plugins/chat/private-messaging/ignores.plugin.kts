import org.apollo.game.message.impl.AddIgnoreMessage
import org.apollo.game.message.impl.RemoveIgnoreMessage

on { AddIgnoreMessage::class }
        .then { it.addIgnore(username) }

on { RemoveIgnoreMessage::class }
        .then { it.removeIgnore(username) }