package org.apollo.game.plugin.shops

import org.apollo.game.message.handler.ItemVerificationHandler
import org.apollo.game.message.impl.decode.ItemActionMessage
import org.apollo.game.message.impl.decode.NpcActionMessage
import org.apollo.game.model.entity.Mob
import org.apollo.game.scheduling.ScheduledTask

fun Mob.shop(): Shop? = SHOPS[definition.id]

start { world ->
    ItemVerificationHandler.addInventory(ShopInterfaces.SHOP_INTERFACE) { it.interactingMob?.shop()?.inventory }
    ItemVerificationHandler.addInventory(ShopInterfaces.INVENTORY_INTERFACE, PlayerInventorySupplier)

    world.schedule(object : ScheduledTask(Shop.RESTOCK_INTERVAL, false) {
        override fun execute() = SHOPS.values.distinct().forEach(Shop::restock)
    })
}

on { NpcActionMessage::class }
    .then { player ->
        val npc = player.world.npcRepository.get(index)
        val shop = npc.shop() ?: return@then

        if (shop.action == option) {
            player.startAction(OpenShopAction(player, shop, npc))
            terminate()
        }
    }


on { ItemActionMessage::class }
    .where { interfaceId == ShopInterfaces.SHOP_INTERFACE || interfaceId == ShopInterfaces.INVENTORY_INTERFACE }
    .then { player ->
        if (ShopInterfaces.SHOP_INTERFACE !in player.interfaceSet) {
            return@then
        }

        val shop = player.interactingMob?.shop() ?: return@then
        when (interfaceId) {
            ShopInterfaces.INVENTORY_INTERFACE -> shop.buy(player, slot, option)
            ShopInterfaces.SHOP_INTERFACE -> shop.sell(player, slot, option)
            else -> error("Supposedly unreacheable case.")
        }

        terminate()
    }
