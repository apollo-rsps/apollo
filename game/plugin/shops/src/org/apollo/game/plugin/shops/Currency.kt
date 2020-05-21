package org.apollo.game.plugin.shops

import org.apollo.game.plugin.api.Definitions

/**
 * A [Shop]'s method of payment.
 *
 * @param id The item id of the currency.
 * @param plural Whether or not the name of this currency is plural.
 */
data class Currency(val id: Int, val plural: Boolean = false) {

    val name = requireNotNull(Definitions.item(id).name?.toLowerCase()) { "Currencies must have a name." }

    fun name(amount: Int): String {
        return when {
            amount == 1 && plural -> name.removeSuffix("s")
            else -> name
        }
    }

    companion object {
        val COINS = Currency(995, plural = true)
    }

}