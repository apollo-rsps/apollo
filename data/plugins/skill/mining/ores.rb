REWARD_ORES = {}

class Ores
  attr_reader :id, :level, :experience, :name

  # Creates the Ores.
  def initialize(id, level, experience)
    @id = id
    @level = level
    @experience = experience
    @name = name_of(:item, id)
  end

end

def append_ore(name, hash)
  unless hash.has_keys?(:id, :level, :experience)
    fail 'Hash must contain an id, level, and experience.'
  end

  REWARD_ORES[name] = Ores.new(hash[:id], hash[:level], hash[:experience])
end

append_ore :rune_essence,         id: 1436, level: 1,  experience: 5
append_ore :clay,                 id: 434,  level: 1,  experience: 5
append_ore :copper_ore,           id: 436,  level: 1,  experience: 17.5
append_ore :tin_ore,              id: 438,  level: 1,  experience: 17.5
append_ore :blurite_ore,          id: 668,  level: 10, experience: 20
append_ore :iron_ore,             id: 440,  level: 15, experience: 35
append_ore :silver_ore,           id: 442,  level: 20, experience: 40
append_ore :pure_essence,         id: 7936, level: 30, experience: 5
append_ore :coal_ore,             id: 453,  level: 30, experience: 50
append_ore :sand_stone_1kg,       id: 6971, level: 35, experience: 30
append_ore :sand_stone_2kg,       id: 6973, level: 35, experience: 40
append_ore :sand_stone_5kg,       id: 6975, level: 35, experience: 50
append_ore :sand_stone_10kg,      id: 6977, level: 35, experience: 60
append_ore :gold_ore,             id: 444,  level: 40, experience: 65
append_ore :granite_500g,         id: 6979, level: 45, experience: 50
append_ore :granite_2kg,          id: 6981, level: 45, experience: 60
append_ore :granite_5kg,          id: 6983, level: 45, experience: 75
append_ore :mithril_ore,          id: 447,  level: 55, experience: 80
append_ore :adamant_ore,          id: 449,  level: 70, experience: 95
append_ore :rune_ore,             id: 451,  level: 85, experience: 125



