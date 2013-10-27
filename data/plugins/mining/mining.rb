require 'java'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.EquipmentConstants'
java_import 'org.apollo.game.model.def.ItemDefinition'

PROSPECT_PULSES = 3
ORE_SIZE = 1

# TODO: finish implementing this
class MiningAction < DistancedAction
  attr_reader :position, :ore, :counter, :started

  def initialize(character, position, ore)
    super 0, true, character, position, ORE_SIZE
    @position = position
    @ore = ore
    @started = false
    @counter = 0
  end

  def find_pickaxe
    PICKAXE_IDS.each do |id|
      weapon = character.equipment.get EquipmentConstants::WEAPON
      if weapon.id == id
        return PICKAXES[id]
      end

      if character.inventory.contains id
        return PICKAXES[id]
      end
    end

    return nil
  end

  # starts the mining animation, sets counters/flags and turns the character to
  # the ore
  def start_mine(pickaxe)
    @started = true
    character.send_message "You swing your pick at the rock."
    character.turn_to @position
    character.play_animation pickaxe.animation
    @counter = pickaxe.pulses
  end

  def executeAction
    skills = character.skill_set
    level = skills.get_skill(Skill::MINING).maximum_level # TODO: is using max level correct?
    pickaxe = find_pickaxe

    # verify the player can mine with their pickaxe
    if not (pickaxe != nil and level >= pickaxe.level)
      character.send_message "You do not have a pickaxe for which you have the level to use."
      stop
      return
    end

    # verify the player can mine the ore
    if ore.level > level
      character.send_message "You do not have the required level to mine this rock."
      stop
      return
    end

    # check if we need to kick start things
    if not @started
      start_mine(pickaxe)
    else
      # count down and check if we can have a chance at some ore now
      if @counter == 0
        # TODO: calculate the chance that the player can actually get the rock

        if character.inventory.add ore.id
          ore_def = ItemDefinition.for_id @ore.id # TODO: split off into some method
          name = ore_def.name.sub(/ ore$/, "").downcase

          character.send_message "You manage to mine some #{name}."
          skills.add_experience Skill::MINING, ore.exp
          # TODO: expire the rock
        end

        stop
      end
      @counter -= 1
    end

  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position and @ore == other.ore)
  end
end

class ExpiredProspectingAction < DistancedAction
  attr_reader :position

  def initialize(character, position)
    super 0, true, character, position, ORE_SIZE
  end

  def executeAction
    character.send_message "There is currently no ore available in this rock."
    stop
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position)
  end

end

class ProspectingAction < DistancedAction
  attr_reader :position, :ore

  def initialize(character, position, ore)
    super PROSPECT_PULSES, true, character, position, ORE_SIZE
    @position = position
    @ore = ore
    @started = false
  end

  def executeAction
    if not @started
      @started = true

      character.send_message "You examine the rock for ores..."
      character.turn_to @position
    else
      ore_def = ItemDefinition.for_id @ore.id
      name = ore_def.name.sub(/ ore$/, "").downcase

      character.send_message "This rock contains #{name}."

      stop
    end
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position and @ore == other.ore)
  end
end

on :event, :object_action do |ctx, player, event|
  if event.option == 1
    ore = ORES[event.id]
    if ore != nil
      player.startAction MiningAction.new(player, event.position, ore)
    end
  end
end

on :event, :object_action do |ctx, player, event|
  if event.option == 2
    ore = ORES[event.id]
    if ore != nil
      player.startAction ProspectingAction.new(player, event.position, ore)
    else
      if EXPIRED_ORES[event.id] != nil
        player.startAction ExpiredProspectingAction.new(player, event.position)
      end
    end
  end
end
