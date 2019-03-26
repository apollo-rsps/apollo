require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.entity.Skill'
java_import 'org.apollo.game.model.entity.Player'

private

# The id the of the sound made when eating food.
EAT_FOOD_SOUND = 317

# Represents an edible piece of food, such as bread or fish.
# TODO: delay eating times
class Food < Consumable

  def initialize(name, id, restoration, replace, delay)
    super(name, id, EAT_FOOD_SOUND, delay, ConsumableType::FOOD)
    @restoration = restoration
    @replace = replace
  end

  # Restore the appropriate amount of hitpoints when consumed.
  def consume(player)
    hitpoints = player.skill_set.skill(Skill::HITPOINTS)
    hitpoints_current = player.skill_set.get_current_level(Skill::HITPOINTS)
    new_curr = [hitpoints.current_level + @restoration, hitpoints.maximum_level].min

    player.inventory.add(@replace) unless @replace == -1

    player.send_message("You eat the #{name}.")
    player.send_message('It heals some health.') if new_curr > hitpoints_current

    skill = Skill.new(hitpoints.experience, new_curr, hitpoints.maximum_level)
    player.skill_set.set_skill(Skill::HITPOINTS, skill)
  end

end

# The default delay before a piece of food is eaten
DEFAULT_DELAY = 3

# Appends a food item to the list of consumables.
def food(hash)
  unless hash.has_keys?(:name, :id, :restoration)
    fail 'Hash must contain a name, id, and a restoration value.'
  end

  name, id, restoration = hash[:name], hash[:id], hash[:restoration];
  replace = hash[:replace] || -1
  delay = hash[:delay] || DEFAULT_DELAY # TODO: ??

  append_consumable(Food.new(name, id, restoration, replace, delay))
end

# TODO: special effects
food name: :cooked_slimy_eel,     id: 3381, restoration: 6 # this has a chance to heal 6 to 10 hp
food name: :thin_snail_meat,      id: 3369, restoration: 5 # this has a chance to heal 5 to 7 hp
food name: :fat_snail_meat,       id: 3373, restoration: 2 # this has a chance to heal 7 to 9 hp
food name: :watermelon_slice,     id: 5984, restoration: 0 # this heals 5% of player's life
food name: :cooked_karambwan,     id: 3146, restoration: 0 # poisons player(50)
food name: :spider_on_stick,      id: 6297, restoration: 7 # heals between 7 and 10
food name: :spider_on_shaft,      id: 6299, restoration: 7 # heals between 7 and 10

# Meats/Fish
food name: :anchovies,        id: 319,  restoration: 1
food name: :crab_meat,        id: 7521, restoration: 2, replace: 7523
food name: :crab_meat,        id: 7523, restoration: 2, replace: 7524
food name: :crab_meat,        id: 7524, restoration: 2, replace: 7525
food name: :crab_meat,        id: 7525, restoration: 2, replace: 7526
food name: :crab_meat,        id: 7526, restoration: 2
food name: :shrimp,           id: 315,  restoration: 3
food name: :sardine,          id: 325,  restoration: 3
food name: :cooked_meat,      id: 2142, restoration: 3
food name: :cooked_chicken,   id: 2140, restoration: 3
food name: :ugthanki_meat,    id: 1861, restoration: 3
food name: :karambwanji,      id: 3151, restoration: 3
food name: :cooked_rabbit,    id: 3228, restoration: 5
food name: :herring,          id: 347,  restoration: 6
food name: :trout,            id: 333,  restoration: 7
food name: :cod,              id: 339,  restoration: 7
food name: :mackeral,         id: 355,  restoration: 7
food name: :roast_rabbit,     id: 7223, restoration: 7
food name: :pike,             id: 351,  restoration: 8
food name: :lean_snail_meat,  id: 3371, restoration: 8
food name: :salmon,           id: 329,  restoration: 9
food name: :tuna,             id: 361,  restoration: 10
food name: :lobster,          id: 379,  restoration: 12
food name: :bass,             id: 365,  restoration: 13
food name: :swordfish,        id: 373,  restoration: 14
food name: :cooked_jubbly,    id: 7568, restoration: 15
food name: :monkfish,         id: 7946, restoration: 16
food name: :cooked_karambwan, id: 3144, restoration: 18, delay: 0
food name: :shark,            id: 385,  restoration: 20
food name: :sea_turtle,       id: 397,  restoration: 21
food name: :manta_ray,        id: 391,  restoration: 22

# Breads/Wraps
food name: :bread,            id: 2309, restoration: 5
food name: :oomlie_wrap,      id: 2343, restoration: 14
food name: :ugthanki_kebab,   id: 1883, restoration: 19

# Fruits
food name: :banana,           id: 1963, restoration: 2
food name: :sliced_banana,    id: 3162, restoration: 2
food name: :lemon,            id: 2102, restoration: 2
food name: :lemon_chunks,     id: 2104, restoration: 2
food name: :lemon_slices,     id: 2106, restoration: 2
food name: :lime,             id: 2120, restoration: 2
food name: :lime_chunks,      id: 2122, restoration: 2
food name: :lime_slices,      id: 2124, restoration: 2
food name: :strawberry,       id: 5504, restoration: 5
food name: :papaya_fruit,     id: 5972, restoration: 8
food name: :pineapple_chunks, id: 2116, restoration: 2
food name: :pineapple_ring,   id: 2118, restoration: 2
food name: :orange,           id: 2108, restoration: 2
food name: :orange_rings,     id: 2110, restoration: 2
food name: :orange_slices,    id: 2112, restoration: 2

