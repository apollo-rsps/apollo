java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.model.entity.EntityType'

class CombatAction < Action
  def initialize(source, once = false)
    super(0, true, source)

    mob.attacking = true

    @combat_state = source.get_combat_state
    @attack_timer = 100
    @once         = once
  end

  def can_attack?
    next_attack           = @combat_state.next_attack true
    target                = @combat_state.target
    current_distance      = mob.position.get_distance target.position
    attack_collision_type = next_attack.range > 1 ? EntityType::PROJECTILE : EntityType::NPC

    in_range = current_distance <= next_attack.range# && !$world.intersects(mob.position, target.position, attack_collision_type)

    unless in_range
      mob.follow @combat_state.target, next_attack.range

      return false
    end

    mob.attack_timer >= next_attack.speed
  end

  def try_attack
    next_attack = @combat_state.next_attack false

    if mob.is_a? Player
      next_attack.requirements.each { |requirement| requirement.validate mob }
      next_attack.requirements.each { |requirement| requirement.apply! mob }
    end

    next_attack.do(mob, @combat_state.target)
  rescue AttackRequirementException => e
    mob.send_message e.message
  ensure
    mob.attack_timer = 0
  end

  def execute
    if @combat_state.target.nil? || !@combat_state.target.is_active || @combat_state.next_attack(true).nil?
      stop
      return
    end

    return unless can_attack?

    try_attack
    stop if @once
  end

  def stop
    super

    mob.attacking = false
    mob.following = -1
    @combat_state.reset
  end
end
