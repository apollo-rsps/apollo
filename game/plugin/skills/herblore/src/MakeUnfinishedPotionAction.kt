import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncAction
import org.apollo.game.model.Animation
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.herblore
import java.util.Objects

class MakeUnfinishedPotionAction(
    player: Player,
    private val potion: UnfinishedPotion
) : AsyncAction<Player>(1, true, player) {

    override fun action(): ActionBlock = {
        val level = mob.herblore.current

        if (level < potion.level) {
            mob.sendMessage("You need a Herblore level of ${potion.level} to make this.")
            stop()
        }

        val inventory = mob.inventory

        if (inventory.contains(VIAL_OF_WATER) && inventory.contains(potion.herb)) {
            inventory.remove(VIAL_OF_WATER)
            inventory.remove(potion.herb)

            mob.playAnimation(MIXING_ANIMATION)

            inventory.add(potion.id)
            mob.sendMessage("You put the ${potion.herbName} into the vial of water.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MakeUnfinishedPotionAction
        return mob == other.mob && potion == other.potion
    }

    override fun hashCode(): Int = Objects.hash(mob, potion)

    private companion object {
        private val MIXING_ANIMATION = Animation(363)
    }
}