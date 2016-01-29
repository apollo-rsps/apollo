class CombatSpell
  attr_reader :button

  attr_reader :spellbook

  attr_reader :requirements

  attr_reader :damage

  attr_reader :animation

  attr_reader :graphic

  attr_reader :hit_graphic

  attr_reader :projectile

  attr_reader :projectile_type

  def initialize(button, spellbook, requirements, damage, animation, graphic, hit_graphic, projectile, projectile_type)
    @spellbook = spellbook
    @button = button
    @requirements = requirements
    @damage = damage
    @animation = animation
    @graphic = graphic
    @hit_graphic = hit_graphic
    @projectile = projectile
    @projectile_type = projectile_type
  end
end

class CombatSpellDSL
  def initialize(&block)
    instance_eval(&block)
  end

  def spellbook(spellbook, button:)
    @spellbook = spellbook
    @button = button
  end

  def effects(animation: nil, graphic: nil, hit_graphic: nil)
    @animation = animation
    @graphic = graphic
    @hit_graphic = hit_graphic
  end

  def projectile(id:, type:)
    @projectile = id
    @projectile_type = type
  end

  def requirements(&block)
    fail 'Block not given' unless block_given?

    @requirements = AttackRequirementDSL.new(&block).requirements
  end

  def level_requirement(level)
    @level = level
  end

  def max_damage(damage)
    @damage = damage
  end

  def runes(runes = {})
    @runes = runes
  end

  def to_combat_spell
    CombatSpell.new(@button, @spellbook, @requirements, @damage, @animation, @graphic, @hit_graphic, @projectile, @projectile_type)
  end
end

COMBAT_SPELLS = {}

def create_combat_spell(name, &block)
  fail 'Block not given' unless block_given?

  combat_spell_dsl = CombatSpellDSL.new(&block)

  COMBAT_SPELLS[name] = combat_spell_dsl.to_combat_spell
end

def spell_for(spellbook, button)
  COMBAT_SPELLS.each do |_name, spell|
    return spell if spell.spellbook == spellbook && spell.button == button
  end

  fail "Unable to find a spell in spellbook '#{spellbook}' with button id '#{button}'"
end
