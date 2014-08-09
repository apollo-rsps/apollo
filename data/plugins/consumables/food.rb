require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.entity.Skill'
java_import 'org.apollo.game.model.entity.Player'

EAT_FOOD_SOUND = 317

# Todo support other foods (e.g. pizza)

# Represents an edible piece of food, such as bread or fish.
class Food < Consumable

  def initialize(name, id, restoration)
    super(name, id, EAT_FOOD_SOUND)
    @restoration = restoration
  end

  # Restore the appropriate amount of hitpoints when consumed.
  def consume(player)
    hitpoints = player.skill_set.skill(HITPOINTS_SKILL_ID)
    new_curr = [ hitpoints.current_level + @restoration, hitpoints.maximum_level ].min

    player.skill_set.set_skill(HITPOINTS_SKILL_ID, Skill.new(hitpoints.experience, new_curr, hitpoints.maximum_level))
    player.send_message("You eat the #{name}.", true)
  end

end

# Appends a food item to the list of consumables.
def append_food(hash)
  raise 'Hash must contain a name, id, and a restoration value.' unless (hash.has_keys?(:name, :id, :restoration))
  name = hash[:name]; id = hash[:id]; restoration = hash[:restoration]

  append_consumable(Food.new(name, id, restoration))
end


append_food :name => :bread,          :id => 2309, :restoration => 4
append_food :name => :cooked_meat,    :id => 2142, :restoration => 3
append_food :name => :cooked_chicken, :id => 2140, :restoration => 3
append_food :name => :ugthanki_meat,  :id => 1861, :restoration => 3

append_food :name => :anchovies,      :id => 319,  :restoration => 1
append_food :name => :shrimps,        :id => 315,  :restoration => 3
append_food :name => :sardine,        :id => 325,  :restoration => 3
append_food :name => :herring,        :id => 347,  :restoration => 5
append_food :name => :mackeral,       :id => 355,  :restoration => 5
append_food :name => :trout,          :id => 333,  :restoration => 7
append_food :name => :cod,            :id => 339,  :restoration => 7
append_food :name => :pike,           :id => 351,  :restoration => 8
append_food :name => :salmon,         :id => 329,  :restoration => 9
append_food :name => :tuna,           :id => 361,  :restoration => 10
append_food :name => :lobster,        :id => 379,  :restoration => 12
append_food :name => :bass,           :id => 365,  :restoration => 13
append_food :name => :swordfish,      :id => 373,  :restoration => 14
append_food :name => :monkfish,       :id => 7946, :restoration => 16
append_food :name => :shark,          :id => 385,  :restoration => 20
append_food :name => :sea_turtle,     :id => 397,  :restoration => 21
append_food :name => :manta_ray,      :id => 391,  :restoration => 22