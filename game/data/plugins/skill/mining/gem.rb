GEMSTONES = {}

# A gemstone that can be received when mining.
class Gemstone
  attr_reader :id, :chance

  def initialize(id, chance)
    @id = id
    @chance = chance
  end
end

def gem(gem)
  GEMSTONES[gem.id] = gem
end

gem(Gemstone.new(1623, 0)) # uncut sapphire
gem(Gemstone.new(1605, 0)) # uncut emerald
gem(Gemstone.new(1619, 0)) # uncut ruby
gem(Gemstone.new(1617, 0)) # uncut diamond
