require 'java'

java_import 'org.apollo.game.action.Action'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Item'
java_import 'org.apollo.game.model.def.ItemDefinition'
java_import 'org.apollo.game.model.inter.EnterAmountListener'
java_import 'org.apollo.game.model.inter.dialogue.DialogueAdapter'

WATER_VIAL_ID = 227
EMPTY_VIAL_ID = 229

MIXING_ANIM = Animation.new(363)

# Represents an unfinished potion which can be invoked as a HerbloreMethod and used as an ingredient.
class UnfinishedPotion < Ingredient
  include HerbloreMethod

  attr_reader :herb, :level

  def initialize(item_id, herb, level)
    super item_id

    @herb = herb
    @level = level
  end

  def invoke(player, primary, secondary)
    action = UnfinishedMixingAction.new(player, self)
    listener = UnfinishedMixingDialogueListener.new(player, action)

    open_dialogue(player, @item_id, listener)
  end
end

# Represents a finished potion which can be invoked as a HerbloreMethod.
class FinishedPotion
  include HerbloreMethod

  attr_reader :item, :ingredients, :level, :experience

  def initialize(item, ingredients, level, experience)
    @item = Item.new(item)
    @ingredients = ingredients
    @level = level
    @experience = experience
  end

  def invoke(player, primary, secondary)
    action = FinishedMixingAction.new(player, primary, secondary, self)
    listener = FinishedMixingDialogueListener.new(player, action)

    open_dialogue(player, @item.id, listener) 
  end
end

# A DialogueAdapter used for mixing potions. It is also used as an EnterAmountListener for the amount of mixing actions.
class MixingDialogueListener < DialogueAdapter
  include EnterAmountListener

  attr_reader :player, :action

  def initialize(player, action)
    super()
    
    @player = player
    @action = action
  end

  # Called when a button has been clicked whilst the dialogue was opened.
  def buttonClicked(button)
    amount = get_amount(button)
    
    return false if amount == 0

    interfaces = @player.interface_set
    interfaces.close

    if amount == -1
      interfaces.open_enter_amount_dialogue(self)
      return true
    end

    amount = calculate_maximum if amount == -2

    execute(amount)
    return true
  end

  # Called when an amount of mixing actions has been entered.
  def amountEntered(amount)
    if amount <= 0 then return else execute(amount) end
  end

  # Called to set the action(s) in motion.
  def execute(amount)
    @action.set_amount(amount)
    @player.start_action(@action)
  end

  def calculate_maximum(code)
    # Override for potion-specific amount calculation.
  end

  # Gets the amount of actions based on the specified button id.
  def get_amount(button)
    case button
      when 2799 then return 1
      when 2798 then return 5
      when 1748 then return -1
      when 1747 then return -2
      else return 0
    end
  end

end

# A MixingDialogueListener used for mixing unfinished potions.
class UnfinishedMixingDialogueListener < MixingDialogueListener
  def calculate_maximum
    inventory = @player.inventory

    amount = inventory.get_amount(WATER_VIAL_ID)

    return 0 if amount <= 0

    herbs = inventory.get_amount(@action.potion.herb.item.id)
    amount = herbs if amount > herbs

    return amount
  end
end

# A MixingDialogueListener used for mixing finished potions.
class FinishedMixingDialogueListener < MixingDialogueListener

  def calculate_maximum
    inventory = @player.inventory

    amount = inventory.capacity
    @action.potion.ingredients.each do |ingredient|
      item_amount = inventory.get_amount(ingredient.item.id)
      amount = item_amount if amount > item_amount
    end

    return amount
  end

end

