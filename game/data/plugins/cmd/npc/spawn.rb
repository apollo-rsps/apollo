require 'java'

java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.entity.Npc'

# An array of npcs that cannot be spawned.
blacklist = []

# Spawns a non-blacklisted npc in the specified position, or the player's position if both 'x' and
# 'y' are not supplied.
on :command, :spawn, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless [1, 3, 4].include?(args.length) && (id = args[0].to_i) > -1
    player.send_message('Invalid syntax - ::spawn [npc id] [optional-x] [optional-y] [optional-z]')
    return
  end

  if blacklist.include?(id)
    player.send_message("Sorry, npc #{id} is blacklisted!")
    return
  end

  if args.length == 1
    position = player.position
  else
    height = args.length == 4 ? args[3].to_i : player.position.height
    position = Position.new(args[1].to_i, args[2].to_i, height)
  end

  $world.register(Npc.new($world, id, position))
end

# Mass spawns npcs around the player.
on :command, :mass, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless args.length == 2 && (id = args[0].to_i) > -1 && (1..5).include?(range = args[1].to_i)
    player.send_message('Invalid syntax - ::spawn [npc id] [range (1-5)]')
    return
  end

  if blacklist.include?(id)
    player.send_message("Sorry, npc #{id} is blacklisted!")
    return
  end

  center_position = player.position

  min_x = center_position.x - range
  min_y = center_position.y - range
  max_x = center_position.x + range
  max_y = center_position.y + range
  z = center_position.height

  (min_x..max_x).each do |x|
    (min_y..max_y).each do |y|
      $world.register(Npc.new($world, id, Position.new(x, y, z)))
    end
  end

  player.send_message("Mass spawning npcs with id #{id}.")
end

# Unregisters all npcs from the world npc repository.
on :command, :clearnpcs, RIGHTS_ADMIN do |player, _command|
  $world.npc_repository.each { |npc| $world.unregister(npc) }
  player.send_message('Unregistered all npcs from the world.')
end
