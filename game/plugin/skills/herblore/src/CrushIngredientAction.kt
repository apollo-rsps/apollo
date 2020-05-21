import java.util.Objects
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncAction
import org.apollo.game.model.Animation
import org.apollo.game.model.entity.Player

class CrushIngredientAction(
    player: Player,
    private val ingredient: CrushableIngredient
) : AsyncAction<Player>(0, true, player) {

    override fun action(): ActionBlock = {
        mob.playAnimation(GRINDING_ANIM)
        wait(pulses = 1)

        val inventory = mob.inventory
        if (inventory.remove(ingredient.uncrushed)) {
            inventory.add(ingredient.id)

            mob.sendMessage("You carefully grind the ${ingredient.uncrushedName} to dust.")
        }

        stop()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CrushIngredientAction
        return mob == other.mob && ingredient == other.ingredient
    }

    override fun hashCode(): Int = Objects.hash(mob, ingredient)

    private companion object {
        private val GRINDING_ANIM = Animation(364)
    }
}