# An Action which handles the none-finished-dependent mixing.
class MixingAction < Action
  attr_reader :potion, :amount, :started, :pulses, :action, :listener

  def initialize(player, potion, action)
    super(1, true, player)

    @potion = potion
    @started = false
    @pulses = 0
    @action = action
    @action.freeze
  end

  def execute
    if @pulses == 0
      unless @started
        unless check_skill(mob, @potion.level, @action)
          stop
          return
        end
        @started = true
      end

      unless gather_materials
        stop
        return
      end
    end
    mob.play_animation(MIXING_ANIM)
    execute_action

    if (@amount -= 1) > 0 then @pulses = 0 else stop end
  end

  def stop
    super()
    mob.inventory.remove_listener(@listener) unless @listener == nil
  end

  def execute_action
    # Override for action execution.
  end

  def gather_materials
    # Override for ingredient checking and gathering
    return false
  end

  # Sets the amount of actions.
  def set_amount(amount)
    @amount = amount
  end

  def equals(other)
    return (get_class == other.get_class and @potion == other.potion)
  end
end

# A MixingAction which handles the execution of making UnfinishedPotions.
class UnfinishedMixingAction < MixingAction
  attr_reader :slots

  def initialize(player, potion)
    super(player, potion, "use this herb.")
  end

  def execute_action
    name = @potion.herb.item.definition.name
    player = mob
    inventory = player.inventory

    player.send_message("You put the #{name} in the water to make an unfinished #{name.sub(/ leaf$/, "")} potion.", true)

    @slots.each do |slot, amount|
      unless inventory.remove_slot(slot, amount)
        stop
        return
      end
    end

    inventory.add(@potion.item)
  end

  def gather_materials
    @slots = {}
    inventory = mob.inventory

    vial_slot = inventory.slot_of(WATER_VIAL_ID)
    if vial_slot == -1
      mob.send_message('You do not have any more vials of water.')
      return false
    end

    item = @potion.herb.item
    herb_slot = inventory.slot_of(item.id)
    if herb_slot == -1
      mob.send_message("You do not have any more #{item.definition.name}.")
      return false
    end

    @slots[vial_slot] = 1
    @slots[herb_slot] = 1

    return true
  end
end

# A MixingAction which handles the execution of making FinishedPotions.
class FinishedMixingAction < MixingAction
  attr_reader :unfinished, :ingredient, :slots
  
  def initialize(player, unfinished, ingredient, potion)
    super(player, potion, "mix this potion")
    
    @unfinished = unfinished
    @ingredient = ingredient
  end
  
  def execute_action
    player = mob    
    ingredient = ItemDefinition.lookup(@ingredient).name.downcase
    name = @potion.item.definition.name.sub('(3)', '')

    player.send_message("You add the #{ingredient} to the mixture to make an #{name}.", true)
    player.skill_set.add_experience(HERBLORE_SKILL_ID, @potion.experience)

    inventory = player.inventory
      
    @slots.each do |slot, amount|
      if not inventory.remove_slot(slot, amount)
        stop
        return
      end
    end
      
    inventory.add(@potion.item)
  end
  
  def gather_materials
    @slots = {}
    inventory = mob.inventory

    vial_slot = inventory.slot_of(@unfinished)
    if vial_slot == -1
      mob.send_message('You do not have enough unfinished potions.')
      return false
    end
    
    ingredient_slot = inventory.slot_of(@ingredient)
    if ingredient_slot == -1
      mob.send_message('You do not have enough ingredients.')
      return false
    end
    
    @slots[vial_slot] = 1
    @slots[ingredient_slot] = 1
    
    return true
  end
end

# Appends a finished potion to the ItemOnItemMessage handling interception.
def append_finished_potion(item, unfinished, ingredient, level, experience)
  potion = FinishedPotion.new(item, [ unfinished, ingredient ], level, experience)
  append_herblore_item(potion, unfinished.item_id, ingredient.item_id)
  return potion
end

# Appends an unfinished potion to the ItemOnItemMessage handling interception.
def append_unfinished_potion(item, herb, level)
  potion = UnfinishedPotion.new(item, herb, level)
  append_herblore_item(potion, herb.item_id, WATER_VIAL_ID)
  return potion
end


