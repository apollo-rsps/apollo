import org.apollo.cache.def.ItemDefinition
import org.apollo.game.action.DistancedAction
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Entity
import org.apollo.game.model.entity.EquipmentConstants
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.model.entity.obj.StaticGameObject
import org.apollo.game.plugin.skills.mining.*
import org.apollo.game.scheduling.ScheduledTask
import java.util.*

class WoodcuttingAction(val player: Player, val objectID: Int, val p: Position, val wood: Wood) : DistancedAction<Player>(PULSES, true, player, p, TREE_SIZE) {

    companion object {
        private val PULSES = 0
        private val TREE_SIZE = 1;
    }

    private var counter: Int = 0
    private var started: Boolean = false
    private val rand: Random = Random()

    override fun executeAction() {
        System.out.println("Cutting " + wood.id + " @ " + p)
        val level = mob.skillSet.getSkill(Skill.WOODCUTTING).currentLevel
        val axe = findAxe()


        //check that our pick can mine the wood
        if (axe == null || level < axe.level) {
            mob.sendMessage("You do not have a axe for which you have the level to use.")
            stop()
            return
        }

        //check that we can mine the wood
        if (level < wood.level) {
            mob.sendMessage("You do not have the required level to mine this rock.")
            stop()
            return
        }

        //start the process of cutting
        if (started) {
            if (counter == 0) {
                //Check inv capacity
                if (mob.inventory.freeSlots() == 0) {
                    mob.inventory.forceCapacityExceeded()
                    stop()
                    return
                }
                if (mob.inventory.add(wood.id)) {
                    //TODO: Use lookup from utils once it has a lookup function for IDs
                    val woodName = ItemDefinition.lookup(wood.id).name.toLowerCase();
                    mob.sendMessage("You managed to cut some " + woodName + ".")
                    mob.skillSet.addExperience(Skill.WOODCUTTING, wood.exp)

                } else {
                    System.out.println("Failed to add wood to inv");
                    stop();
                    return;
                }

                if (!cuttingSuccessful(wood.chance)) {
                    System.out.println("Chopping...")
                    cut(axe)
                    return //We did not cut down the tree... Keep going
                } else {
                    //We cut down the tree
                    var treeEntity: StaticGameObject? = null
                    val region = mob.world.regionRepository.fromPosition(position)
                    val entities = region.getEntities(position)
                    for (entity: Entity in entities) {
                        if (entity is StaticGameObject) {
                            System.out.println("Entity at cutting location: " + entity.id + " with type: " + entity.entityType)
                            if (entity.id == objectID) {
                                treeEntity = entity
                            }
                        }
                    }
                    if (treeEntity == null) { //tree entity not found at location...
                        System.out.println("WARNING: Invalid cutting condition on tree");
                        stop()
                        return
                    }
                    //Get ID of exipred wood
                    val expiredObjectID = wood.stump;
                    val expiredRockEntity = StaticGameObject(mob.world, expiredObjectID!!, position, treeEntity!!.type, treeEntity!!.orientation)
                    //Remove normal wood and replace with expired
                    System.out.println("Removing " + objectID + " addding " + expiredObjectID)
                    System.out.println("Adding tasks")
                    //add task to remove normal wood and replace with depleted
                    mob.world.schedule(object: ScheduledTask(0, true) {
                        override fun execute() {
                            System.out.println("running deplete task")
                            //Replace normal wood with expired wood
                            region.removeEntity(treeEntity);
                            region.addEntity(expiredRockEntity)
                            this.stop() //Makes task run once
                        }
                    })
                    //add task to respawn normal wood
                    //respawn time: http://runescape.wikia.com/wiki/Trees
                    val respawn = ((30 * 1000) / 600) + ((rand.nextInt(150) * 1000) / 600) // between 30 sec and 3 min respawm
                    mob.world.schedule(object: ScheduledTask(respawn, false) {
                        override fun execute() {
                            System.out.println("running wood task")
                            //Replace expired wood with normal wood
                            region.removeEntity(expiredRockEntity)
                            region.addEntity(treeEntity);
                            this.stop() //Makes task run once
                        }
                    })
                    stop()
                    return
                }
            }
            counter -= 1
        } else {
            started = true
            cut(axe)
        }
    }

    private fun findAxe(): Axe? {
        for (axe in getAxes()) {
            if (axe!!.level > mob.skillSet.getSkill(Skill.WOODCUTTING).currentLevel) {
                continue;
            }
            val weponSlot = mob.equipment.get(EquipmentConstants.WEAPON)
            if (weponSlot != null && weponSlot.id == axe.id) {
                return axe;
            } else if (mob.inventory.contains(axe.id)) {
                return axe;
            }
        }
        return null;
    }

    private fun cut(axe: Axe) {
        mob.sendMessage("You swing your axe at the tree.")
        mob.playAnimation(axe.animation)
        counter = axe.pulses
        mob.turnTo(position)
    }

    private fun cuttingSuccessful(woodChance: Double): Boolean {
        return rand.nextInt(100) <= woodChance * 100;
    }
}


on {ObjectActionMessage::class}
        .where {option == 1}
        .then {
            if (lookupTree(id) != null) {
                it.startAction(WoodcuttingAction(it, id, this.position, lookupTree(id)!!))
            } else {
                System.out.println("Unknown wood: " + id)
            }
        }
