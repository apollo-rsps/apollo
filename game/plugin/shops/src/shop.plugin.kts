import org.apollo.game.message.handler.ItemVerificationHandler
import org.apollo.game.message.impl.ItemActionMessage
import org.apollo.game.message.impl.NpcActionMessage
import org.apollo.game.model.entity.Mob
import org.apollo.game.plugin.shops.Interfaces
import org.apollo.game.plugin.shops.OpenShopAction
import org.apollo.game.plugin.shops.PlayerInventorySupplier
import org.apollo.game.plugin.shops.SHOPS
import org.apollo.game.plugin.shops.Shop
import org.apollo.game.scheduling.ScheduledTask

fun Mob.shop(): Shop? = SHOPS[definition.id]

start {
    ItemVerificationHandler.addInventory(Interfaces.SHOP_CONTAINER) { it.interactingMob?.shop()?.inventory }
    ItemVerificationHandler.addInventory(Interfaces.INVENTORY_CONTAINER, PlayerInventorySupplier())

    it.schedule(object : ScheduledTask(Shop.RESTOCK_INTERVAL, false) {
        override fun execute() = SHOPS.values.distinct().forEach(Shop::restock)
    })
}

on { NpcActionMessage::class }
    .then {
        val npc = it.world.npcRepository.get(index)
        val shop = npc.shop() ?: return@then

        if (shop.action == option) {
            it.startAction(OpenShopAction(it, shop, npc))
            terminate()
        }
    }


on { ItemActionMessage::class }
    .where { interfaceId == Interfaces.SHOP_CONTAINER || interfaceId == Interfaces.INVENTORY_CONTAINER }
    .then {
        if (!it.interfaceSet.contains(Interfaces.SHOP_WINDOW)) {
            return@then
        }

        val shop = it.interactingMob?.shop() ?: return@then
        when (interfaceId) {
            Interfaces.INVENTORY_CONTAINER -> shop.buy(it, slot, option)
            Interfaces.SHOP_CONTAINER -> shop.sell(it, slot, option)
            else -> throw IllegalStateException("Supposedly unreacheable case.")
        }

        terminate()
    }