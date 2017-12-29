import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.action.DistancedAction
import org.apollo.game.model.Animation
import org.apollo.game.model.Graphic
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.Definitions
import org.apollo.game.plugin.api.runecraft
import org.apollo.util.LanguageUtil

private val blankTiaraId = 5525
private val runecraftingAnimation = Animation(791)
private val runecraftingGraphic = Graphic(186, 0, 100)
private val runeEssenceId = 1436

class TeleportAction(val player: Player, val start: Position, val distance: Int, val end: Position) : DistancedAction<Player>(0, true, player, start, distance) {
    override fun executeAction() {
        player.teleport(end)
        stop()
    }
}

class CreateTiaraAction(val player: Player, val position: Position, val tiara: Tiara, val altar: Altar) : DistancedAction<Player>(0, true, player, position, 2) {
    override fun executeAction() {
        if (tiara.altar != altar) {
            player.sendMessage("You can't use that talisman on this altar.")
            stop()
            return
        }

        if (player.inventory.contains(blankTiaraId)) {
            player.inventory.remove(blankTiaraId)
            player.inventory.add(tiara.id)
            player.runecraft.experience += tiara.xp
            player.playAnimation(runecraftingAnimation)
            player.playGraphic(runecraftingGraphic)
            stop()
        }
    }
}

class RunecraftingAction(val player: Player, val rune: Rune, altar: Altar) : AsyncDistancedAction<Player>(0, true, player, altar.center, 3) {
    override fun action(): ActionBlock = {
        if (player.runecraft.current < rune.level) {
            player.sendMessage("You need a runecrafting level of ${rune.level} to craft this rune.")
            stop()
        }

        if (!player.inventory.contains(runeEssenceId)) {
            player.sendMessage("You need rune essence to craft runes.")
            stop()
        }

        player.turnTo(position)
        player.playAnimation(runecraftingAnimation)
        player.playGraphic(runecraftingGraphic)

        wait(1)

        val name = Definitions.item(rune.id)?.name;
        val nameArticle = LanguageUtil.getIndefiniteArticle(name)
        val essenceAmount = player.inventory.removeAll(runeEssenceId)
        val runeAmount = essenceAmount * rune.getBonus()
        val runesDescription = if (runeAmount > 1) "some ${name}s" else "$nameArticle $name"

        player.sendMessage("You craft the rune essence into $runesDescription")
        player.inventory.add(rune.id, runeAmount.toInt())
        player.runecraft.experience += rune.xp
        stop()
    }

}


