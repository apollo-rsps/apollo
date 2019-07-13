package org.apollo.game.plugin.shops

import org.apollo.cache.def.ItemDefinition
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class CurrencyTests {

    @Test
    fun `items used as currencies must have names in their definitions`() {
        assertThrows<ExceptionInInitializerError>("Should not be able to create a Currency with an item missing a name") {
            Currency(id = ITEM_MISSING_NAME)
        }
    }

    private companion object {
        private const val ITEM_MISSING_NAME = 0

        @ItemDefinitions
        private val unnamed = listOf(ItemDefinition(ITEM_MISSING_NAME))
    }

}