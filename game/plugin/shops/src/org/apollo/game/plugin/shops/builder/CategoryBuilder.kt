package org.apollo.game.plugin.shops.builder

/**
 * A builder for a category - a collection of sold items that share a common prefix or suffix.
 *
 * ```
 *   category("mould") {
 *     sell(10) of "Ring"
 *     sell(2) of "Necklace"
 *     sell(10) of "Amulet"
 *   }
 * ```
 */
@ShopDslMarker
class CategoryBuilder {

    /**
     * The items that this shop sells, as a pair of item name to amount sold.
     */
    private val items = mutableListOf<Pair<String, Int>>()

    /**
     * Creates a [SellBuilder] with the specified [amount].
     */
    fun sell(amount: Int): SellBuilder = SellBuilder(amount, items)

    /**
     * Builds this category into a list of sold items, represented as a pair of item name to amount sold.
     */
    fun build(): List<Pair<String, Int>> = items

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

}