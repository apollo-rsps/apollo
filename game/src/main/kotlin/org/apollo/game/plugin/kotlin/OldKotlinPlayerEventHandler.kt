package org.apollo.game.plugin.kotlin

import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.event.EventListener
import org.apollo.game.model.event.PlayerEvent
import kotlin.reflect.KClass

/**
 * A handler for [PlayerEvent]s.
 */
@Deprecated("To be removed")
class OldKotlinPlayerEventHandler<T : PlayerEvent>(val world: World, val type: KClass<T>) :
    KotlinPlayerHandlerProxyTrait<T>, EventListener<T> {

    override var callback: T.(Player) -> Unit = {}
    override var predicate: T.() -> Boolean = { true }

    override fun handle(event: T) = handleProxy(event.player, event)
    override fun register() = world.listenFor(type.java, this)
}