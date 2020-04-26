java_import 'org.apollo.game.model.entity.path.AStarPathfindingAlgorithm'
java_import 'org.apollo.game.model.entity.path.EuclideanHeuristic'
java_import 'org.apollo.game.model.entity.path.SimplePathfindingAlgorithm'
java_import 'org.apollo.game.model.entity.obj.GameObject'
java_import 'org.apollo.game.model.entity.Mob'
java_import 'org.apollo.game.model.entity.Player'
java_import 'org.apollo.game.model.entity.EntityType'
java_import 'org.apollo.game.model.Direction'

##
# A pathfinder used for simple following (i.e.: An NPC attacking a player).
SIMPLE_PATH_FINDER = SimplePathfindingAlgorithm.new($world.collision_manager)

##
# A pathfinder used for precise following (i.e.: A player attacking another player).
FOLLOW_PATH_FINDER = AStarPathfindingAlgorithm.new($world.collision_manager, EuclideanHeuristic.new)

##
# The directions that we use as a random offset when not walking to the facing direction.
OFFSET_DIRECTIONS = Direction::NESW.to_a

module WalkToMobExtension
  def walk_to(entity, front: false, behind: false)
    if [front, behind].count { |option| option == true } > 1
      fail 'Can only specify one of "front" or "behind"'
    end

    position = self.position
    target_position = entity.position
    target_distance = position.get_distance(target_position)
    target_offset = walk_to_offset(entity)
    target_offset_direction = OFFSET_DIRECTIONS.sample
    target_facing_direction = walk_to_facing_direction(entity)

    unless target_facing_direction.nil?
      if front
        target_offset_direction = target_facing_direction
      elsif behind
        target_offset_direction = target_facing_direction.opposite
      end
    end

    target_offset_position = target_position.step(target_offset, target_offset_direction)
    return if target_offset_position.eql?(position)

    pathfinder = FOLLOW_PATH_FINDER
    path = pathfinder.find(position, target_offset_position)
    path.each { |tile| self.walking_queue.add_step(tile) }
  end
end

MobExtension::register(WalkToMobExtension)

private

##
# Gets the number of tiles away from an entity's actual origin that we can
# walk to.
def walk_to_offset(entity)
  case entity.entity_type
    when EntityType::DYNAMIC_OBJECT, EntityType::STATIC_OBJECT
      return max(entity.definition.width, entity.definition.length) + 1
    when EntityType::NPC
      return entity.definition.size + 1
    when EntityType::PLAYER
      return 1
    else
      fail "walk_to_offset called with invalid entity type: #{type.to_s}"
  end
end

##
# Gets the direction that an entity is facing, so a mob can walk up infront of them.
def walk_to_facing_direction(entity)
  case entity.entity_type
    when EntityType::DYNAMIC_OBJECT, EntityType::STATIC_OBJECT
      return Direction::WNES[entity.orientation]
    when EntityType::NPC, EntityType::PLAYER
      return entity.last_direction
    else
      fail "walk_to_offset called with invalid entity type: #{type.to_s}"
  end
end