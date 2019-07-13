package org.apollo.game.plugin.shops.builder

/**
 * A builder to provide the items to sell.
 *
 * @param amount The amount to sell (of each item).
 * @param items The [MutableList] to insert the given items into.
 */
@ShopDslMarker
class SellBuilder(val amount: Int, val items: MutableList<Pair<String, Int>>) {

    infix fun of(lambda: SellBuilder.() -> Unit) = lambda(this)

    /**
     * Provides an item with the specified name.
     *
     * @name The item name. Must be unambiguous.
     */
    infix fun of(name: String) {
        items += Pair(name, amount)
    }

    /**
     * Overloads unary minus on Strings so that item names can be listed.
     */
    operator fun String.unaryMinus() {
        of(this)
    }

    /**
     * Overloads the unary minus on Pairs so that name+id pairs can be listed. Only intended to be used with the
     * overloaded String invokation operator.
     */ // ShopBuilder uses the lookup plugin, which can operate on _ids tacked on the end
    operator fun Pair<String, Int>.unaryMinus() {
        items += Pair("${first}_$second", amount)
    }

    /**
     * Overloads function invokation on Strings to map `"ambiguous_npc_name"(id)` to a [Pair].
     */
    operator fun String.invoke(id: Int): Pair<String, Int> = Pair(this, id)

}