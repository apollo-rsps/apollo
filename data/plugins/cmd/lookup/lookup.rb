require 'java'

java_import 'org.apollo.game.model.Entity'
java_import 'org.apollo.game.model.Player'
java_import 'org.apollo.game.model.World'
java_import 'org.apollo.game.model.def.ItemDefinition'
java_import 'org.apollo.game.model.def.NpcDefinition'
java_import 'org.apollo.game.model.def.ObjectDefinition'

on :command, :lookup, RIGHTS_ADMIN do |player, command|
  args = command.arguments.to_a
  unless args.length > 1
    player.send_message('Invalid syntax - ::lookup [npc/object/item] [name]')
    next
  end
  
  type = args.shift.downcase
  limit = args.first.to_i != 0 ? args.shift.to_i : 5
  name = args.join(' ').downcase

  if ['npc', 'object', 'item'].index(type) == nil
    player.send_message('Invalid syntax - ::lookup [npc/object/item] [name]') 
    next
  end
  
  ids = locate_entity(type, name, limit).join(', ')
  
  message = ids.empty? ? "Could not find an #{type} called #{name}." : "Possible ids are: #{ids}." 
  player.send_message(message)
end

# Sends the user a message with information about the item with the specified id.
on :command, :iteminfo, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless args.length == 1
    player.send_message('Invalid syntax - ::iteminfo [item id]')
    next
  end

  id = args[0].to_i
  definition = ItemDefinition.lookup(id)

  members = definition.is_members_only ? 'members' : 'not members'
  player.send_message("Item #{id} is called #{definition.name}, is #{members} only, and has a team of #{definition.team}.")
  player.send_message("Its description is \"#{definition.description}\".")
end

# Sends the user a message with information about the npc with the specified id.
on :command, :npcinfo, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless args.length == 1
    player.send_message('Invalid syntax - ::npcinfo [npc id]')
    next
  end

  id = args[0].to_i
  definition = NpcDefinition.lookup(id)

  is_combative = definition.has_combat_level ? "has a combat level of #{definition.combat_level}" : "does not have a combat level"
  player.send_message("Npc #{id} is called #{definition.name} and #{is_combative}.")
  player.send_message("Its description is \"#{definition.description}\".")
end

# Sends the user a message with information about the object with the specified id.
on :command, :objectinfo, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless args.length == 1
    player.send_message('Invalid syntax - ::objectinfo [npc id]')
    next
  end

  id = args[0].to_i
  definition = ObjectDefinition.lookup(id)
  player.send_message("Object #{id} is called #{definition.name} and its description is \"#{definition.description}\".")
end