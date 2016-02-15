require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.model.Item'
java_import 'org.apollo.game.model.entity.Skill'

CONVERT_SPELLS = {}
BONES_ID = 526

CONVERT_ANIM = Animation.new(722)
CONVERT_GRAPHIC = Graphic.new(141, 0, 100)

# A `Spell` for converting items.
class ConvertSpell < Spell
  attr_reader :reward

  def initialize(level, elements, experience, reward)
    super(level, elements, experience)
    @reward = Item.new(reward)
  end

end

# A `SpellAction` for a `ConvertSpell`.
class ConvertingAction < SpellAction
  attr_reader :slots

  def initialize(player, spell, slots)
    super(player, spell)
    @slots = slots
  end

  def execute_action
    if @pulses == 0
      mob.play_animation(CONVERT_ANIM)
      mob.play_graphic(CONVERT_GRAPHIC)

      inventory = mob.inventory
      firing = (@slots.length * 2) < inventory.capacity

      inventory.stop_firing_events unless firing # In case of many changes, wait with firing events

      reward = @spell.reward
      @slots.each { |slot| inventory.set(slot, reward) }

      unless firing
        inventory.start_firing_events
        inventory.force_refresh
      end

      mob.skill_set.add_experience(Skill::MAGIC, @spell.experience)
      set_delay(2)
    elsif @pulses == 1
      mob.stop_animation
      mob.stop_graphic
      stop
    end
  end

end

def bone_slots(player)
  inventory = player.inventory
  items = inventory.items
  size = inventory.size

  counter = 0
  slots = []

  (0...inventory.capacity).each do |slot|
    break unless counter <= size

    item = items[slot]
    slots << slot if !item.nil? && item.id == BONES_ID

    counter += 1
  end

  slots
end

def convert(_name, button, level, elements, experience, reward)
  CONVERT_SPELLS[button] = ConvertSpell.new(level, elements, experience, reward)
end

convert :bones_to_bananas, 1159, 15, { NATURE => 1, WATER => 2, EARTH => 2 }, 25, 1963
convert :bones_to_peaches, 15877, 60, { NATURE => 2, WATER => 4, EARTH => 4 }, 35.5, 6883

