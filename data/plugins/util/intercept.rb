# Essentially a wrapper for specific message types to make them easier to use. Only supports the interception of a message type once (for good reason - this is supposed to
# be a utility, not a chain of interceptors inside the existing chain).
#
# Plugins that wish to expand the list of available message types (you probably don't) should use the add_interception method - see the item-on-item script for example usage.
# 
# If you only wish to intercept a message, use the intercept method, e.g.
#
# intercept :item_on_item, used_id, target_id, :irreversible do |ctx, player, message|
#   # code here
# end


# Calls the registered interception(s), if applicable.
def intercept(message, *args, &block)
  raise 'Error - interceptions must provide a block.' unless block_given?
  interception = INTERCEPTIONS[message]

  if interception == nil then raise "No interception for message #{message}" else interception.call(*args, block) end
end

# Adds an interception.
def add_interception(message, &block)
  INTERCEPTIONS[message] = block
end

private
INTERCEPTIONS = {}