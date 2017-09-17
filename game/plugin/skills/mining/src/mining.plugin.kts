import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.action.DistancedAction
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.plugin.skills.mining.Ore
import org.apollo.game.plugin.skills.mining.PICKAXES
import org.apollo.game.plugin.skills.mining.Pickaxe
import org.apollo.game.plugin.skills.mining.lookupOreRock
import org.apollo.game.plugins.api.Definitions
import org.apollo.game.plugins.api.mining
import org.apollo.game.plugins.api.skills
import org.apollo.net.message.Message
import java.util.*

class MiningTarget(val objectId: Int, val position: Position, val ore: Ore) {
    fun getObject(world: World): Optional<GameObject> {
        val region = world.regionRepository.fromPosition(position)
        val obj = region.findObject(position, objectId)

        return obj
    }

    fun isSuccessful(mob: Player): Boolean {
        val offset = if (ore.chanceOffset) 1 else 0
        val percent = (ore.chance * mob.skills.mining.currentLevel + offset) * 100

        return rand(100) < percent;
    }
}

class MiningAction(player: Player, val tool: Pickaxe, val target: MiningTarget) : AsyncDistancedAction<Player>(
        PULSES,
        true,
        player,
        target.position,
        ORE_SIZE
) {

    companion object {
        private val PULSES = 0
        private val ORE_SIZE = 1;

        fun pickaxeFor(player: Player): Pickaxe? {
            return PICKAXES
                    .filter { it.level <= player.skills.mining.currentLevel }
                    .filter { player.equipment.contains(it.id) || player.inventory.contains(it.id) }
                    .sortedByDescending { it.level }
                    .firstOrNull()
        }

        /**
         * Starts a [MiningAction] for the specified [Player], terminating the [Message] that triggered it.
         */
        fun start(message: ObjectActionMessage, player: Player, ore: Ore) {
            val pickaxe = pickaxeFor(player)
            if (pickaxe != null) {
                val action = MiningAction(player, pickaxe, MiningTarget(message.id, message.position, ore))
                player.startAction(action)
            } else {
                player.sendMessage("You do not have a pickaxe for which you have the level to use.")
            }

            message.terminate()
        }
    }

    override fun start() {
        mob.turnTo(position)

        val level = mob.skills.mining.currentLevel

        if (level < target.ore.level) {
            mob.sendMessage("You do not have the required level to mine this rock.")
            stop()
        }
    }

    override suspend fun executeActionAsync() {
        mob.sendMessage("You swing your pick at the rock.")
        mob.playAnimation(tool.animation)

        wait(tool.pulses)

        val obj = target.getObject(mob.world)
        if (!obj.isPresent) {
            stop()
            return
        }

        if (target.isSuccessful(mob)) {
            if (mob.inventory.freeSlots() == 0) {
                mob.inventory.forceCapacityExceeded()
                stop()
                return
            }

            if (mob.inventory.add(target.ore.id)) {
                val oreName = Definitions.item(target.ore.id)?.name?.toLowerCase()
                mob.sendMessage("You manage to mine some $oreName")

                mob.skills.addExperience(Skill.MINING, target.ore.exp)
                mob.world.expireObject(obj.get(), target.ore.objects[target.objectId]!!, target.ore.respawn)
                stop()
                return
            }
        }
    }
}

class ExpiredProspectingAction : DistancedAction<Player> {

    constructor(mob: Player, position: Position) : super(PROSPECT_PULSES, true, mob, position, ORE_SIZE)

    companion object {
        private val PROSPECT_PULSES = 0
        private val ORE_SIZE = 1;
    }

    override fun executeAction() {
        mob.sendMessage("There is currently no ore available in this rock.")
        stop();
    }
}

class ProspectingAction(val m: Player, val p: Position, val ore: Ore) : AsyncDistancedAction<Player>(PROSPECT_PULSES, true, m, p, ORE_SIZE) {
    companion object {
        private val PROSPECT_PULSES = 3
        private val ORE_SIZE = 1;

        /**
         * Starts a [MiningAction] for the specified [Player], terminating the [Message] that triggered it.
         */
        fun start(message: ObjectActionMessage, player: Player, ore: Ore) {
            val action = ProspectingAction(player, message.position, ore)
            player.startAction(action)

            message.terminate()
        }
    }

    suspend override fun executeActionAsync() {
        mob.sendMessage("You examine the rock for ores...")
        mob.turnTo(position)

        wait()

        val oreName = Definitions.item(ore.id)?.name?.toLowerCase()
        mob.sendMessage("This rock contains $oreName")

        stop()
    }
}

on { ObjectActionMessage::class }
        .where { option == 1 }
        .then {
            val ore = lookupOreRock(id)
            if (ore != null) {
                MiningAction.start(this, it, ore)
            }
        }

on { ObjectActionMessage::class }
        .where { option == 2 }
        .then {
            var ore = lookupOreRock(id)
            if (ore != null) {
                ProspectingAction.start(this, it, ore)
            }
        }
