package org.apollo.game.plugin.kotlin

import org.apollo.game.model.entity.Player
import org.apollo.game.model.event.Event
import org.apollo.game.model.event.PlayerEvent
import org.apollo.net.message.Message
import kotlin.reflect.KClass

/**
 * A game occurrence that can be listened to.
 */
sealed class Listenable<T : ListenableContext, F : Any> {
    abstract val type: KClass<F>
}

abstract class EventListenable<T : ListenableContext, F : Event> : Listenable<T, F>() {
    abstract fun from(event: F): T
}

abstract class MessageListenable<T : ListenableContext, F : Message> : Listenable<T, F>() {
    abstract fun from(player: Player, message: F): T
}

abstract class PlayerEventListenable<T : ListenableContext, F : PlayerEvent> : EventListenable<T, F>() {

    abstract fun from(player: Player, event: F): T

    override fun from(event: F): T {
        return from(event.player, event)
    }

}