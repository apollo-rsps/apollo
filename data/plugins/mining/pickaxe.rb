require 'java'
java_import 'org.apollo.game.model.Animation'

PICKAXES = {}
PICKAXE_IDS = []

class Pickaxe
  attr_reader :id, :level, :animation, :pulses

  def initialize(id, level, animation, pulses)
    @id = id
    @level = level
    @animation = Animation.new(animation)
    @pulses = pulses
  end
end

def append_pickaxe(pickaxe)
  PICKAXES[pickaxe.id] = pickaxe
  PICKAXE_IDS << pickaxe.id # tacky way of keeping things in order
end

# NOTE: ADD LOWER LEVEL PICKAXES FIRST
append_pickaxe(Pickaxe.new(1265, 1,  625, 8)) # bronze pickaxe
append_pickaxe(Pickaxe.new(1267, 1,  626, 7)) # iron pickaxe
append_pickaxe(Pickaxe.new(1269, 1,  627, 6)) # steel pickaxe
append_pickaxe(Pickaxe.new(1273, 21, 629, 5)) # mithril pickaxe
append_pickaxe(Pickaxe.new(1271, 31, 628, 4)) # adamant pickaxe
append_pickaxe(Pickaxe.new(1275, 41, 624, 3)) # rune pickaxe

PICKAXE_IDS.reverse!
