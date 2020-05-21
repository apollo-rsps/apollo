package org.apollo.game.plugin.kotlin

import org.apollo.game.model.World
import org.apollo.game.model.event.Event
import org.apollo.game.model.event.EventListener
import kotlin.reflect.KClass

/**
 * A handler for [Event]s.
 */
@Deprecated("To be removed")
class OldKotlinEventHandler<S : Event>(val world: World, val type: KClass<S>) : EventListener<S> {

    private var callback: S.() -> Unit = {}
    private var predicate: S.() -> Boolean = { true }

    fun where(predicate: S.() -> Boolean): OldKotlinEventHandler<S> {
        this.predicate = predicate
        return this
    }

    fun then(callback: S.() -> Unit) {
        this.callback = callback
        this.register()
    }

    override fun handle(event: S) {
        if (event.predicate()) {
            event.callback()
        }
    }

    fun register() = world.listenFor(type.java, this)
}