java_import 'org.apollo.game.model.entity.path.AStarPathfindingAlgorithm'
java_import 'org.apollo.game.model.entity.path.EuclideanHeuristic'
java_import 'org.apollo.game.model.entity.path.SimplePathfindingAlgorithm'
java_import 'org.apollo.game.model.entity.Npc'
java_import 'org.apollo.game.model.Direction'

module FollowingModule

  ##
  # An array containing the north, east, south, and west directions.
  HORIZONTAL_DIRECTIONS = [Direction::NORTH, Direction::EAST, Direction::SOUTH, Direction::WEST]

  ##
  # A pathfinder used for simple following (i.e.: An NPC attacking a player).
  SIMPLE_PATH_FINDER = SimplePathfindingAlgorithm.new($world)

  ##
  # A pathfinder used for precise following (i.e.: A player attacking another player).
  FOLLOW_PATH_FINDER = AStarPathfindingAlgorithm.new($world, EuclideanHeuristic.new)

  ##
  # Gets the minimum distance that a mob must be at while following another.
  # TODO: This function should work well for following larger entities, but it needs to be cleaned up.
  #
  # @param [Mob] mob The following mob.
  # @param [Mob] other_mob The mob that is being followed.
  def self.get_minimum_distance(mob, other_mob)

    minimum_distance = 1

    delta_x = other_mob.position.x - mob.position.x
    delta_y = other_mob.position.y - mob.position.y

    other_mob_size = other_mob.instance_of?(Npc) ? other_mob.definition.size : 1

    if other_mob_size > 1
      other_mob_size += 1 if other_mob_size == -delta_x + 1 || other_mob_size == -delta_y + 1
      minimum_distance = other_mob_size if delta_y <= 0 && delta_x <= 0
    end

    mob_size = mob.instance_of?(Npc) ? mob.definition.size : 1

    if mob_size > 1
      mob_size += 1 if mob_size == delta_x + 1 || mob_size == delta_y + 1
      minimum_distance = mob_size if delta_y >= 0 && delta_x >= 0
    end

    return minimum_distance
  end

  ##
  # Generates a path and adds the steps to the given mob's walking queue.
  #
  # @param [Mob] mob The following mob.
  # @param [Mob] other_mob The mob that is being followed.
  # @param [Integer] minimum_distance The minimum distance that must be kept between the two mobs while following.
  # @param [Integer] maximum_distance The maximum distance that can be kept between the two mobs while following.
  # @param [Boolean] simple A flag indicating whether or not the path generated will use a 'simple' pathfinder.
  def self.walk_to_position(mob, other_mob, minimum_distance, maximum_distance, end_position, simple)
    position = mob.position
    distance = position.get_distance(other_mob.position)

    pathfinder = simple ? SIMPLE_PATH_FINDER : FOLLOW_PATH_FINDER
    path = pathfinder.find(position, end_position)

    path.each do |tile|
      tile_distance = tile.get_distance(other_mob.position)
      break if tile_distance < minimum_distance

      mob.walking_queue.add_step(tile)

      break if distance > maximum_distance && tile_distance <= maximum_distance
    end
  end

  ##
  # Resets the following state for the given mob.
  #
  # @param [Mob] mob The mob.
  def self.reset(mob)
    mob.reset_interacting_mob
    mob.following = -1
  end

  ##
  # Schedules a following task.
  #
  # @param [Mob] mob The mob that is doing the following.
  # @param [Mob] other_mob The mob that is being followed.
  # @param [Integer] maximum_distance The maximum distance that can be kept between the two mobs while following.
  # @param [Boolean] simple A flag indicating whether or not this mob will use a 'simple' pathfinder.
  # @param [Boolean] projectile_clipped A flag indicating whether or not pathfinding should be clipped for projectiles.
  def self.follow(mob, other_mob, maximum_distance, simple, projectile_clipped)

    mob.following = other_mob.index
    mob.interacting_mob = other_mob

    schedule 0, true do |task|

      # if we're following a different entity, stop this task
      # TODO: Could the indices of NPCs and Players collide since they are in different instances of MobRepository..?
      if mob.following != other_mob.index
        task.stop && next
      end

      distance = other_mob.position.get_distance(mob.position)

      # check if the mob is too far away
      if distance > 15
        reset(mob) && task.stop && next
      end

      next unless mob.walking_queue.size <= 1

      # the minimum distance that we must keep between us and the mob we're following
      minimum_distance = get_minimum_distance(mob, other_mob)

      # the maximum distance that we can be while following the mob
      max_distance = maximum_distance
      max_distance = minimum_distance if minimum_distance > max_distance

      # the position that we generate a path to while following the mob
      end_position = nil

      if distance < minimum_distance
        end_position = other_mob.position.add(HORIZONTAL_DIRECTIONS.sample)

      elsif distance > max_distance
        end_position = other_mob.position
      end

      # TODO: Projectile clipping.

      walk_to_position(mob, other_mob, minimum_distance, max_distance, end_position, simple) unless end_position.nil?

    end
  end

end