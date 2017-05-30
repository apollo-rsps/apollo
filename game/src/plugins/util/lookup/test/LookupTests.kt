import org.apollo.cache.def.ItemDefinition
import org.junit.Test
import kotlin.test.assertEquals

class LookupTests {
    @Test fun itemLookup() {
        val testItem = ItemDefinition(0)
        testItem.name = "sword"

        ItemDefinition.init(arrayOf(testItem))

        assertEquals(testItem, lookup_item("sword"))
    }
}