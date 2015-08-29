require 'java'

java_import 'org.apollo.game.model.Position'

# The list of talismans.
TALISMANS = {}

# A talisman that will indicate a direction when activated.
class Talisman

  def initialize(entrance_altar_position)
    @locate_position = entrance_altar_position
  end

  def get_message(position)
    return 'Your talisman glows brightly.' if position.is_within_distance(@locate_position, 10)

    direction = (position.y > @locate_position.y ? 'North' : 'South') + '-'
    direction += (position.x > @locate_position.x ? 'East' : 'West')

    "The talisman pulls toward the #{direction}."
  end

end

# Intercepts the item option message.
on :message, :fourth_item_option do |player, message|
  talisman = TALISMANS[message.id]

  unless talisman.nil?
    player.send_message(talisman.get_message(player.position))
    message.terminate
  end
end

# Appends a talisman to the list.
def talisman(name, hash)
  fail 'Hash must contain an id and an altar position.' unless hash.has_keys?(:id, :altar)
  id, altar_position = hash[:id], Position.new(*hash[:altar])

  TALISMANS[id] = Talisman.new(altar_position)
end

talisman :air_talisman,    id: 1438, altar: [2985, 3292]
talisman :earth_talisman,  id: 1440, altar: [3306, 3474]
talisman :fire_talisman,   id: 1442, altar: [3313, 3255]
talisman :water_talisman,  id: 1444, altar: [3185, 3165]
talisman :body_talisman,   id: 1446, altar: [3053, 3445]
talisman :mind_talisman,   id: 1448, altar: [2982, 3514]
talisman :chaos_talisman,  id: 1452, altar: [3059, 3590]
talisman :cosmic_talisman, id: 1454, altar: [2408, 4377]
talisman :death_talisman,  id: 1456, altar: [0, 0]
talisman :law_talisman,    id: 1458, altar: [2858, 3381]
talisman :nature_talisman, id: 1462, altar: [2869, 3019]
