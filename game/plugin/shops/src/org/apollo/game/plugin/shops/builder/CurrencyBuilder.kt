package org.apollo.game.plugin.shops.builder

import org.apollo.game.plugin.shops.Currency

/**
 * A builder to provide the currency used by the [Shop].
 */
@ShopDslMarker
class CurrencyBuilder {

    private var currency = Currency.COINS

    /**
     * Overloads the `in` operator on [Currency] to achieve e.g. `trades in tokkul`.
     *
     * This function violates the contract for the `in` operator and is only to be used inside the Shops DSL.
     */
    operator fun Currency.contains(builder: CurrencyBuilder): Boolean {
        builder.currency = this
        return true
    }

    fun build(): Currency = currency

}