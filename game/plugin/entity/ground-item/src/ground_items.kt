import org.apollo.game.message.impl.RemoveTileItemMessage
import org.apollo.game.message.impl.SendTileItemMessage
import org.apollo.game.model.Item
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.GroundItem
import org.apollo.game.model.entity.Player

/**
 * Spawns a new local [GroundItem] for this Player at the specified [Position].
 */
fun Player.addGroundItem(item: Item, position: Position) {
    world.addGroundItem(this, GroundItem.dropped(world, position, item, this))
}

internal fun World.addGroundItem(player: Player, item: GroundItem) {
    val region = regionRepository.fromPosition(item.position)

    if (item.isGlobal) {
        region.addEntity(item, true)
        return
    }

    groundItems.computeIfAbsent(player.encodedName, { HashSet<GroundItem>() }) += item

    val offset = region.getPositionOffset(item)
    player.send(SendTileItemMessage(item.item, offset))

    schedule(GroundItemSynchronizationTask(item))
}

internal fun World.removeGroundItem(player: Player, item: GroundItem) {
    val region = regionRepository.fromPosition(item.position)

    if (item.isGlobal) {
        region.removeEntity(item)
    }

    val items = groundItems[player.encodedName] ?: return
    items -= item

    val offset = region.getPositionOffset(item)
    player.send(RemoveTileItemMessage(item.item, offset))
}