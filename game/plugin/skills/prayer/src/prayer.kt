
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncAction
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.prayer

class BuryBoneAction(val player: Player, val slot: Int, val bone: Bone) : AsyncAction<Player>(0, true, player) {
    override fun action(): ActionBlock = {
        if (player.inventory.removeSlot(slot, 1) > 0) {
            player.sendMessage("You dig a hole in the ground...")
            player.playAnimation(BURY_BONE_ANIMATION)

            wait(1) //Wait for animation

            player.sendMessage("You bury the bones.")
            player.prayer.experience += bone.xp
        }

        stop()
    }
}