
# The hash of names to fish.
CATCHABLE_FISH = {}

# A fish that can be caught.
class Fish
  attr_reader :id, :level, :experience, :name

  # Creates the Fish.
  def initialize(id, level, experience)
    @id = id
    @level = level
    @experience = experience
    @name = name_of(:item, id)
  end

end

# Appends a Fish to the hash.
def append_fish(name, hash)
  unless hash.has_keys?(:id, :level, :experience)
    fail 'Hash must contain an id, level, and experience.'
  end

  CATCHABLE_FISH[name] = Fish.new(hash[:id], hash[:level], hash[:experience])
end

append_fish :shrimp,    id: 317, level: 1,  experience: 10
append_fish :sardine,   id: 327, level: 5,  experience: 20
append_fish :herring,   id: 345, level: 10, experience: 30
append_fish :anchovy,   id: 321, level: 15, experience: 40
append_fish :mackerel,  id: 353, level: 16, experience: 20
append_fish :trout,     id: 335, level: 20, experience: 50
append_fish :cod,       id: 341, level: 23, experience: 45
append_fish :pike,      id: 349, level: 25, experience: 60
append_fish :salmon,    id: 331, level: 30, experience: 70
append_fish :tuna,      id: 359, level: 35, experience: 80
append_fish :lobster,   id: 377, level: 40, experience: 90
append_fish :bass,      id: 363, level: 46, experience: 100
append_fish :swordfish, id: 371, level: 50, experience: 100
append_fish :shark,     id: 383, level: 76, experience: 110
