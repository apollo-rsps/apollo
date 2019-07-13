package org.apollo.game.plugin.shops.builder

import org.apollo.game.plugin.api.Definitions

/**
 * A builder to provide the list of shop operators - the npcs that can be interacted with to access the shop.
 *
 * ```
 * shop("General Store.") {
 *   operated by "Shopkeeper"(522) and "Shop assistant"(523) and "Shop assistant"(524)
 *   ...
 * }
 * ```
 */
@ShopDslMarker
class OperatorBuilder internal constructor(private val shopName: String) {

    /**
     * The [List] of shop operator ids.
     */
    private val operators = mutableListOf<Int>()

    /**
     * Adds a shop operator, using the specified [name] to resolve the npc id.
     */
    infix fun by(name: String): OperatorBuilder {
        val npc = requireNotNull(Definitions.npc(name)) {
            "Failed to resolve npc named `$name` when building shop $shopName."
        }

        operators += npc.id
        return this
    }

    /**
     * Adds a shop operator, using the specified [name] to resolve the npc id.
     *
     * An alias for [by].
     */
    infix fun and(name: String): OperatorBuilder = by(name)

    /**
     * Adds a shop operator, using the specified [name] to resolve the npc id.
     *
     * An alias for [by].
     */
    operator fun plus(name: String): OperatorBuilder = and(name)

    /**
     * Adds a shop operator with the specified npc id. Intended to be used with the overloaded String invokation
     * operator, solely to disambiguate between npcs with the same name (e.g. `"Shopkeeper"(500) vs
     * `"Shopkeeper"(501)`). Use [by(String)][by] if the npc name is unambiguous.
     */
    infix fun by(pair: Pair<String, Int>): OperatorBuilder {
        operators += pair.second
        return this
    }

    /**
     * Adds a shop operator with the specified npc id. Intended to be used with the overloaded String invokation
     * operator, solely to disambiguate between npcs with the same name (e.g. `"Shopkeeper"(500) vs
     * `"Shopkeeper"(501)`). Use [by(String)][by] if the npc name is unambiguous.
     *
     * An alias for [by(Pair<String, Int>)][by].
     */
    infix fun and(pair: Pair<String, Int>): OperatorBuilder = by(pair)

    /**
     * Adds a shop operator with the specified npc id. Intended to be used with the overloaded String invokation
     * operator, solely to disambiguate between npcs with the same name (e.g. `"Shopkeeper"(500) vs
     * `"Shopkeeper"(501)`). Use [by(String)][by] if the npc name is unambiguous.
     *
     * An alias for [by(Pair<String, Int>)][by].
     */
    operator fun plus(pair: Pair<String, Int>): OperatorBuilder = by(pair)

    /**
     * Builds this [OperatorBuilder] into a [List] of operator npc ids.
     */
    fun build(): List<Int> = operators

}