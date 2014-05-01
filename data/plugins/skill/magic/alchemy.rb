require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'

ALCHEMY_SPELLS = {}

LOW_ALCH_ANIM = Animation.new(712)
LOW_ALCH_GRAPHIC = Graphic.new(112, 0, 100)
LOW_ALCH_MULTIPLIER = 0.4

HIGH_ALCH_ANIM = Animation.new(713)
HIGH_ALCH_GRAPHIC = Graphic.new(113, 0, 100)
HIGH_ALCH_MULTIPLIER = 0.6

ILLEGAL_ALCH_ITEMS = [ 995, 6529, 6306, 6307, 6308, 6309, 6310 ]

class AlchemySpell < Spell
  attr_reader :high, :animation, :graphic, :multiplier, :experience, :delay
  
  def initialize(level, elements, high, animation, graphic, multiplier, experience, delay)
    super(level, elements, experience)    
    @high = high
    @animation = animation
    @graphic = graphic
    @multiplier = multiplier
    @delay = delay
  end

end

class AlchemyAction < ItemSpellAction
  
  def initialize(player, alchemy, slot, item)
    super(player, alchemy, slot, item)
  end
  
  def illegal_item?
    return ILLEGAL_ALCH_ITEMS.include?(@item.id)
  end
  
  def execute_action
    if @pulses == 0
      mob.play_animation(@spell.animation)
      mob.play_graphic(@spell.graphic)
      mob.send(DISPLAY_SPELLBOOK)
      
      inventory = mob.inventory
      gold = (item.definition.value * @spell.multiplier) + 1
      
      inventory.remove(inventory.get(@slot).id, 1)
      inventory.add(995, gold)
      
      mob.skill_set.add_experience(MAGIC_SKILL_ID, @spell.experience)      
      set_delay(@spell.delay)
    elsif @pulses == 1
      mob.stop_animation
      mob.stop_graphic
      stop
    end
  end

end

def append_alchemy(button, level, elements, high, animation, graphic, multiplier, experience, delay)
  ALCHEMY_SPELLS[button] = AlchemySpell.new(level, elements, high, animation, graphic, multiplier, experience, delay)
end

append_alchemy(1162, 21, { FIRE => 3, NATURE => 1 }, false, LOW_ALCH_ANIM,  LOW_ALCH_GRAPHIC,  0.48, 31, 1) # Low level alchemy
append_alchemy(1178, 55, { FIRE => 5, NATURE => 1 },  true, HIGH_ALCH_ANIM, HIGH_ALCH_GRAPHIC, 0.72, 65, 4) # High level alchemy