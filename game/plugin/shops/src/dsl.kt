package org.apollo.game.plugin.shops

import org.apollo.cache.def.NpcDefinition
import org.apollo.game.plugin.shops.CategoryWrapper.Affix
import org.apollo.game.plugin.util.lookup.lookup_item
import org.apollo.game.plugin.util.lookup.lookup_npc

/**
 * Creates a [Shop].
 *
 * @param name The name of the shop.
 */
fun shop(name: String, builder: ShopBuilder.() -> Unit) {
    val shop = ShopBuilder(name)
    builder(shop)

    val built = shop.build()
    val operators = shop.operators().map { it to built }.toMap()

    SHOPS.putAll(operators)
}

/**
 * A [DslMarker] for the shop DSL.
 */
@DslMarker
annotation class ShopDslMarker

/**
 * A builder for a [Shop].
 */
@ShopDslMarker
class ShopBuilder(val name: String) {

    /**
     * Overloads function invokation on strings to map `"ambiguous_npc_name"(id)` to a [Pair].
     */
    operator fun String.invoke(id: Int): Pair<String, Int> = Pair(this, id)

    /**
     * Adds a sequence of items to this Shop, grouped together (in the DSL) for convenience. Items will be displayed
     * in the same order they are provided.
     *
     * @param name The name of the category.
     * @param affix The method of affixation between the item and category name (see [Affix]).
     * @param depluralise Whether or not the category name should have the "s".
     * @param builder The builder used to add items to the category.
     */
    fun category(name: String, affix: Affix = Affix.Suffix, depluralise: Boolean = true,
                 builder: CategoryWrapper.() -> Unit) {
        val items = mutableListOf<Pair<String, Int>>()
        builder.invoke(CategoryWrapper(items))

        val category = when {
            depluralise -> name.removeSuffix("s")
            else -> name
        }

        val affixed = items.map { (name, amount) -> Pair(affix.join(name, category), amount) }
        sold.addAll(affixed)
    }

    /**
     * Creates a [SellBuilder] with the specified [amount].
     */
    fun sell(amount: Int): SellBuilder = SellBuilder(amount, sold)

    /**
     * The id on the operator npc's action menu used to open the shop.
     */
    val action = ActionBuilder()

    /**
     * The type of [Currency] the [Shop] makes exchanges with.
     */
    var trades = CurrencyBuilder()

    /**
     * The [Shop]'s policy towards purchasing items from players.
     */
    var buys = PurchasesBuilder()

    /**
     * Redundant variable used only to complete the [PurchasesBuilder] (e.g. `buys no items`).
     */
    val items = Unit

    /**
     * Places the category name before the item name (inserting a space between the names).
     */
    val prefix = Affix.Prefix

    /**
     * Prevents the category name from being joined to the item name in any way.
     */
    val nothing = Affix.None

    /**
     * The [OperatorBuilder] used to collate the [Shop]'s operators.
     */
    val operated = OperatorBuilder()

    /**
     * The [List] of items sold by the shop, as (name, amount) [Pair]s.
     */
    private val sold = mutableListOf<Pair<String, Int>>()

    /**
     * Converts this builder into a [Shop].
     */
    internal fun build(): Shop {
        val items = sold.associateBy({ (first) -> lookup_item(first)!!.id }, Pair<String, Int>::second)
        val npc = NpcDefinition.lookup(operators().first())

        return Shop(name, action.action(npc), items, trades.currency, buys.policy)
    }

    /**
     * Gets the [List] of shop operator ids.
     */
    internal fun operators(): MutableList<Int> = operated.operators

}

@ShopDslMarker
class CategoryWrapper(private val items: MutableList<Pair<String, Int>>) {

    /**
     * The method of joining the item and category name.
     */
    sealed class Affix(private val joiner: (item: String, category: String) -> String) {

        /**
         * Appends the category after the item name (with a space between).
         */
        object Suffix : Affix({ item, affix -> "$item $affix" })

        /**
         * Prepends the category before the item name (with a space between).
         */
        object Prefix : Affix({ item, affix -> "$affix $item" })

        /**
         * Does not join the category at all (i.e. only returns the item name).
         */
        object None : Affix({ item, _ -> item })

        /**
         * Joins the item and category name in the expected manner.
         */
        fun join(item: String, category: String): String = joiner(item, category)

    }

    /**
     * Creates a [SellBuilder] with the specified [amount].
     */
    fun sell(amount: Int): SellBuilder = SellBuilder(amount, items)

}

/**
 * A builder to provide the list of shop operators - the npcs that can be interacted with to access the shop.
 */
@ShopDslMarker
class OperatorBuilder internal constructor() {

    /**
     * The [List] of shop operators.
     */
    val operators: MutableList<Int> = mutableListOf()

    /**
     * Adds a shop operator, using the specified [name] to resolve the npc id.
     */
    infix fun by(name: String): OperatorBuilder {
        operators.add(lookup_npc(name)!!.id)
        return this
    }

    /**
     * Adds a shop operator, using the specified [name] to resolve the npc id.
     */
    infix fun and(name: String): OperatorBuilder = by(name)

    /**
     * Adds a shop operator, using the specified [name] to resolve the npc id.
     */
    operator fun plus(name: String): OperatorBuilder = and(name)

