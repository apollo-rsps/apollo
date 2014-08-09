require 'java'

java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.entity.Npc'

# An array of npcs that cannot be spawned.
blacklist = []

# Spawns a non-blacklisted npc in the specified position, or the player's position if both 'x' and 'y' are not supplied.
on :command, :spawn, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless [1, 3].include?(args.length) and (id = args[0].to_i) > -1
    player.send_message('Invalid syntax - ::spawn [npc id] [optional-x] [optional-y]')
    return
  end

  if blacklist.include?(id)
    player.send_message("Sorry, npc #{id} is blacklisted!")
    return
  end

  position = args.length == 1 ? player.position : Position.new(args[1].to_i, args[2].to_i, player.position.height)

  $world.register(Npc.new(id, position))
end

# Mass spawns npcs around the player.
on :command, :mass, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless args.length == 2 and (id = args[0].to_i) > -1 and (1..5).include?(range = args[1].to_i)
    player.send_message('Invalid syntax - ::spawn [npc id] [range (1-5)]')
    return
  end

  if blacklist.include?(id)
    player.send_message("Sorry, npc #{id} is blacklisted!")
    return
  end

  center_position = player.position

  minX = center_position.x - range
  minY = center_position.y - range
  maxX = center_position.x + range
  maxY = center_position.y + range
  z = center_position.height

  for x in minX..maxX do
    for y in minY..maxY do
      $world.register(Npc.new(id, Position.new(x, y, z)))
    end
  end
  player.send_message("Mass spawning npcs with id #{id}.")
end

# Unregisters all npcs from the world npc repository.
on :command, :clearnpcs, RIGHTS_ADMIN do |player, command|
  $world.npc_repository.each { |npc| $world.unregister(npc) }
  player.send_message('Unregistered all npcs from the world.')
end