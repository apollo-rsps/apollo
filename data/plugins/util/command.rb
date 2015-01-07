
# Checks whether the amount of arguments provided is correct, sending the player the specified message if not.
def valid_arg_length(args, length, player, message)
  valid = length.kind_of?(Range) ? length.include?(args.length) : length == args.length

  player.send_message(message) if !valid
  return valid
end

# Returns the name of the Object, Npc, or Item with the specified id.
def name_of(type, id)
  types = [ :object, :item, :npc ]
  unless types.include?(type)
    raise "Invalid type of #{type} specified, must be one of #{types}"
  end

  return Kernel.const_get("#{type.capitalize}Definition").lookup(id).name.to_s
end

# Add a has_keys? method to hash
class Hash
  
  def has_keys?(*keys)
    keys.each { |key| return false unless has_key?(key) }
    return true
  end
end


class Player

  # Returns whether or not the player's current level is greater than or equal to the specified level.
  def has_level(skill, level)
    return skill_set.get_skill(skill).current_level >= level
  end

end