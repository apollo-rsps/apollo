import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.area.Region
import org.apollo.game.model.entity.Entity
import org.apollo.game.model.entity.EntityType
import org.apollo.game.model.entity.EntityType.DYNAMIC_OBJECT
import org.apollo.game.model.entity.EntityType.STATIC_OBJECT
import org.apollo.game.model.entity.obj.DynamicGameObject
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.model.entity.obj.StaticGameObject
import org.apollo.game.scheduling.ScheduledTask
import java.lang.IllegalArgumentException
import java.util.*
import java.util.stream.Stream

fun <T : Entity> Region.find(position: Position, pred: (T) -> Boolean, vararg types: EntityType): Stream<T> {
    val result = this.getEntities<T>(position, *types)

    return result.stream()
            .filter(pred::invoke)
}

fun Region.findObjects(position: Position, id: Int): Stream<GameObject> {
    return find<GameObject>(position, { it.id == id }, DYNAMIC_OBJECT, STATIC_OBJECT)
}

fun Region.findObject(position: Position, id: Int): Optional<GameObject> {
    return find<GameObject>(position, { it.id == id }, DYNAMIC_OBJECT, STATIC_OBJECT)
            .findFirst()
}

class ExpireObjectTask(
        val world: World,
        val obj: GameObject,
        val replacement: GameObject,
        val respawnDelay: Int
) : ScheduledTask(0, true) {

    private var respawning: Boolean = false

    override fun execute() {
        val region = world.regionRepository.fromPosition(obj.position)

        if (!respawning) {
            world.spawn(replacement)
            respawning = true
            setDelay(respawnDelay)
        } else {
            region.removeEntity(replacement)
            world.spawn(obj)
            stop()
        }
    }
}

fun World.expireObject(obj: GameObject, replacement: Int, respawnDelay: Int) {
    val replacementObj = DynamicGameObject.createPublic(this, replacement, obj.position, obj.type, obj.orientation)
    val respawnedObj = DynamicGameObject.createPublic(this, obj.id, obj.position, obj.type, obj.orientation)

    schedule(ExpireObjectTask(this, obj, replacementObj, respawnDelay))
}
