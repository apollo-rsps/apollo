package org.apollo.plugin.navigation.door

import java.util.*
import org.apollo.game.action.DistancedAction
import org.apollo.game.model.Direction
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.DynamicGameObject
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.model.event.PlayerEvent
import org.apollo.game.plugin.api.findObject
import org.apollo.net.message.Message

enum class DoorType {
    LEFT, RIGHT, NOT_SUPPORTED
}

class Door(private val gameObject: GameObject) {

    companion object {

        val LEFT_HINGE_ORIENTATION: HashMap<Direction, Direction> = hashMapOf(
            Direction.NORTH to Direction.WEST,
            Direction.SOUTH to Direction.EAST,
            Direction.WEST to Direction.SOUTH,
            Direction.EAST to Direction.NORTH
        )

        val RIGHT_HINGE_ORIENTATION: HashMap<Direction, Direction> = hashMapOf(
            Direction.NORTH to Direction.EAST,
            Direction.SOUTH to Direction.WEST,
            Direction.WEST to Direction.NORTH,
            Direction.EAST to Direction.SOUTH
        )

        val toggledDoors = hashMapOf<GameObject, GameObject>()

        val LEFT_HINGED = setOf(1516, 1536, 1533)

        val RIGHT_HINGED = setOf(1519, 1530, 4465, 4467, 3014, 3017, 3018, 3019)

        /**
         * Find a given door in the world
         * @param world The [World] the door lives in
         * @param position The [Position] of the door
         * @param objectId The [GameObject] id of the door
         */
        fun find(world: World, position: Position, objectId: Int): Door? {
            return world.findObject(position, objectId)?.let(::Door)
        }
    }

    /**
     * Returns the supported doors by the system
     * See [DoorType]
     */
    fun supported(): Boolean {
        return type() !== DoorType.NOT_SUPPORTED
    }

    /**
     * Computes the given door type by which id exists in
     * the supported left and right hinged doors
     */
    fun type(): DoorType {
        return when {
            gameObject.id in LEFT_HINGED -> DoorType.LEFT
            gameObject.id in RIGHT_HINGED -> DoorType.RIGHT
            else -> DoorType.NOT_SUPPORTED
        }
    }

    /**
     * Toggles a given [GameObject] orientation and position
     * Stores the door state in toggleDoors class variable
     */
    fun toggle() {
        val world = gameObject.world
        val regionRepository = world.regionRepository

        regionRepository.fromPosition(gameObject.position).removeEntity(gameObject)

        val originalDoor = toggledDoors[gameObject]

        if (originalDoor == null) {
            val position = movePosition()
            val orientation: Int = translateDirection()?.toOrientationInteger() ?: gameObject.orientation

            val toggledDoor = DynamicGameObject.createPublic(world, gameObject.id, position, gameObject.type,
                orientation)

            regionRepository.fromPosition(position).addEntity(toggledDoor)
            toggledDoors[toggledDoor] = gameObject
        } else {
            toggledDoors.remove(gameObject)
            regionRepository.fromPosition(originalDoor.position).addEntity(originalDoor)
        }
    }

    /**
     * Calculates the position to move the door based on orientation
     */
    private fun movePosition(): Position {
        return gameObject.position.step(1, Direction.WNES[gameObject.orientation])
    }

    /**
     * Calculates the orientation of the door based on
     * if it is right or left hinged door
     */
    private fun translateDirection(): Direction? {
        val direction = Direction.WNES[gameObject.orientation]

        return when (type()) {
            DoorType.LEFT -> LEFT_HINGE_ORIENTATION[direction]
            DoorType.RIGHT -> RIGHT_HINGE_ORIENTATION[direction]
            DoorType.NOT_SUPPORTED -> null
        }
    }
}

class OpenDoorAction(private val player: Player, private val door: Door, position: Position) : DistancedAction<Player>(
    0, true, player, position, DISTANCE) {

    companion object {

        /**
         * The distance threshold that must be reached before the door is opened.
         */
        const val DISTANCE = 1

        /**
         * Starts a [OpenDoorAction] for the specified [Player], terminating the [Message] that triggered.
         */
        fun start(message: Message, player: Player, door: Door, position: Position) {
            player.startAction(OpenDoorAction(player, door, position))
            message.terminate()
        }
    }

    override fun executeAction() {
        if (player.world.submit(OpenDoorEvent(player))) {
            player.turnTo(position)
            door.toggle()
        }
        stop()
    }

    override fun equals(other: Any?): Boolean {
        return other is OpenDoorAction && position == other.position && player == other.player
    }

    override fun hashCode(): Int = Objects.hash(position, player)
}

class OpenDoorEvent(player: Player) : PlayerEvent(player)
