import org.apollo.cache.def.ItemDefinition
import org.apollo.cache.def.ObjectDefinition
import org.apollo.game.action.DistancedAction
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Animation
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Entity
import org.apollo.game.model.entity.EquipmentConstants
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.model.entity.obj.StaticGameObject
import org.apollo.game.plugin.skills.mining.*
import org.apollo.game.scheduling.ScheduledTask
import org.apollo.game.scheduling.Scheduler
import java.util.*
import kotlin.properties.Delegates

class MiningAction(val player: Player, val objectID: Int, val p: Position, val ore: Ore) : DistancedAction<Player>(PULSES, true, player, p, ORE_SIZE) {

    companion object {
        private val PULSES = 0
        private val ORE_SIZE = 1;
    }

    private var counter: Int = 0
    private var started: Boolean = false
    private val rand: Random = Random()

    override fun executeAction() {
        System.out.println("Mining " + ore.id + " @ " + p)
        val level = mob.skillSet.getSkill(Skill.MINING).currentLevel
        val pickaxe = findPickaxe()


        //check that our pick can mine the ore
        if (pickaxe == null || level < pickaxe.level) {
            mob.sendMessage("You do not have a pickaxe for which you have the level to use.")
            stop()
            return
        }

        //check that we can mine the ore
        if (level < ore.level) {
            mob.sendMessage("You do not have the required level to mine this rock.")
            stop()
            return
        }

        //start the process of mining
        if (started) {
            if (counter == 0) {
                if (!miningSuccessful(ore.chance, ore.chanceOffset, level)) {
                    System.out.println("Mining...")
                    mine(pickaxe)
                    return //We did not mine the ore... Keep going
                }
                //Check inv capacity
                if (mob.inventory.freeSlots() == 0) {
                    mob.inventory.forceCapacityExceeded()
                    stop()
                    return
                }
                if (mob.inventory.add(ore.id)) {
                    //TODO: Use lookup from utils once it has a lookup function for IDs
                    val oreName = ItemDefinition.lookup(ore.id).name.toLowerCase();
                    mob.sendMessage("You managed to mine some " + oreName + ".")
                    mob.skillSet.addExperience(Skill.MINING, ore.exp)
                    //Expire ore
                    var rockEntity: StaticGameObject? = null
                    val region = mob.world.regionRepository.fromPosition(position)
                    val entities = region.getEntities(position)
                    for (entity: Entity in entities) {
                        if (entity is StaticGameObject) {
                            System.out.println("Entity at mining location: " + entity.id + " with type: " + entity.entityType)
                            if (entity.id == objectID) {
                                rockEntity = entity
                            }
                        }
                    }
                    if (rockEntity == null) { //Mining entity not found at location...
                        System.out.println("WARNING: Invalid mining condition on rock");
                        stop()
                        return
                    }
                    //Get ID of exipred ore
                    val expiredObjectID = ore.objects.get(objectID);
                    val expiredRockEntity = StaticGameObject(mob.world, expiredObjectID!!, position, rockEntity!!.type, rockEntity!!.orientation)
                    //Remove normal ore and replace with expired
                    System.out.println("Removing " + objectID + " addding " + expiredObjectID)
                    System.out.println("Adding tasks")
                    //add task to remove normal ore and replace with depleted
                    mob.world.schedule(object: ScheduledTask(0, true) {
                        override fun execute() {
                            System.out.println("running deplete task")
                            //Replace normal ore with expired ore
                            region.removeEntity(rockEntity);
                            region.addEntity(expiredRockEntity)
                            this.stop() //Makes task run once
                        }
                    })
                    //add task to respawn normal ore
                    mob.world.schedule(object: ScheduledTask(ore.respawn, false) {
                        override fun execute() {
                            System.out.println("running ore task")
                            //Replace expired ore with normal ore
                            region.removeEntity(expiredRockEntity)
                            region.addEntity(rockEntity);
                            this.stop() //Makes task run once
                        }
                    })
                } else {
                    System.out.println("Failed to add ore to inv");
                }
                System.out.println("We are done now")
                stop(); //Force this action to stop after we are done
            }
            counter -= 1
        } else {
            started = true
            mine(pickaxe)
        }
    }

    private fun findPickaxe(): Pickaxe? {
        for (pick in getPickaxes()) {
            if (pick!!.level > mob.skillSet.getSkill(Skill.MINING).currentLevel) {
                continue;
            }
            if (mob.equipment.get(EquipmentConstants.WEAPON).id == pick.id) {
                return pick;
            } else if (mob.inventory.contains(pick.id)) {
                return pick;
            }
        }
        return null;
    }

    private fun mine(pickaxe: Pickaxe) {
        mob.sendMessage("You swing your pick at the rock.")
        mob.playAnimation(pickaxe.animation)
        counter = pickaxe.pulses
        mob.turnTo(position)
    }

    /**
     * Returns the chance of mining being successful.
     * Algorithm comes from: http://runescape.wikia.com/wiki/Talk:Mining#Mining_success_rate_formula
     */
    private fun miningSuccessful(oreChance: Double, oreChanceOffset: Boolean, playerLevel: Int): Boolean {
        val percent: Double
        if (oreChanceOffset) {
            percent = (oreChance * playerLevel + 1) * 100
        } else {
            percent = (oreChance * playerLevel) * 100
        }
        System.out.println("Chance: " + percent)
        return rand.nextInt(100) < percent;
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

class ProspectingAction(val m: Player, val p: Position, val ore: Ore) : DistancedAction<Player>(PROSPECT_PULSES, true, m, p, ORE_SIZE) {

    companion object {
        private val PROSPECT_PULSES = 3
        private val ORE_SIZE = 1;
    }

    var started = false;

    override fun executeAction() {
        if (started) {
            val oreName = ItemDefinition.lookup(ore.id).name.toLowerCase()
            mob.sendMessage("This rock contains " + oreName + ".")
            stop();
        } else {
            started = true
            mob.sendMessage("You examine the rock for ores...")
            mob.turnTo(position)
        }
    }
}

on {ObjectActionMessage::class}
        .where {option == 1}
        .then {
            if (lookupOreRock(id) != null) {
                it.startAction(MiningAction(it, id, this.position, lookupOreRock(id)!!))
            } else {
                System.out.println("Unknown ore: " + id)
            }
        }

on {ObjectActionMessage::class}
        .where {option == 2}
        .then {
            if (lookupOreRock(id) != null) {
                it.startAction(ProspectingAction(it, this.position, lookupOreRock(id)!!))
            }
        }
