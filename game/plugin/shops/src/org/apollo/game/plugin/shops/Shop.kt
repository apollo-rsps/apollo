package org.apollo.game.plugin.shops

import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.Item
import org.apollo.game.model.entity.Player
import org.apollo.game.model.inv.Inventory
import org.apollo.game.model.inv.Inventory.StackMode.STACK_ALWAYS
import org.apollo.game.plugin.shops.Shop.Companion.ExchangeType.BUYING
import org.apollo.game.plugin.shops.Shop.Companion.ExchangeType.SELLING
import org.apollo.game.plugin.shops.Shop.PurchasePolicy.ANY
import org.apollo.game.plugin.shops.Shop.PurchasePolicy.NOTHING
import org.apollo.game.plugin.shops.Shop.PurchasePolicy.OWNED

/**
 * Contains shop-related interface ids.
 */
object Interfaces {

    /**
     * The container interface id for the player's inventory.
     */
    const val INVENTORY_CONTAINER = 3823

    /**
     * The sidebar id for the inventory, when a Shop window is open.
     */
    const val INVENTORY_SIDEBAR = 3822

    /**
     * The shop window interface id.
     */
    const val SHOP_WINDOW = 3824

    /**
     * The container interface id for the shop's inventory.
     */
    const val SHOP_CONTAINER = 3900

    /**
     * The id of the text widget that displays a shop's name.
     */
    const val SHOP_NAME = 3901
}

/**
 * The [Map] from npc ids to [Shop]s.
 */
val SHOPS = mutableMapOf<Int, Shop>()

/**
 * An in-game shop, operated by one or more npcs.
 *
 * @param name The name of the shop.
 * @param action The id of the NpcActionMessage sent (by the client) when a player opens this shop.
 * @param sells The [Map] from item id to amount sold.
 * @param operators The [List] of Npc ids that can open this shop.
 * @param currency The [Currency] used when making exchanges with this [Shop].
 * @param purchases This [Shop]'s attitude towards purchasing items from players.
 */