# Pies
# TODO: pie special effects (e.g. fish pie raises fishing level)
food name: :redberry_pie, id: 2325, restoration: 5, replace: 2333, delay: 1
food name: :redberry_pie, id: 2333, restoration: 5, delay: 1

food name: :meat_pie,     id: 2327, restoration: 6, replace: 2331, delay: 1
food name: :meat_pie,     id: 2331, restoration: 6, delay: 1

food name: :apple_pie,    id: 2323, restoration: 7, replace: 2335, delay: 1
food name: :apple_pie,    id: 2335, restoration: 7, delay: 1

food name: :fish_pie,     id: 7188, restoration: 6, replace: 7190, delay: 1
food name: :fish_pie,     id: 7190, restoration: 6, delay: 1

food name: :admiral_pie,  id: 7198, restoration: 8, replace: 7200, delay: 1
food name: :admiral_pie,  id: 7200, restoration: 8, delay: 1

food name: :wild_pie,     id: 7208, restoration: 11, replace: 7210, delay: 1
food name: :wild_pie,     id: 7210, restoration: 11, delay: 1

food name: :summer_pie,   id: 7218, restoration: 11, replace: 7220, delay: 1
food name: :summer_pie,   id: 7220, restoration: 11, delay: 1

# Stews
food name: :stew,        id: 2003, restoration: 11
food name: :banana_stew, id: 4016, restoration: 11
food name: :curry,       id: 2011, restoration: 19

# Pizzas
food name: :plain_pizza,     id: 2289, restoration: 7, replace: 2291
food name: :plain_pizza,     id: 2291, restoration: 7

food name: :meat_pizza,      id: 2293, restoration: 8, replace: 2295
food name: :meat_pizza,      id: 2295, restoration: 8

food name: :anchovy_pizza,   id: 2297, restoration: 9, replace: 2299
food name: :anchovy_pizza,   id: 2299, restoration: 9

food name: :pineapple_pizza, id: 2301, restoration: 11, replace: 2303
food name: :pineapple_pizza, id: 2303, restoration: 11

# Cakes
food name: :fishcake,       id: 7530, restoration: 11

food name: :cake, id: 1891, restoration: 4, replace: 1893
food name: :cake, id: 1893, restoration: 4, replace: 1895
food name: :cake, id: 1895, restoration: 4

food name: :chocolate_cake, id: 1897, restoration: 5, replace: 1899
food name: :chocolate_cake, id: 1899, restoration: 5, replace: 1901
food name: :chocolate_cake, id: 1901, restoration: 5

# Vegetables
food name: :potato,             id: 1942, restoration: 1
food name: :spinach_roll,       id: 1969, restoration: 2
food name: :baked_potato,       id: 6701, restoration: 4
food name: :sweetcorn,          id: 5988, restoration: 10
food name: :sweetcorn_bowl,     id: 7088, restoration: 13
food name: :potato_with_butter, id: 6703, restoration: 14
food name: :chili_potato,       id: 7054, restoration: 14
food name: :potato_with_cheese, id: 6705, restoration: 16
food name: :egg_potato,         id: 7056, restoration: 16
food name: :mushroom_potato,    id: 7058, restoration: 20
food name: :tuna_potato,        id: 7060, restoration: 22

# Dairy
food name: :cheese,       id: 1985, restoration: 2
food name: :pot_of_cream, id: 2130, restoration: 1

# Gnome Food
food name: :toads_legs,   id: 2152, restoration: 3

# Gnome Bowls
food name: :worm_hole,          id: 2191, restoration: 12
food name: :worm_hole,          id: 2233, restoration: 12
food name: :vegetable_ball,     id: 2195, restoration: 12
food name: :vegetable_ball,     id: 2235, restoration: 12
food name: :tangled_toads_legs, id: 2187, restoration: 15
food name: :tangled_toads_legs, id: 2231, restoration: 15
food name: :chocolate_bomb,     id: 2185, restoration: 15
food name: :chocolate_bomb,     id: 2229, restoration: 15

# Gnome Crunchies
food name: :toad_crunchies,     id: 2217, restoration: 7
food name: :toad_crunchies,     id: 2243, restoration: 7
food name: :spicy_crunchies,    id: 2213, restoration: 7
food name: :spicy_crunchies,    id: 2241, restoration: 7
food name: :worm_crunchies,     id: 2205, restoration: 8
food name: :worm_crunchies,     id: 2237, restoration: 8
food name: :chocchip_crunchies, id: 2209, restoration: 7
food name: :chocchip_crunchies, id: 2239, restoration: 7

# Gnome Battas
food name: :fruit_batta,      id: 2225, restoration: 11
food name: :fruit_batta,      id: 2277, restoration: 11
food name: :toad_batta,       id: 2221, restoration: 11
food name: :toad_batta,       id: 2255, restoration: 11
food name: :worm_batta,       id: 2219, restoration: 11
food name: :worm_batta,       id: 2253, restoration: 11
food name: :vegetable_batta,  id: 2227, restoration: 11
food name: :vegetable_batta,  id: 2281, restoration: 11
food name: :cheese_tom_batta, id: 2223, restoration: 11
food name: :cheese_tom_batta, id: 2259, restoration: 11
