import org.apollo.cache.def.ItemDefinition
import org.apollo.game.plugin.testing.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LookupTests : KotlinPluginTest() {
    @Test fun itemLookup() {
        val testItem = ItemDefinition(0)
        testItem.name = "sword"

        ItemDefinition.init(arrayOf(testItem))

        assertThat(lookup_item("sword")).isEqualTo(testItem);
    }
}