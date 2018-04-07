
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.api.expireObject
import org.apollo.game.plugin.skills.mining.Ore
import org.apollo.game.plugin.skills.mining.Pickaxe
import org.apollo.game.plugin.skills.mining.TIN_OBJECTS
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.apollo.game.plugin.testing.actionCompleted
import org.apollo.game.plugin.testing.ticks
import org.apollo.game.plugin.testing.verifiers.StringMockkVerifiers.contains
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MiningActionTests : KotlinPluginTest() {
    private val TIN_OBJ_IDS = TIN_OBJECTS.entries.first()

    @Test
    fun `Attempting to mine a rock we don't have the skill to should send the player a message`() {
        val obj = world.spawnObject(1, player.position)
        val target = spyk(MiningTarget(obj.id, obj.position, Ore.TIN))

        every { target.skillRequirementsMet(player) } returns false
        player.startAction(MiningAction(player, Pickaxe.BRONZE, target))
        verifyAfter(actionCompleted()) { player.sendMessage(contains("do not have the required level")) }
    }

    @Test
    fun `Mining a rock we have the skill to mine should eventually reward ore and experience`() {
        val (tinId, expiredTinId) = TIN_OBJ_IDS
        val obj = world.spawnObject(tinId, player.position)
        val target = spyk(MiningTarget(obj.id, obj.position, Ore.TIN))

        every { target.skillRequirementsMet(player) } returns true
        every { target.isSuccessful(player, any()) } returns true
        every { world.expireObject(obj, any(), any()) } answers {}

        player.skillSet.setCurrentLevel(Skill.MINING, Ore.TIN.level)
        player.startAction(MiningAction(player, Pickaxe.BRONZE, target))

        verifyAfter(ticks(1)) { player.sendMessage(contains("You swing your pick")) }
        after(actionCompleted()) {
            verify { player.sendMessage("You manage to mine some <ore_type>") }
            verify { world.expireObject(obj, expiredTinId, Ore.TIN.respawn) }

            assertTrue(player.inventory.contains(Ore.TIN.id))
            assertEquals(player.skillSet.getExperience(Skill.MINING), Ore.TIN.exp)
        }
    }

    init {
        Ore.values()
            .map { ItemDefinition(it.id).also { it.name = "<ore_type>" } }
            .forEach { items[it.id] = it }

        items[Pickaxe.BRONZE.id] = ItemDefinition(Pickaxe.BRONZE.id)
    }

}