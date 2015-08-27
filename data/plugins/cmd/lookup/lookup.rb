require 'java'

java_import 'org.apollo.game.model.World'
java_import 'org.apollo.cache.def.ItemDefinition'
java_import 'org.apollo.cache.def.NpcDefinition'
java_import 'org.apollo.cache.def.ObjectDefinition'
java_import 'org.apollo.game.model.entity.Entity'
java_import 'org.apollo.game.model.entity.Player'

on :command, :lookup, RIGHTS_ADMIN do |player, command|
  args = command.arguments.to_a
  message = 'Invalid syntax - ::lookup [npc/object/item] [name]'
  next unless valid_arg_length(args, (1..10), player, message)

  type = args.shift.downcase
  limit = args.first.to_i == 0 ? 5 : args.shift.to_i
  name = args.join(' ').downcase

  if %w(npc object item).index(type).nil?
    player.send_message('Invalid syntax - ::lookup [npc/object/item] [name]')
    next
  end

  ids = find_entities(type, name, limit).join(', ')

  message = ids.empty? ? "Could not find an #{type} called #{name}." :
                         "Possible ids for \"#{name}\" are: #{ids}."
  player.send_message(message)
end

# Sends the user a message with information about the item with the specified id.
on :command, :iteminfo, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  next unless valid_arg_length(args, 1, player, 'Invalid syntax - ::iteminfo [item id]')

  id = args[0].to_i
  definition = ItemDefinition.lookup(id)
  members = definition.is_members_only ? 'members' : 'not members'

  player.send_message("Item #{id} is called #{definition.name}, is #{members} only, and has a "\
                      "team of #{definition.team}.")
  player.send_message("Its description is \"#{definition.description}\".")
end

# Sends the user a message with information about the npc with the specified id.
on :command, :npcinfo, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  next unless valid_arg_length(args, 1, player, 'Invalid syntax - ::npcinfo [npc id]')

  id = args[0].to_i
  definition = NpcDefinition.lookup(id)
  is_combative = definition.has_combat_level ? "has a combat level of #{definition.combat_level}" :
                 'does not have a combat level'

  player.send_message("Npc #{id} is called #{definition.name} and #{is_combative}.")
  player.send_message("Its description is \"#{definition.description}\".")
end

# Sends the user a message with information about the object with the specified id.
on :command, :objectinfo, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  next unless valid_arg_length(args, 1, player, 'Invalid syntax - ::objectinfo [object id]')

  id = args[0].to_i
  definition = ObjectDefinition.lookup(id)
  player.send_message("Object #{id} is called #{definition.name} and its description is "\
                      "\"#{definition.description}\".")
  player.send_message("Its width is #{definition.width} and its length is #{definition.length}.")
end
