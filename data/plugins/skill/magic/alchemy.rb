require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.model.entity.Skill'

ALCHEMY_SPELLS = {}

ILLEGAL_ALCH_ITEMS = [995, 6529, 6306, 6307, 6308, 6309, 6310]

# A spell that alchemises an item.
class AlchemySpell < Spell
  attr_reader :animation, :graphic, :multiplier, :experience

  def initialize(level, elements, experience, animation, graphic, multiplier)
    super(level, elements, experience)
    @animation = animation
    @graphic = graphic
    @multiplier = multiplier
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

  def executeAction
    if @pulses == 0
      mob.play_animation(@spell.animation)
      mob.play_graphic(@spell.graphic)
      mob.send(DISPLAY_SPELLBOOK)

      inventory = mob.inventory
      gold = (item.definition.value * @spell.multiplier) + 1

      inventory.remove(inventory.get(@slot).id, 1)
      inventory.add(995, gold)

      mob.skill_set.add_experience(Skill::MAGIC, @spell.experience)
      set_delay(ALCHEMY_DELAY)
    elsif @pulses == 1
      mob.stop_animation
      mob.stop_graphic
      stop
    end
  end

end

private

# The delay of an alchemy spell.
ALCHEMY_DELAY = 4

# The height of the graphic.
GRAPHIC_HEIGHT = 100

# Inserts an `AlchemySpell` into the hash of available alchemy spells.
def alchemy(_name, hash)
  unless hash.has_keys?(:button, :level, :runes, :animation, :graphic, :multiplier, :experience)
    fail 'Hash must have button, level, runes, animation, graphic, multiplier, experience keys.'
  end

  id, multiplier = hash[:button], hash[:multiplier]
  level, runes, experience = hash[:level], hash[:runes], hash[:experience]

  animation = Animation.new(hash[:animation])
  graphic = Graphic.new(hash[:graphic], 0, GRAPHIC_HEIGHT)

  ALCHEMY_SPELLS[id] = AlchemySpell.new(level, runes, experience, animation, graphic, multiplier)
end

alchemy :low_level, button: 1_162, level: 21, runes: { FIRE => 3, NATURE => 1 }, animation: 712,
                    graphic: 112, multiplier: 0.48, experience: 31

alchemy :high_level, button: 1_178, level: 55, runes: { FIRE => 5, NATURE => 1 }, animation: 713,
                     graphic: 113, multiplier: 0.72, experience: 65
