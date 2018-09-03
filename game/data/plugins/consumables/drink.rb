require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.entity.Skill'
java_import 'org.apollo.game.model.entity.Player'

private

# The id the of the sound made when drinking something.
#TODO change sound if this id is incorrect
DRINK_SOUND = 334

# Represents something drinkable, such as a jug of wine or a nettle tea.
class Drink < Consumable

  def initialize(name, id, restoration, replace, delay)
    super(name, id, DRINK_SOUND, delay, ConsumableType::DRINK)
    @restoration = restoration
    @replace = replace
  end

  # Restore the appropriate amount of hitpoints when consumed.
  def consume(player)
    hitpoints = player.skill_set.skill(Skill::HITPOINTS)
    hitpoints_current = player.skill_set.get_current_level(Skill::HITPOINTS)
    new_curr = [hitpoints.current_level + @restoration, hitpoints.maximum_level].min

    player.inventory.add(@replace) unless @replace == -1

    player.send_message("You drink the #{name}.")
    player.send_message('It heals some health.') if new_curr > hitpoints_current

    skill = Skill.new(hitpoints.experience, new_curr, hitpoints.maximum_level)
    player.skill_set.set_skill(Skill::HITPOINTS, skill)
  end

end

# The default delay before the consumable is drunk.
DEFAULT_DELAY = 3

# Appends a drink item to the list of consumables.
def drink(hash)
  unless hash.has_keys?(:name, :id, :restoration)
    fail 'Hash must contain a name, id, and a restoration value.'
  end

  name, id, restoration = hash[:name], hash[:id], hash[:restoration];
  replace = hash[:replace] || -1
  delay = hash[:delay] || DEFAULT_DELAY # TODO: ??

  append_consumable(Drink.new(name, id, restoration, replace, delay))
end

# Wine
drink name: :jug_of_wine, id: 1993, restoration: 11

# Hot Drinks
drink name: :nettle_tea, id: 4239, restoration: 3
drink name: :nettle_tea, id: 4240, restoration: 3

# Gnome Cocktails
drink name: :fruit_blast,        id: 2034, restoration: 9
drink name: :fruit_blast,        id: 2084, restoration: 9
drink name: :pineapple_punch,    id: 2036, restoration: 9
drink name: :pineapple_punch,    id: 2048, restoration: 9
drink name: :wizard_blizzard,    id: 2040, restoration: 5 # -4 attack, +5 strength also
drink name: :wizard_blizzard,    id: 2054, restoration: 5 # -4 attack, +5 strength also
drink name: :short_green_guy,    id: 2038, restoration: 5 # -4 attack, +5 strength also
drink name: :short_green_guy,    id: 2080, restoration: 5 # -4 attack, +5 strength also
drink name: :drunk_dragon,       id: 2032, restoration: 5 # -4 attack, +6 strength also
drink name: :drunk_dragon,       id: 2092, restoration: 5 # -4 attack, +6 strength also
drink name: :chocolate_saturday, id: 2030, restoration: 7 # -4 attack, +6 strength also
drink name: :chocolate_saturday, id: 2074, restoration: 7 # -4 attack, +6 strength also
drink name: :blurberry_special,  id: 2028, restoration: 7 # -4 attack, +6 strength also
drink name: :blurberry_special,  id: 2064, restoration: 7 # -4 attack, +6 strength also
