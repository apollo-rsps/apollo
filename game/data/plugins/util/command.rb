require 'java'

java_import 'org.apollo.cache.def.ItemDefinition'
java_import 'org.apollo.cache.def.NpcDefinition'
java_import 'org.apollo.cache.def.ObjectDefinition'

# Checks whether the amount of arguments provided is correct, sending the player the specified
# message if not.
def valid_arg_length(args, length, player, message)
  valid = length.is_a?(Range) ? length.include?(args.length) : length == args.length

  player.send_message(message) unless valid
  valid
end

# Returns the name of the Object, Npc, or Item with the specified id.
def name_of(type, id)
  types = [:object, :item, :npc]
  unless types.include?(type)
    fail "Invalid type of #{type} specified, must be one of #{types}"
  end

  Kernel.const_get("#{type.capitalize}Definition").lookup(id).name.to_s
end

# Monkey-patches Hash to add a has_keys? method.
class Hash

  def has_keys?(*keys)
    keys.all? { |key| self.key?(key) }
  end

end

# Monkey-patches Player to add a level? method.
class Player

  # Returns whether or not the player's current level is greater than or equal to the specified
  # level.
  def level?(skill, level)
    skill_set.get_skill(skill).current_level >= level
  end

end
