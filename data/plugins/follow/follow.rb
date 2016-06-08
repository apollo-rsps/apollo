java_import 'org.apollo.game.model.entity.Npc'

class Mob

  ##
  # Follows the given mob.
  #
  # @param [Mob] mob The mob being followed.
  # @param [Integer] maximum_distance The maximum distance that can be kept between the two mobs while following.
  # @param [Boolean] simple A flag indicating whether or not this mob will use a 'simple' pathfinder.
  # @param [Boolean] projectile_clipped A flag indicating whether or not pathfinding should be clipped for projectiles.
  def follow(mob, maximum_distance=1, simple=false, projectile_clipped=false)
    FollowingModule::follow(self, mob, maximum_distance, simple, projectile_clipped)
  end
end

on :message, :walk do |player, msg|
  FollowingModule::reset(player) if player.following != -1
end

on :message, :player_action do |player, msg|
  player.follow($world.player_repository.get(msg.index))
end