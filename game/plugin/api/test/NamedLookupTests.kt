import org.apollo.cache.def.ItemDefinition
import org.apollo.game.plugin.api.Definitions
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.assertj.core.api.Assertions.assertThat
import org.apollo.game.plugin.util.lookup.lookup_item
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class NamedLookupTests : KotlinPluginTest() {
    @Test
    fun itemLookup() {
        val testItem = ItemDefinition(0)
        testItem.name = "sword"

        ItemDefinition.init(arrayOf(testItem))

        assertEquals(Definitions.item("sword"), testItem)
    }
}