java_import 'org.apollo.game.model.entity.GameObject'
java_import 'org.apollo.game.model.entity.Entity'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.message.impl.PositionMessage'
java_import 'org.apollo.game.message.impl.RemoveObjectMessage'
java_import 'org.apollo.game.message.impl.SendObjectMessage'

module DoorUtil
  include DoorConstants

  # A hash containing currently toggled door objects mapped to the original door objects.
  TOGGLED_DOOR_REPOSITORY = {}

  # Translates a door's position in the direction of its orientation.
  def self.translate_door_position(door)
    position = door.position
    orientation = door.orientation
    case orientation
      when Orientation::WEST
        return Position.new(position.x - 1, position.y, position.height)
      when Orientation::EAST
        return Position.new(position.x + 1, position.y, position.height)
      when Orientation::NORTH
        return Position.new(position.x, position.y + 1, position.height)
      when Orientation::SOUTH
        return Position.new(position.x, position.y - 1, position.height)
    end
    raise 'Invalid orientation for door!'
  end

  # Translates the orientation of a door to a toggled position.
  def self.translate_door_orientation(door)
    object_id = door.id
    orientation = door.orientation
    if RIGHT_SIDE_HINGE_DOOR_IDS.include?(object_id)
      return ORIENTATIONS[:right_side_hinge][orientation]
    elsif LEFT_SIDE_HINGE_DOOR_IDS.include?(object_id)
      return ORIENTATIONS[:left_side_hinge][orientation]
    end
    raise 'Given object was not registered as a door!'
  end

  # Replaces a door object for a given player.
  # TODO: This is temporary.
  def self.replace_door(player, original, new)
    player.send PositionMessage.new(player.last_known_region, original.position)
    player.send RemoveObjectMessage.new(original)
    player.send PositionMessage.new(player.last_known_region, new.position)
    player.send SendObjectMessage.new(new)
  end

  # Toggles the given door.
  def self.toggle(door, player)
    # First, we remove the door we're toggling (or un-toggling) from the game world.
    position = door.position
    region = $world.region_repository.from_position(position)
    region.remove_entity door

    # Have we toggled this door already?
    if TOGGLED_DOOR_REPOSITORY.include?(door)
      # If we have, we get our original door. This also deletes the entry from our repository.
      original_door = TOGGLED_DOOR_REPOSITORY.delete(door)

      # Now we add our new door to the game world.
      original_region = $world.region_repository.from_position(original_door.position)
      original_region.add_entity original_door

      # TODO: This and the 'player' parameter are temporary. We still need to synchronize objects for local players.
      replace_door player, door, original_door
    else
      # If not, we get the translated orientation and position for this door, and create a new game object.
      toggled_position = translate_door_position(door)
      toggled_orientation = translate_door_orientation(door)
      toggled_door = GameObject.new(door.id, toggled_position, door.type, toggled_orientation)

      # Now we add our new door to the game world.
      toggled_region = $world.region_repository.from_position(toggled_position)
      toggled_region.add_entity toggled_door

      # Insert our toggled door in the repository.
      TOGGLED_DOOR_REPOSITORY[toggled_door] = door

      # TODO: This and the 'player' parameter are temporary. We still need to synchronize objects for local players.
      replace_door player, door, toggled_door
    end
  end

  # Gets the door object at the given position, if it exists.
  def self.get_door_object(position, object_id)
    game_objects = $world.region_repository.from_position(position).get_entities(position, EntityType::GAME_OBJECT)
    game_objects.each { |game_object| return game_object if game_object.id == object_id }
    return nil
  end

  # Checks if the given game object id is a door.
  def self.is_door?(object_id)
    RIGHT_SIDE_HINGE_DOOR_IDS.include?(object_id) || LEFT_SIDE_HINGE_DOOR_IDS.include?(object_id)
  end
end