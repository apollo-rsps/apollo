
import CrushableIngredient.Companion.isCrushable
import CrushableIngredient.Companion.isGrindingTool
import Herb.Companion.isIdentified
import Herb.Companion.isUnidentified
import Ingredient.Companion.isIngredient
import UnfinishedPotion.Companion.isUnfinished
import org.apollo.game.message.impl.ItemOnItemMessage
import org.apollo.game.message.impl.ItemOptionMessage

on { ItemOptionMessage::class }
    .where { option == IdentifyHerbAction.IDENTIFY_OPTION && id.isUnidentified() }
    .then { player ->
        val unidentified = Herb[id]!!

        player.startAction(IdentifyHerbAction(player, slot, unidentified))
        terminate()
    }

on { ItemOnItemMessage::class }
    .where { (id.isGrindingTool() && targetId.isCrushable() || id.isCrushable() && targetId.isGrindingTool()) }
    .then { player ->
        val crushableId = if (id.isCrushable()) id else targetId
        val raw = CrushableIngredient[crushableId]!!

        player.startAction(CrushIngredientAction(player, raw))
        terminate()
    }

on { ItemOnItemMessage::class }
    .where { id == VIAL_OF_WATER && targetId.isIdentified() || id.isIdentified() && targetId == VIAL_OF_WATER }
    .then { player ->
        val herbId = if (id.isIdentified()) id else targetId
        val unfinished = UnfinishedPotion[herbId]!!

        player.startAction(MakeUnfinishedPotionAction(player, unfinished))
        terminate()
    }

on { ItemOnItemMessage::class }
    .where { id.isUnfinished() && targetId.isIngredient() || id.isIngredient() && targetId.isUnfinished() }
    .then { player ->
        val key = if (id.isUnfinished()) Pair(id, targetId) else Pair(targetId, id)
        val finished = FinishedPotion[key]!!

        player.startAction(MakeFinishedPotionAction(player, finished))
        terminate()
    }