import org.apollo.game.action.DistancedAction
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.DynamicGameObject
import org.apollo.game.model.entity.EntityType
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.event.PlayerEvent
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.net.message.Message

enum class Orientation(val value: Int) {
    WEST(0), NORTH(1), EAST(2), SOUTH(3);
    companion object {
        fun from(findValue: Int): Orientation = Orientation.values().first { it.value == findValue }
    }
}

enum class DoorType {
    LEFT, RIGHT, NOT_SUPPORTED
}

class Door(private val gameObject: GameObject) {

    companion object {

        /**
         * Mapping to change orientation of a left hinged door
         */
        val LEFT_HINGE_ORIENTATION: HashMap<Orientation, Orientation> = hashMapOf(
                Orientation.NORTH to Orientation.WEST,
                Orientation.SOUTH to Orientation.EAST,
                Orientation.WEST to Orientation.SOUTH,
                Orientation.EAST to Orientation.NORTH
        )

        /**
         * Mapping to change orientation of a right hinged door
         */
        val RIGHT_HINGE_ORIENTATION: HashMap<Orientation, Orientation> = hashMapOf(
                Orientation.NORTH to Orientation.EAST,
                Orientation.SOUTH to Orientation.WEST,
                Orientation.WEST to Orientation.NORTH,
                Orientation.EAST to Orientation.SOUTH
        )

        /**
         * The list of doors that have already been toggled
         */
        val toggledDoors: HashMap<GameObject, GameObject> = hashMapOf()

        /**
         * The ids of all left hinged doors.
         */
        val LEFT_HINGED = setOf(1516, 1536, 1533)

        /**
         * The ids of all right hinged doors.
         */
        val RIGHT_HINGED = setOf(1519, 1530, 4465, 4467, 3014, 3017, 3018, 3019)

        /**
         * Find a given door in the world
         * @param world The [World] the door lives in
         * @param position The [Position] of the door
         * @param objectId The [GameObject] id of the door
         */
        fun find(world: World, position: Position, objectId: Int): Door? {
            val objects: Set<GameObject> = world.regionRepository.fromPosition(position).getEntities(position, EntityType.DYNAMIC_OBJECT, EntityType.STATIC_OBJECT)
            val gameObject: GameObject? = objects.find { it.id == objectId }
            return if(gameObject == null) {
                null
            } else {
                Door(gameObject)
            }
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

        // Remove current door from region
        regionRepository.fromPosition(gameObject.position).removeEntity(gameObject)

        // Checks if door has been toggled
        val originalDoor: GameObject? = toggledDoors[gameObject]

        if (originalDoor == null) {
            // If not toggled it will change orientation and position of door
            val position = movePosition()
            val orientation: Int = translateOrientation()?.value ?: gameObject.orientation

            val toggledDoor = DynamicGameObject.createPublic(world, gameObject.id, position, gameObject.type, orientation)

            // Renders new door
            regionRepository.fromPosition(position).addEntity(toggledDoor)
            toggledDoors.put(toggledDoor, gameObject)
        } else {
            // Removes toggled door
            toggledDoors.remove(gameObject)
            // Renders original door
            regionRepository.fromPosition(originalDoor.position).addEntity(originalDoor)
        }

    }

    /**
     * Calculates the position to move the door based on orientation
     */
    private fun movePosition(): Position {
        val position = gameObject.position
        return when(Orientation.from(gameObject.orientation)) {
            Orientation.WEST -> Position(position.x - 1, position.y, position.height)
            Orientation.EAST -> Position(position.x + 1, position.y, position.height)
            Orientation.NORTH -> Position(position.x, position.y + 1, position.height)
            Orientation.SOUTH -> Position(position.x, position.y - 1, position.height)
        }

    }

    /**
     * Calculates the orientation of the door based on
     * if it is right or left hinged door
     */
    private fun translateOrientation(): Orientation? {
        val orientation = Orientation.from(gameObject.orientation)
        return when(type()) {
            DoorType.LEFT -> LEFT_HINGE_ORIENTATION[orientation]
            DoorType.RIGHT -> RIGHT_HINGE_ORIENTATION[orientation]
            DoorType.NOT_SUPPORTED -> null
        }
    }

}

class OpenDoorAction(private val player: Player, private val door: Door, position: Position) : DistancedAction<Player>(0, true, player, position, DISTANCE) {

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
        if(player.world.submit(OpenDoorEvent(player))) {
            player.turnTo(position)
            door.toggle()
        }
        stop()
    }

    override fun equals(other: Any?): Boolean {
        return other is OpenDoorAction && position == other.position
    }

    override fun hashCode(): Int {
        return position.hashCode()
    }

}

class OpenDoorEvent(player: Player) : PlayerEvent(player)


/**
 * Hook into the [ObjectActionMessage] and listens for a supported door [GameObject]
 */
on { ObjectActionMessage::class }
        .where { option == 1 }
        .then {
            val door: Door? = Door.find(it.world, position, id)
            if (door != null && door.supported()) {
                OpenDoorAction.start(this, it, door, position)
            }
        }