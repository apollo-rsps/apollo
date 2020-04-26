require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.model.entity.Skill'

ALCHEMY_SPELLS = {}

ILLEGAL_ALCH_ITEMS = [995]

# A spell that alchemises an item.
class AlchemySpell < Spell
  attr_reader :animation, :graphic, :multiplier, :experience, :delay

  def initialize(level, elements, experience, animation, graphic, multiplier, delay)
    super(level, elements, experience)
    @animation = animation
    @graphic = graphic
    @multiplier = multiplier
    @delay = delay
  end

end

# An Action that performs an AlchemySpell.
class AlchemyAction < ItemSpellAction

  def initialize(player, alchemy, slot, item)
    super(player, alchemy, slot, item)
  end

  def illegal_item?
    ILLEGAL_ALCH_ITEMS.include?(@item.id)
  end

  def execute_action
    if @pulses == 0
      mob.play_animation(@spell.animation)
      mob.play_graphic(@spell.graphic)

      inventory = mob.inventory
      gold = (item.definition.value * @spell.multiplier)

      inventory.remove(inventory.get(@slot).id, 1)
      inventory.add(995, gold)

      mob.skill_set.add_experience(Skill::MAGIC, @spell.experience)
      set_delay(@spell.delay)
    elsif @pulses == 1
      mob.stop_animation
      mob.stop_graphic
      stop
    end
  end

end

private

# The height of the graphic.
GRAPHIC_HEIGHT = 100

# Inserts an `AlchemySpell` into the hash of available alchemy spells.
def alchemy(_name, hash)
  unless hash.has_keys?(:button, :level, :fires, :animation, :graphic, :multiplier, :experience, :delay)
    fail 'Hash must have button, level, fires, animation, graphic, multiplier, experience, delay keys.'
  end

  id, multiplier = hash[:button], hash[:multiplier]
  level, experience = hash[:level], hash[:experience]

  runes = { NATURE => 1, FIRE => hash[:fires] }
  animation = Animation.new(hash[:animation])
  graphic = Graphic.new(hash[:graphic], 0, GRAPHIC_HEIGHT)
  delay = hash[:delay]

  ALCHEMY_SPELLS[id] = AlchemySpell.new(level, runes, experience, animation, graphic, multiplier, delay)
end

alchemy :low_level, button: 1_162, level: 21, fires: 3, animation: 712, graphic: 112, multiplier: 0.4, experience: 31, delay: 2
alchemy :high_level, button: 1_178, level: 55, fires: 5, animation: 713, graphic: 113, multiplier: 0.6, experience: 65, delay: 3
