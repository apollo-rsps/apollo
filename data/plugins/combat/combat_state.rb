def get_combat_state(mob)
  mob.is_a?(Player) ? type = :player : type = :npc

  unless MOB_COMBAT_STATE_CACHE[type].key? mob.index
    MOB_COMBAT_STATE_CACHE[type][mob.index] = CombatState.new(mob)
  end

  MOB_COMBAT_STATE_CACHE[type][mob.index]
end

private

class CombatState
  attr_reader :target

  def initialize(mob, supports_weapon = true)
    @mob             = mob
    @supports_weapon = supports_weapon
    reset
  end

  def reset
    @target         = nil
    @next_attack    = nil
    @queued_attacks = []
  end

  def will_attack?
    is_attacking? && next_attack(true).speed >= @mob.attack_timer
  end

  def is_attacking?
    !target.nil? && @mob.attacking
  end

  def target=(target)
    @mob.reset_interacting_mob
    @mob.interacting_mob = target unless target.nil?
    @target              = target
  end

  def queue_attack(attack)
    @queued_attacks.push(attack)
  end

  def next_attack(peek = false)
    if @queued_attacks.size > 0
      peek ? @queued_attacks.last : @queued_attacks.pop
    else
      weapon = EquipmentUtil.equipped_weapon @mob

      if @mob.using_special && weapon.special_attack?
        weapon.special_attack
      else
        combat_style = weapon.weapon_class.selected_style @mob
        combat_style.attack
      end
    end
  end
end

MOB_COMBAT_STATE_CACHE = {
  player: {},
  npc:    {}
}

on :logout do |event|
  MOB_COMBAT_STATE_CACHE[:player].delete event.player.index
end
