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


# Functional npcs

# 'Above-ground' npcs

spawn_npc :name => :runescape_guide,   :x => 3093, :y => 3107
spawn_npc :name => :survival_expert,   :x => 3104, :y => 3095, :face => :north
spawn_npc :name => :master_chef,       :x => 3076, :y => 3085
spawn_npc :name => :quest_guide,       :x => 3086, :y => 3122, :face => :north
spawn_npc :name => :financial_advisor, :x => 3127, :y => 3124, :face => :west
spawn_npc :name => :brother_brace,     :x => 3124, :y => 3107, :face => :east
spawn_npc :name => :magic_instructor,  :x => 3140, :y => 3085

# 'Below-ground' npcs
# Note: They aren't actually on a different plane, they're just in a different location that pretends to be underground.

spawn_npc :name => :mining_instructor, :x => 3081, :y => 9504
spawn_npc :name => :combat_instructor, :x => 3104, :y => 9506

# Non-humanoid npcs

spawn_npc :name => :fishing_spot_316, :x => 3102, :y => 3093

spawn_npc :name => :chicken, :x => 3140, :y => 3095
spawn_npc :name => :chicken, :x => 3140, :y => 3093
spawn_npc :name => :chicken, :x => 3138, :y => 3092
spawn_npc :name => :chicken, :x => 3137, :y => 3094
spawn_npc :name => :chicken, :x => 3138, :y => 3095


# 'Below-ground' npcs
# Note: They aren't actually on a different plane, they're just in a different location that pretends to be underground.

spawn_npc :name => :giant_rat_87,     :x => 3105, :y => 9514
spawn_npc :name => :giant_rat_87,     :x => 3105, :y => 9517
spawn_npc :name => :giant_rat_87,     :x => 3106, :y => 9514
spawn_npc :name => :giant_rat_87,     :x => 3104, :y => 9514
spawn_npc :name => :giant_rat_87,     :x => 3105, :y => 9519
spawn_npc :name => :giant_rat_87,     :x => 3109, :y => 9516
spawn_npc :name => :giant_rat_87,     :x => 3108, :y => 9520
spawn_npc :name => :giant_rat_87,     :x => 3102, :y => 9517