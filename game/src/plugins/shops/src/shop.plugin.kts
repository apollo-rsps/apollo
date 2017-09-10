import org.apollo.game.action.DistancedAction
import org.apollo.game.message.handler.ItemVerificationHandler
import org.apollo.game.message.impl.ItemActionMessage
import org.apollo.game.message.impl.NpcActionMessage
import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.game.model.inv.Inventory

//Actions
class OpenShopAction(val player: Player, val shop: Shop, val npc: Mob) : DistancedAction<Player>(1, true, player, npc.position, 1) {
    override fun executeAction() {
        mob.interactingMob = npc
        openShop(player, npc, shop)
        stop()
    }
}

class ShopInventorySupplier: ItemVerificationHandler.InventorySupplier {
    override fun getInventory(player: Player?): Inventory {
        if (player == null || OPEN_SHOPS[player.username] == null) {
            return Inventory(0)
        }
        return OPEN_SHOPS[player.username]!!.inv
    }
}

class PlayerInventorySupplier: ItemVerificationHandler.InventorySupplier {
    override fun getInventory(player: Player?): Inventory {
        if (player == null || OPEN_SHOPS[player.username] == null) {
            return Inventory(0)
        }
        return player.inventory
    }
}

//Open shop
on { NpcActionMessage::class}
        .where{option == 1}
        .then {
            val npc = it.world.npcRepository.get(index)
            if (SHOPS[npc.id] != null) {
                val shop = SHOPS[npc.id]
                if (shop!!.options.isEmpty() || shop.options.contains(option)) {
                    it.startAction(OpenShopAction(it, shop, npc))
                }
            }
        }


//Item action in shop
on { ItemActionMessage::class }
        .then {
            //Check if the player is in a shop
            if (!OPEN_SHOPS.containsKey(it.username) || (interfaceId != INV_CONTAINER && interfaceId != SHOP_CONTAINER)) {
                terminate()
            } else {
                val shop = OPEN_SHOPS[it.username]!!
                if (interfaceId == INV_CONTAINER) {
                    sell(it, shop, id, slot, option)
                    terminate()
                } else if (interfaceId == SHOP_CONTAINER) {
                    buy(it, shop, slot, option)
                    terminate()
                }
            }

        }


start {
    ItemVerificationHandler.addInventory(SHOP_CONTAINER, ShopInventorySupplier())
    ItemVerificationHandler.addInventory(INV_CONTAINER, PlayerInventorySupplier())
}