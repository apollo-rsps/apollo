package org.apollo.game.plugin.shops.builder

import org.apollo.cache.def.NpcDefinition
import org.apollo.game.plugin.api.Definitions
import org.apollo.game.plugin.shops.Currency
import org.apollo.game.plugin.shops.SHOPS
import org.apollo.game.plugin.shops.Shop
import org.apollo.game.plugin.shops.builder.CategoryBuilder.Affix

/**
 * Creates a [Shop].
 *
 * @param name The name of the shop.
 */
fun shop(name: String, builder: ShopBuilder.() -> Unit) {
    val shop = ShopBuilder(name).apply(builder).build()

    shop.operators.associateByTo(SHOPS, { it }, { shop })
}

/**
 * A builder for a [Shop].
 */
@ShopDslMarker
class ShopBuilder(val name: String) {

    /**
     * The id on the operator npc's action menu used to open the shop.
     */
    val action = ActionBuilder()

    /**
     * The type of [Currency] the [Shop] makes exchanges with.
     */
    var trades = CurrencyBuilder()

    /**
     * The [OperatorBuilder] used to collate the [Shop]'s operators.
     */
    val operated = OperatorBuilder(name)

    /**
     * The [Shop]'s policy towards purchasing items from players.
     */
    var buys = PurchasesBuilder()

    /**
     * Redundant variable used in the purchases dsl, to complete the [PurchasesBuilder] (e.g. `buys no items`).
     */
    val items = Unit

    /**
     * Used in the category dsl. Places the category name before the item name (inserting a space between the names).
     */
    val prefix = Affix.Prefix

    /**
     * Used in the category dsl. Prevents the category name from being joined to the item name in any way.
     */
    val nothing = Affix.None

    /**
     * The [List] of items sold by the shop, as (name, amount) [Pair]s.
     */
    private val sold = mutableListOf<Pair<String, Int>>()

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
     * @param builder The builder that adds items to the category.
     */
    fun category(
        name: String,
        affix: Affix = Affix.Suffix,
        depluralise: Boolean = true, // TODO search for both with and without plural
        builder: CategoryBuilder.() -> Unit
    ) {
        val items = CategoryBuilder().apply(builder).build()

        val category = when {
            depluralise -> name.removeSuffix("s")
            else -> name
        }

        sold += items.map { (name, amount) -> Pair(affix.join(name, category), amount) }
    }

    /**
     * Creates a [SellBuilder] with the specified [amount].
     */
    fun sell(amount: Int): SellBuilder = SellBuilder(amount, sold)

    /**
     * Converts this builder into a [Shop].
     */
    internal fun build(): Shop {
        val operators = operated.build()
        val npc = NpcDefinition.lookup(operators.first())

        val items = sold.associateBy(
            { requireNotNull(Definitions.item(it.first)?.id) { "Failed to find item ${it.first} in shop $name." } },
            { it.second }
        )

        return Shop(name, action.slot(npc), items, operators, trades.build(), buys.build())
    }

}
