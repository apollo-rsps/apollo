
# Checks whether the amount of arguments provided is correct, sending the player the specified message if not.
def valid_arg_length(args, length, player, message)
  valid = length.kind_of?(Range) ? length.include?(args.length) : length == args.length

  player.send_message(message) if !valid
  return valid
end

# Add a has_keys? method to hash
class Hash
  
  def has_keys?(*keys)
    keys.each { |key| return false unless has_key?(key) }
    return true
  end
end