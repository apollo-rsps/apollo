require 'java'

java_import 'org.apollo.game.model.def.ItemDefinition'

# The list of runes.
COMBINATION_RUNES = {}

BINDING_NECKLACE_ID = 5521

# Represents a combinationrune that can be crafted.
class CombinationRune
  attr_reader :name, :id, :level, :low_rune_id, :high_rune_id, :low_talisman_id, :high_talisman_id, :low_rune_exp, :high_rune_exp
  
  def initialize(id, level, low_rune_id, low_rune_exp, low_talisman_id, low_altar_id, high_rune_id, high_rune_exp, high_talisman_id, high_altar_id)
    @id = id
    @name = ItemDefinition.lookup(id).name.downcase
    @level = level
    @low_rune_id = low_rune_id
    @low_rune_exp = low_rune_exp
    @low_talisman_id = low_talisman_id
    @low_altar_id = low_altar_id
    @high_rune_id = high_rune_id
    @high_rune_exp = high_rune_exp
    @high_talisman_id = high_talisman_id
    @high_altar_id = high_altar_id
  end
end

private
# Appends a combinationrune to the list.
def append_combination_rune(hash)
  raise 'Hash must contain an id, level, low/high rune_id/rune_exp/talisman_id/altar.' unless hash.has_key?(:id) && hash.has_key?(:level) && hash.has_key?(:low_rune_id) && hash.has_key?(:low_rune_exp) && hash.has_key?(:low_talisman_id) && hash.has_key?(:low_altar_id) && hash.has_key?(:high_rune_id) && hash.has_key?(:high_rune_exp) && hash.has_key?(:high_talisman_id) && hash.has_key?(:high_altar_id)
  id = hash[:id]; level = hash[:level]
  low_rune_id = hash[:low_rune_id]; low_rune_exp = hash[:low_rune_exp]; low_talisman_id = hash[:low_talisman_id]; low_altar_id = hash[:low_altar_id]
  high_rune_id = hash[:high_rune_id]; high_rune_exp = hash[:high_rune_exp]; high_talisman_id = hash[:high_talisman_id]; high_altar_id = hash[:high_altar_id]

  COMBINATION_RUNES[high_altar_id << 32 | low_talisman_id] = COMBINATION_RUNES[low_altar_id << 32 | high_talisman_id] = CombinationRune.new(id, level, low_rune_id, low_rune_exp, low_talisman_id, low_altar_id, high_rune_id, high_rune_exp, high_talisman_id, high_altar_id)
end

# Intercepts the item on object event.
on :event, :item_on_object do |ctx, player, event|
  rune = COMBINATION_RUNES[event.object_id << 32 | event.id]
  if rune != nil
    ctx.break_handler_chain
    player.start_action(CombinationRunecraftingAction.new(player, rune, event.position, event.id))
  end
end

