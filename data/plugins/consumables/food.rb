require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.entity.Skill'
java_import 'org.apollo.game.model.entity.Player'

private

EAT_FOOD_SOUND = 317

# Represents an edible piece of food, such as bread or fish.
class Food < Consumable

  def initialize(name, id, restoration, replace)
    super(name, id, EAT_FOOD_SOUND)
    @restoration = restoration
    @replace = replace
  end

  # Restore the appropriate amount of hitpoints when consumed.
  def consume(player)
    hitpoints = player.skill_set.skill(Skill::HITPOINTS)
    new_curr = [ hitpoints.current_level + @restoration, hitpoints.maximum_level ].min

    player.inventory.add(@replace) unless (@replace == -1)

    player.send_message("You eat the #{name}.", true)
    player.send_message("It heals some health.", true) if new_curr == hitpoints

    player.skill_set.set_skill(Skill::HITPOINTS, Skill.new(hitpoints.experience, new_curr, hitpoints.maximum_level))
  end

end

# Appends a food item to the list of consumables.
def food(hash)
  raise 'Hash must contain a name, id, and a restoration value.' unless (hash.has_keys?(:name, :id, :restoration))
  name = hash[:name]; id = hash[:id]; restoration = hash[:restoration]; replace = hash[:replace] || -1; @delay = hash[:delay] || 3

  append_consumable(Food.new(name, id, restoration, replace))
end


food :name => :bread,          :id => 2309, :restoration => 4
food :name => :cooked_meat,    :id => 2142, :restoration => 3
food :name => :cooked_chicken, :id => 2140, :restoration => 3
food :name => :ugthanki_meat,  :id => 1861, :restoration => 3

food :name => :anchovies,      :id => 319,  :restoration => 1
food :name => :shrimps,        :id => 315,  :restoration => 3
food :name => :sardine,        :id => 325,  :restoration => 3
food :name => :herring,        :id => 347,  :restoration => 5
food :name => :mackeral,       :id => 355,  :restoration => 5
food :name => :trout,          :id => 333,  :restoration => 7
food :name => :cod,            :id => 339,  :restoration => 7
food :name => :pike,           :id => 351,  :restoration => 8
food :name => :salmon,         :id => 329,  :restoration => 9
food :name => :tuna,           :id => 361,  :restoration => 10
food :name => :lobster,        :id => 379,  :restoration => 12
food :name => :bass,           :id => 365,  :restoration => 13
food :name => :swordfish,      :id => 373,  :restoration => 14
food :name => :monkfish,       :id => 7946, :restoration => 16
food :name => :shark,          :id => 385,  :restoration => 20
food :name => :sea_turtle,     :id => 397,  :restoration => 21
food :name => :manta_ray,      :id => 391,  :restoration => 22

food :name => :cake,           :id => 1891, :restoration => 4, :replace => 1893
food :name => :cake,           :id => 1893, :restoration => 4, :replace => 1895
food :name => :cake,           :id => 1895, :restoration => 4

food :name => :chocolate_cake, :id => 1897, :restoration => 5, :replace => 1899
food :name => :chocolate_cake, :id => 1899, :restoration => 5, :replace => 1901
food :name => :chocolate_cake, :id => 1901, :restoration => 5

food :name => :plain_pizza,    :id => 2289, :restoration => 7, :replace => 2291
food :name => :plain_pizza,    :id => 2291, :restoration => 7

food :name => :meat_pizza,     :id => 2293, :restoration => 8, :replace => 2295
food :name => :meat_pizza,     :id => 2295, :restoration => 8

food :name => :anchovy_pizza,  :id => 2297, :restoration => 9, :replace => 2299
food :name => :anchovy_pizza,  :id => 2299, :restoration => 9

food :name => :pineapple_pizza, :id => 2301, :restoration => 11, :replace => 2303
food :name => :pineapple_pizza, :id => 2303, :restoration => 11

food :name => :redberry_pie,    :id => 2325, :restoration => 5, :replace => 2333, :delay => 1
food :name => :redberry_pie,    :id => 2333, :restoration => 5, :delay => 1

food :name => :meat_pie,        :id => 2327, :restoration => 6, :replace => 2331, :delay => 1
food :name => :meat_pie,        :id => 2331, :restoration => 6, :delay => 1

food :name => :apple_pie,       :id => 2323, :restoration => 7, :replace => 2335, :delay => 1
food :name => :apple_pie,       :id => 2335, :restoration => 7, :delay => 1
