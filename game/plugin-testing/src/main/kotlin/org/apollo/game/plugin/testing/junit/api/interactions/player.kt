package org.apollo.game.plugin.testing.junit.api.interactions

import org.apollo.game.message.impl.ItemOptionMessage
import org.apollo.game.model.entity.Player

/**
 * Send an [ItemOptionMessage] for the given [id], [option], [slot], and [interfaceId], simulating a
 * player interacting with an item.
 */
fun Player.interactWithItem(id: Int, option: Int, slot: Int? = null, interfaceId: Int? = null) {
    send(ItemOptionMessage(option, interfaceId ?: -1, id, slot ?: inventory.slotOf(id)))
}