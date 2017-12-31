import org.apollo.game.message.impl.NpcActionMessage
import org.apollo.game.model.event.impl.LogoutEvent

/**

on :message, :npc_action do |player, message|
target = $world.npc_repository.get message.index

# unless target.attacking
#   target_combat_state        = get_combat_state target
#   target_combat_state.target = player
#
#   target.start_action CombatAction.new(target)
# end

player_combat_state = player.get_combat_state
player_combat_state.target = target

player.send HintIconMessage.for_npc(target.index)
player.walking_queue.clear
player.start_action CombatAction.new(player)
end
 */

start {

}

on_player_event { LogoutEvent::class }
    .then {
        CombatStateManager.remove(it)
    }

on { NpcActionMessage::class }
    .then {
        CombatAction.start(this, it, it.world.npcRepository[index])
    }
