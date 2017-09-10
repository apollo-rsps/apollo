import org.apollo.game.action.Action
import org.apollo.game.message.impl.ItemOnItemMessage
import org.apollo.game.model.Direction
import org.apollo.game.model.Item
import org.apollo.game.model.Position
import org.apollo.game.model.entity.*
import org.apollo.game.model.entity.obj.DynamicGameObject
import org.apollo.game.scheduling.ScheduledTask
import java.util.*
import kotlin.properties.Delegates


class FiremakingAction(val player: Player, val log: Log):Action<Player>(DELAY, true, player) {

    private var started = false
    private var groundLog: GroundItem by Delegates.notNull()
    val rand = Random()

    companion object {
        private val DELAY = 0
    }

    override fun execute() {
        mob.walkingQueue.clear()
        //Check log level
        if (log.level > player.skillSet.getSkill(Skill.FIREMAKING).currentLevel) {
            player.sendMessage("You need a Firemaking level of " + log.level + " to light this log.")
            stop()
            return
        }

        //check if we have a tinderbox
        if (!player.inventory.contains(TINDER_BOX)) {
            player.sendMessage("You need a tinderbox in your inventory in order to light fires.")
            stop()
            return
        }

        if (!started) {
            val region = player.world.regionRepository.fromPosition(player.position)
            player.sendMessage("You attempt to light the logs.")
            if (region.getEntities<Entity>(player.position, EntityType.DYNAMIC_OBJECT, EntityType.STATIC_OBJECT).isEmpty()) {
                player.inventory.remove(log.id)
                groundLog = GroundItem.dropped(player.world, player.position, Item(log.id), player)
                player.world.spawn(groundLog)
            } else {
                player.sendMessage("You cannot light a fire here.")
                stop()
                return
            }
            started = true
        }

        //light the fire
        player.playAnimation(LIGHT_ANIMATION)

        if (successfulLight()) {
            if (lightFire(Direction.WEST) ||
                    lightFire(Direction.EAST) ||
                    lightFire(Direction.NORTH) ||
                    lightFire(Direction.SOUTH) ||
                    lightFire(Direction.NONE)
                    ) {
                player.sendMessage("The fire catches and the logs begin to burn.")
                player.skillSet.addExperience(Skill.FIREMAKING, log.xp)
            } else {
                player.sendMessage("You cannot light a fire here.")
            }
            stop()
        }
    }

    override fun stop() {
        super.stop()
        player.stopAnimation()
    }

    fun successfulLight(): Boolean {
        //TODO: This is from lare96, as he mentioned, we need to find the actual chance
        val playerLevel = player.skillSet.getSkill(Skill.FIREMAKING).currentLevel
        val lowChance = playerLevel - log.level + 5
        if (lowChance > 30) {
            return 30 > rand.nextInt(40)
        } else {
            return lowChance > rand.nextInt(40)
        }
    }

    fun walkCoords(direction: Direction): Position {
        if (direction == Direction.NORTH) {
            return Position(player.position.x, player.position.y + 1, player.position.height)
        } else if (direction == Direction.SOUTH) {
            return Position(player.position.x, player.position.y - 1, player.position.height)
        } else if (direction == Direction.WEST) {
            return Position(player.position.x - 1, player.position.y, player.position.height)
        } else if (direction == Direction.EAST) {
            return Position(player.position.x + 1, player.position.y, player.position.height)
        }  else if (direction == Direction.NONE) {
            return Position(player.position.x, player.position.y, player.position.height)
        } else {
            return player.position //Should never happen so I just put this
        }
    }

    fun lightFire(direction: Direction): Boolean {
        if (canLight(direction)) {
            val fire = DynamicGameObject.createPublic(player.world, FIRE_OBJ, player.position, 10, 0)
            val walkTo = walkCoords(direction)
            if (walkTo != mob.position) {
                mob.walkingQueue.addFirstStep(walkTo)
            }
            val region = player.world.regionRepository.fromPosition(player.position)
            region.removeEntity(groundLog)
            player.world.spawn(fire)

            //TODO: burn time = log level * 5 + rand(30). I think this is right can someone verify?

            val burnTime = (log.level * 5 + rand.nextInt(30) * 1000) / 600 //convert to pulses
            player.world.schedule(object: ScheduledTask(burnTime, false) {
                override fun execute() {
                    region.removeEntity(fire)
                    player.world.spawn(GroundItem.create(player.world, fire.position, Item(ASH)))
                    this.stop()
                }

            })
            return true
        }
        return false
    }

    fun canLight(direction: Direction): Boolean {
        val region = player.world.regionRepository.fromPosition(player.position)
        if (direction == Direction.NONE) {
            return true
        }
        if (region.traversable(player.position, EntityType.PLAYER, direction)) {
            return true
        }
        return false
    }

}


on { ItemOnItemMessage::class }
        .where { (id == TINDER_BOX && lookupLog(targetId) != null) || (targetId == TINDER_BOX && lookupLog(id) != null) }
        .then {
            if (id == TINDER_BOX) {
                it.startAction(FiremakingAction(it, lookupLog(targetId)!!))
                terminate()
            } else {
                it.startAction(FiremakingAction(it, lookupLog(id)!!))
                terminate()
            }
        }