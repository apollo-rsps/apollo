java_import 'org.apollo.game.message.impl.HintIconMessage'

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

on :message, :magic_on_mob do |player, message|
  target = $world.npc_repository.get(message.index)

  next unless SPELLBOOKS.key? message.interface_id

  player_combat_state = player.get_combat_state
  player_combat_state.target = target

  spellbook = SPELLBOOKS[message.interface_id]
  spell = spell_for(spellbook, message.spell_id)
  player_combat_state.queue_attack(spell.attack)

  player.walking_queue.clear
  player.start_action CombatAction.new(player, true)
end

on :message, :player_action do |player, message|

end

## TODO: another way of handling this
schedule 0 do |_task|
  $world.player_repository.each { |player| player.attack_timer = player.attack_timer + 1 }
  $world.npc_repository.each { |npc| npc.attack_timer = npc.attack_timer + 1 }
end
