import org.apollo.game.action.Action
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncAction
import org.apollo.game.message.impl.ButtonMessage
import org.apollo.game.message.impl.ItemOptionMessage
import org.apollo.game.model.entity.Player
import org.apollo.game.model.event.impl.LogoutEvent
import org.apollo.game.plugin.api.prayer

//Clear the player prayer on logout
on_player_event { LogoutEvent::class }
        .then {
            PLAYER_PRAYERS[it] = null;
        }

on { ButtonMessage::class }
        .then {
            val prayer = Prayer.findByButton(widgetId) ?: return@then
            if (prayer.level > it.prayer.current) {
                updatePrayer(it,null)
                terminate();
                return@then
            }
            updatePrayer(it, prayer)
            terminate()
        }

on { ItemOptionMessage::class }
        .where { option == 1 }
        .then {
            val bone = Bone.findById(id) ?: return@then
            it.startAction(BuryBoneAction(it, slot, bone))
            terminate()
        }

class BuryBoneAction(val player: Player, val slot: Int, val bone: Bone): AsyncAction<Player>(0, true, player) {
    override fun action(): ActionBlock = {
        if (player.inventory.get(slot).id == bone.id) {
            player.sendMessage("You dig a hole in the ground...")
            player.playAnimation(BURY_BONE_ANIMATION)
            wait(1) //Wait for animation
            player.inventory.reset(slot)
            player.sendMessage("You bury the bones.")
            player.prayer.experience += bone.xp
        }
        stop()
    }
}