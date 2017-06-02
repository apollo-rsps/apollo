package org.apollo.game.plugin.kotlin

import org.apollo.game.command.Command
import org.apollo.game.command.CommandListener
import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.plugin.PluginContext
import org.apollo.net.message.Message
import kotlin.reflect.KClass
import kotlin.script.templates.ScriptTemplateDefinition

@ScriptTemplateDefinition(
        scriptFilePattern = ".*\\.plugin\\.kts"
)
abstract class KotlinPluginScript(private var world: World, val context: PluginContext) {
    var startListener: (World) -> Unit = { _ -> };
    var stopListener: (World) -> Unit = { _ -> };

    protected fun <T : Message> on(type: () -> KClass<T>): KotlinMessageHandler<T> {
        return KotlinMessageHandler(world, context, type.invoke())
    }

    protected fun on_command(command: String, privileges: PrivilegeLevel): KotlinCommandHandler {
        return KotlinCommandHandler(world, command, privileges)
    }

    protected fun start(callback: (World) -> Unit) {
        this.startListener = callback
    }

    protected fun stop(callback: (World) -> Unit) {
        this.stopListener = callback
    }

    fun doStart(world: World) {
        this.startListener.invoke(world)
    }

    fun doStop(world: World) {
        this.stopListener.invoke(world)
    }
}

class KotlinMessageHandler<T : Message>(val world: World, val context: PluginContext, val type: KClass<T>) : MessageHandler<T>(world) {

    override fun handle(player: Player, message: T) {
        if (message.predicate()) {
            message.function(player)
        }
    }

    var function: T.(Player) -> Unit = { _ -> }

    var predicate: T.() -> Boolean = { true }

    fun where(predicate: T.() -> Boolean): KotlinMessageHandler<T> {
        this.predicate = predicate
        return this
    }

    fun then(function: T.(Player) -> Unit) {
        this.function = function
        this.context.addMessageHandler(type.java, this)
    }

}

class KotlinCommandListener(val level: PrivilegeLevel, val function: (Player, Command) -> Unit) : CommandListener(level) {

    override fun execute(player: Player, command: Command) {
        function.invoke(player, command)
    }

}

class KotlinCommandHandler(val world : World, val command: String, val privileges: PrivilegeLevel) {

    var function: (Player, Command) -> Unit = { _, _ -> }

    fun then(function: (Player, Command) -> Unit) {
        this.function = function
        world.commandDispatcher.register(command, KotlinCommandListener(privileges, function))
    }

}
