package org.apollo.game.plugin.kotlin

import org.apollo.game.command.Command
import org.apollo.game.command.CommandListener
import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.model.event.EventListener
import org.apollo.game.model.event.PlayerEvent
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

    fun <T : Message> on(type: () -> KClass<T>) = KotlinMessageHandler<T>(world, context, type.invoke())

    fun <T : PlayerEvent> on_player_event(type: () -> KClass<T>) = KotlinPlayerEventHandler(world, type.invoke())

    fun on_command(command: String, privileges: PrivilegeLevel) = KotlinCommandHandler(world, command, privileges)


    fun start(callback: (World) -> Unit) {
        this.startListener = callback
    }

    fun stop(callback: (World) -> Unit) {
        this.stopListener = callback
    }

    fun doStart(world: World) {
        this.startListener.invoke(world)
    }

    fun doStop(world: World) {
        this.stopListener.invoke(world)
    }
}

interface KotlinPlayerHandlerProxyTrait<S : Any> {

    var callback: S.(Player) -> Unit
    var predicate: S.() -> Boolean

    fun where(predicate: S.() -> Boolean): KotlinPlayerHandlerProxyTrait<S> {
        this.predicate = predicate
        return this
    }

    fun then(callback: S.(Player) -> Unit) {
        this.callback = callback
        this.register()
    }

    fun register()

    fun handleProxy(player: Player, subject: S) {
        if (subject.predicate()) {
            subject.callback(player)
        }
    }
}

class KotlinPlayerEventHandler<T : PlayerEvent>(val world: World, val type: KClass<T>) :
        KotlinPlayerHandlerProxyTrait<T>, EventListener<T> {

    override var callback: T.(Player) -> Unit = {}
    override var predicate: T.() -> Boolean = { true }

    override fun handle(event: T) = handleProxy(event.player, event)
    override fun register() = world.listenFor(type.java, this)

}

class KotlinMessageHandler<T : Message>(val world: World, val context: PluginContext, val type: KClass<T>) :
        KotlinPlayerHandlerProxyTrait<T>, MessageHandler<T>(world) {

    override var callback: T.(Player) -> Unit = {}
    override var predicate: T.() -> Boolean = { true }

    override fun handle(player: Player, message: T) = handleProxy(player, message)
    override fun register() = context.addMessageHandler(type.java, this)

}

class KotlinCommandHandler(val world: World, val command: String, privileges: PrivilegeLevel) :
        KotlinPlayerHandlerProxyTrait<Command>, CommandListener(privileges) {

    override var callback: Command.(Player) -> Unit = {}
    override var predicate: Command.() -> Boolean = { true }

    override fun execute(player: Player, command: Command) = handleProxy(player, command)
    override fun register() = world.commandDispatcher.register(command, this)

}
