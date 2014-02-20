require 'java'

# Adds the specified item to the player's own inventory.
on :command, :item, RIGHTS_ADMIN do |player, command|
  args = command.arguments
  if (1..2).include? args.length
    id = args[0].to_i
    amount = args.length == 2 ? args[1].to_i : 1
    if (id < 0 || id >= ItemDefinition.count)
      player.send_message('The item id you specified is out of bounds!')
      next
    end

    player.inventory.add(id, amount)
  else
    player.send_message('Syntax: ::item [id] [amount=1]')
  end
end

# Removes the specified item from the player's own inventory.
on :command, :remove, RIGHTS_MOD do |player, command|
  args = command.arguments
  if (1..2).include? args.length
    id = args[0].to_i
    amount = args.length == 2 ? args[1].to_i : 1

    player.inventory.remove(id, amount)
  else
    player.send_message('Syntax: ::remove [id] [amount=1]')
  end
end

# Clears the player's own inventory.
on :command, :empty, RIGHTS_MOD do |player, command|
  player.inventory.clear
end