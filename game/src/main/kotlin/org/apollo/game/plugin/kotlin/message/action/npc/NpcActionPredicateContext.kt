package org.apollo.game.plugin.kotlin.message.action.npc

import org.apollo.cache.def.NpcDefinition
import org.apollo.game.plugin.kotlin.message.action.ActionPredicateContext
import org.apollo.game.plugin.kotlin.message.action.obj.InteractiveObject

data class NpcActionPredicateContext(
    override val option: String,
    val npcDefinitions: List<NpcDefinition>
) : ActionPredicateContext(option)