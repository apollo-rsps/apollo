import org.apollo.cache.def.ItemDefinition
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.action.DistancedAction
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.runecraft
import org.apollo.util.LanguageUtil

class TeleportAction(val player: Player, val start: Position, val distance: Int, val end: Position) : DistancedAction<Player>(0, true, player, start, distance) {
    override fun executeAction() {
        player.teleport(end)
        stop()
    }
}

class CreateTiaraAction(val player: Player, val position: Position, val tiara: Tiara, val altar: Altar) : DistancedAction<Player>(0, true, player, position, 2) {
    override fun executeAction() {
        //Check if correct altar
        if (tiara.altar != altar) {
            player.sendMessage("You can't use that talisman on this altar.")
            stop()
            return
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

class RunecraftingAction(val player: Player, val rune: Rune, val altar: Altar) : AsyncDistancedAction<Player>(0, true, player, altar.center, 3) {
    override fun action(): ActionBlock = action@ {
        //Check player runecrafting level
        if (player.runecraft.current < rune.level) {
            val level = rune.level
            player.sendMessage("You need a runecrafting level of $level to craft this rune.")
            stop()
            return@action
        }

        if (!player.inventory.contains(RUNE_ESSENCE_ID)) {
            player.sendMessage("You need rune essence to craft runes.")
            stop()
            return@action
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
        val runes = if (added > 1) "some $name" + "s" else LanguageUtil.getIndefiniteArticle(name) + " $name"
        player.sendMessage("You craft the rune essence into $runes")
        player.runecraft.experience += rune.xp
        stop()
    }

}


