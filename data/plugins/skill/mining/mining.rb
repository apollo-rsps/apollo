require 'java'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.def.ItemDefinition'
java_import 'org.apollo.game.model.entity.EquipmentConstants'

PROSPECT_PULSES = 3
ORE_SIZE = 1

# TODO: finish implementing this
class MiningAction < DistancedAction
  attr_reader :position, :ore, :counter, :started

  def initialize(mob, position, ore)
    super(0, true, mob, position, ORE_SIZE)
    @position = position
    @ore = ore
    @started = false
    @counter = 0
  end

  def find_pickaxe
    PICKAXE_IDS.each do |id|
      weapon = mob.equipment.get(EquipmentConstants::WEAPON)
      if (weapon != nil && weapon.id == id)
        return PICKAXES[id]
      end

      return PICKAXES[id] if mob.inventory.contains(id)
    end

    return nil
  end

  # starts the mining animation, sets counters/flags and turns the mob to
  # the ore
  def start_mine(pickaxe)
    @started = true
    mob.send_message('You swing your pick at the rock.', true)
    mob.play_animation(pickaxe.animation)
    @counter = pickaxe.pulses
  end

  def executeAction
    skills = mob.skill_set
    level = skills.get_skill(MINING_SKILL_ID).current_level
    pickaxe = find_pickaxe
    mob.turn_to(@position)

    # verify the mob can mine with their pickaxe
    unless (pickaxe != nil and level >= pickaxe.level)
      mob.send_message('You do not have a pickaxe for which you have the level to use.')
      stop
      return
    end

    # verify the mob can mine the ore
    if ore.level > level
      mob.send_message('You do not have the required level to mine this rock.')
      stop
      return
    end

    # check if we need to kick start things
    unless @started
      start_mine(pickaxe)
    else
      # count down and check if we can have a chance at some ore now
      if @counter == 0
        # TODO: calculate the chance that the mob can actually get the rock

        if mob.inventory.add(ore.id)
          ore_def = ItemDefinition.lookup(@ore.id) # TODO: split off into some method
          name = ore_def.name.sub(/ ore$/, '').downcase

          mob.send_message("You manage to mine some #{name}.", true)
          skills.add_experience(MINING_SKILL_ID, ore.exp)
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

  def initialize(mob, position)
    super(0, true, mob, position, ORE_SIZE)
  end

  def executeAction
    mob.send_message('There is currently no ore available in this rock.')
    stop
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position)
  end

end

class ProspectingAction < DistancedAction
  attr_reader :position, :ore

  def initialize(mob, position, ore)
    super(PROSPECT_PULSES, true, mob, position, ORE_SIZE)
    @position = position
    @ore = ore
    @started = false
  end

  def executeAction
    unless @started
      @started = true

      mob.send_message('You examine the rock for ores...')
      mob.turn_to(@position)
    else
      ore_def = ItemDefinition.lookup(@ore.id)
      name = ore_def.name.sub(/ ore$/, '').downcase

      mob.send_message("This rock contains #{name}.")
      stop
    end
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position and @ore == other.ore)
  end
end

on :message, :first_object_action do |ctx, mob, message|
  ore = ORES[message.id]
  if ore != nil
    mob.start_action(MiningAction.new(mob, message.position, ore))
  end
end

on :message, :second_object_action do |ctx, mob, message|
  ore = ORES[message.id]
  if ore != nil
    mob.start_action(ProspectingAction.new(mob, message.position, ore))
  elsif EXPIRED_ORES[message.id] != nil
    mob.start_action(ExpiredProspectingAction.new(mob, message.position))
  end
end