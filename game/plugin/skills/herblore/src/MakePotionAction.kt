import java.util.*
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncAction
import org.apollo.game.model.Animation
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.api.herblore

abstract class MakePotionAction(
    player: Player,
    private val potion: Potion
) : AsyncAction<Player>(1, true, player) {

    override fun action(): ActionBlock = {
        val level = mob.herblore.current

        if (level < potion.level) {
            mob.sendMessage("You need a Herblore level of ${potion.level} to make this.")
            stop()
        }

        val inventory = mob.inventory

        if (inventory.containsAll(*ingredients)) {
            ingredients.forEach { inventory.remove(it) }
            inventory.add(potion.id)

            mob.playAnimation(MIXING_ANIMATION)
            mob.sendMessage(message)
            reward()
        }
    }

    abstract val ingredients: IntArray
    abstract val message: String

    open fun reward() {}

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MakePotionAction
        return mob == other.mob && potion == other.potion
    }

    override fun hashCode(): Int = Objects.hash(mob, potion)

    private companion object {
        private val MIXING_ANIMATION = Animation(363)
    }
}

class MakeFinishedPotionAction(
    player: Player,
    private val potion: FinishedPotion
) : MakePotionAction(player, potion) {

    override val ingredients = intArrayOf(potion.unfinished.id, potion.ingredient)
    override val message by lazy { "You mix the ${potion.ingredientName} into your potion." }

    override fun reward() {
        mob.skillSet.addExperience(Skill.HERBLORE, potion.experience)
    }
}

class MakeUnfinishedPotionAction(
    player: Player,
    private val potion: UnfinishedPotion
) : MakePotionAction(player, potion) {

    override val ingredients = intArrayOf(VIAL_OF_WATER, potion.herb)
    override val message by lazy { "You put the ${potion.herbName} into the vial of water." }
}