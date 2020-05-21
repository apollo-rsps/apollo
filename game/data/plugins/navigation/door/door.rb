
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.event.Event'

private

# A distanced action which opens a door.
class OpenDoorAction < DistancedAction
  include DoorConstants

  attr_reader :door

  def initialize(mob, door)
    super(0, true, mob, door.position, DOOR_SIZE)
    @door = door
  end

  def executeAction
    if $world.submit(OpenDoorEvent.new(mob, @door))
      mob.turn_to(@door.position)
      DoorUtil.toggle(@door)
    end

    stop
  end

  def equals(other)
    get_class == other.get_class && @door == other.door
  end

end

# A PlayerEvent that is fired when a player attempts to open a door.
class OpenDoorEvent < PlayerEvent
  attr_reader :door

  def initialize(player, door)
    super(player)
    @door = door
  end

end


# Listens for FirstObjectActions performed on doors.
on :message, :first_object_action do |player, message|
  id = message.id

  if DoorUtil.door?(id)
    door = DoorUtil.get_door_object(message.position, id)
    player.start_action(OpenDoorAction.new(player, door)) unless door.nil?
  end
end

