require 'java'

java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.area.Region'
java_import 'org.apollo.game.model.entity.Entity'
java_import 'org.apollo.game.model.entity.obj.DynamicGameObject'

# Contains door-related utility methods.
module DoorUtil
  include DoorConstants

  # A hash containing currently toggled door objects mapped to the original door objects.
  TOGGLED_DOORS = {}

  # Translates a door's position in the direction of its orientation.
  def self.translate_door_position(door)
    position = door.position

    case door.orientation
      when Orientation::WEST
        Position.new(position.x - 1, position.y, position.height)
      when Orientation::EAST
        Position.new(position.x + 1, position.y, position.height)
      when Orientation::NORTH
        Position.new(position.x, position.y + 1, position.height)
      when Orientation::SOUTH
        Position.new(position.x, position.y - 1, position.height)
      else fail "Unsupported orientation #{door.orientation}."
    end
  end

  # Translates the orientation of a door to a toggled position.
  def self.translate_door_orientation(door)
    object_id = door.id
    orientation = door.orientation

    if RIGHT_HINGE_DOORS.include?(object_id)
      return ORIENTATIONS[:right_side_hinge][orientation]
    elsif LEFT_HINGE_DOORS.include?(object_id)
      return ORIENTATIONS[:left_side_hinge][orientation]
    end

    fail 'Given object was not registered as a door.'
  end

  # Toggles the given door.
  def self.toggle(door)
    region = $world.region_repository.from_position(door.position)
    region.remove_entity(door)

    if TOGGLED_DOORS.include?(door)
      original_door = TOGGLED_DOORS.delete(door)

      original_region = $world.region_repository.from_position(original_door.position)
      original_region.add_entity(original_door)
    else
      position = translate_door_position(door)
      orientation = translate_door_orientation(door)
      type = door.type

      toggled_door = DynamicGameObject.create_public($world, door.id, position, type, orientation)

      toggled_region = $world.region_repository.from_position(position)
      toggled_region.add_entity(toggled_door)

      TOGGLED_DOORS[toggled_door] = door
    end
  end

  # Gets the door object at the given position, if it exists.
  def self.get_door_object(position, object_id)
    region = $world.region_repository.from_position(position)
    objects = region.get_entities(position, EntityType::DYNAMIC_OBJECT, EntityType::STATIC_OBJECT)
    objects.each { |game_object| return game_object if game_object.id == object_id }
    nil
  end

  # Checks if the given game object id is a door.
  def self.door?(object_id)
    RIGHT_HINGE_DOORS.include?(object_id) || LEFT_HINGE_DOORS.include?(object_id)
  end

end
