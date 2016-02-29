def get_combat_state(mob)
  mob.is_a?(Player) ? type = :player : type = :npc

  unless MOB_COMBAT_STATE_CACHE[type].has_key? mob.index
    MOB_COMBAT_STATE_CACHE[type][mob.index] = CombatState.new(mob)
  end

  MOB_COMBAT_STATE_CACHE[type][mob.index]
end

private

class CombatState
  attr_accessor :state, :next_attack
  attr_reader :queued_attacks, :supports_weapon

  def initialize(mob, supports_weapon = true)
    @mob             = mob
    @supports_weapon = supports_weapon
    reset
  end

  def reset
    @state          = :idle
    @target         = nil
    @next_attack    = nil
    @queued_attacks = []
    @mob.reset_interacting_mob
  end

  def target
    @target
  end

  def target=(target)
    @mob.reset_interacting_mob
    @mob.interacting_mob = target
    @target              = target
  end

  def queue_attack(attack)
    @queued_attacks.push(attack)
  end
end

MOB_COMBAT_STATE_CACHE = {
  :player => {},
  :npc    => {}
}
