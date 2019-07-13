package org.apollo.game.plugin.shops

import org.apollo.game.action.DistancedAction
import org.apollo.game.message.handler.ItemVerificationHandler
import org.apollo.game.message.impl.SetWidgetTextMessage
import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Player
import org.apollo.game.model.inter.InterfaceListener
import org.apollo.game.model.inv.Inventory
import org.apollo.game.model.inv.SynchronizationInventoryListener

/**
 * A [DistancedAction] that opens a [Shop].
 */
class OpenShopAction(
    player: Player,
    private val shop: Shop,
    private val operator: Mob
) : DistancedAction<Player>(0, true, player, operator.position, 1) { // TODO this needs to follow the NPC if they move

    override fun executeAction() {
        mob.interactingMob = operator

        val closeListener = addInventoryListeners(mob, shop.inventory)
        mob.send(SetWidgetTextMessage(ShopInterfaces.SHOP_NAME, shop.name))

        mob.interfaceSet.openWindowWithSidebar(closeListener, ShopInterfaces.SHOP_WINDOW,
            ShopInterfaces.INVENTORY_SIDEBAR)
        stop()
    }

    /**
     * Adds [SynchronizationInventoryListener]s to the [Player] and [Shop] [Inventories][Inventory], returning an
     * [InterfaceListener] that removes them when the interface is closed.
     */
    private fun addInventoryListeners(player: Player, shop: Inventory): InterfaceListener {
        val invListener = SynchronizationInventoryListener(player, ShopInterfaces.INVENTORY_CONTAINER)
        val shopListener = SynchronizationInventoryListener(player, ShopInterfaces.SHOP_CONTAINER)

        player.inventory.addListener(invListener)
        player.inventory.forceRefresh()

        shop.addListener(shopListener)
        shop.forceRefresh()

        return InterfaceListener {
            mob.interfaceSet.close()
            mob.resetInteractingMob()

            mob.inventory.removeListener(invListener)
            shop.removeListener(shopListener)
        }
    }
}

/**
 * An [InventorySupplier] that returns a [Player]'s [Inventory] if they are browsing a shop.
 */
object PlayerInventorySupplier : ItemVerificationHandler.InventorySupplier {

    override fun getInventory(player: Player): Inventory? {
        return if (Interfaces.SHOP_WINDOW in player.interfaceSet) {
            player.inventory
        } else {
            null
        }
    }

}