    /**
     * Adds a shop operator with the specified npc id. Intended to be used with the overloaded String invokation
     * operator, solely to disambiguate between npcs with the same name (e.g.
     * `"Shopkeeper"(500) vs `"Shopkeeper"(501)`). Use [by(String][by] if the npc name is unambiguous.
     */
    infix fun by(pair: Pair<String, Int>): OperatorBuilder {
        operators.add(pair.second)
        return this
    }

    /**
     * Adds a shop operator with the specified npc id. Intended to be used with the overloaded String invokation
     * operator, solely to disambiguate between npcs with the same name (e.g.
     * `"Shopkeeper"(500) vs `"Shopkeeper"(501)`). Use [by(String][by] if the npc name is unambiguous.
     */
    infix fun and(pair: Pair<String, Int>): OperatorBuilder = by(pair)

    /**
     * Adds a shop operator with the specified npc id. Intended to be used with the overloaded String invokation
     * operator, solely to disambiguate between npcs with the same name (e.g.
     * `"Shopkeeper"(500) vs `"Shopkeeper"(501)`). Use [by(String][by] if the npc name is unambiguous.
     */
    operator fun plus(pair: Pair<String, Int>): OperatorBuilder = by(pair)

}

/**
 * A builder to provide the action id used to open the shop.
 */
@ShopDslMarker
class ActionBuilder {

    private var action: String = "Trade"

    private var actionId: Int? = null

    /**
     * Sets the name or id of the action used to open the shop interface with an npc. Defaults to "Trade".
     *
     * If specifying an id it must account for hidden npc menu actions (if any exist) - if "Open Shop" is the first
     * action displayed when the npc is right-clicked, it does not necessarily mean that the action id is `1`.
     *
     * @param action The `name` (as a [String]) or `id` (as an `Int`) of the npc's action menu, to open the shop.
     * @throws IllegalArgumentException If `action` is not a [String] or [Int].
     */
    override fun equals(@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") action: Any?): Boolean {
        if (action is String) {
            this.action = action
            return true
        } else if (action is Int) {
            actionId = action
            return true
        }

        throw IllegalArgumentException("The Npc option must be provided as a String (the option name) or the ")
    }

    /**
     * Returns the open shop action slot.
     *
     * @throws IllegalArgumentException If the action id or name is invalid.
     */
    internal fun action(npc: NpcDefinition): Int {
        actionId?.let { action ->
            if (npc.hasInteraction(action - 1)) { // ActionMessages are 1-based
                return action
            }

            throw IllegalArgumentException("Npc ${npc.name} does not have an an action $action.")
        }

        val index = npc.interactions.indexOf(action)
        when (index) {
            -1 -> throw IllegalArgumentException("Npc ${npc.name} does not have an an action $action.")
            else -> return index + 1 // ActionMessages are 1-based
        }
    }

    /**
     * Throws [UnsupportedOperationException].
     */
    override fun hashCode(): Int = throw UnsupportedOperationException("ActionBuilder is a utility class for a DSL " +
        "and improperly implements equals() - it should not be used anywhere outside of the DSL.")

}

/**
 * A builder to provide the currency used by the [Shop].
 */
@ShopDslMarker
class CurrencyBuilder {

    internal var currency = Currency.COINS

    /**
     * Overloads the `in` operator on [Currency] to achieve e.g. `trades in tokkul`.
     */
    operator fun Currency.contains(builder: CurrencyBuilder): Boolean {
        builder.currency = this
        return true
    }

}

/**
 * A builder to provide the [Shop.PurchasePolicy].
 */
@ShopDslMarker
class PurchasesBuilder {

    internal var policy = Shop.PurchasePolicy.OWNED

    /**
     * Instructs the shop to purchase no items, regardless of whether or not it sells it.
     */
    infix fun no(@Suppress("UNUSED_PARAMETER") items: Unit) {
        policy = Shop.PurchasePolicy.NOTHING
    }

    /**
     * Instructs the shop to purchase any tradeable item.
     */
    infix fun any(@Suppress("UNUSED_PARAMETER") items: Unit) {
        policy = Shop.PurchasePolicy.ANY
    }

}

/**
 * A builder to provide the items to sell.
 *
 * @param amount The amount to sell (of each item).
 * @param items The [MutableList] to insert the given items into.
 */
@ShopDslMarker
class SellBuilder(val amount: Int, val items: MutableList<Pair<String, Int>>) {

    infix fun of(lambda: SellBuilder.() -> Unit) = lambda.invoke(this)

    /**
     * Provides an item with the specified name.
     *
     * @name The item name. Must be unambiguous.
     */
    infix fun of(name: String) = items.add(Pair(name, amount))

    /**
     * Overloads unary minus on Strings so that item names can be listed.
     */
    operator fun String.unaryMinus() = items.add(Pair(this, amount))

    /**
     * Overloads the unary minus on Pairs so that name+id pairs can be listed. Only intended to be used with the
     * overloaded String invokation operator.
     */ // ShopBuilder uses the lookup plugin, which can operate on _ids tacked on the end
    operator fun Pair<String, Int>.unaryMinus() = items.add(Pair("${this.first}_${this.second}", amount))

    /**
     * Overloads function invokation on Strings to map `"ambiguous_npc_name"(id)` to a [Pair].
     */
    operator fun String.invoke(id: Int): Pair<String, Int> = Pair(this, id)

}
