require 'set'

# Contains door-related constants.
module DoorConstants

  # TODO: GameObjectOrientation enumeration in Apollo's core?
  # The orientation of a door.
  module Orientation
    WEST = 0
    NORTH = 1
    EAST = 2
    SOUTH = 3
  end

  # The size of a door object.
  DOOR_SIZE = 1

  # Door object ids that have a hinge on the left side.
  LEFT_HINGE_DOORS = Set.new [1516, 1536, 1533]

  # Door object ids that have a hinge on the right side.
  RIGHT_HINGE_DOORS = Set.new [1519, 1530, 4465, 4467, 3014, 3017, 3018, 3019]

  # The hash of orientations that a door will translate to when opened.
  ORIENTATIONS = {

    # Orientations for doors that have a hinge on the left side.
    left_side_hinge: {
      Orientation::NORTH => Orientation::WEST,
      Orientation::SOUTH => Orientation::EAST,
      Orientation::WEST => Orientation::SOUTH,
      Orientation::EAST => Orientation::NORTH
    },

    # Orientations for doors that have a hinge on the right side.
    right_side_hinge: {
      Orientation::NORTH => Orientation::EAST,
      Orientation::SOUTH => Orientation::WEST,
      Orientation::WEST => Orientation::NORTH,
      Orientation::EAST => Orientation::SOUTH
    }
  }

end
