require 'java'

java_import 'org.apollo.game.event.impl.ConfigEvent'
java_import 'org.apollo.game.model.entity.EquipmentConstants'
java_import 'org.apollo.game.action.DistancedAction'

# The list of tiaras.
TIARAS_BY_ALTAR = {}
TIARAS_BY_ID = {}
TIARAS_BY_TALISMAN = {}

CHANGE_ALTAR_OBJECT_CONFIG = 491
TIARA_ITEM_ID = 5525

# A tiara will make an altar accessible with 1 click
class Tiara
  attr_reader :altar, :bitshift, :tiara_id, :experience, :talisman

  def initialize(tiara_id, altar, talisman, bitshift, experience)
      @tiara_id = tiara_id
      @name = ItemDefinition.lookup(tiara_id).name.downcase
      @altar = altar
      @talisman = talisman
      @bitshift = bitshift
      @experience = experience
  end

  def send_config(player)
     player.send(ConfigEvent.new(CHANGE_ALTAR_OBJECT_CONFIG, 1 << @bitshift))
  end
end

private
def send_empty_config(player)
  player.send(ConfigEvent.new(CHANGE_ALTAR_OBJECT_CONFIG, 0))
end

# Appends a tiara to the list.
def append_tiara(hash)
  raise 'Hash must contain a tiara id, altar id, talisman id, a bitshift number, and experience.' unless hash.has_key?(:tiara_id) && hash.has_key?(:altar) && hash.has_key?(:talisman) && hash.has_key?(:bitshift) && hash.has_key?(:experience)
  tiara_id = hash[:tiara_id]; altar = hash[:altar]; talisman = hash[:talisman]; bitshift = hash[:bitshift]; experience = hash[:experience]

  TIARAS_BY_TALISMAN[talisman] = TIARAS_BY_ID[tiara_id] = TIARAS_BY_ALTAR[altar] = Tiara.new(tiara_id, altar, talisman, bitshift, experience)
end

#Set the config upon login
on :login do |player|
  hat = player.equipment.get(EquipmentConstants::HAT)
  if hat != nil
    tiara = TIARAS_BY_ID[hat]
    if(tiara != nil)
      tiara.send_config
      return
    end
  end
  send_empty_config(player)
end

#Accesses the altar with 1 click when wielding the correct tiara.
on :event, :object_action do |ctx, player, event|
  if (event.option == 1)
    object_id = event.id
    tiara = TIARAS_BY_ALTAR[object_id]
    if(tiara != nil)
      hat = player.equipment.get(EquipmentConstants::HAT)
        if(hat != nil && hat.id == tiara.tiara_id)
          altar = ENTRANCE_ALTARS[tiara.altar]
          if(altar != nil)
            player.start_action(TeleportAction.new(player, event.position, 2, altar.entrance_position))
          end
          ctx.break_handler_chain
        end
    end
  end
end

#Equip tiara
on :event, :item_option do |ctx, player, event|
  if (event.option == 2)
    tiara = TIARAS_BY_ID[event.id]
    if(tiara != nil)
      tiara.send_config(player)
      ctx.break_handler_chain
    end
  end
end

#Unequip tiara
on :event, :item_action do |ctx, player, event|
  if (event.option == 1)
    tiara = TIARAS_BY_ID[event.id]
    if(tiara != nil)
      send_empty_config(player)
      ctx.break_handler_chain
    end
  end
end

#Create tiara
on :event, :item_on_object do |ctx, player, event|
  tiara= TIARAS_BY_TALISMAN[event.id]; altar = CRAFTING_ALTARS[event.object_id]
  if (tiara != nil && altar != nil && tiara.altar == altar.entrance_altar)
    player.start_action(CreateTiaraAction.new(player, event.position, tiara, altar))
    ctx.break_handler_chain
  end
end

# An action lets the player create a tiara when it comes within the specified distance of a specified position.
# noinspection JRubyImplementInterfaceInspection
class CreateTiaraAction < DistancedAction
  def initialize(player, position, tiara, altar)
    super(0, true, player, position, 2)
    @position = position
    @player = player
    @tiara = tiara
    @altar = altar
  end

  def execute_action
    if(@player.inventory.contains(TIARA_ITEM_ID) && @player.inventory.contains(@tiara.talisman))
      @player.inventory.remove(@tiara.talisman)
      @player.inventory.remove(TIARA_ITEM_ID)
      @player.inventory.add(@tiara.tiara_id)
      @player.skill_set.addExperience(RUNECRAFT_SKILL_ID, @tiara.experience)
      @player.turn_to(@position)
      @player.play_animation(RUNECRAFTING_ANIMATION)
      @player.play_graphic(RUNECRAFTING_GRAPHIC)
    else
      @player.send_message("You need to have a talisman and blank tiara to enchant a tiara.")
    end
    stop
  end

  def equals(other)
    return (get_class == other.get_class && @player == other.player)
  end
end

append_tiara :name => :air_tiara,    :tiara_id => 5527, :altar => 2452,    :talisman => 1438, :bitshift => 0, :experience => 25
append_tiara :name => :mind_tiara,    :tiara_id => 5529, :altar => 2453,    :talisman => 1448, :bitshift => 1, :experience => 27.5
append_tiara :name => :water_tiara,    :tiara_id => 5531, :altar => 2454,    :talisman => 1444, :bitshift => 2, :experience => 30
append_tiara :name => :body_tiara,    :tiara_id => 5533, :altar => 2457,    :talisman => 1446, :bitshift => 5, :experience => 37.5
append_tiara :name => :earth_tiara,    :tiara_id => 5535, :altar => 2455,    :talisman => 1440, :bitshift => 3, :experience => 32.5
append_tiara :name => :fire_tiara,    :tiara_id => 5537, :altar => 2456,    :talisman => 1442, :bitshift => 4, :experience => 35
append_tiara :name => :cosmic_tiara,    :tiara_id => 5539, :altar => 2458,    :talisman => 1454, :bitshift => 6, :experience => 40
append_tiara :name => :nature_tiara,    :tiara_id => 5541, :altar => 2460,    :talisman => 1462, :bitshift => 8, :experience => 45
append_tiara :name => :chaos_tiara,    :tiara_id => 5543, :altar => 2461,    :talisman => 1452, :bitshift => 9, :experience => 42.5
append_tiara :name => :law_tiara,    :tiara_id => 5545, :altar => 2459,    :talisman => 1458, :bitshift => 7, :experience => 47.5
append_tiara :name => :death_tiara,    :tiara_id => 5548, :altar => 2462,    :talisman => 1456, :bitshift => 10, :experience => 50
#TODO there are 2 other altars, which probably just aren't spawned on the map