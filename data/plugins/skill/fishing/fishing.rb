require 'java'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.entity.Skill'

# An action that causes a mob to fish at a spot.
class FishingAction < DistancedAction
  attr_reader :spot, :tool, :position, :started

  # Creates the FishingAction.
  def initialize(mob, position, spot, option)
    super(4, true, mob, position, 1)
    @position = position
    @spot = spot
    @tool = spot.tools[option - 1]

    @options = (option == 1) ? spot.first_option : spot.second_option
    @minimum_level = options.map { |fish| fish.level }.min
  end

  # Returns whether or not a catch is successful.
  def successful_catch(level, requirement)
    return [level - requirement, 30].min > rand(40)
  end

  # Starts the fishing process.
  def start_fishing()
    @started = true
    mob.send_message(tool.message, true)
  end

  # Executes the action.
  def executeAction()
    skills = mob.skill_set
    fishing_level = skills.get_skill(Skill::FISHING).current_level
    mob.turn_to(position)
    
    if (@minimum_level > fishing_level)
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
	    mob.send_message("You need a #{@tool.name} to fish at this spot.")
	    stop
      return
 	  end

    bait = @tool.bait
	  if (bait.empty? && !mob.inventory.contains(bait))
		  mob.send_message("You need #{name_of(:item, bait)}s to fish at this spot.")
	  	stop
      return
	  end

    unless @started
      start_fishing
    else
      options = @options.reject { |fish| fish.level > fishing_level } # Player may level up mid-action so reject here, not at initialisation.
      fish = options.sample # TODO it's a ~70/30 chance, not 50/50

  	  if successful_catch(fishing_level, fish.level)
  	    inventory.remove(bait) unless bait.empty?
  	    inventory.add(type.id)

  	    name = fish.name
  	    mob.send_message("You catch #{name.end_with?('s') ? 'some' : 'a'} #{name}.", true)
  	    skills.add_experience(Skill::FISHING, fish.experience)

  	    unless (bait != -1 && !mob.inventory.contains(bait))
          mob.send_message("You need more #{name_of(:item, bait)}s to fish at this spot.")
          stop
          return
        end
  	  end
    end

    mob.play_animation(@tool.animation)
  end

  # Stops this action.
  def stop()
    super
    mob.stop_animation
  end

  def equals(other)
    return (get_class == other.get_class and @spot == other.spot and @position == other.position && @options == @other.options)
  end

end

# Intercepts the NpcAction message to determine whether or not a clicked npc was a fishing spot.
on :message, :npc_action do |ctx, player, message|
  npc = $world.npc_repository.get(message.index)
  spot = FISHING_SPOTS[npc.id]

	unless spot.nil?
	  player.start_action(FishingAction.new(player, npc.position, spot, message.option))
	  ctx.break_handler_chain
  end
end