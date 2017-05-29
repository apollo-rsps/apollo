require 'java'

java_import 'org.apollo.game.model.Animation'

PICKAXES = {}
PICKAXE_IDS = []

# A pickaxe that can be mined with.
class Pickaxe
  attr_reader :id, :level, :animation, :pulses, :ratio

  def initialize(id, level, animation, pulses, ratio)
    @id = id
    @level = level
    @animation = Animation.new(animation)
    @pulses = pulses
    @ratio = ratio
  end
end

def append_pickaxe(pickaxe)
  PICKAXES[pickaxe.id] = pickaxe
  PICKAXE_IDS << pickaxe.id
end

# NOTE: ADD LOWER LEVEL PICKAXES FIRST
append_pickaxe(Pickaxe.new(1265, 1,  625, 8, 0.05)) # bronze pickaxe
append_pickaxe(Pickaxe.new(1267, 1,  626, 7, 0.1)) # iron pickaxe
append_pickaxe(Pickaxe.new(1269, 6,  627, 6, 0.2)) # steel pickaxe
append_pickaxe(Pickaxe.new(1273, 21, 629, 5, 0.3)) # mithril pickaxe
append_pickaxe(Pickaxe.new(1271, 31, 628, 4, 0.45)) # adamant pickaxe
append_pickaxe(Pickaxe.new(1275, 41, 624, 3, 0.65)) # rune pickaxe

PICKAXE_IDS.reverse!
