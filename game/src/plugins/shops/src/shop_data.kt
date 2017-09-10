import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.Item
import org.apollo.game.model.entity.Player
import org.apollo.game.model.inv.Inventory

val INV_SIDEBAR = 3822 //The sidebar id for the inventory, when a Shop window is open.
val INV_CONTAINER = 2323 //The container id for the above inventory, when a Shop window is open.
val SHOP_UI = 3824
val SHOP_CONTAINER = 3900
val SHOP_NAME_WIDGET = 3901
//
val SHOP_CAPACITY = 30
val CURRENCY = Currency(995) //Coins
//
val SHOPS = mutableMapOf<Int, Shop>() //Map shops to npcs
val OPEN_SHOPS = mutableMapOf<String, Shop>() //Map shops to players that have them open


data class ShopItem(val id: Int, val sellValue: Int, val buyValue: Int, val amount: Int)

class Currency(val id: Int) {
    fun add(player: Player, amount: Int) {
        player.inventory.add(id, amount)
    }

    fun remove(player: Player, amount: Int) {
        player.inventory.remove(id, amount)
    }

    fun total(player: Player): Int {
        return player.inventory.getAmount(id)
    }

    /**
     * How much a shop should buy something for when using this currency
     */
    fun buyValue(id: Int): Int {
        return Math.floor(ItemDefinition.lookup(id).value* 0.60).toInt()
    }

    fun sellValue(id: Int): Int {
        return ItemDefinition.lookup(id).value
    }
}

class Shop(val name: String, val sells: Array<ShopItem>, val buying: Array<ShopItem>?, val options: IntArray, val currency: Currency) {
    val inv = Inventory(SHOP_CAPACITY, Inventory.StackMode.STACK_ALWAYS)

    fun init() { //Init the shop for sale of the defined items
        inv.clear()
        for (item: ShopItem in sells) {
            inv.add(item.id, item.amount)
        }
    }

    fun isBuyingAll(): Boolean {
        return buying == null
    }

    fun isBuyingOwn(): Boolean {
        return buying != null && buying.isEmpty()
    }

    fun isBuyingCustom(): Boolean {
        return buying != null && buying.isNotEmpty()
    }

    /**
     * How much a shop should buy something for
     */
    fun buyValue(id: Int): Int {
        if (buying == null) {
            return currency.buyValue(id)
        }
        for (item in buying!!) {
            if (item.id == id) {
                if (item.buyValue == -1) {
                    return currency.buyValue(id)
                }
                return item.buyValue
            }
        }
        return currency.buyValue(id)
    }

    fun sellValue(id: Int): Int {
        if (sells == null) {
            return currency.buyValue(id)
        }
        for (item in sells!!) {
            if (item.id == id) {
                if (item.sellValue == -1) {
                    return currency.sellValue(id)
                }
                return item.sellValue
            }
        }
        return currency.sellValue(id)
    }
}