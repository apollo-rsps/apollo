
import java.util.Objects
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncAction
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.herblore
import org.apollo.util.LanguageUtil

class IdentifyHerbAction(
    player: Player,
    private val slot: Int,
    private val herb: Herb
) : AsyncAction<Player>(0, true, player) {

    override fun action(): ActionBlock = {
        if (mob.herblore.current < herb.level) {
            mob.sendMessage("You need a Herblore level of ${herb.level} to clean this herb.")
            stop()
        }

        val inventory = mob.inventory

        if (inventory.removeSlot(slot, 1) > 0) {
            inventory.add(herb.identified)
            mob.herblore.experience += herb.experience

            val name = herb.identifiedName
            val article = LanguageUtil.getIndefiniteArticle(name)

            mob.sendMessage("You identify the herb as $article $name.")
        }

        stop()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IdentifyHerbAction
        return mob == other.mob && herb == other.herb && slot == other.slot
    }

    override fun hashCode(): Int = Objects.hash(mob, herb, slot)

    companion object {
        const val IDENTIFY_OPTION = 1
    }
}