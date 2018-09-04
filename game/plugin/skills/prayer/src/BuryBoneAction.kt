import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncAction
import org.apollo.game.model.Animation
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.prayer

class BuryBoneAction(
    player: Player,
    private val slot: Int,
    private val bone: Bone
) : AsyncAction<Player>(0, true, player) {

    override fun action(): ActionBlock = {
        if (mob.inventory.removeSlot(slot, 1) > 0) {
            mob.sendMessage("You dig a hole in the ground...")
            mob.playAnimation(BURY_BONE_ANIMATION)

            wait(pulses = 1)

            mob.sendMessage("You bury the bones.")
            mob.prayer.experience += bone.xp
        }

        stop()
    }

    companion object {
        public val BURY_BONE_ANIMATION = Animation(827)
        internal const val BURY_OPTION = 1
    }
}