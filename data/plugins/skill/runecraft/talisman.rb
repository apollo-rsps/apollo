require 'java'

java_import 'org.apollo.game.model.Position'

# The list of talismans.
TALISMANS = {}

# A talisman that will indicate a direction when activated.
class Talisman

  def initialize(entrance_altar_position)
    @locate_position = entrance_altar_position
  end

  def get_message(player_position)
    return 'Your talisman glows brightly.' if player_position.is_within_distance(@locate_position, 10)

    direction = (player_position.y > @locate_position.y ? 'North' : 'South') + '-' + (player_position.x > @locate_position.x ? 'East' : 'West')    
    return "The talisman pulls toward the #{direction}."
  end

end

# Appends a talisman to the list.
def append_talisman(hash)
  raise 'Hash must contain an id and an altar position.' unless hash.has_key?(:id) && hash.has_key?(:altar)
  id = hash[:id]; altar_position = Position.new(*hash[:altar])

  TALISMANS[id] = Talisman.new(altar_position)
end

# Intercepts the item option message.
on :message, :fourth_item_option do |ctx, player, message|
  talisman = TALISMANS[message.id]
  if (talisman != nil)
    player.send_message(talisman.get_message(player.position))
    ctx.break_handler_chain
  end
end

# Appends talismans to the list.
append_talisman :name => :air_talisman,    :id => 1438, :altar => [ 2985, 3292 ]
append_talisman :name => :earth_talisman,  :id => 1440, :altar => [ 3306, 3474 ]
append_talisman :name => :fire_talisman,   :id => 1442, :altar => [ 3313, 3255 ]
append_talisman :name => :water_talisman,  :id => 1444, :altar => [ 3185, 3165 ]
append_talisman :name => :body_talisman,   :id => 1446, :altar => [ 3053, 3445 ]
append_talisman :name => :mind_talisman,   :id => 1448, :altar => [ 2982, 3514 ]
append_talisman :name => :chaos_talisman,  :id => 1452, :altar => [ 3059, 3590 ]
append_talisman :name => :cosmic_talisman, :id => 1454, :altar => [ 2408, 4377 ]
append_talisman :name => :death_talisman,  :id => 1456, :altar => [    0,    0 ]
append_talisman :name => :law_talisman,    :id => 1458, :altar => [ 2858, 3381 ]
append_talisman :name => :nature_talisman, :id => 1462, :altar => [ 2869, 3019 ]