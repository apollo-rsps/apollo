import org.apollo.cache.def.ItemDefinition
import org.apollo.game.message.impl.SetWidgetTextMessage
import org.apollo.game.model.Item
import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Player
import org.apollo.game.model.inter.InterfaceListener
import org.apollo.game.model.inv.SynchronizationInventoryListener
import java.lang.Math.floor

//functions for creating shops

fun createShopItem(name: String, amount: Int): ShopItem {
    System.out.println("Lookup item: " + name);
    return ShopItem(lookup_item(name)!!.id, -1, -1, amount)
}

fun createShopItem(id: Int, amount: Int): ShopItem {
    System.out.println("Lookup item: " + id);
    return ShopItem(id, -1, -1, amount)
}

fun createShopItem(name: String, amount: Int, sellValue: Int, buyValue: Int): ShopItem {
    return ShopItem(lookup_item(name)!!.id, sellValue, buyValue, amount)
}

fun createShop(name: String, selling: Array<ShopItem>, options:IntArray, buying: Array<ShopItem>): Shop {
    val shop = Shop(name, selling, buying, options, CURRENCY)
    shop.init()
    return shop
}

fun createShop(name: String, selling: Array<ShopItem>, buying: Array<ShopItem>): Shop {
    val shop = Shop(name, selling, buying, intArrayOf(1), CURRENCY)
    shop.init()
    return shop
}

fun createShop(name: String, selling: Array<ShopItem>, buysAll: Boolean): Shop {
    if (buysAll) {
        val shop = Shop(name, selling, null, intArrayOf(1), CURRENCY)
        shop.init()
        return shop
    } else {
        return createShop(name, selling)
    }
}

fun createShop(name: String, selling: Array<ShopItem>): Shop {
    val shop = Shop(name, selling, arrayOf<ShopItem>(), intArrayOf(1), CURRENCY)
    shop.init()
    return shop
}

fun provideShop(npc: String, shop: Shop) {
    val npcID = lookup_npc(npc)!!.id;
    provideShop(npcID, shop);
}

fun provideShop(npc: Int, shop: Shop) {
    SHOPS[npc] = shop;
}

//functions for working with shops

fun openShop(player: Player, npc: Mob, shop: Shop) {
    OPEN_SHOPS[player.username] = shop // save the shop that the user is interacting with
    //Sync inventories
    val invListener = SynchronizationInventoryListener(player, INV_CONTAINER)
    val shopListener = SynchronizationInventoryListener(player, SHOP_CONTAINER)
    player.inventory.addListener(invListener)
    player.inventory.forceRefresh()
    shop.inv.addListener(shopListener)
    shop.inv.forceRefresh()
    //Send message to player
    player.send(SetWidgetTextMessage(SHOP_NAME_WIDGET, shop.name))
    //Setup close action for shop
    val closeListener = ShopCloseInterfaceListener(player, shop, invListener, shopListener)
    player.interfaceSet.openWindowWithSidebar(closeListener, SHOP_UI, INV_SIDEBAR)
}

fun buy(player: Player, shop: Shop, slot: Int, option: Int) {

    val invItem = shop.inv.get(slot)
    //val shopItem = shop.sells[slot]

    if (option == 1) {
        player.sendMessage(ItemDefinition.lookup(invItem.id).name + ": currently costs " + shop.sellValue(invItem.id) + " " + ItemDefinition.lookup(shop.currency.id).name)
        return
    }

    var buyAmount = if (option == 2) 1 else if (option == 3) 5 else if (option == 4) 10 else 0

    var noStock = false
    if (buyAmount > invItem.amount) {
        buyAmount = invItem.amount
        noStock = true
    }

    val has_item = player.inventory.getAmount(invItem.id) == 0

    val definition = invItem.definition
    val spaceRequired = if (definition.isStackable && has_item) 0 else if (!definition.isStackable) buyAmount else 1

    val freeSlots = player.inventory.freeSlots()
    var not_enough_space = false

    if (spaceRequired > freeSlots) {
        not_enough_space = true
        buyAmount = freeSlots
    }

    val totalCurrency = shop.currency.total(player)
    var tooPoor = false
    val total_cost = buyAmount * shop.sellValue(invItem.id)

    if (total_cost > totalCurrency) {
        val tmp = Math.floor(totalCurrency.toDouble() / shop.sellValue(invItem.id).toDouble())
        buyAmount = tmp.toInt()
        tooPoor = true
    }

    if (buyAmount > 0) {
        shop.currency.remove(player, buyAmount * shop.sellValue(invItem.id))
        player.inventory.add(invItem.id, buyAmount)

        val keep = invItem.amount == buyAmount && shop.sells(invItem.id) //Shop does not have quantities on items if it sells this item from its main stock
        if (keep) {
            shop.inv.set(slot, Item(invItem.id, 0))
        } else {

            shop.inv.remove(invItem.id, buyAmount)
        }
    }

    val warning = if (tooPoor) "You don't have enough " + ItemDefinition.lookup(shop.currency.id).name + "."
    else if (noStock) "The shop has run out of stock."
    else if (not_enough_space) "You don\'t have enough inventory space." else ""

    if (!warning.isEmpty()) {
        player.sendMessage(warning)
    }
}

fun sell(player: Player, shop: Shop, id: Int, slot: Int, option: Int) {

    if (shop.isBuyingOwn() && !shop.inv.contains(id) || id == shop.currency.id) {
        player.sendMessage("You can\'t sell this item to this shop.")
        return
    } else if (shop.isBuyingCustom()) {
        var found = false;
        for (item: ShopItem in shop.buying!!) {
            if (item.id == id) {
                found = true
            }
        }
        if (!found) {
            player.sendMessage("You can\'t sell this item to this shop.")
            return
        }
    }

    if (!shop.inv.contains(id) && shop.inv.freeSlots() == 0) {
        player.sendMessage("The shop is currently full at the moment.")
        return
    }

    val item = player.inventory.get(slot)
    val value = shop.buyValue(id)


    if (option == 1) {
        player.sendMessage(ItemDefinition.lookup(item.id).name + ": shop will buy for " + value + " " + ItemDefinition.lookup(shop.currency.id).name + ".")
        return
    }

    var sellAmount = if (option == 2) 1 else if (option == 3) 5 else if (option == 4) 10 else 0

    val available = player.inventory.getAmount(id)
    sellAmount = if (sellAmount > available) available else sellAmount

    val totalValue = Math.floor(value.toDouble() * sellAmount).toInt()

    player.inventory.remove(id, sellAmount)
    shop.inv.add(id, sellAmount)
    if (totalValue > 0) {
        shop.currency.add(player, totalValue)
    }
}


//Listeners

class ShopCloseInterfaceListener(val player: Player, val shop: Shop, val invListener: SynchronizationInventoryListener, val shopListener: SynchronizationInventoryListener) : InterfaceListener {
    override fun interfaceClosed() {
        OPEN_SHOPS.remove(player.username)
        player.inventory.removeListener(invListener)
        player.resetInteractingMob()
        shop.inv.removeListener(shopListener)
    }
}