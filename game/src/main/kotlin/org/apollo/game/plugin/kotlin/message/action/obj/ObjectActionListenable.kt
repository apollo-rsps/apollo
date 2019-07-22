package org.apollo.game.plugin.kotlin.message.action.obj

import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.kotlin.MessageListenable

abstract class ObjectActionListenable : MessageListenable<ObjectActionMessage, ObjectAction<*>>() {

    override val type = ObjectActionMessage::class

    abstract fun <T : InteractiveObject> createContext(
        player: Player,
        other: ObjectActionMessage,
        objects: List<T>
    ): ObjectAction<T>?

}