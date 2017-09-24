import org.apollo.cache.def.ItemDefinition
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.apollo.game.plugin.util.lookup.lookup_item
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LookupTests : KotlinPluginTest() {
    @Test
    fun itemLookup() {
        val testItem = ItemDefinition(0)
        testItem.name = "sword"

        ItemDefinition.init(arrayOf(testItem))

        assertThat(lookup_item("sword")).isEqualTo(testItem)
    }
}