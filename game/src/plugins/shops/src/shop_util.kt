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
    //This is really rough. I just ported it 1 to 1 from the ruby.

    val invItem = shop.inv.get(slot)
    val shopItem = shop.sells[slot]

    val id = shopItem.id

    //option = message.option
    if (option == 1) {
        player.sendMessage(ItemDefinition.lookup(shopItem.id).name + ": currently costs " + shopItem.sellValue + ItemDefinition.lookup(shop.currency.id).name)
        return
    }

    var buyAmount = if (option == 2) 1 else if (option == 3) 5 else if (option == 4) 10 else 0

    var noStock = false
    if (buyAmount > invItem.amount) {
        buyAmount = invItem.amount
        noStock = true
    }

    //player_inventory = player.inventory
    val has_item = player.inventory.getAmount(id) == 0

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
    val total_cost = buyAmount * shopItem.sellValue

    if (total_cost > totalCurrency) {
        val tmp = Math.floor(totalCurrency.toDouble() / shopItem.sellValue.toDouble())
        buyAmount = tmp.toInt()
        tooPoor = true
    }

    if (buyAmount > 0) {
        shop.currency.remove(player, buyAmount * shopItem.sellValue)
        player.inventory.add(id, buyAmount)

        val keep = invItem.amount == buyAmount && shop.isBuyingOwn() //Shop does not have quantities on items if buying own
        if (keep) {
            shop.inv.set(slot, Item(id, 0))
        } else {
            shop.inv.remove(id, buyAmount)
        }
        //keep ? inventory.set(slot, Item.new(id, 0)) : inventory.remove(id, buy_Amount)
    }

    val warning = if (tooPoor) "You don't have enough #{currency.name}."
    else if (noStock) "The shop has run out of stock."
    else if (not_enough_space) "You don\'t have enough inventory space." else ""

    if (!warning.isEmpty()) {
        player.sendMessage(warning)
    }
}

fun sell(player: Player, shop: Shop, id: Int, slot: Int, option: Int) {
    //This is really rough. I just ported it 1 to 1 from the ruby.

    if (shop.isBuyingOwn() && !shop.inv.contains(id)) {
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
        player.sendMessage("#{item.definition.name}: shop will buy for #{value} #{currency.name}.")
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