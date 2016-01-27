class CombatSpell

  attr_reader :button

  attr_reader :spellbook

  attr_reader :level

  attr_reader :damage

  attr_reader :runes

  attr_reader :animation

  attr_reader :graphic

  attr_reader :hit_graphic

  attr_reader :projectile

  attr_reader :projectile_type

  def initialize(button, spellbook, level, damage, runes, animation, graphic, hit_graphic, projectile, projectile_type)
    @spellbook = spellbook
    @button = button
    @level = level
    @damage = damage
    @runes = runes
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

  def interface(spellbook:, button:)
    @spellbook = spellbook
    @button = button
  end

  def effects(animation: nil, graphic: nil, hit_graphic: nil)
    @animation = animation
    @graphic = graphic
    @hit_graphic = hit_graphic
  end

  def projectile(projectile:, projectile_type:)
    @projectile = projectile
    @projectile_type = projectile_type
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
    return CombatSpell.new(@button, @spellbook, @level, @damage, @runes, @animation, @graphic, @hit_graphic, @projectile, @projectile_type)
  end

end

COMBAT_SPELLS = {}

def create_combat_spell(name, &block)
  fail 'Block not given' unless block_given?

  combat_spell_dsl = CombatSpellDSL.new(&block)

  COMBAT_SPELLS[name] = combat_spell_dsl.to_combat_spell
end

def spell_for(spellbook, button)

  COMBAT_SPELLS.each do |name, spell|
    return spell if spell.spellbook == spellbook && spell.button == button
  end

  fail "Unable to find a spell in spellbook '#{spellbook}' with button id '#{button}'"
end