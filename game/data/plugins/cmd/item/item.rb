require 'java'

java_import 'org.apollo.cache.def.ItemDefinition'

# Adds the specified item to the player's inventory.
on :command, :item, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  next unless valid_arg_length(args, (1..2), player, 'Invalid syntax - ::item [id] [amount]')

  id = args[0].to_i
  amount = args.length == 2 ? args[1].to_i : 1

  if id < 0 || id >= ItemDefinition.count
    player.send_message('The item id you specified is out of bounds!')
    next
  end

  player.inventory.add(id, amount)
end

# Removes the specified item from the player's inventory.
on :command, :remove, RIGHTS_MOD do |player, command|
  args = command.arguments
  next unless valid_arg_length(args, (1..2), player, 'Invalid syntax - ::remove [id] [amount]')

  id = args[0].to_i
  amount = args.length == 2 ? args[1].to_i : 1

  if id < 0 || id >= ItemDefinition.count
    player.send_message('The item id you specified is out of bounds!')
    next
  end

  player.inventory.remove(id, amount)
end

# Clears the player's inventory.
on :command, :empty, RIGHTS_MOD do |player, _command|
  player.inventory.clear
end

# Gives the player 1,000 of each rune.
on :command, :runes, RIGHTS_ADMIN do |player, _command|
  (554..566).each { |item| player.inventory.add(item, 1000) }
end
