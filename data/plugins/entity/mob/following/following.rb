on :message, :walk do |player, msg|
  player.reset_interacting_mob
end

on :message, :player_action do |player, msg|
  ## todo: need a better way of mapping option numbers to their purpose
  player.follow($world.player_repository.get(msg.index)) if msg.option == 3
end

##
# A <code>MobExtension</code> for making a <code>Mob</code> trail behind, or chase a given mob.
module FollowingMobExtension
  ##
  # Follow a mob and trail behind them.
  def follow(mob)
    do_follow(self, mob, behind: true)
  end

  ##
  # Chase a mob (with the intention of getting in front of them), also optionally
  # stopping within a projectile distance when at a position where a projectile can
  # reach the target.
  def chase(mob, projectile_distance: nil)
    do_follow(self, mob, front: true, projectile_distance: projectile_distance)
  end
end

MobExtension::register(FollowingMobExtension)

private

def do_follow(source, mob, behind: false, front: false, projectile_distance: nil)
  source.interacting_mob = mob

  schedule 0, true do |task|
    # stop the task unless we're still interacting with the other mob.
    unless self.interacting_mob.eql? mob
      task.stop
      next
    end

    next unless source.walking_queue.size <= 1

    distance = mob.position.get_distance(source.position)

    unless projectile_distance.nil?
      next if distance <= projectile_distance &&
        $world.collision_manager.raycast(source.position, mob.position)
    end

    if distance > 15
      reset_interacting_mob
    end

    walk_to(mob, behind: behind, front: front)
  end
end

