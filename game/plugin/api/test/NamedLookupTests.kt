import org.apollo.cache.def.ItemDefinition
import org.apollo.game.plugin.api.Definitions
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class NamedLookupTests : KotlinPluginTest() {
    @Test
    fun itemLookup() {
        val testItem = ItemDefinition(0)
        testItem.name = "sword"

        ItemDefinition.init(arrayOf(testItem))

        assertThat(Definitions.item("sword")).isEqualTo(testItem)
    }
}