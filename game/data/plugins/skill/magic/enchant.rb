require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.model.Item'

ENCHANT_SPELLS = {}
ENCHANT_ITEMS = {}

RING_GFX = Graphic.new(238, 0, 100)
RING_ANIM = Animation.new(931)

LOW_NECK_GFX = Graphic.new(114, 0, 100)
LOW_NECK_ANIM = Animation.new(719)

MED_NECK_GFX = Graphic.new(115, 0, 100)
MED_NECK_ANIM = Animation.new(720)

HIGH_NECK_GFX = Graphic.new(116, 0, 100)
HIGH_NECK_ANIM = Animation.new(721)

ONYX_NECK_GFX = Graphic.new(452, 0, 100)

# A `Spell` for enchanting an item.
class EnchantSpell < Spell
  attr_reader :button, :animation, :graphic, :delay

  def initialize(button, level, elements, animation, graphic, delay, experience)
    super(level, elements, experience)
    @button = button
    @animation = animation
    @graphic = graphic
    @delay = delay
  end

end

# A `SpellAction` for an `EnchantSpell`.
class EnchantAction < ItemSpellAction
  attr_reader :reward

  def initialize(player, enchant, slot, item, reward)
    super(player, enchant, slot, item)
    @reward = Item.new(reward)
  end

  def illegal_item?
    ENCHANT_ITEMS[@item.id].nil?
  end

  def execute_action
    if @pulses == 0
      mob.play_animation(@spell.animation)
      mob.play_graphic(@spell.graphic)

      mob.inventory.set(@slot, @reward)
      mob.skill_set.add_experience(Skill::MAGIC, @spell.experience)

      set_delay(@spell.delay)
    elsif @pulses == 1
      mob.stop_animation
      mob.stop_graphic
      stop
    end
  end

end

def enchant(button, level, elements, item, animation, graphic, delay, experience, reward)
  enchant = EnchantSpell.new(button, level, elements, animation, graphic, delay, experience)
  ENCHANT_SPELLS[item] = enchant
  ENCHANT_ITEMS[item] = reward
end

SAPPHIRE_ELEMENTS = { COSMIC => 1, WATER => 1 }
EMERALD_ELEMENTS  = { COSMIC => 1, AIR => 1 }
RUBY_ELEMENTS     = { COSMIC => 1, FIRE => 5 }
DIAMOND_ELEMENTS  = { COSMIC => 1, EARTH => 10 }
DSTONE_ELEMENTS   = { COSMIC => 1, EARTH => 15, WATER => 15 }
ONYX_ELEMENTS     = { COSMIC => 1, FIRE => 20, EARTH => 20 }

# Sapphire
enchant 1155, 7, SAPPHIRE_ELEMENTS, 1637, RING_ANIM, RING_GFX, 2, 17.5, 2550 # Ring
enchant 1155, 7, SAPPHIRE_ELEMENTS, 1656, LOW_NECK_ANIM, LOW_NECK_GFX, 1, 17.5, 3853 # Necklace
enchant 1155, 7, SAPPHIRE_ELEMENTS, 1692, LOW_NECK_ANIM, LOW_NECK_GFX, 1, 17.5, 1727 # Amulet

# Emerald
enchant 1165, 27, EMERALD_ELEMENTS, 1639, RING_ANIM, RING_GFX, 2, 37, 2552 # Ring
enchant 1165, 27, EMERALD_ELEMENTS, 1658, LOW_NECK_ANIM, LOW_NECK_GFX, 1, 37, 5521 # Necklace
enchant 1165, 27, EMERALD_ELEMENTS, 1696, LOW_NECK_ANIM, LOW_NECK_GFX, 1, 37, 1729 # Amulet

# Ruby
enchant 1176, 49, RUBY_ELEMENTS, 1641, RING_ANIM, RING_GFX, 2, 59, 2568 # Ring
enchant 1176, 49, RUBY_ELEMENTS, 1698, MED_NECK_ANIM, MED_NECK_GFX, 2, 59, 1725 # Amulet

# Diamond
enchant 1180, 57, DIAMOND_ELEMENTS, 1643, RING_ANIM, RING_GFX, 2, 67, 2570 # Ring
enchant 1180, 57, DIAMOND_ELEMENTS, 1700, MED_NECK_ANIM, MED_NECK_GFX, 2, 67, 1731 # Amulet

# Dragonstone
enchant 1187, 68, DSTONE_ELEMENTS, 1645, RING_ANIM, RING_GFX, 2, 78, 2572 # Ring
enchant 1187, 68, DSTONE_ELEMENTS, 1702, HIGH_NECK_ANIM, HIGH_NECK_GFX, 3, 78, 1712 # Amulet

# Onyx
enchant 6003, 87, ONYX_ELEMENTS, 6575, RING_ANIM, RING_GFX, 2, 97, 6583 # Ring
enchant 6003, 87, ONYX_ELEMENTS, 6581, HIGH_NECK_ANIM, ONYX_NECK_GFX, 2, 97, 6585 # Amulet
