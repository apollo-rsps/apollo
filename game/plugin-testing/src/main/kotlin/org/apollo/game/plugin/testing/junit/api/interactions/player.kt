package org.apollo.game.plugin.testing.junit.api.interactions

import org.apollo.game.message.impl.decode.ItemOptionMessage
import org.apollo.game.message.impl.decode.NpcActionMessage
import org.apollo.game.message.impl.decode.ObjectActionMessage
import org.apollo.game.message.impl.decode.PlayerActionMessage
import org.apollo.game.model.Direction
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Entity
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.GameObject

/**
 * Send an [ItemOptionMessage] for the given [id], [option], [slot], and [interfaceId], simulating a
 * player interacting with an item.
 */
fun Player.interactWithItem(id: Int, option: Int, slot: Int? = null, interfaceId: Int? = null, componentId: Int? = null) {
    send(ItemOptionMessage(option, interfaceId ?: -1, componentId
            ?: -1, id, slot ?: inventory.slotOf(id)))
}

/**
 * Spawn a new object (defaulting to in-front of the player) and immediately interact with it.
 */
fun Player.interactWithObject(id: Int, option: Int, at: Position? = null) {
    val obj = world.spawnObject(id, at ?: position.step(1, Direction.NORTH))
    interactWith(obj, option)
}

/**
 * Move the player within interaction distance to the given [Entity] and fake an action
 * message.
 */
fun Player.interactWith(entity: Entity, option: Int = 1) {
    position = entity.position.step(1, Direction.NORTH)

    when (entity) {
        is GameObject -> send(ObjectActionMessage(option, entity.id, entity.position, 0))
        is Npc -> send(NpcActionMessage(option, entity.index, 0))
        is Player -> send(PlayerActionMessage(option, entity.index, 0))
    }
}