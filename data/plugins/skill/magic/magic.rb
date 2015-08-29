require 'java'

java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.message.impl.DisplayTabInterfaceMessage'
java_import 'org.apollo.game.model.entity.EquipmentConstants'
java_import 'org.apollo.game.model.entity.Skill'

# A `Message` to display the magic spellbook.
DISPLAY_SPELLBOOK = DisplayTabInterfaceMessage.new(6)

# A spell that can be cast.
class Spell
  attr_reader :level, :elements, :experience

  def initialize(level, elements, experience)
    @level = level
    @elements = elements
    @experience = experience
  end

end

# An `Action` for casting a `Spell`.
class SpellAction < Action
  attr_reader :spell, :pulses

  def initialize(mob, spell)
    super(0, true, mob)
    @spell = spell
    @pulses = 0
  end

  def execute
    if @pulses == 0
      unless check_skill && process_elements
        stop
        return
      end
    end

    execute_action
    @pulses += 1
  end

  def execute_action
    stop
  end

  def check_skill
    required = @spell.level
    if required > mob.skill_set.skill(Skill::MAGIC).maximum_level
      mob.send_message("You need a Magic level of at least #{required} to cast this spell.")
      return false
    end

    true
  end

  def process_elements
    elements = @spell.elements

    elements.each do |element, amount|
      unless element.check_remove(mob, amount, false)
        mob.send_message("You do not have enough #{element.name}s to cast this spell.")
        return false
      end
    end

    elements.each { |element, amount| element.check_remove(mob, amount, true) }
    true
  end

  def equals(other)
    get_class == other.get_class && @spell == other.spell
  end

end

# A `SpellAction` that verifies an input `Item` is legal.
class ItemSpellAction < SpellAction
  attr_reader :slot, :item

  def initialize(mob, spell, slot, item)
    super(mob, spell)
    @slot = slot
    @item = item
  end

  # We override SpellAction#execute to implement an illegal item check (e.g. coins for alchemy)
  def execute
    if @pulses == 0
      if illegal_item?
        mob.send_message('You cannot use that spell on this item!')
        stop
        next
      end

      id = @item.id

      # TODO: There has to be a better way to do this.
      @spell.elements.each do |element, amount|
        element.runes.each do |rune|
          if id == rune && !element.check_remove(mob, amount + 1, false)
            mob.send_message("You do not have enough #{element.name}s to cast this spell.")
            stop
            return false
          end
        end
      end

    end

    super
  end

  def illegal_item?
    # Override this method if necessary
    false
  end

end

# Intercepts the magic on item message.
on :message, :magic_on_item do |player, message|
  spell = message.spell_id

  alch = ALCHEMY_SPELLS[spell]
  unless alch.nil?
    slot = message.slot
    item = player.inventory.get(slot)
    player.start_action(AlchemyAction.new(player, alch, slot, item))
    message.terminate
    return
  end

  ench = ENCHANT_SPELLS[message.id]
  if !ench.nil? && ench.button == spell
    slot = message.slot
    item = player.inventory.get(slot)
    player.start_action(EnchantAction.new(player, ench, slot, item, ENCHANT_ITEMS[item.id]))
    message.terminate
  end
end

# Intercepts the button message
on :message, :button do |player, message|
  button = message.widget_id

  tele = TELEPORT_SPELLS[button]
  unless tele.nil?
    player.start_action(TeleportingAction.new(player, tele))
    message.terminate
    return
  end

  conv = CONVERT_SPELLS[button]
  unless conv.nil?
    slots = bone_slots(player)

    if slots.length == 0
      player.send_message("You can't convert these bones!")
    else
      player.start_action(ConvertingAction.new(player, conv, slots))
    end

    message.terminate
  end

end
