
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
def append_fish(name, fish)
  	CATCHABLE_FISH[name] = fish
end

append_fish(:shrimp,    Fish.new(317, 1,  10))
append_fish(:sardine,   Fish.new(327, 5,  20))
append_fish(:herring,   Fish.new(345, 10, 30))
append_fish(:anchovy,   Fish.new(321, 15, 40))
append_fish(:mackerel,  Fish.new(353, 16, 20))
append_fish(:trout,     Fish.new(335, 20, 50))
append_fish(:cod,       Fish.new(341, 23, 45))
append_fish(:pike,      Fish.new(349, 25, 60))
append_fish(:salmon,    Fish.new(331, 30, 70))
append_fish(:tuna,      Fish.new(359, 35, 80))
append_fish(:lobster,   Fish.new(377, 40, 90))
append_fish(:bass,      Fish.new(363, 46, 100))
append_fish(:swordfish, Fish.new(371, 50, 100))
append_fish(:shark,     Fish.new(383, 76, 110))