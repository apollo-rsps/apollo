import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncAction
import org.apollo.game.model.Animation
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.herblore
import java.util.Objects

class MakeFinishedPotionAction(
    player: Player,
    private val potion: FinishedPotion
) : AsyncAction<Player>(1, true, player) {

    override fun action(): ActionBlock = {
        val level = mob.herblore.current

        if (level < potion.level) {
            mob.sendMessage("You need a Herblore level of ${potion.level} to make this.")
            stop()
        }

        val unfinished = potion.unfinished.id
        val inventory = mob.inventory

        if (inventory.contains(unfinished) && inventory.contains(potion.ingredient)) {
            inventory.remove(unfinished)
            inventory.remove(potion.ingredient)

            mob.playAnimation(MIXING_ANIMATION)

            inventory.add(potion.id)
            mob.herblore.experience += potion.experience

            mob.sendMessage("You mix the ${potion.ingredientName} into your potion.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MakeFinishedPotionAction
        return mob == other.mob && potion == other.potion
    }

    override fun hashCode(): Int = Objects.hash(mob, potion)

    private companion object {
        private val MIXING_ANIMATION = Animation(363)
    }
}