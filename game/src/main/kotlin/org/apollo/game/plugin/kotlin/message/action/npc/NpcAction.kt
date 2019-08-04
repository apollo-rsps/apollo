package org.apollo.game.plugin.kotlin.message.action.npc

import org.apollo.cache.def.NpcDefinition
import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.message.impl.NpcActionMessage
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.World
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.kotlin.KotlinPluginScript
import org.apollo.game.plugin.kotlin.MessageListenable
import org.apollo.game.plugin.kotlin.message.action.ActionContext
import org.apollo.game.plugin.kotlin.message.action.obj.InteractiveObject
import org.apollo.game.plugin.kotlin.message.action.obj.ObjectAction
import org.apollo.game.plugin.kotlin.message.action.obj.ObjectActionPredicateContext

class NpcAction(override val option: String, override val player: Player, val target: Npc) : ActionContext {

    companion object : MessageListenable<NpcActionMessage, NpcAction, NpcActionPredicateContext>() {
        override val type = NpcActionMessage::class

        override fun createHandler(world: World, predicateContext: NpcActionPredicateContext, callback: NpcAction.() -> Unit): MessageHandler<NpcActionMessage> {
            val ids = predicateContext.npcDefinitions.map(NpcDefinition::getId).toSet()

            return handler(world) { player, message ->
                val npc = world.npcRepository[message.index]
                val option = npc.definition.interactions[message.option]
                val npcMatched = ids.isEmpty() || npc.id in ids

                if (npcMatched && predicateContext.option.equals(option, ignoreCase = true)) {
                    val context = NpcAction(option, player, npc)
                    context.callback()
                }
            }
        }
    }
}
