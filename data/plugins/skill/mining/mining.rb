require 'java'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.entity.EquipmentConstants'
java_import 'org.apollo.game.model.entity.Skill'

PROSPECT_PULSES = 3
ORE_SIZE = 1

# TODO: finish implementing this
# A `DistancedAction` for mining ore.
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
    weapon = mob.equipment.get(EquipmentConstants::WEAPON)
    PICKAXE_IDS.each do |id|
      return PICKAXES[id] if (!weapon.nil? && weapon.id == id) || mob.inventory.contains(id)
    end

    nil
  end

  # starts the mining animation, sets counters/flags and turns the mob to
  # the ore
  def start_mine(pickaxe)
    @started = true
    mob.send_message('You swing your pick at the rock.')
    mob.play_animation(pickaxe.animation)
    @counter = pickaxe.pulses
  end

  def executeAction
    skills = mob.skill_set
    level = skills.get_skill(Skill::MINING).current_level
    pickaxe = find_pickaxe
    mob.turn_to(@position)

    # verify the mob can mine with their pickaxe
    if pickaxe.nil? || level < pickaxe.level
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
    if @started
      # count down and check if we can have a chance at some ore now
      if @counter == 0
        # TODO: calculate the chance that the player can actually get the rock

        if mob.inventory.add(ore.id)
          name = name_of(@ore.id).sub(/ ore$/, '').downcase

          mob.send_message("You manage to mine some #{name}.")
          skills.add_experience(Skill::MINING, ore.exp)
          # TODO: expire the rock
        end

        stop
      end

      @counter -= 1
    else
      start_mine(pickaxe)
    end
  end

  def equals(other)
    get_class == other.get_class && @position == other.position && @ore == other.ore
  end

end

# A `DistancedAction` for a rock with no available ore.
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
    get_class == other.get_class && @position == other.position
  end

end

# A `DistancedAction` for prospecting a rock.
class ProspectingAction < DistancedAction
  attr_reader :position, :ore

  def initialize(mob, position, ore)
    super(PROSPECT_PULSES, true, mob, position, ORE_SIZE)
    @position = position
    @ore = ore
    @started = false
  end

  def executeAction
    if @started
      ore_def = ItemDefinition.lookup(@ore.id)
      name = ore_def.name.sub(/ ore$/, '').downcase

      mob.send_message("This rock contains #{name}.")
      stop
    else
      @started = true

      mob.send_message('You examine the rock for ores...')
      mob.turn_to(@position)
    end
  end

  def equals(other)
    get_class == other.get_class && @position == other.position && @ore == other.ore
  end

end

on :message, :first_object_action do |mob, message|
  ore = ORES[message.id]

  mob.start_action(MiningAction.new(mob, message.position, ore)) unless ore.nil?
end

on :message, :second_object_action do |mob, message|
  ore = ORES[message.id]

  if !ore.nil?
    mob.start_action(ProspectingAction.new(mob, message.position, ore))
  elsif !EXPIRED_ORES[message.id].nil?
    mob.start_action(ExpiredProspectingAction.new(mob, message.position))
  end
end
