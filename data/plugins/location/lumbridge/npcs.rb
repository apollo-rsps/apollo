# Information about npc spawning
#
# Npcs are passed to spawn npc as a hash. Every key and every non-integer value must be a Symbol. Every hash must implement the following:
#   :name - the name of the npc. If this npc shares its name with another, append the specific id after the name (e.g. :woman_4)
#   :x - the x coordinate where the npc will spawn.
#   :y - the y coordinate where the npc will spawn.
# Optional arguments are as follows:
#   :face - the direction the npc should face when it spawns. Supported options are :north, :north_east, :east, :south_east, :south, :south_west, :west, and :north_west
#   :bounds - the rectangular bound that the npc can wander about in. Order is [top-left x-coordinate, top-left y-coordinate, bottom-right x-coordinate, bottom-right y-coordinate]
#   :delta_bounds - the rectangular bound that the npc can wander about in, as a difference from the spawn point. Order is [x-delta, y-delta]. Should not be used with :bounds.
#   :spawn_animation - the animation that will be played when the npc spawns.
#   :spawn_graphic - the graphic that will be played when the npc spawns.


# Generic npcs

spawn_npc :name => :woman_4, :x => 3232, :y => 3207 # southernmost house
spawn_npc :name => :man_1,   :x => 3231, :y => 3237 # house by willow tree
spawn_npc :name => :man_2,   :x => 3224, :y => 3240 # house by willow tree
spawn_npc :name => :woman_5, :x => 3229, :y => 2329 # house by willow tree

# Functional npcs

spawn_npc :name => :hans,            :x => 3221, :y => 3221
spawn_npc :name => :father_aereck,   :x => 3243, :y => 3210
spawn_npc :name => :bob,             :x => 3231, :y => 3203
spawn_npc :name => :shop_keeper,     :x => 3212, :y => 3247
spawn_npc :name => :shop_assistant,  :x => 3211, :y => 3245
spawn_npc :name => :lumbridge_guide, :x => 3232, :y => 3229