class Shop(
    val name: String,
    val action: Int,
    private val sells: Map<Int, Int>,
    val operators: List<Int>,
    private val currency: Currency = Currency.COINS,
    private val purchases: PurchasePolicy = OWNED
) {

    /**
     * The [Inventory] containing this [Shop]'s current items.
     */
    val inventory = Inventory(CAPACITY, STACK_ALWAYS)

    init {
        sells.forEach { (id, amount) -> inventory.add(id, amount) }
    }

    /**
     * Restocks this [Shop], adding and removing items as necessary to move the stock closer to its initial state.
     */
    fun restock() {
        for (item in inventory.items.filterNotNull()) {
            val id = item.id

            if (!sells(id) || item.amount > sells[id]!!) {
                inventory.remove(id)
            } else if (item.amount < sells[id]!!) {
                inventory.add(id)
            }
        }
    }

    /**
     * Sells an item to a [Player].
     */
    fun sell(player: Player, slot: Int, option: Int) {
        val item = inventory.get(slot)
        val id = item.id
        val itemCost = value(id, SELLING)

        if (option == VALUATION_OPTION) {
            val itemId = ItemDefinition.lookup(id).name
            player.sendMessage("$itemId: currently costs $itemCost ${currency.name(itemCost)}.")
            return
        }

        var buying = amount(option)
        var unavailable = false

        val amount = item.amount
        if (buying > amount) {
            buying = amount
            unavailable = true
        }

        val stackable = item.definition.isStackable
        val slotsRequired = when {
            stackable && player.inventory.contains(id) -> 0
            !stackable -> buying
            else -> 1
        }

        val freeSlots = player.inventory.freeSlots()
        var full = false

        if (slotsRequired > freeSlots) {
            buying = freeSlots
            full = true
        }

        val totalCost = buying * itemCost
        val totalCurrency = player.inventory.getAmount(currency.id)
        var unaffordable = false

        if (totalCost > totalCurrency) {
            buying = totalCurrency / itemCost
            unaffordable = true
        }

        if (buying > 0) {
            player.inventory.remove(currency.id, totalCost)
            val remaining = player.inventory.add(id, buying)

            if (remaining > 0) {
                player.inventory.add(currency.id, remaining * itemCost)
            }

            if (buying >= amount && sells(id)) {
                // If the item is from the shop's main stock, set its amount to zero so it can be restocked over time.
                inventory.set(slot, Item(id, 0))
            } else {
                inventory.remove(id, buying - remaining)
            }
        }

        val message = when {
            unaffordable -> "You don't have enough ${currency.name}."
            full -> "You don't have enough inventory space."
            unavailable -> "The shop has run out of stock."
            else -> return
        }

        player.sendMessage(message)
    }

    /**
     * Purchases the item from the specified [Player].
     */
    fun buy(seller: Player, slot: Int, option: Int) {
        val player = seller.inventory
        val id = player.get(slot).id

        if (!verifyPurchase(seller, id)) {
            return
        }

        val value = value(id, BUYING)
        if (option == VALUATION_OPTION) {
            seller.sendMessage("${ItemDefinition.lookup(id).name}: shop will buy for $value ${currency.name(value)}.")
            return
        }

        val amount = Math.min(player.getAmount(id), amount(option))

        player.remove(id, amount)
        inventory.add(id, amount)

        if (value != 0) {
            player.add(currency.id, value * amount)
        }
    }

    /**
     * Returns the value of the item with the specified id.
     *
     * @param method The [ExchangeType].
     */
    private fun value(item: Int, method: ExchangeType): Int {
        val value = ItemDefinition.lookup(item).value

        return when (method) {
            BUYING -> when (purchases) {
                NOTHING -> throw UnsupportedOperationException("Cannot get sell value in shop that doesn't buy.")
                OWNED -> (value * 0.6).toInt()
                ANY -> (value * 0.4).toInt()
            }
            SELLING -> when (purchases) {
                ANY -> Math.ceil(value * 0.8).toInt()
                else -> value
            }
        }
    }

    /**
     * Verifies that the [Player] can actually sell an item with the given id to this [Shop].
     *
     * @param id The id of the [Item] to sell.
     */
    private fun verifyPurchase(player: Player, id: Int): Boolean {
        val item = ItemDefinition.lookup(id)

        if (!purchases(id) || item.isMembersOnly && !player.isMembers || item.value == 0) {
            player.sendMessage("You can't sell this item to this shop.")
            return false
        } else if (inventory.freeSlots() == 0 && !inventory.contains(id)) {
            player.sendMessage("The shop is currently full at the moment.")
            return false
        }

        return true
    }

    /**
     * Returns whether or not this [Shop] will purchase an item with the given id.
     *
     * @param id The id of the [Item] purchase buy.
     */
    private fun purchases(id: Int): Boolean {
        return id != currency.id && when (purchases) {
            NOTHING -> false
            OWNED -> sells.containsKey(id)
            ANY -> true
        }
    }

    /**
     * Returns whether or not this [Shop] sells the item with the given id.
     *
     * @param id The id of the [Item] to sell.
     */
    private fun sells(id: Int): Boolean = sells.containsKey(id)

    /**
     * The [Shop]s policy regarding purchasing items from players.
     */
    enum class PurchasePolicy {

        /**
         * Never purchase anything from players.
         */
        NOTHING,

        /**
         * Only purchase items that this Shop sells by default.
         */
        OWNED,

        /**
         * Purchase any tradeable items.
         */
        ANY
    }

    companion object {

        /**
         * The amount of pulses between shop inventory restocking.
         */
        const val RESTOCK_INTERVAL = 100

        /**
         * The capacity of a [Shop].
         */
        private const val CAPACITY = 30

        /**
         * The type of exchange occurring between the [Player] and [Shop].
         */
        private enum class ExchangeType { BUYING, SELLING }

        /**
         * The option id for item valuation.
         */
        private const val VALUATION_OPTION = 1

        /**
         * Returns the amount that a player tried to buy or sell.
         *
         * @param option The id of the option the player selected.
         */
        private fun amount(option: Int): Int {
            return when (option) {
                2 -> 1
                3 -> 5
                4 -> 10
                else -> throw IllegalArgumentException("Option must be 1-4")
            }
        }

    }

}