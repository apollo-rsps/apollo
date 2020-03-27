import Bone.Companion.isBone
import Prayer.Companion.isPrayerButton
import org.apollo.game.message.impl.decode.IfActionMessage
import org.apollo.game.message.impl.decode.ItemOptionMessage
import org.apollo.game.model.event.impl.LogoutEvent
import org.apollo.game.plugin.api.prayer

// Clear the player's prayer(s) on logout
on_player_event { LogoutEvent::class }
    .then {
        playerPrayers.removeAll(it)
    }

on { IfActionMessage::class }
    .where { componentId.isPrayerButton() }
    .then { player ->
        val prayer = Prayer.forButton(componentId)!!
        val level = prayer.level

        if (level > player.prayer.current) {
            player.sendMessage("You need a prayer level of $level to use this prayer.")
            terminate()
            return@then
        }

        player.updatePrayer(prayer)
        terminate()
    }

on { ItemOptionMessage::class }
    .where { option == BuryBoneAction.BURY_OPTION && id.isBone() }
    .then { player ->
        val bone = Bone[id]!!

        player.startAction(BuryBoneAction(player, slot, bone))
        terminate()
    }
