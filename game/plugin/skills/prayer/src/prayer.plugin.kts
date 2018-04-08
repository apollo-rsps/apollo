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
        PLAYER_PRAYERS.removeAll(it)
    }

on { ButtonMessage::class }
    .then {
        val prayer = Prayer.forButton(widgetId) ?: return@then
        if (prayer.level > it.prayer.current) {
            terminate()
            return@then
        }
        updatePrayer(it, prayer)
        terminate()
    }

on { ItemOptionMessage::class }
    .where { option == 1 }
    .then {
        val bone = Bone[id] ?: return@then

        it.startAction(BuryBoneAction(it, slot, bone))
        terminate()
    }
