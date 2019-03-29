require 'java'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.entity.Skill'

# An action that causes a mob to fish at a spot.
class FishingAction < DistancedAction
  attr_reader :position, :options, :spot, :started, :tool

  # Creates the FishingAction.
  def initialize(mob, position, spot, option)
    super(4, true, mob, position, 1)
    @position = position
    @spot = spot
    @tool = spot.tools[option - 1]

    @options = (option == 1) ? spot.first_fish : spot.second_fish
    @minimum_level = @options.map(&:level).min
  end

  # Returns whether or not a catch is successful.
  def successful_catch(level, requirement)
    [level - requirement + 5, 30].min > rand(40)
  end

  # Starts the fishing process.
  def start_fishing
    @started = true
    mob.send_message(tool.message)
  end

  # Executes the action.
  def executeAction
    skills = mob.skill_set
    fishing_level = skills.get_skill(Skill::FISHING).current_level
    mob.turn_to(position)

    if @minimum_level > fishing_level
      mob.send_message("You need a fishing level of #{@minimum_level} to fish at this spot.")
      stop
      return
    end

    inventory = mob.inventory
    if inventory.free_slots.zero?
      inventory.force_capacity_exceeded
      stop
      return
    end

    unless inventory.contains(@tool.id)
      mob.send_message("You need a #{@tool.name.downcase} to fish at this spot.")
      stop
      return
    end

    bait = find_bait
    if bait == -1
      mob.send_message("You need #{name_of(:item, bait).downcase}s to fish at this spot.")
      stop
      return
    end

    if @started
      options = @options.reject { |fish| fish.level > fishing_level }
      # Player may level up mid-action so reject here, not at initialisation.
      fish = options.sample # TODO: it's a ~70/30 chance, not 50/50

      if successful_catch(fishing_level, fish.level)
        inventory.remove(bait) unless bait.nil?
        inventory.add(fish.id)

        name = fish.name
        mob.send_message("You catch #{name.end_with?('s') ? 'some' : 'a'} #{name.downcase}.")
        skills.add_experience(Skill::FISHING, fish.experience)

        if find_bait == -1
          mob.send_message("You need more #{name_of(:item, bait).downcase}s to fish at this spot.")
          stop
          return
        end
      end
    else
      start_fishing
    end

    mob.play_animation(@tool.animation)
  end

  # Finds the id of the first piece of bait in the player's inventory, or nil if no bait is
  # required, or -1 if the player's inventory does not contain any valid bait.
  def find_bait
    baits = @tool.bait
    baits.empty? ? nil : baits.find(-1) { |bait| mob.inventory.contains(bait) }
  end

  # Stops this action.
  def stop
    super
    mob.stop_animation
  end

  def equals(other)
    get_class == other.get_class && @spot == other.spot && @position == other.position &&
      @options == @other.options
  end

end

# Intercepts the NpcAction message to determine whether or not a clicked npc was a fishing spot.
on :message, :npc_action do |player, message|
  npc = $world.npc_repository.get(message.index)
  spot = FISHING_SPOTS[npc.id]

  unless spot.nil?
    player.start_action(FishingAction.new(player, npc.position, spot, message.option))
    message.terminate
  end
end
