require 'java'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.entity.EquipmentConstants'
java_import 'org.apollo.game.model.entity.Skill'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.area.Region'
java_import 'org.apollo.game.model.entity.obj.DynamicGameObject'

PROSPECT_PULSES = 3

# A `DistancedAction` for mining ore.
class MiningAction < DistancedAction
  attr_reader :position, :ore, :counter, :started, :object_id

  def initialize(mob, position, ore, object_id)
    super(0, true, mob, position, ObjectDefinition.lookup(object_id).length)
    @position = position
    @ore = ore
    @started = false
    @counter = 0
    @object_id = object_id
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
    hostRatio = Random.new.rand * (100.0 * ore.rate).to_f
    clientRatio = Random.new.rand * ((skill - ore.level) * (1.0 + pickaxe.ratio)).to_f
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
    ore_id = if object_id == 2491
               level >= 30 ? ores.last : ores.first
             else
               @ore.id.sample
             end
    ore_def = ItemDefinition.lookup(ore_id.id)

    name = ore_def.name.sub(/ ore$/, '').sub(/ \(.*kg\)/, '').downcase
    granite_or_sandstone = ['sandstone', 'granite'].include? name;
    essence = ['pure essence', 'rune essence'].include? name;

    mob.turn_to(@position)

    if mob.inventory.free_slots.zero?
      mob.send_message("Your inventory is too full to hold any more #{essence ? 'rune stones' : name}.")
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

    if @counter == 0
      mob.play_animation(pickaxe.animation)
      @counter = pickaxe.pulses
    end

    # check if we need to kick start things
    if @started
      if get_mining_rate pickaxe, level
        if ore.respawn != 0
          mob.inventory.add(ore_id.id)
          mob.send_message("You manage to #{granite_or_sandstone ? 'quarry' : 'mine'} some #{name}.") unless object_id == 2491
          mob.skill_set.add_experience(Skill::MINING, ore_id.experience)
          unless ore.respawn == -1
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

  obj = objects.select { |game_object| game_object.id == object_id }.first

  toggled_ore = DynamicGameObject.create_public($world, ore.objects[object_id], position, obj.type, obj.orientation)
  toggled_region.add_entity(toggled_ore)

  $world.schedule(ExpireOre.new(players, ore.id, position, ore.respawn, object_id, ore.objects[object_id]))
end

class ExpireOre < ScheduledTask

  attr_reader :ore, :position, :object_id, :expired_id

  def initialize(players, ore, position, tick, object_id, expired_id)
    super(respawn_pulses(tick, players), false)
    @ore = ore
    @position = position
    @object_id = object_id
    @expired_id = expired_id
  end

  def execute
    toggled_region = $world.region_repository.from_position(position)
    objects = toggled_region.get_entities(position, EntityType::DYNAMIC_OBJECT, EntityType::STATIC_OBJECT)

    remove_obj = objects.select { |game_object| game_object.id == expired_id }.first
    toggled_region.remove_entity(remove_obj)

    obj = DynamicGameObject.create_public($world, object_id, position, remove_obj.type, remove_obj.orientation)
    toggled_region.add_entity(obj)
    stop
  end
end

# A `DistancedAction` for a rock with no available ore.
class ExpiredProspectingAction < DistancedAction
  attr_reader :position, :object_id

  def initialize(mob, position, objId)
    super(0, true, mob, position, ObjectDefinition.lookup(object_id).length)
    @object_id = object_id
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
  attr_reader :position, :ore, :object_id

  def initialize(mob, position, ore, object_id)
    super(PROSPECT_PULSES, true, mob, position, ObjectDefinition.lookup(object_id).length)
    @position = position
    @ore = ore
    @object_id = object_id
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
      mob.send_message(not_ore_rock ? 'You examine the rock...' : 'You examine the rock for ores...')
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
  rock = ROCK[message.id]
  if !rock.nil?
    mob.start_action(MiningAction.new(mob, message.position, rock, message.id))
  elsif !EXPIRED_ROCK[message.id].nil?
    mob.start_action(ExpiredMiningAction.new(mob, message.position, rock))
  end
end

on :message, :second_object_action do |mob, message|
  rock = ROCK[message.id]
  if !rock.nil?
    mob.start_action(ProspectingAction.new(mob, message.position, rock, message.id))
  elsif !EXPIRED_ROCK[message.id].nil?
    mob.start_action(ExpiredProspectingAction.new(mob, message.position, rock))
  end
end
