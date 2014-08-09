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

spawn_npc :name => :man, :x => 3095, :y => 3508
spawn_npc :name => :man, :x => 3095, :y => 3511
spawn_npc :name => :man, :x => 3098, :y => 3509

spawn_npc :name => :guard, :x => 3108, :y => 3514
spawn_npc :name => :guard, :x => 3110, :y => 3514
spawn_npc :name => :guard, :x => 3113, :y => 3514
spawn_npc :name => :guard, :x => 3113, :y => 3516

spawn_npc :name => :sheep_43, :x => 3053, :y => 3514
spawn_npc :name => :sheep_43, :x => 3053, :y => 3517
spawn_npc :name => :sheep_43, :x => 3053, :y => 3518
spawn_npc :name => :sheep_43, :x => 3056, :y => 3517

spawn_npc :name => :mugger, :x => 3076, :y => 3504

spawn_npc :name => :monk, :x => 3044, :y => 3491
spawn_npc :name => :monk, :x => 3045, :y => 3483
spawn_npc :name => :monk, :x => 3045, :y => 3497
spawn_npc :name => :monk, :x => 3050, :y => 3490
spawn_npc :name => :monk, :x => 3054, :y => 3490
spawn_npc :name => :monk, :x => 3058, :y => 3497


# Functional npcs

spawn_npc :name => :brother_jered,   :x => 3045, :y => 3488
spawn_npc :name => :brother_althric, :x => 3054, :y => 3504
spawn_npc :name => :abbot_langley,   :x => 3059, :y => 3484
spawn_npc :name => :oziach,			 :x => 3067, :y => 3518, :face => :east
spawn_npc :name => :shop_assistant,  :x => 3079, :y => 3509
spawn_npc :name => :shop_keeper,     :x => 3082, :y => 3513
spawn_npc :name => :banker,          :x => 3096, :y => 3489, :face => :west # TODO probably not all the same bankers.
spawn_npc :name => :banker,          :x => 3096, :y => 3491, :face => :west
spawn_npc :name => :banker,          :x => 3096, :y => 3492, :face => :north
spawn_npc :name => :banker,          :x => 3098, :y => 3492, :face => :north
spawn_npc :name => :mage_of_zamorak, :x => 3106, :y => 3560