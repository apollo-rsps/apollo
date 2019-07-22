package org.apollo.game.plugin.kotlin

import org.apollo.game.model.entity.Player

/**
 * A proxy interface for any handler that operates on [Player]s.
 */
@Deprecated("To be removed")
interface KotlinPlayerHandlerProxyTrait<S : Any> {

    var callback: S.(Player) -> Unit
    var predicate: S.() -> Boolean

    fun register()

    fun where(predicate: S.() -> Boolean): KotlinPlayerHandlerProxyTrait<S> {
        this.predicate = predicate
        return this
    }

    fun then(callback: S.(Player) -> Unit) {
        this.callback = callback
        this.register()
    }

    fun handleProxy(player: Player, subject: S) {
        if (subject.predicate()) {
            subject.callback(player)
        }
    }
}
