# Thanks to Sillhouette <http://www.rune-server.org/members/silhouette> for posting
# a large amount of Herblore skill data which has been thankfully used in this plugin.

require 'java'

java_import 'org.apollo.game.message.impl.SetWidgetItemModelMessage'
java_import 'org.apollo.game.model.entity.Skill'

HERBLORE_DIALOGUE = 4429

HERBLORE_ITEM = {}
HERBLORE_ITEM_ON_ITEM = {}

DRINK_ITEM = {}

# A module which describes an invocable method of the Herblore skill.
module HerbloreMethod
  def self.new
    fail 'You cannot instantiate this module!'
  end

  def invoke(_player, _primary, _secondary)
    fail 'You must implement the invocation of HerbloreMethod!'
  end
end

# The ItemOnItemMessage listener for all Herblore-related functions.
on :message, :item_on_item do |player, message|
  primary = message.id
  secondary = message.target_id
  hash = HERBLORE_ITEM_ON_ITEM[primary]

  if hash.nil?
    secondary = message.id
    primary = message.target_id
    hash = HERBLORE_ITEM_ON_ITEM[primary]
  end

  unless hash.nil?
    method = hash[secondary]
    unless method.nil?
      method.invoke(player, primary, secondary)
      message.terminate
    end
  end
end

# The ItemOptionMessage listener for all Herblore-related functions.
on :message, :first_item_option do |player, message|
  id = message.id
  method = HERBLORE_ITEM[id]

  unless method.nil?
    method.invoke(player, id, message.slot)
    message.terminate
  end
  method = DRINK_ITEM[id]

  unless method.nil?
    method.invoke(player, id, message.slot)
    message.terminate
  end
end

# Utility for adding the various Herblore methods to the handled constant arrays.
def append_herblore_item(method, key, secondary = -1)
  if secondary == -1
    HERBLORE_ITEM[key] = method
  else
    hash = HERBLORE_ITEM_ON_ITEM[key]
    hash = {} if hash.nil?

    hash[secondary] = method
    HERBLORE_ITEM_ON_ITEM[key] = hash
  end
end

# Utility method for checking if a player's inventory has a of the specified id, with optionally
# the specified amount (1 by default), at the specified slot.
def check_slot(player, slot, id, amount = 1)
  item = player.inventory.get(slot)
  !item.nil? && item.id == id && item.amount >= amount
end

# Utility method for checking if a player's Herblore (maximum) level is at the required level. Also
# informs the player if this is not the case with use of the action variable, like so:
# "You need a Herblore level of at least #{required.to_s} to #{action}."
def check_skill(player, required, action)
  if required > player.skill_set.get_skill(Skill::HERBLORE).current_level
    player.send_message("You need a Herblore level of at least #{required} to #{action}.")
    return false
  end

  true
end

# Opens a 'make' dialogue for the specified player, displaying the specified item. Optionally, a
# listener can be used for the dialogue.
def open_dialogue(player, item, listener = nil)
  player.send(SetWidgetItemModelMessage.new(1746, item, 170))
  player.interface_set.open_dialogue(listener, HERBLORE_DIALOGUE)
end
