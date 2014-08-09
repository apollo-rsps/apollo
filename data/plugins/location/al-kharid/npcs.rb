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

spawn_npc :name => :man, :x => 3276, :y => 3186

# Palace guards

spawn_npc :name => "Al-Kharid warrior", :x => 3283, :y => 3161 # String must be used here because the actual npc name is 'Al-Kharid warrior', and symbols don't support hyphens.
spawn_npc :name => "Al-Kharid warrior", :x => 3285, :y => 3174
spawn_npc :name => "Al-Kharid warrior", :x => 3286, :y => 3164
spawn_npc :name => "Al-Kharid warrior", :x => 3287, :y => 3168
spawn_npc :name => "Al-Kharid warrior", :x => 3288, :y => 3169
spawn_npc :name => "Al-Kharid warrior", :x => 3290, :y => 3162
spawn_npc :name => "Al-Kharid warrior", :x => 3295, :y => 3162
spawn_npc :name => "Al-Kharid warrior", :x => 3295, :y => 3170
spawn_npc :name => "Al-Kharid warrior", :x => 3297, :y => 3175
spawn_npc :name => "Al-Kharid warrior", :x => 3300, :y => 3171
spawn_npc :name => "Al-Kharid warrior", :x => 3301, :y => 3164
spawn_npc :name => "Al-Kharid warrior", :x => 3301, :y => 3168

spawn_npc :name => :shantay_guard_838, :x => 3301, :y => 3120
spawn_npc :name => :shantay_guard,     :x => 3304, :y => 3119
spawn_npc :name => :shantay_guard_838, :x => 3307, :y => 3122

# Mine

spawn_npc :name => :scorpion, :x => 3296, :y => 3294
spawn_npc :name => :scorpion, :x => 3298, :y => 3280
spawn_npc :name => :scorpion, :x => 3299, :y => 3299
spawn_npc :name => :scorpion, :x => 3299, :y => 3309
spawn_npc :name => :scorpion, :x => 3300, :y => 3287
spawn_npc :name => :scorpion, :x => 3301, :y => 3305


# Functional npcs

spawn_npc :name => :gnome_pilot, :x => 3279, :y => 3213

spawn_npc :name => :banker_496, :x => 3267, :y => 3164, :face => :east # TODO are these the correct banker ids?
spawn_npc :name => :banker_497, :x => 3267, :y => 3166, :face => :east
spawn_npc :name => :banker_496, :x => 3267, :y => 3167, :face => :east
spawn_npc :name => :banker_497, :x => 3267, :y => 3168, :face => :east
spawn_npc :name => :banker_496, :x => 3267, :y => 3169, :face => :east

spawn_npc :name => :gem_trader, :x => 3287, :y => 3210
spawn_npc :name => :zeke,       :x => 3289, :y => 3189
spawn_npc :name => :shantay,    :x => 3304, :y => 3124

spawn_npc :name => :rug_merchant_2296,  :x => 3311, :y => 3109, :face => :west
spawn_npc :name => :ranael,             :x => 3315, :y => 3163, :face => :north
spawn_npc :name => :shop_assistant_525, :x => 3315, :y => 3178, :face => :north # TODO are these the correct shop staff ids?
spawn_npc :name => :shop_keeper_524,    :x => 3315, :y => 3180, :face => :west
spawn_npc :name => :louie_legs,         :x => 3316, :y => 3175, :face => :west