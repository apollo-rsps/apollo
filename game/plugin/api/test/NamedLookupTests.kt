
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.plugin.util.lookup.lookup_item
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class LookupTests {
    @Test
    fun `looking up an item by name returns the correct item`() {
        val sword = ItemDefinition(0).apply { name = "sword" }
        ItemDefinition.init(arrayOf(sword))

        assertEquals(lookup_item("sword"), sword)
    }

    @Test
    fun `looking up an item by name with a suffixed id returns the item with that id`() {
        val firstSword = ItemDefinition(0).apply { name = "sword" }
        val secondSword = ItemDefinition(1).apply { name = "sword" }

        ItemDefinition.init(arrayOf(firstSword, secondSword))

        assertEquals(lookup_item("sword_1"), secondSword)
    }
}