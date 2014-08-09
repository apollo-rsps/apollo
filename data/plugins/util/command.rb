
# Checks whether the amount of arguments provided is correct, sending the player the specified message if not.
def valid_arg_length(args, length, player, message)
  valid = length.kind_of?(Range) ? length.include?(args.length) : length == args.length

  if (!valid)
    player.send_message(message)
    return false
  end
  return true
end