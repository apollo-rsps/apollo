require 'java'

java_import 'org.apollo.game.model.entity.Skill'

private

# Contains potion-related constants.
module Constants

  # The id of the sound made when drinking a potion.
  DRINK_POTION_SOUND = 334

  # The id of an empty vial.
  EMPTY_VIAL_ID = 229

  # The delay between drinking potions
  POTION_DELAY = 2
end

# A drinkable potion.
class Potion < Consumable

  def initialize(id, name, doses)
    super(name, id, Constants::DRINK_POTION_SOUND, Constants::POTION_DELAY, ConsumableType::POTION)
    @doses = doses
  end

  def consume(player)
    index = @doses.find_index(id) + 1

    if index != @doses.length # Consumable removes the old potion for us, so don't do it here.
      player.inventory.add(@doses[index])
      player.send_message("You drink some of your #{name} potion.")

      remaining = "You have #{@doses.length - index} dose#{'s' unless index == 3} of potion left."
      player.send_message(remaining)
    else
      player.send_message('You drink the last of your potion.')
      player.inventory.add(Constants::EMPTY_VIAL_ID)
    end

    drink(player)
  end

  def drink(_player)
    # Override to provide functionality
  end

end

# A potion that can be consumed to boost the current skill level of a stat (or stats).
class BoostingPotion < Potion

  def initialize(id, name, doses, skills, boost)
    super(id, name, doses)
    @skill_ids = skills.is_a?(Array) ? skills : [skills]
    @boost = boost
  end

  def drink(player)
    @skill_ids.each do |id|
      skill = player.skill_set.skill(id)
      max = skill.maximum_level
      level = [skill.current_level, max].min

      new_current = @boost.call(max, level).floor
      player.skill_set.set_skill(id, Skill.new(skill.experience, new_current, max))
    end
  end

end

# Returns the parameters for the potion, as an array. Fails if any of the specified keys do not
# exist.
def get_parameters(hash, keys)
  fail "Hash must contain the following keys: #{keys.join(', ')}." unless hash.has_keys?(*keys)

  keys.map { |key| hash[key] }
end

# Appends a potion to the list of consumables.
def potion(hash)
  class_name = 'Potion'
  keys = [:name, :doses]

  unless (hash.size == 2)
    keys << :skills << :boost
    class_name.prepend('Boosting')
  end

  parameters = get_parameters(hash, keys)
  doses = hash[:doses]
  doses.each { |dose| append_consumable(Object.const_get(class_name).new(dose, *parameters)) }
end

# Some frequently-used boosts and skills
# Lambda parameters are | maximum_skill_level, current_skill_level |
basic_combat_boost = ->(_, level) { level * 1.10 + 3 }
super_combat_boost = ->(_, level) { level * 1.15 + 5 }
non_combat_boost = ->(_, level) { level + 3 }

ATTACK, STRENGTH, DEFENCE = Skill::ATTACK, Skill::STRENGTH, Skill::DEFENCE
MAGIC, RANGED, PRAYER = Skill::MAGIC, Skill::RANGED, Skill::PRAYER

all_skills = (Skill::ATTACK..Skill::RUNECRAFT).to_a
combat_skills = [ATTACK, STRENGTH, DEFENCE, MAGIC, RANGED]

# Boosting potions:
# Note that the order of the elements must be: :name, :doses, :skills, :boost.
potion name: :attack, doses: [2428, 121, 123, 125], skills: ATTACK, boost: basic_combat_boost
potion name: :strength, doses: [113, 115, 117, 119], skills: STRENGTH, boost: basic_combat_boost
potion name: :defence, doses: [2432, 133, 135, 137], skills: DEFENCE, boost: basic_combat_boost

potion name: :agility, doses: [3032, 3034, 3036, 3038], skills: Skill::AGILITY,
       boost: non_combat_boost
potion name: :fishing, doses: [2438, 151, 153, 155], skills: Skill::FISHING,
       boost: non_combat_boost
potion name: :prayer,  doses: [2434, 139, 141, 143], skills: Skill::PRAYER,
       boost: ->(_, level) { level / 4 + 7 }

potion name: :restore, doses: [2430, 127, 129, 131], skills: combat_skills,
       boost: ->(_, level) { [level * 1.3 + 10, max].min }
potion name: :super_restore, doses: [3024, 3026, 3028, 3030], skills: all_skills,
       boost: ->(_, level) { [level * 1.25 + 8, max].min }

potion name: :super_attack, doses: [2436, 145, 147, 149], skills: ATTACK, boost: super_combat_boost
potion name: :super_strength, doses: [2440, 157, 159, 161], skills: STRENGTH,
       boost: super_combat_boost
potion name: :super_defence, doses: [2442, 163, 165, 167], skills: DEFENCE,
       boost: super_combat_boost

potion name: :ranging, doses: [2444, 169, 171, 173], skills: RANGED,
       boost: ->(_, level) { level * 1.10 + 4 }

potion name: :magic, doses: [3040, 3042, 3044, 3046], skills: MAGIC,
       boost: ->(_, level) { level + 4 }
