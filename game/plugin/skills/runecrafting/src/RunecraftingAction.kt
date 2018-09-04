package org.apollo.game.plugin.skill.runecrafting

import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.Definitions
import org.apollo.game.plugin.api.runecraft
import org.apollo.util.LanguageUtil

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

        val name = Definitions.item(rune.id).name
        val nameArticle = LanguageUtil.getIndefiniteArticle(name)
        val essenceAmount = player.inventory.removeAll(runeEssenceId)
        val runeAmount = essenceAmount * rune.getBonusMultiplier(player.runecraft.current)
        val runesDescription = if (runeAmount > 1) "some ${name}s" else "$nameArticle $name"

        player.sendMessage("You craft the rune essence into $runesDescription")
        player.inventory.add(rune.id, runeAmount.toInt())
        player.runecraft.experience += rune.xp * essenceAmount
        stop()
    }
}