# Unfinished potions
UNF_GUAM        = append_unfinished_potion(91,   GUAM_LEAF,   1) # 3
UNF_MARRENTILL  = append_unfinished_potion(93,   MARRENTILL,  5)
UNF_TARROMIN    = append_unfinished_potion(95,   TARROMIN,    12)
UNF_HARRALANDER = append_unfinished_potion(97,   HARRALANDER, 22)
UNF_RANARR      = append_unfinished_potion(99,   RANARR,      30)
UNF_TOADFLAX    = append_unfinished_potion(3002, TOADFLAX,    34)
UNF_IRIT        = append_unfinished_potion(101,  IRIT_LEAF,   45)
UNF_AVANTOE     = append_unfinished_potion(103,  AVANTOE,     50)
UNF_KWUARM      = append_unfinished_potion(105,  KWUARM,      55)
UNF_SNAPDRAGON  = append_unfinished_potion(3004, SNAPDRAGON,  63)
UNF_CADANTINE   = append_unfinished_potion(107,  CADANTINE,   66)
UNF_LANTADYME   = append_unfinished_potion(2483, LANTADYME,   69)
UNF_DWARF_WEED  = append_unfinished_potion(109,  DWARF_WEED,  72)
UNF_TORSTOL     = append_unfinished_potion(111,  TORSTOL,     78)


# Finished potions
ATTACK_POT           = append_finished_potion(121,  UNF_GUAM,        EYE_NEWT,          1,  25) # 3, 25
ANTIPOISON_POT       = append_finished_potion(175,  UNF_MARRENTILL,  UNICORN_HORN_DUST, 5,  37.5)
STRENGTH_POT         = append_finished_potion(115,  UNF_TARROMIN,    LIMPWURT_ROOT,     12, 50)
RESTORE_POT          = append_finished_potion(127,  UNF_HARRALANDER, RED_SPIDERS_EGGS,  18, 62.5)
ENERGY_POT           = append_finished_potion(3010, UNF_HARRALANDER, CHOCOLATE_DUST,    26, 67.5)
DEFENCE_POT          = append_finished_potion(133,  UNF_RANARR,      WHITE_BERRIES,     30, 75)
AGILITY_POT          = append_finished_potion(3034, UNF_TOADFLAX,    TOADS_LEGS,        34, 80)
PRAYER_POT           = append_finished_potion(139,  UNF_RANARR,      SNAPE_GRASS,       38, 87.5)
SUPER_ATTACK_POT     = append_finished_potion(145,  UNF_IRIT,        EYE_NEWT,          45, 100)
SUPER_ANTIPOISON_POT = append_finished_potion(181,  UNF_IRIT,        UNICORN_HORN_DUST, 48, 106.3)
FISHING_POT          = append_finished_potion(151,  UNF_AVANTOE,     SNAPE_GRASS,       50, 112.5)
SUPER_ENERGY_POT     = append_finished_potion(3018, UNF_AVANTOE,     MORT_MYRE_FUNGI,   52, 117.5)
SUPER_STRENGTH_POT   = append_finished_potion(157,  UNF_KWUARM,      LIMPWURT_ROOT,     55, 125)
WEAPON_POISON        = append_finished_potion(187,  UNF_KWUARM,      DRAGON_SCALE_DUST, 60, 137.5)
SUPER_RESTORE_POT    = append_finished_potion(3026, UNF_SNAPDRAGON,  RED_SPIDERS_EGGS,  63, 142.5)
SUPER_DEFENCE_POT    = append_finished_potion(163,  UNF_CADANTINE,   WHITE_BERRIES,     66, 150)
ANTIFIRE_POT         = append_finished_potion(2428, UNF_LANTADYME,   DRAGON_SCALE_DUST, 69, 157.5)
RANGING_POT          = append_finished_potion(169,  UNF_DWARF_WEED,  WINE_ZAMORAK,      72, 162.5)
MAGIC_POT            = append_finished_potion(3042, UNF_LANTADYME,   POTATO_CACTUS,     76, 172.5)
ZAMORAK_BREW         = append_finished_potion(189,  UNF_TORSTOL,     JANGERBERRIES,     78, 175)