require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'

ALCHEMY_SPELLS = {}

LOW_ALC_ANIM = Animation.new(712)
LOW_ALC_GFX = Graphic.new(112, 0, 100)
LOW_ALC_MULTIPLIER = 0.4

HIGH_ALC_ANIM = Animation.new(713)
HIGH_ALC_GFX = Graphic.new(113, 0, 100)
HIGH_ALC_MULTIPLIER = 0.6

ILLEGAL_ALC_ITEMS = [ 995, 6529, 6306, 6307, 6308, 6309, 6310 ]

class AlchemySpell < Spell
  attr_reader :high, :animation, :graphic, :multiplier, :experience, :delay
  
  def initialize(level, elements, high, animation, graphic, multiplier, experience, delay)
    super level, elements, experience
    
    @high = high
    @animation = animation
    @graphic = graphic
    @multiplier = multiplier
    @delay = delay
  end
end

class AlchemyAction < ItemSpellAction
  
  def initialize(player, alchemy, slot, item)
    super player, alchemy, slot, item
  end
  
  def illegal_item?
    return ILLEGAL_ALC_ITEMS.include? @item.id
  end
  
  def execute_action
    player = character
    if @pulses == 0
      player.play_animation(@spell.animation)
      player.play_graphic(@spell.graphic)
      player.send(DISPLAY_SPELLBOOK)
      
      inventory = player.inventory
      gold = (item.definition.value * @spell.multiplier)
      
      inventory.remove(inventory.get(@slot).id, 1)
      inventory.add(995, gold)
      
      player.skill_set.add_experience(MAGIC_ID, @spell.experience)
      
      set_delay(@spell.delay)
    elsif @pulses == 1
      player.stop_animation
      player.stop_graphic
      stop
    end
  end
end

def append_alchemy(button, level, elements, high, animation, graphic, multiplier, experience, delay)
  ALCHEMY_SPELLS[button] = AlchemySpell.new(level, elements, high, animation, graphic, multiplier, experience, delay)
end

append_alchemy(1162, 21, { FIRE => 3, NATURE => 1 }, false, LOW_ALC_ANIM, LOW_ALC_GFX, 0.48, 31, 1) # Low level alchemy
append_alchemy(1178, 55, { FIRE => 5, NATURE => 1 }, true, HIGH_ALC_ANIM, HIGH_ALC_GFX, 0.72, 65,  4) # High level alchemy