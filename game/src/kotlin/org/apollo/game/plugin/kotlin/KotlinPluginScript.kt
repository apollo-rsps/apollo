package org.apollo.game.plugin.kotlin

import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.PluginContext
import org.apollo.net.message.Message
import kotlin.reflect.KClass
import kotlin.script.templates.ScriptTemplateDefinition

@ScriptTemplateDefinition(
        scriptFilePattern = ".*\\.plugin\\.kts"
)
abstract class KotlinPluginScript(val world: World, val context: PluginContext) {

    protected fun <T : Message> on(type: () -> KClass<T>): KotlinMessageHandler<T> {
        return KotlinMessageHandler(world, context, type.invoke())
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
