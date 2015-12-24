java_import 'org.apollo.game.action.Action'

# Represents a one off {@code Attack}, which will not continue a combat session
# upon completion.
class AttackAction < Action

  def initialize(source, target, delay, attack)
    @source = source
    @target = target
    @delay  = delay
    @attack = attack
  end

  def execute
    begin
      @attack.do(mob, @target)

      stop
    rescue AttackRequirementException => e
      player.send_message e.message
      stop
    end
  end
end

class CombatAction < Action
  def initialize(source, attack = nil)
    super(0, true, source)

    @combat_state = get_combat_state(source)
    @attack       = attack
  end

  def execute
    if @combat_state.target.nil? and @combat_state.queued_attacks.empty?
      @combat_state.reset
      stop
    end

    case @combat_state.state
      when :idle
        update_idle
      when :attacking
        update_attacking
      when :chasing
        update_chasing
      else
        throw ArgumentError.new('invalid combat state')
    end
  end

  def update_idle
    if @combat_state.queued_attacks.empty? and @combat_state.supports_weapon
      weapon       = EquipmentUtil.equipped_weapon mob
      weapon_class = weapon.weapon_class
      combat_style = weapon_class.style_at mob.combat_style

      if mob.attacking
        set_delay weapon_class.speed(combat_style) - 1
      else
        set_delay 0
        mob.attacking = true
      end

      if mob.using_special and weapon.special_attack?
        @combat_state.next_attack = weapon.special_attack
      else
        @combat_state.next_attack = weapon_class.attack(combat_style)
      end

      @combat_state.state = :attacking
    elsif @combat_state.queued_attacks.size > 0
      @combat_state.next_attack = @combat_state.queued_attacks.pop
      @combat_state.state       = :attacking
    else
      stop
      @combat_state.reset
    end
  end

  def update_attacking
    raise RuntimeError.new('no attack when in :attacking state') if @combat_state.next_attack.nil?

    begin
      @combat_state.next_attack.do(mob, @combat_state.target)

      @combat_state.state = :idle
      set_delay 0
    rescue AttackRequirementException => e
      mob.send_message e.message
    end
  end

  def update_chasing

  end

  def stop
    super

    @combat_state.reset
    mob.attacking = false
  end
end