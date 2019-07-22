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
) : KotlinPlayerHandlerProxyTrait<Command>, CommandListener(privileges) {

    override var callback: Command.(Player) -> Unit = {}
    override var predicate: Command.() -> Boolean = { true }

    override fun execute(player: Player, command: Command) {
        handleProxy(player, command)
    }

    override fun register() {
        world.commandDispatcher.register(command, this)
    }

}