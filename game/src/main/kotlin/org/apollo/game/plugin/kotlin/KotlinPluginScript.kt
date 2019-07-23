package org.apollo.game.plugin.kotlin

import org.apollo.game.command.CommandListener
import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.message.impl.ButtonMessage
import org.apollo.game.model.World
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.model.event.Event
import org.apollo.game.model.event.EventListener
import org.apollo.game.model.event.PlayerEvent
import org.apollo.game.plugin.PluginContext
import org.apollo.net.message.Message
import kotlin.reflect.KClass
import kotlin.script.experimental.annotations.KotlinScript

@KotlinScript("Apollo Plugin Script", fileExtension = "plugin.kts")
abstract class KotlinPluginScript(var world: World, val context: PluginContext) {

    private var startListener: (World) -> Unit = { _ -> }

    private var stopListener: (World) -> Unit = { _ -> }

    fun <T : Any, C : ListenableContext, I : PredicateContext> on(
        listenable: Listenable<T, C, I>,
        callback: C.() -> Unit
    ) {
        registerListener(listenable, null, callback)
    }

    internal fun <T : Any, C : ListenableContext, I : PredicateContext> registerListener(
        listenable: Listenable<T, C, I>,
        predicateContext: I?,
        callback: C.() -> Unit
    ) {
        // Smart-casting/type-inference is completely broken in this function in intelliJ, so assign to otherwise
        // pointless `l` values for now.

        return when (listenable) {
            is MessageListenable -> {
                @Suppress("UNCHECKED_CAST")
                val l = listenable as MessageListenable<Message, C, I>

                val handler = l.createHandler(world, predicateContext, callback)
                context.addMessageHandler(l.type.java, handler)
            }
            is EventListenable -> {
                @Suppress("UNCHECKED_CAST")
                val l = listenable as EventListenable<Event, C, I>

                val handler = l.createHandler(world, predicateContext, callback)
                world.listenFor(l.type.java, handler)
            }
        }
    }

    /**
     * Create a [CommandListener] for the given [command] name, which only players with a [PrivilegeLevel]
     * of [privileges] and above can use.
     */
    fun on_command(command: String, privileges: PrivilegeLevel): KotlinCommandHandler { // TODO what to do with this?
        return KotlinCommandHandler(world, command, privileges)
    }

    /**
     * Creates a [MessageHandler].
     */
    @Deprecated("Use new on(Type) listener")
    fun <T : Message> on(type: () -> KClass<T>): OldKotlinMessageHandler<T> {
        return OldKotlinMessageHandler(world, context, type())
    }

    /**
     * Create an [EventListener] for a [PlayerEvent].
     */
    @Deprecated("Use new on(Type) listener")
    fun <T : PlayerEvent> on_player_event(type: () -> KClass<T>): OldKotlinPlayerEventHandler<T> {
        return OldKotlinPlayerEventHandler(world, type())
    }

    /**
     * Create an [EventListener] for an [Event].
     */
    @Deprecated("Use new on(Type) listener")
    fun <T : Event> on_event(type: () -> KClass<T>): OldKotlinEventHandler<T> {
        return OldKotlinEventHandler(world, type())
    }

    /**
     * Create a [ButtonMessage] [MessageHandler] for the given [id].
     */
    @Deprecated("Use new on(Type) listener")
    fun on_button(id: Int): KotlinPlayerHandlerProxyTrait<ButtonMessage> {
        return on { ButtonMessage::class }.where { widgetId == id }
    }

    fun start(callback: (World) -> Unit) {
        startListener = callback
    }

    fun stop(callback: (World) -> Unit) {
        stopListener = callback
    }

    fun doStart(world: World) {
        startListener(world)
    }

    fun doStop(world: World) {
        stopListener(world)
    }

}