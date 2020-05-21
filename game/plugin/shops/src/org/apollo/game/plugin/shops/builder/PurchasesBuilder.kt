package org.apollo.game.plugin.shops.builder

import org.apollo.game.plugin.shops.Shop

/**
 * A builder to provide the [Shop.PurchasePolicy].
 */
@ShopDslMarker
class PurchasesBuilder {

    private var policy = Shop.PurchasePolicy.OWNED

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

    fun build(): Shop.PurchasePolicy = policy

}