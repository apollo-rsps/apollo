java_import 'org.apollo.cache.def.ItemDefinition'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'

##
# The {@code BaseAttack} which all other {@code Attack}s derive from.
#
# Supports most general attacks by playing an {@code Animation} and an optional {@code Graphic}.
# Default options are:
#   * a range of 1
#   * no requirements
#   * no graphic
class BaseAttack
  ##
  # The {@code AttackRequirement}s a {@code Player} must meet to use this attack.

  attr_reader :requirements

  ##
  # The maximum range this {@code Attack} can be executed from.

  attr_reader :range

  ##
  # How often this {@code Attack} can be executed in ticks.

  attr_reader :speed

  ##
  # Create a new {@code Attack} with the given properties.
  #
  # @param [Number] speed        The minimum number of ticks to wait before this attack can be executed after
  #                              a previous {@code Attack}.
  # @param [Number] animation    The {@code Animation} to play on the player when executing this {@code Attack}.
  # @param [Hash]   graphic      The {@code Graphic} to play on the player when executing this {@code Attack}.
  # @param [Number] range        The maximum distance this {@code Attack} can be executed from.
  # @param [Array]  requirements The requirements that must be met to execute this {@code Attack}.

  def initialize(speed:, animation:, graphic: nil, range: 1, requirements: [])
    fail 'Attack speed must be a non-negative number' if speed < 0
    fail 'Attack range must be a non-negative number' if range < 0

    @speed = speed
    @animation = animation
    @graphic = graphic
    @range = range
    @requirements = requirements
  end

  ##
  # Execute this {@code Attack} and apply its effect.
  #
  # @param [Mob] source The attacker.
  # @param [Mob] target The target.

  def do(source, target)
    source.play_animation(Animation.new(@animation))

    unless @graphic.nil?
      if @graphic.is_a?(Hash)
        source.play_graphic(Graphic.new(@graphic[:id], @graphic[:delay] || 0, @graphic[:height] || 0))
      else
        source.play_graphic(Graphic.new(@graphic))
      end
    end

    apply(source, target)
  end

  def apply(_source, _target)
    fail 'BaseAttack#apply unimplemented'
  end
end

class ProcAttack < BaseAttack
  def initialize(block, hash)
    super(hash)

    @block = block
  end

  def apply(source, target)
    instance_exec(source, target, &@block)
  end
end

##
# A simple ranged attack, which sends a projectile based on the currently equipped ammo and weapon
# and deals damage delayed by the speed and travel distance of the projectile.

class RangedAttack < BaseAttack
  def apply(source, target)
    do_ranged_damage! source, target
  end
end

##
# A simple magic attack, which sends a projectile based on the current spell that the player is casting and deals
# damage delayed by the speed and travel distance of the projectile.

class MagicAttack < BaseAttack
  ##
  # The maximum distance that a magic attack can be performed from.
  MAX_DISTANCE = 8

  ##
  # The speed that magic attacks can be casted at.
  SPEED = 5

  def initialize(spell)
    super(animation: spell.animation, graphic: spell.graphic, requirements: spell.requirements, speed: SPEED, range: MAX_DISTANCE)

    @damage = spell.damage
    @hit_graphic = spell.hit_graphic
    @projectile = spell.projectile
    @projectile_type = PROJECTILE_TYPES[spell.projectile_type]
  end

  def apply(source, target)
    projectile!(source, target, @projectile, @projectile_type)

    distance = source.position.get_distance(target.position)
    damage_delay = (@projectile_type.delay + @projectile_type.speed + distance * 5) * 0.02857

    schedule_damage!(source, target, rand(@damage), damage_delay) do

      unless @hit_graphic.nil?
        if @hit_graphic.is_a?(Hash)
          target.play_graphic(Graphic.new(@hit_graphic[:id], @hit_graphic[:delay] || 0, @hit_graphic[:height] || 0))
        else
          target.play_graphic(Graphic.new(@hit_graphic))
        end
      end

    end
  end
