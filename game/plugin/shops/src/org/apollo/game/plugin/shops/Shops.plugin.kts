package org.apollo.game.plugin.shops

import org.apollo.game.message.handler.ItemVerificationHandler
import org.apollo.game.message.impl.ItemActionMessage
import org.apollo.game.message.impl.NpcActionMessage
import org.apollo.game.model.entity.Mob
import org.apollo.game.scheduling.ScheduledTask

fun Mob.shop(): Shop? = SHOPS[definition.id]

start { world ->
    ItemVerificationHandler.addInventory(ShopInterfaces.SHOP_CONTAINER) { it.interactingMob?.shop()?.inventory }
    ItemVerificationHandler.addInventory(ShopInterfaces.INVENTORY_CONTAINER, PlayerInventorySupplier)

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
    .where { interfaceId == ShopInterfaces.SHOP_CONTAINER || interfaceId == ShopInterfaces.INVENTORY_CONTAINER }
    .then { player ->
        if (ShopInterfaces.SHOP_WINDOW !in player.interfaceSet) {
            return@then
        }

        val shop = player.interactingMob?.shop() ?: return@then
        when (interfaceId) {
            ShopInterfaces.INVENTORY_CONTAINER -> shop.buy(player, slot, option)
            ShopInterfaces.SHOP_CONTAINER -> shop.sell(player, slot, option)
            else -> error("Supposedly unreacheable case.")
        }

        terminate()
    }
