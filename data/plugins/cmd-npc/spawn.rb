require 'java'

java_import 'org.apollo.game.model.def.NpcDefinition'
java_import 'org.apollo.game.model.Npc'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.Position'

blacklist = []

on :command, :spawn, RIGHTS_ADMIN do |player, command|
	args = command.arguments
	id = args[0].to_i
	unless [1, 3].include? args.length and id > -1
		player.send_message("Invalid syntax - ::spawn [npc id] [x] [y]")
		return
	end

	if blacklist.include? id
		player.send_message("Sorry, that npc is blacklisted!")
		return
	end

	definition = NpcDefinition.lookup(id)
	position = args.length == 1 ? player.position : Position.new(args[1].to_i, args[2].to_i, player.position.height)

	World.world.register(Npc.new(definition, position))
end


on :command, :mass, RIGHTS_ADMIN do |player, command|
	args = command.arguments
	unless args.length == 2
		player.send_message("Invalid syntax - ::spawn [npc id] [range (1-5)]")
		return
	end

	id = args[0].to_i
	range = args[1].to_i

	unless (id > -1 and (1..5).include? range)
		return player.send_message("Invalid syntax - ::spawn [npc id] [range (1-5)]")
	end

	if blacklist.include? id
		player.send_message("Sorry, that npc is blacklisted!") 
		return
	end

	center_position = player.position

	minX = center_position.x - range
	minY = center_position.y - range
	maxX = center_position.x + range
	maxY = center_position.y + range

	for x in minX..maxX do
		for y in minY..maxY do
			World.world.register(Npc.new(NpcDefinition.lookup(id), Position.new(x, y, center_position.height)))
		end
	end
	player.send_message("Mass spawning npcs with id #{id}.")
end

on :command, :clearnpcs, RIGHTS_ADMIN do |player, command|
	iterator = World.world.npc_repository.iterator
	while iterator.has_next
		World.world.unregister(iterator.next)
	end
	player.send_message("All npcs removed.")
end