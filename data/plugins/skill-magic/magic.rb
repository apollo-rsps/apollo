require 'java'
java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.event.impl.DisplayTabInterfaceEvent'
java_import 'org.apollo.game.model.EquipmentConstants'
java_import 'org.apollo.game.model.Skill'

MAGIC_ID = Skill::MAGIC
DISPLAY_SPELLBOOK = DisplayTabInterfaceEvent.new 6

class Spell
  attr_reader :level, :elements, :experience
  
  def initialize(level, elements, experience)
    @level = level
    @elements = elements
    @experience = experience
  end
end

class SpellAction < Action
  attr_reader :spell, :pulses
  
  def initialize(character, spell)
    super(0, true, character)
    
    @spell = spell
    @pulses = 0
  end
  
  def execute
    if @pulses == 0
      unless (check_skill and process_elements)
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
    if required > character.skill_set.skill(MAGIC_ID).maximum_level
      character.send_message "You need a Magic level of at least #{required} to cast this spell."
      return false
    end
    
    return true
  end
  
  def process_elements
    elements = @spell.elements
    
    elements.each do |element, amount|
      unless element.check_remove(character, amount, false)
        character.send_message "You do not have enough #{element.name}s to cast this spell."
        return false
      end
    end
    
    elements.each do |element, amount|
      element.check_remove(character, amount, true)
    end
    
    return true
  end
  
  def equals(other)
    return (get_class == other.get_class and @spell == other.spell)
  end
end

class ItemSpellAction < SpellAction
  attr_reader :slot, :item

  def initialize(character, spell, slot, item)
    super(character, spell)
    
    @slot = slot
    @item = item
  end
  
  # We override SpellAction#execute to implement an illegal item check (e.g. coins for alchemy)
  def execute
    if @pulses == 0
      if illegal_item?
        character.send_message "You cannot use that spell on this item!"
        stop
        return
      end
      
      id = @item.id
      
      # TODO: There has to be a better way to do this.
      @spell.elements.each do |element, amount|
        element.runes.each do |rune|
          if id == rune && !element.check_remove(character, amount + 1, false)
            character.send_message("You do not have enough " + element.name + "s to cast this spell.")
            stop
            return
          end
        end
      end

    end
    
    super
  end

  def illegal_item?
    # Override this method if necessary
    return false
  end
end

# MagicOnItemEvent handling
on :event, :magic_on_item do |ctx, player, event|
  spell = event.spell_id
  
  alch = ALCHEMY_SPELLS[spell]
  if alch != nil
    slot = event.slot
    item = player.inventory.get slot
    player.start_action AlchemyAction.new(player, alch, slot, item)
    ctx.break_handler_chain
    return
  end
  
  ench = ENCHANT_SPELLS[event.id]
  if ench != nil and ench.button == spell
    slot = event.slot
    item = player.inventory.get slot
    player.start_action EnchantAction.new(player, ench, slot, item, ENCHANT_ITEMS[item.id])
    ctx.break_handler_chain
  end
end

# ButtonEvent handling
on :event, :button do |ctx, player, event|
  button = event.widget_id

  tele = TELEPORT_SPELLS[button]
  if tele != nil
    player.start_action TeleportingAction.new(player, tele)
    ctx.break_handler_chain
    return
  end
  
  conv = CONVERT_SPELLS[button]
  if conv != nil
    slots = bone_slots player
    if slots.length == 0 then player.send_message("You cant convert these bones!") else player.start_action(ConvertingAction.new(player, conv, slots)) end
    ctx.break_handler_chain
  end
end