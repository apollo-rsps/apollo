java_import 'org.apollo.game.action.DistancedAction'

# A distanced action which opens a door.
class OpenDoorAction < DistancedAction
  include DoorConstants

  attr_reader :door_object

  def initialize(mob, door_object)
    super(0, true, mob, door_object.position, DOOR_SIZE)
    @door_object = door_object
  end

  def executeAction
    mob.turn_to @door_object.position
    DoorUtil::toggle(@door_object, mob)
    stop
  end

  def equals(other)
    return (get_class == other.get_class && @door_object == other.door_object)
  end
end

# Message handler for opening and closing doors.
on :message, :first_object_action do |ctx, player, message|
  if DoorUtil::is_door?(message.id)
    door_object = DoorUtil::get_door_object(message.position, message.id)
    player.start_action(OpenDoorAction.new(player, door_object)) unless door_object.nil?
  end
end