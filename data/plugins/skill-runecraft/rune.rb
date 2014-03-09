require 'java'

java_import 'org.apollo.game.model.def.ItemDefinition'

# The list of runes.
RUNES = {}

# Represents a rune that can be crafted.
class Rune
  attr_reader :name, :id, :level, :experience
  
  def initialize(id, level, experience, multiplier)
    @id = id
    @name = ItemDefinition.lookup(id).name.downcase
    @level = level
    @experience = experience
    @multiplier = multiplier
  end

  def multiplier(level)
    return @multiplier.call(level)
  end

  def equals(other)
    return (get_class == other.get_class && id == other.id)
  end
end

# Appends a rune to the list.
def append_rune(hash)
  raise 'Hash must contain an id, experience, .' unless hash.has_key?(:id) && hash.has_key?(:level) && hash.has_key?(:experience) && hash.has_key?(:multiplier)
  id = hash[:id]; altar = hash[:altar]; level = hash[:level]; experience = hash[:experience]; multiplier = hash[:multiplier]
  
  RUNES[altar] = Rune.new(id, level, experience, multiplier)
end

append_rune :name => :air_rune,    :altar => 2478, :id => 556, :level => 1,  :experience =>    5, :multiplier => lambda { |level| (level / 11).floor + 1 }
append_rune :name => :mind_rune,   :altar => 2479, :id => 558, :level => 1,  :experience =>  5.5, :multiplier => lambda { |level| (level / 14).floor + 1 }
append_rune :name => :water_rune,  :altar => 2480, :id => 555, :level => 5,  :experience =>    6, :multiplier => lambda { |level| (level / 19).floor + 1 }
append_rune :name => :earth_rune,  :altar => 2481, :id => 557, :level => 9,  :experience =>  6.5, :multiplier => lambda { |level| (level / 26).floor + 1 }
append_rune :name => :fire_rune,   :altar => 2482, :id => 554, :level => 14, :experience =>    7, :multiplier => lambda { |level| (level / 35).floor + 1 }
append_rune :name => :body_rune,   :altar => 2483, :id => 559, :level => 20, :experience =>  7.5, :multiplier => lambda { |level| (level / 46).floor + 1 }
append_rune :name => :cosmic_rune, :altar => 2484, :id => 564, :level => 27, :experience =>    8, :multiplier => lambda { |level| level >= 59 ? 2 : 1    }
append_rune :name => :chaos_rune,  :altar => 2487, :id => 562, :level => 35, :experience =>  8.5, :multiplier => lambda { |level| level >= 74 ? 2 : 1    }
append_rune :name => :nature_rune, :altar => 2486, :id => 561, :level => 44, :experience =>    9, :multiplier => lambda { |level| level >= 91 ? 2 : 1    }
append_rune :name => :law_rune,    :altar => 2485, :id => 563, :level => 54, :experience =>  9.5, :multiplier => lambda { |level| 1                      }
append_rune :name => :death_rune,  :altar => 2488, :id => 560, :level => 65, :experience =>   10, :multiplier => lambda { |level| 1                      }