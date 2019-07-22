package org.apollo.game.plugin.kotlin

import org.apollo.game.model.entity.Player
import org.apollo.game.model.event.Event
import org.apollo.game.model.event.PlayerEvent
import org.apollo.net.message.Message
import kotlin.reflect.KClass

/**
 * A game occurrence that can be listened to.
 */
sealed class Listenable<T : Any, C : ListenableContext> {
    abstract val type: KClass<T>
}

abstract class EventListenable<T : Event, C : ListenableContext> : Listenable<T, C>() {
    abstract fun createContext(event: T): C?
}

abstract class MessageListenable<T : Message, C : ListenableContext> : Listenable<T, C>() {
    abstract fun createContext(player: Player, message: T): C?
}

abstract class PlayerEventListenable<T : PlayerEvent, C : ListenableContext> : EventListenable<T, C>() {

    abstract fun createContext(player: Player, event: T): C?

    final override fun createContext(event: T): C? {
        return createContext(event.player, event)
    }

}