GEMSTONES = {}

class Gemstone
  attr_reader :id, :chance

  def initialize(id, chance)
    @id = id
    @chance = chance
  end
end

def append_gem(gem)
  GEMSTONES[gem.id] = gem
end

append_gem(Gemstone.new(1623, 0)) # uncut sapphire
append_gem(Gemstone.new(1605, 0)) # uncut emerald
append_gem(Gemstone.new(1619, 0)) # uncut ruby
append_gem(Gemstone.new(1617, 0)) # uncut diamond