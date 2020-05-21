package org.apollo.game.plugin.skills.fishing

import org.apollo.game.message.impl.NpcActionMessage

// TODO: moving fishing spots, seaweed and caskets, evil bob

/**
 * Intercepts the [NpcActionMessage] and starts a [FishingAction] if the npc
 */
on { NpcActionMessage::class }
    .where { option == 1 || option == 3 }
    .then { player ->
        val entity = player.world.npcRepository[index]
        val option = FishingSpot.lookup(entity.id)?.option(option) ?: return@then

        val target = FishingTarget(entity.position, option)
        player.startAction(FishingAction(player, target))

        terminate()
    }
