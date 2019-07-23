package org.apollo.game.plugin.kotlin

import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.model.World
import org.apollo.game.model.event.Event
import org.apollo.game.model.event.EventListener
import org.apollo.net.message.Message
import kotlin.reflect.KClass

/**
 * A game occurrence that can be listened to.
 */
sealed class Listenable<T : Any, C : ListenableContext, P : PredicateContext> {
    abstract val type: KClass<T>
}

abstract class EventListenable<T : Event, C : ListenableContext, P : PredicateContext> : Listenable<T, C, P>() {

    abstract fun createHandler(world: World, predicateContext: P?, callback: C.() -> Unit): EventListener<T>

}

abstract class MessageListenable<T : Message, C : ListenableContext, P : PredicateContext> : Listenable<T, C, P>() {

    abstract fun createHandler(world: World, predicateContext: P?, callback: C.() -> Unit): MessageHandler<T>

}
