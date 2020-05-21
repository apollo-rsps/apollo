import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.Definitions
import org.apollo.game.plugin.api.findObject
import org.apollo.game.plugin.api.mining
import org.apollo.game.plugin.api.replaceObject
import org.apollo.game.plugin.skills.mining.Ore

data class MiningTarget(val objectId: Int, val position: Position, val ore: Ore) {

    /**
     * Deplete this mining resource from the [World], and schedule it to be respawned
     * in a number of ticks specified by the [Ore].
     */
    fun deplete(world: World) {
        val obj = world.findObject(position, objectId)!!

        world.replaceObject(obj, ore.objects[objectId]!!, ore.respawn)
    }

    /**
     * Check if the [Player] was successful in mining this ore with a random success [chance] value between 0 and 100.
     */
    fun isSuccessful(mob: Player, chance: Int): Boolean {
        val percent = (ore.chance * mob.mining.current + ore.chanceOffset) * 100
        return chance < percent
    }

    /**
     * Check if this target is still valid in the [World] (i.e. has not been [deplete]d).
     */
    fun isValid(world: World) = world.findObject(position, objectId) != null

    /**
     * Get the normalized name of the [Ore] represented by this target.
     */
    fun oreName() = Definitions.item(ore.id).name.toLowerCase()

    /**
     * Reward a [player] with experience and ore if they have the inventory capacity to take a new ore.
     */
    fun reward(player: Player): Boolean {
        val hasInventorySpace = player.inventory.add(ore.id)

        if (hasInventorySpace) {
            player.mining.experience += ore.exp
        }

        return hasInventorySpace
    }

    /**
     * Check if the [mob] has met the skill requirements to mine te [Ore] represented by
     * this [MiningTarget].
     */
    fun skillRequirementsMet(mob: Player) = mob.mining.current < ore.level
}