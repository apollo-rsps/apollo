package org.apollo.game.plugin.kotlin

import org.apollo.game.command.Command
import org.apollo.game.command.CommandListener
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.setting.PrivilegeLevel

/**
 * A handler for [Command]s.
 */
class KotlinCommandHandler(
    val world: World,
    val command: String,
    privileges: PrivilegeLevel
) : CommandListener(privileges) {

    var callback: Command.(Player) -> Unit = {}
    var predicate: Command.() -> Boolean = { true }

    override fun execute(player: Player, command: Command) {
        if (command.predicate()) {
            command.callback(player)
        }
    }

    fun register() {
        world.commandDispatcher.register(command, this)
    }

    fun where(predicate: Command.() -> Boolean): KotlinCommandHandler {
        this.predicate = predicate
        return this
    }

    fun then(callback: Command.(Player) -> Unit) {
        this.callback = callback
        this.register()
    }

}