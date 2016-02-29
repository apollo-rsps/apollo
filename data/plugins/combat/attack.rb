java_import 'org.apollo.cache.def.ItemDefinition'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'

class BaseAttack
  attr_reader :requirements, :range

  def initialize(animation, graphic = nil, range = 1, requirements = [])
    @animation    = animation
    @graphic      = graphic
    @range        = range
    @requirements = requirements
  end

  def do(source, target)
    source.play_animation(Animation.new(@animation))

    unless @graphic.nil?
      if @graphic.is_a?(Hash)
        source.play_graphic(Graphic.new(@graphic[:id], @graphic[:delay] || 0, @graphic[:height] | 0))
      else
        source.play_graphic(Graphic.new(@graphic))
      end
    end

    apply(source, target)
  end

  def apply(source, target)
    raise "BaseAttack#apply unimplemented"
  end

  def damage!(source, target, amount, delay = 0)
    schedule delay do |task|
      task.stop && return if source.dead or target.dead

      target_combat_state = get_combat_state target
      target_hitpoints    = target.skill_set.get_skill(Skill::HITPOINTS).get_current_level
      amount              = target_hitpoints if target_hitpoints < amount

      type = amount > 0 ? 1 : 0
      target.play_animation(Animation.new(424)) unless target_combat_state.state == :attacking
      target.damage(amount, type, false)

      task.stop
    end
  end
end

class ProcAttack < BaseAttack
  def initialize(block, animation:, graphic:, range: 1, requirements: [])
    super(animation, graphic, range, requirements)

    @block = block
  end

  def apply(source, target)
    self.instance_exec(source, target, &@block)
  end
end

class RangedAttack < BaseAttack
  def initialize(animation:, graphic:, requirements: [])

  end

  def calculate_delay(source, target)

  end

  def apply(source, target)

  end

  def projectile!(source, target, projectile_id)

  end
end

class Attack < BaseAttack
  def initialize(animation:, graphic: nil, range: 1, requirements: [])
    super(animation, graphic, range, requirements)
  end

  def apply(source, target)
    damage! source, target, CombatUtil::calculate_hit(source, target)
  end
end
