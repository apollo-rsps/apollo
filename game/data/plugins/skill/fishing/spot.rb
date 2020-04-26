
# The hash of fishing spots.
FISHING_SPOTS = {}

# A Fishing spot.
class Spot
  attr_reader :tools, :first_fish, :second_fish

  # Creates the fishing spot.
  def initialize(tools, first_fish, second_fish)
    @tools = tools.map { |id| FISHING_TOOLS[id] }
    @first_fish = first_fish.map { |fish| CATCHABLE_FISH[fish] }
    @second_fish = second_fish.map { |fish| CATCHABLE_FISH[fish] }
  end

end

# Appends a fishing spot to the hash.
def append_spot(id, spot)
  FISHING_SPOTS[id] = spot
end

append_spot(309, Spot.new([:fly_fishing_rod, :fishing_rod], [:trout, :salmon], [:pike]))
append_spot(312, Spot.new([:lobster_cage, :harpoon], [:lobster], [:tuna, :swordfish]))
append_spot(313, Spot.new([:big_net, :harpoon], [:mackerel, :cod], [:bass, :shark]))
append_spot(316, Spot.new([:small_net, :fishing_rod], [:shrimp, :anchovy], [:sardine, :herring]))
