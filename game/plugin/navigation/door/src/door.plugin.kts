
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.plugin.navigation.door.Door
import org.apollo.plugin.navigation.door.OpenDoorAction

/**
 * Hook into the [ObjectActionMessage] and listens for a supported door [GameObject]
 */
on { ObjectActionMessage::class }
        .where { option == 1 }
        .then {
            val door = Door.find(it.world, position, id) ?: return@then
            if (door.supported()) {
                OpenDoorAction.start(this, it, door, position)
            }
        }