# An action when the player crafts a rune.
class CombinationRunecraftingAction < DistancedAction
  attr_reader :player, :rune

  def initialize(player, rune, object_position, used_talisman_id)
    super(1, true, player, object_position, 3)
    @player = player
    @rune = rune
    @position = object_position
    @executions = 0

    #Figuring out the correct rune, experience (and talisman) you need
    if used_talisman_id == @rune.low_talisman_id
      #crafting happens at higher level altar
      @required_rune_id = @rune.low_rune_id
      @required_talisman_id = @rune.low_talisman_id
      @experience = @rune.high_rune_exp
    else
      #crafting happens at lower level altar
      @required_rune_id = @rune.high_rune_id
      @required_talisman_id = @rune.high_talisman_id
      @experience = @rune.low_rune_exp
    end
  end

  def executeAction
    #Level requirement
    level_requirement = @rune.level
    runecrafting_level = @player.skill_set.get_skill(RUNECRAFT_SKILL_ID).current_level
    if runecrafting_level < level_requirement
      @player.send_message("You need a runecrafting level of #{@rune.level} to craft a " + @rune.name + ".")
      stop
      return
    end

    #Rune essence required
    pure_ess_amount = @player.inventory.getAmount(RUNE_ESSENCE_ID)
    if pure_ess_amount == 0
      @player.send_message("You need rune essence in order to craft combination runes.")
      stop
      return
    end

    #Runes required
    runes_amount = @player.inventory.getAmount(@required_rune_id)
    if runes_amount == 0
      rune_name = ItemDefinition.lookup(@required_rune_id).name
      @player.send_message("You need " + rune_name + "s in order to craft " + @rune.name)
      stop
      return
    end

    #Talisman should already be available

    if @executions == 0
      @player.turn_to(@position)
      @player.play_animation(RUNECRAFTING_ANIMATION)
      @player.play_graphic(RUNECRAFTING_GRAPHIC)
      @executions += 1
    elsif @executions == 1
      #Maximum amount of combination runes that can be made
      amount = [runes_amount, pure_ess_amount].min

      #Removal of the consumed items
      @player.inventory.remove(RUNE_ESSENCE_ID, amount)
      @player.inventory.remove(@required_rune_id, amount)
      @player.inventory.remove(@required_talisman_id, 1)

      #Calculation of the number of combination runes to add
      amulet = player.equipment.get(EquipmentConstants::AMULET)
      if amulet != nil && amulet == BINDING_NECKLACE_ID
        #100% success rate
        added = amount
      else
        #50% success rate
        #A repetition of the 50% chance for each essence
        added = 0
        amount.times {added += rand(2)}
      end

      #Crafting of the combination runes
      @player.inventory.add(@rune.id, added)
      @player.send_message("Your craft the rune essence into #{added > 1 ? 'some ' + @rune.name + 's' : 'an ' + @rune.name}.", true)
      @player.skill_set.add_experience(RUNECRAFT_SKILL_ID, added * @experience)
      stop
    end
  end

  def equals(other)
    return (get_class == other.get_class && @player == other.player)
  end

end

append_combination_rune :name => :mist_rune, :id => 4695, :level => 6,  :low_rune_id => 556, :low_rune_exp => 8.0,:low_talisman_id => 1438,:low_altar_id=> 2478,  :high_rune_id => 555, :high_rune_exp => 8.5,:high_talisman_id => 1444,:high_altar_id=>2480
append_combination_rune :name => :dust_rune, :id => 4696, :level => 10,  :low_rune_id => 556, :low_rune_exp => 8.3,:low_talisman_id => 1438,:low_altar_id=> 2478,  :high_rune_id => 557, :high_rune_exp => 9.0,:high_talisman_id => 1440,:high_altar_id=>2481
append_combination_rune :name => :mud_rune, :id => 4698, :level => 13,  :low_rune_id => 555, :low_rune_exp => 9.3,:low_talisman_id => 1444,:low_altar_id=> 2480,  :high_rune_id => 557, :high_rune_exp => 9.5,:high_talisman_id => 1440,:high_altar_id=>2481
append_combination_rune :name => :smoke_rune, :id => 4697, :level => 15,  :low_rune_id => 556, :low_rune_exp => 8.5,:low_talisman_id => 1438,:low_altar_id=> 2478,  :high_rune_id => 554, :high_rune_exp => 9.5,:high_talisman_id => 1442,:high_altar_id=>2482
append_combination_rune :name => :steam_rune, :id => 4694, :level => 19,  :low_rune_id => 555, :low_rune_exp => 9.3,:low_talisman_id => 1444,:low_altar_id=> 2480,  :high_rune_id => 554, :high_rune_exp => 10.0,:high_talisman_id => 1442,:high_altar_id=>2482
append_combination_rune :name => :lava_rune, :id => 4699, :level => 23,  :low_rune_id => 557, :low_rune_exp => 10.0,:low_talisman_id => 1440,:low_altar_id=> 2481,  :high_rune_id => 554, :high_rune_exp => 10.5,:high_talisman_id => 1442,:high_altar_id=>2482