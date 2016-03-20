GEMSTONES = {}

# A gemstone that can be received when mining.
class Gemstone
  attr_reader :name, :id, :chance

  def initialize(id, chance)
    @name = name_of(:item, id)
    @id = id
    @chance = chance
  end
end

def gem(name, hash)
  unless hash.has_keys?(:id, :chance)
    fail 'Hash must contain an id and chance.'
  end

  GEMSTONES[name] = Gemstone.new(hash[:id], hash[:chance])
end

gem :uncut_sapphire,  id: 1623, chance: 7
gem :uncut_emerald,   id: 1605, chance: 5
gem :uncut_ruby,      id: 1619, chance: 3
gem :uncut_diamond,   id: 1617, chance: 1