import org.apollo.cache.def.ItemDefinition
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.action.DistancedAction
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.runecraft

class TeleportAction(val player: Player, val start: Position, val distance: Int, val end: Position): DistancedAction<Player>(0, true, player, start, distance) {
    override fun executeAction() {
        player.teleport(end)
        stop()
    }
}

class CreateTiaraAction(val player: Player, val position: Position, val tiara: Tiara, val alter: Alter): DistancedAction<Player>(0, true, player, position, 2) {
    override fun executeAction() {
        //Check if correct alter
        if (tiara.alter != alter) {
            player.sendMessage("You can't use that talisman on this alter.")
            stop()
        }

        //Check player inventory for blank tiara
        if (player.inventory.contains(TIARA_ITEM_ID)) {
            player.inventory.remove(TIARA_ITEM_ID)
            player.inventory.add(tiara.id)
            player.runecraft.experience += tiara.xp
            player.playAnimation(ANIMATION)
            player.playGraphic(GRAPHIC)
            stop()
        }
    }
}

class RunecraftingAction(val player: Player, val rune: Rune, val alter: Alter): AsyncDistancedAction<Player>(0, true, player, alter.center, 3) {
    override fun action(): ActionBlock = {
        //Check player runecrafting level
        if (player.runecraft.current < rune.level) {
            val level = rune.level
            player.sendMessage("You need a rinecrafting level of $level to craft this rune.")
            stop()
        }

        if (!player.inventory.contains(RUNE_ESSENCE_ID)) {
            player.sendMessage("You need rune essence to craft runes.")
            stop()
        }
        //Initial setup for animation
        player.turnTo(position)
        player.playAnimation(ANIMATION)
        player.playGraphic(GRAPHIC)

        //delay for animation
        wait(1)

        //craft rune
        val removed = player.inventory.remove(RUNE_ESSENCE_ID, player.inventory.getAmount(RUNE_ESSENCE_ID))
        val added = removed * rune.getBonus()
        player.inventory.add(rune.id, added.toInt())
        val name = ItemDefinition.lookup(rune.id).name;
        val runes = if (added > 1) "some $name" + "s" else "an $name"
        player.sendMessage("You craft the rune essence into $runes")
        player.runecraft.experience += rune.xp
        stop()
    }

}


