ROCK = {}
EXPIRED_ROCK = {}

# An ore that can be mined.
class Rock #originall ore
  attr_reader :id, :objects, :level, :respawn, :rate

  def initialize(id, objects, level, respawn, rate)
    @id = id.map { |ores| ORES[ores] }
    @objects = objects
    @level = level
    @respawn = respawn
    @rate = rate
  end
end

def append_ore(ore)
  ore.objects.each do |obj, expired_obj|
    ROCK[obj] = ore
    EXPIRED_ROCK[expired_obj] = true
  end
end

CLAY_OBJECTS = {
  2108 => 450, 2109 => 452, 14_904 => 14_896, 14_905 => 14_897
}

COPPER_OBJECTS = {
  11_960 => 11_555, 11_961 => 11_556, 11_962 => 11_557, 11_936 => 11_552,
  11_937 => 11_553, 11_938 => 11_554,  2090 => 450, 2091 => 451,
  14_906 => 14_898, 14_907 => 14_899, 14_856 => 14_832, 14_857 => 14_833,
  14_858 => 14_834
}

TIN_OBJECTS = {
  11_597 => 11_555, 11_958 => 11_556, 11_959 => 11_557, 11_933 => 11_552,
  11_934 => 11_553, 11_935 => 11_554, 2094 => 450, 2095 => 451,
  14_092 => 14_894, 14_903 => 14_895
}

IRON_OBJECTS = {
  11_954 => 11_555, 11_955 => 11_556, 11_956 => 11_557, 2092 => 450,
  2093 => 451, 14_900 => 14_892, 14_901 => 14_893, 14_913 => 14_915,
  14_914 => 14_916
}

COAL_OBJECTS = {
  11_963 => 11_555, 11_964 => 11_556, 11_965 => 11_557, 11_930 => 11_552,
  11_931 => 11_553, 11_932 => 11_554, 2096 => 450, 2097 => 451,
  14_850 => 14_832, 14_851 => 14_833, 14_852 => 14_834
}

SILVER_OBJECTS = {
  11_948 => 11_555, 11_949 => 11_556, 11_950 => 11_557, 2100 => 450, 2101 => 451
}

GOLD_OBJECTS = {
  11_951 => 11_555, 11_952 => 11_556, 11_953 => 11_557, 2098 => 450, 2099 => 451
}

MITHRIL_OBJECTS = {
  11_945 => 11_555, 11_946 => 11_556, 11_947 => 11_557, 11_942 => 11_552,
  11_943 => 11_553, 11_944 => 11_554, 2102 => 450, 2103 => 451,
  14_853 => 14_832, 14_854 => 14_833, 14_855 => 14_834
}

ADAMANT_OBJECTS = {
  11_939 => 11_552, 11_940 => 11_553, 11_941 => 11_554, 2104 => 450,
  2105 => 451, 14_862 => 14_832, 14_863 => 14_833, 14_864 => 14_834
}

RUNE_OBJECTS = {
  2106 => 450, 2107 => 451, 14_859 => 14_832, 14_860 => 14_833,
  14_861 => 14_834
}

ESSENCE_OBJECTS = {
  2491 => 2491
}

GRANITE_OBJECTS = {
  10947 => 10945
}

SANDSTONE_OBJECTS = {
  10946 => 10944
}

BLURITE_OBJECTS = {
  10583 => 10585, 10584 => 10586, 2110 => 2108
}


append_ore Rock.new [:rune_essence, :pure_essence],              ESSENCE_OBJECTS, 1,  -1,    0.30
append_ore Rock.new [:clay],                                     CLAY_OBJECTS,    1,   3,    0.1
append_ore Rock.new [:copper_ore],                               COPPER_OBJECTS,  1,   6,    0.05
append_ore Rock.new [:tin_ore],                                  TIN_OBJECTS,     1,   6,    0.05
append_ore Rock.new [:blurite_ore],                              BLURITE_OBJECTS, 10,  6,    0.05
append_ore Rock.new [:iron_ore],                                 IRON_OBJECTS,    15,  16,   0.35
append_ore Rock.new [:silver_ore],                               SILVER_OBJECTS,  20,  200,  0.3
append_ore Rock.new [:coal_ore],                                 COAL_OBJECTS,    30,  100,  0.6
append_ore Rock.new [:sand_stone_1kg, :sand_stone_2kg, :sand_stone_5kg, :sand_stone_10kg], SANDSTONE_OBJECTS, 35,  175, 0.2
append_ore Rock.new [:gold_ore],                                 GOLD_OBJECTS,    40,  200,  0.6
append_ore Rock.new [:granite_500g, :granite_2kg, :granite_5kg], GRANITE_OBJECTS, 45,  150,    0.1
append_ore Rock.new [:mithril_ore],                              MITHRIL_OBJECTS, 55,  400,  0.70
append_ore Rock.new [:adamant_ore],                              ADAMANT_OBJECTS, 70,  800,  0.85
append_ore Rock.new [:rune_ore],                                 RUNE_OBJECTS,    85,  2500, 0.95