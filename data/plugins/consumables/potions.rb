require 'java'

java_import 'org.apollo.game.model.entity.Skill'

DRINK_POTION_SOUND = 334

# A drinkable potion.
class Potion < Consumable

  def initialize(id, name, doses)
    super(name, id, DRINK_POTION_SOUND)
    @doses = doses
  end

  def consume(player)
    index = @doses.find_index(id) + 1

    unless index == @doses.length
      player.inventory.add(@doses[index])
      player.send_message("You drink some of your #{name} potion.", true)      
      player.send_message("You have #{ @doses.length - index } dose#{ "s" unless index == 3 } of potion left.", true)
    else
      player.send_message('You drink the last of your potion.')
      player.inventory.add(EMPTY_VIAL_ID)
    end

    drink(player)
  end

  def drink(player)
    # Override to provide functionality
  end

end

# A potion that can be consumed to boost the current skill level of a stat (or stats).
class BoostingPotion < Potion

  def initialize(id, name, doses, skills, boost)
    super(id, name, doses)
    @skill_ids = skills.kind_of?(Array) ? skills : [ skills ]
    @boost = boost
  end

  def drink(player)
    @skill_ids.each do |id|
      skill = player.skill_set.skill(id); max = skill.maximum_level
      level = [ skill.current_level, max ].min

      new_current = @boost.call(max, level).floor
      player.skill_set.set_skill(id, Skill.new(skill.experience, new_current, max))
    end
  end

end

# Returns the parameters for the potion, as an array. Raises if any of the specified keys do not exist
def get_parameters(hash, keys)
  raise "Hash must contain the following keys: #{ keys.join(", ") }." unless hash.has_keys?(*keys)

  parameters = []
  keys.each { |key| parameters << hash[key] }
  
  return parameters
end

# Appends a potion to the list of consumables.
def append_potion(hash)
  class_name = 'Potion'; keys = [ :name, :doses ]

  unless (hash.size == 2)
    keys << :skills << :boost
    class_name.prepend('Boosting')
  end

  parameters = get_parameters(hash, keys)

  hash[:doses].each do |dose_id|
    append_consumable(Object.const_get(class_name).new(dose_id, *parameters))
  end
end


# Some frequently-used boosts and skills
# Lambda parameters are | maximum_skill_level, current_skill_level |
basic_combat_boost = lambda { |max, level| level * 1.08 + 1 }
super_combat_boost = lambda { |max, level| level * 1.12 + 2 }
non_combat_boost   = lambda { |max, level| level + 3        }

all_skills = (ATTACK_SKILL_ID..RUNECRAFT_SKILL_ID).to_a
combat_skills = [ ATTACK_SKILL_ID, STRENGTH_SKILL_ID, DEFENCE_SKILL_ID, MAGIC_SKILL_ID, RANGED_SKILL_ID ]


# Boosting potions:
# Note that the order of the elements must be: :name, :doses, :skills, :boost.
append_potion :name => :attack,   :doses => [ 2428, 121,  123,  125  ], :skills => ATTACK_SKILL_ID,   :boost => basic_combat_boost
append_potion :name => :strength, :doses => [ 113,  115,  117,  119  ], :skills => STRENGTH_SKILL_ID, :boost => basic_combat_boost
append_potion :name => :defence,  :doses => [ 2432, 133,  135,  137  ], :skills => DEFENCE_SKILL_ID,  :boost => basic_combat_boost

append_potion :name => :agility, :doses => [ 3032, 3034, 3036, 3038 ], :skills => AGILITY_SKILL_ID,  :boost => non_combat_boost
append_potion :name => :fishing, :doses => [ 2438, 151,  153,  155  ], :skills => FISHING_SKILL_ID,  :boost => non_combat_boost
append_potion :name => :prayer,  :doses => [ 2434, 139,  141,  143  ], :skills => PRAYER_SKILL_ID,   :boost => lambda { |max, level| level / 4 + 7 }

append_potion :name => :restore,       :doses => [ 2430, 127,  129,  131  ], :skills => combat_skills, :boost => lambda { |max, level| [ level * 1.3 + 10, max ].min }
append_potion :name => :super_restore, :doses => [ 3024, 3026, 3028, 3030 ], :skills => all_skills,    :boost => lambda { |max, level| [ level * 1.25 + 8, max ].min }

append_potion :name => :super_attack,   :doses => [ 2436, 145,  147,  149  ], :skills => ATTACK_SKILL_ID,   :boost => super_combat_boost
append_potion :name => :super_strength, :doses => [ 2440, 115,  117,  119  ], :skills => STRENGTH_SKILL_ID, :boost => super_combat_boost
append_potion :name => :super_defence,  :doses => [ 2442, 133,  135,  137  ], :skills => DEFENCE_SKILL_ID,  :boost => super_combat_boost
append_potion :name => :ranging,        :doses => [ 2444, 169,  171,  173  ], :skills => RANGED_SKILL_ID,   :boost => super_combat_boost
append_potion :name => :magic,          :doses => [ 3040, 3042, 3044, 3046 ], :skills => MAGIC_SKILL_ID,    :boost => super_combat_boost