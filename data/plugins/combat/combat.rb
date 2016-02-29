on :message, :npc_action do |player, message|
  player_combat_state        = get_combat_state player
  player_combat_state.target = $world.npc_repository.get message.index

  unless player.attacking
    player.start_action CombatAction.new(player)
  end
end

on :message, :player_action do |player, message|
  
end