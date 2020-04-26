require 'java'

# The hash of runes.
RUNES = {}

# Represents a rune that can be crafted.
class Rune
  attr_reader :name, :id, :level, :experience

  def initialize(id, level, experience, multiplier)
    @id = id
    @name = name_of(:item, id)
    @level = level
    @experience = experience
    @multiplier = multiplier
  end

  def equals(other)
    get_class == other.get_class && id == other.id
  end

  def multiplier(level)
    @multiplier.call(level)
  end

end

# Appends a rune to the list.
def rune(name, hash)
  unless hash.has_keys?(:altar, :id, :level, :reward)
    fail "#{name} is missing one of id, altar, level, or reward."
  end

  id, altar, level, experience = hash[:id], hash[:altar], hash[:level], hash[:reward]
  bonus = hash[:bonus] || ->(_) { 1 }

  RUNES[altar] = Rune.new(id, level, experience, bonus)
end

rune :air, altar: 2478, id: 556, level: 1, reward: 5, bonus: ->(level) { (level / 11).floor + 1 }
rune :mind, altar: 2479, id: 558, level: 1, reward: 5.5, bonus: ->(level) { (level / 14).floor + 1 }
rune :water, altar: 2480, id: 555, level: 5, reward: 6, bonus: ->(level) { (level / 19).floor + 1 }
rune :earth, altar: 2481, id: 557, level: 9, reward: 6.5,
             bonus: ->(level) { (level / 26).floor + 1 }
rune :fire, altar: 2482, id: 554, level: 14, reward: 7, bonus: ->(level) { (level / 35).floor + 1 }
rune :body, altar: 2483, id: 559, level: 20, reward: 7.5,
            bonus: ->(level) { (level / 46).floor + 1 }
rune :cosmic, altar: 2484, id: 564, level: 27, reward: 8, bonus: ->(level) { level >= 59 ? 2 : 1 }
rune :chaos, altar: 2487, id: 562, level: 35, reward: 8.5, bonus: ->(level) { level >= 74 ? 2 : 1 }
rune :nature, altar: 2486, id: 561, level: 44, reward: 9, bonus: ->(level) { level >= 91 ? 2 : 1 }
rune :law, altar: 2485, id: 563, level: 54, reward: 9.5
rune :death, altar: 2488, id: 560, level: 65, reward: 10
