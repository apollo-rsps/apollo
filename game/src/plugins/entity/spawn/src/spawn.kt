import org.apollo.cache.def.NpcDefinition
import org.apollo.game.model.*
import org.apollo.game.model.entity.Npc

data class Spawn(val id: Int?, val name: String, val position: Position, val facing: Direction,
                 val spawnAnimation: Animation? = null,
                 val spawnGraphic: Graphic? = null)

object Spawns {
    val list = mutableListOf<Spawn>()
}

fun npc_spawn(name: String, x: Int, y: Int, id: Int? = null) {
    Spawns.list.add(Spawn(id, name, Position(x, y), Direction.NORTH))
}
