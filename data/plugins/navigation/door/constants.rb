java_import 'org.apollo.game.model.Direction'

module DoorConstants
  # TODO: GameObjectOrientation enumeration in Apollo's core?
  module Orientation
    WEST = 0
    NORTH = 1
    EAST = 2
    SOUTH = 3
  end

  # The object size of a door.
  DOOR_SIZE = 1

  # Door object ids that have a hinge on the left side.
  LEFT_SIDE_HINGE_DOOR_IDS = [1516, 1536, 1533]

  # Door object ids that have a hinge on the right side.
  RIGHT_SIDE_HINGE_DOOR_IDS = [1519, 1530, 4465, 4467, 3014, 3017, 3018, 3019]

  # A map of orientations that a door will translate to when opened.
  ORIENTATIONS = {
      # Orientations for doors that have a hinge on the left side.
      :left_side_hinge => {
          Orientation::NORTH => Orientation::WEST,
          Orientation::SOUTH => Orientation::EAST,
          Orientation::WEST => Orientation::SOUTH,
          Orientation::EAST => Orientation::NORTH
      },
      # Orientations for doors that have a hinge on the right side.
      :right_side_hinge => {
          Orientation::NORTH => Orientation::EAST,
          Orientation::SOUTH => Orientation::WEST,
          Orientation::WEST => Orientation::NORTH,
          Orientation::EAST => Orientation::SOUTH
      }
  }
end