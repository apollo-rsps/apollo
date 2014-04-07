require 'java'

java_import 'org.apollo.game.model.def.ItemDefinition'

# Adds the specified item to the player's own inventory.
on :command, :item, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  unless (1..2).include? args.length
    player.send_message('Invalid syntax - ::item [id] [amount]')
    next
  end
 
  id = args[0].to_i
  amount = args.length == 2 ? args[1].to_i : 1
  if (id < 0 || id >= ItemDefinition.count)
    player.send_message('The item id you specified is out of bounds!')
    next
  end

  player.inventory.add(id, amount)
end

# Removes the specified item from the player's own inventory.
on :command, :remove, RIGHTS_MOD do |player, command|
  args = command.arguments
  unless (1..2).include? args.length
    player.send_message('Invalid syntax - ::remove [id] [amount]')
    next
  end

  id = args[0].to_i
  amount = args.length == 2 ? args[1].to_i : 1
  if (id < 0 || id >= ItemDefinition.count)
    player.send_message('The item id you specified is out of bounds!')
    next
  end

  player.inventory.remove(id, amount)
end

# Clears the player's own inventory.
on :command, :empty, RIGHTS_MOD do |player, command|
  player.inventory.clear
end

# Gives the player one thousand of each rune.
on :command, :runes, RIGHTS_ADMIN do |player, command|
	(554..566).each do |i|
		player.inventory.add(i, 1000)
	end
end