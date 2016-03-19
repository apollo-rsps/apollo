require 'java'
require 'securerandom'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.entity.EquipmentConstants'
java_import 'org.apollo.game.model.entity.Skill'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.area.Region'
java_import 'org.apollo.game.model.entity.obj.DynamicGameObject'

PROSPECT_PULSES = 3

# A `DistancedAction` for mining ore.
class MiningAction < DistancedAction
  attr_reader :position, :ore, :counter, :started, :objId

  def initialize(mob, position, ore, objId)
    super(0, true, mob, position, ObjectDefinition.lookup(objId).getLength)
    @position = position
    @ore = ore
    @started = false
    @counter = 0
    @objId = objId
  end

  def find_pickaxe
    level = mob.skill_set.get_skill(Skill::MINING).current_level
    inventory, equipment = mob.inventory, mob.equipment

    indices = PICKAXE_IDS.select do |id|
      PICKAXES[id].level <= level && (inventory.contains(id) || equipment.contains(id))
    end

    PICKAXES[indices.max]
  end

  def get_mining_rate(pickaxe, level)
    skill = 1 + level
    hostRatio = SecureRandom.random_number * (100.0 * ore.rate).to_f
    clientRatio = SecureRandom.random_number * ((skill - ore.level) * (1.0 + pickaxe.ratio)).to_f
    return hostRatio < clientRatio
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
    level = mob.skill_set.get_skill(Skill::MINING).current_level
    pickaxe = find_pickaxe

    ores = @ore.id.reject { |ore| ore.level > level}
    ore_id = if objId == 2491 then level >= 30 ? ores.last : ores.first else @ore.id.sample end
    ore_def = ItemDefinition.lookup(ore_id.id)

    name = ore_def.name.sub(/ ore$/, '').sub(/ \(.*kg\)/, '').downcase
    granite_or_sandstone = ['sandstone', 'granite'].include? name;
    essence = ['pure essence', 'rune essence'].include? name;

    mob.turn_to(@position)

    # check to see if player has enough room to mine
    if mob.inventory.free_slots.zero?
      mob.send_message(essence ? 'Your inventory is too full to hold any more rune stones.' : "Your inventory is too full to hold any more #{name}.")
      stop
      return
    end

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

    # checks to see if your pulse is on 0 and restarts the animation & pulse.
    if @counter == 0
      mob.play_animation pickaxe.animation
      @counter = pickaxe.pulses
    end

    # check if we need to kick start things
    if @started
      if get_mining_rate pickaxe, level
        if ore.respawn != 0
          mob.inventory.add(ore_id.id)
          if objId != 2491 then mob.send_message(granite_or_sandstone ? "You manage to quarry some #{name}." : "You manage to mine some #{name}.") end
          mob.skill_set.add_experience(Skill::MINING, ore_id.experience)
          if ore.respawn != -1 then
            expire_ore
            stop
          end
        end
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

def expire_ore
  players = $world.player_repository.size

  toggled_region = $world.region_repository.from_position(position)
  objects = toggled_region.get_entities(position, EntityType::DYNAMIC_OBJECT, EntityType::STATIC_OBJECT)

  obj = objects.select { |game_object| game_object.id == objId }.first

  toggled_ore = DynamicGameObject.create_public($world, ore.objects[objId], position, obj.getType, obj.getOrientation)
  toggled_region.add_entity(toggled_ore)

  $world.schedule ExpireOre.new(players, ore.id, position, ore.respawn, objId, ore.objects[objId])
end

class ExpireOre < ScheduledTask

  attr_reader :ore, :position, :objId, :expiredId

  def initialize(players, ore, position, tick, objId, expiredId)
    super respawn_pulses(tick, players), false
    @ore = ore
    @position = position
    @objId = objId
    @expiredId = expiredId
  end

  def execute
    toggled_region = $world.region_repository.from_position(position)
    objects = toggled_region.get_entities(position, EntityType::DYNAMIC_OBJECT, EntityType::STATIC_OBJECT)

    remove_obj = objects.select { |game_object| game_object.id == expiredId }.first
    toggled_region.remove_entity(remove_obj)

    obj = DynamicGameObject.create_public($world, objId, position, remove_obj.getType, remove_obj.getOrientation)
    toggled_region.add_entity(obj)
    stop
  end
end

# A `DistancedAction` for a rock with no available ore.
class ExpiredProspectingAction < DistancedAction
  attr_reader :position, :objId

  def initialize(mob, position, objId)
    super(0, true, mob, position, ObjectDefinition.lookup(objId).getLength)
    @objId = objId
  end

  def executeAction
    mob.send_message('There is no ore currently available in this rock.')
    stop
  end

  def equals(other)
    get_class == other.get_class && @position == other.position
  end

end

# A `DistancedAction` for prospecting a rock.
class ProspectingAction < DistancedAction
  attr_reader :position, :ore, :objId

  def initialize(mob, position, ore, objId)
    super(PROSPECT_PULSES, true, mob, position, ObjectDefinition.lookup(objId).getLength)
    @position = position
    @ore = ore
    @objId = objId
    @started = false
  end

  def executeAction
    ores = @ore.id.sample
    ore_def = ItemDefinition.lookup(ores.id)
    name = ore_def.name.split(' ').map { |s| s.downcase }
    ore_examine = {'granite' => 'is granite', 'sandstone' => 'is sandstone', 'essence' => 'unbound Rune Stone essence', 'ore' => "#{name[0]}",
                   'coal' => 'contains coal', 'clay' => 'contains clay'}
    not_ore_rock = ['sandstone', 'granite', 'coal', 'clay'].include?(name[0]);

    if @started
      mob.send_message(not_ore_rock ? "This rock #{ore_examine.fetch(name[0])}." : "This rock contains #{ore_examine.fetch(name[1])}.")
      stop
    else
      @started = true
      mob.send_message(not_ore_rock ? 'You examine the rock....' : 'You examine the rock for ores...')
      mob.turn_to(@position)
    end
  end

  def equals(other)
    get_class == other.get_class && @position == other.position && @ore == other.ore
  end

end

# A `DistancedAction` for mining an expired ore.
class ExpiredMiningAction < DistancedAction
  attr_reader :position, :ore

  def initialize(mob, position, ore)
    super(0, true, mob, position, 1)
    @position = position
    @ore = ore

  end

  def executeAction
    mob.turn_to(@position)
    mob.send_message('There is no ore currently available in this rock.')
    stop
  end

  def equals(other)
    get_class == other.get_class && @position == other.position && @ore == other.ore
  end

end

on :message, :first_object_action do |mob, message|
  ore = ORES[message.id]
  if !ore.nil?
    mob.start_action(MiningAction.new(mob, message.position, ore, message.id))
  elsif !EXPIRED_ORES[message.id].nil?
    mob.start_action(ExpiredMiningAction.new(mob, message.position, ore))
  end
end

on :message, :second_object_action do |mob, message|
  ore = ORES[message.id]
  if !ore.nil?
    mob.start_action(ProspectingAction.new(mob, message.position, ore, message.id))
  elsif !EXPIRED_ORES[message.id].nil?
    mob.start_action(ExpiredProspectingAction.new(mob, message.position, ore))
  end
end