end

##
# A basic melee attack, which deals damage a tick after the attack was made.

class Attack < BaseAttack
  def apply(source, target)
    do_damage! source, target, CombatUtil.calculate_hit(source, target)
  end
end

##
# A DSL-like builder object for creating a new Attack, used mainly (exclusively?) for special attacks.

class AttackDSL
  attr_accessor :animation, :speed, :range, :graphic

  def initialize
    @requirements = []
    @subattacks = []
  end

  def add_requirement(requirement)
    @requirements.push requirement
  end

  ##
  # Deal ranged damage.

  def range_damage!(hash)
    @subattacks.push lambda { |source, target|
      do_ranged_damage! source, target, hash
    }
  end

  ##
  # Deal melee damage.

  def damage!(damage_modifier: 1, delay: 0, secondary: false)
    @subattacks.push lambda { |source, target|
      schedule_damage! source, target, 1, delay, secondary
    }
  end

  ##
  # Process the attack instructions in this DSL then build and return an {@link Attack} object.

  def to_attack
    subattacks = @subattacks

    attack = lambda do |source, target|
      subattacks.each do |subattack|
        subattack[source, target]
      end
    end

    # TODO: clean up? Decide where keyword arguments are appropriate and where not
    ProcAttack.new(attack, speed: speed, animation: animation, graphic: graphic, range: range, requirements: @requirements)
  end
end

private

def schedule_damage!(source, target, amount, delay, secondary = false, &_callback)
  schedule delay do |task|

    do_damage!(source, target, amount, secondary)
    yield if block_given?

    task.stop
  end
end

def do_damage!(source, target, amount, secondary = false)
  return if source.dead || target.dead

  target_hitpoints = target.skill_set.get_skill(Skill::HITPOINTS).get_current_level
  amount = target_hitpoints if target_hitpoints < amount

  type = (amount > 0) ? 1 : 0
  target.damage(amount, type, secondary)

  auto_retaliate!(source, target)
end

def auto_retaliate!(source, target)
  target_combat_state = target.get_combat_state

  if target.auto_retaliate && !target_combat_state.attacking?
    target_combat_state.target = source
    target.start_action(CombatAction.new(target))
  end
end

##
# TODO: refactor

def do_ranged_damage!(source, target, _damage_modifier = 1, speed_modifier = 1, projectile = nil, projectile_type = nil, projectile_graphic = nil,
                      delay = 0, secondary = false)
  apply = lambda do
    distance = source.position.get_distance(target.position)

    ammo = EquipmentUtil.equipped_ammo source

    if projectile.nil? && projectile_type.nil?
      projectile = ammo.projectile
      projectile_type = ammo.projectile_type
      projectile_graphic = ammo.graphic
    end

    projectile! source, target, projectile, projectile_type, speed_modifier
    schedule_damage! source, target, rand(1...50), (projectile_type.delay + projectile_type.speed + distance * 5) * 0.02857, secondary

    unless projectile_graphic.nil?
      source.play_graphic Graphic.new(projectile_graphic, 0, 100)
    end
  end

  if delay == 0
    apply.call
  else
    # account for the one tick delay in the scheduler
    schedule(delay - 1) do |task|
      apply.call
      task.stop
    end
  end
end

##
# TODO: refactor

def projectile!(source, target, projectile_id, projectile_type, speed_modifier = 1)
  distance = source.position.get_distance(target.position)

  projectile_parameters = {
    angle: -1,
    start_height: projectile_type.start_height,
    end_height: projectile_type.end_height,
    delay: projectile_type.delay * (2 - speed_modifier),
    speed: (projectile_type.delay + projectile_type.speed + distance * 5) * speed_modifier,
    slope: projectile_type.slope,
    radius: projectile_type.radius
  }

  ProjectileModule.fire_projectile(source.position, target.position, projectile_id, projectile_parameters, target)
end
