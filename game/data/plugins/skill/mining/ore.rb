# Thanks to Mikey` <http://www.rune-server.org/members/mikey%60/> for helping
# to find some of the item/object IDs, minimum levels and experiences.
#
# Thanks to Clifton <http://www.rune-server.org/members/clifton/> for helping
# to find some of the expired object IDs.

ORES = {}
EXPIRED_ORES = {}

# An ore that can be mined.
class Ore
  attr_reader :id, :objects, :level, :exp, :respawn

  def initialize(id, objects, level, exp, respawn)
    @id = id
    @objects = objects
    @level = level
    @exp = exp
    @respawn = respawn
  end
end

def append_ore(ore)
  ore.objects.each do |obj, expired_obj|
    ORES[obj] = ore
    EXPIRED_ORES[expired_obj] = true
  end
end

CLAY_OBJECTS = {
  2180 => 450, 2109 => 451, 14_904 => 14_896, 14_905 => 14_897
}

COPPER_OBJECTS = {
  11_960 => 11_555, 11_961 => 11_556, 11_962 => 11_557, 11_936 => 11_552,
  11_937 => 11_553, 11_938 => 11_554, 2090  => 450, 2091  => 451,
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

RUNITE_OBJECTS = {
  2106 => 450, 2107 => 451, 14_859 => 14_832, 14_860 => 14_833,
  14_861 => 14_834
}

append_ore Ore.new 434,  CLAY_OBJECTS,    1,  5,    3 # clay
append_ore Ore.new 436,  COPPER_OBJECTS,  1,  17.5, 6 # copper ore
append_ore Ore.new 438,  TIN_OBJECTS,     1,  17.5, 6 # tin ore
append_ore Ore.new 440,  IRON_OBJECTS,    15, 35,   16 # iron ore
append_ore Ore.new 453,  COAL_OBJECTS,    30, 50,   100 # coal
append_ore Ore.new 444,  GOLD_OBJECTS,    40, 65,   200 # gold ore
append_ore Ore.new 442,  SILVER_OBJECTS,  20, 40,   200 # silver ore
append_ore Ore.new 447,  MITHRIL_OBJECTS, 55, 80,   400 # mithril ore
append_ore Ore.new 449,  ADAMANT_OBJECTS, 70, 95,   800 # adamant ore
append_ore Ore.new 451,  RUNITE_OBJECTS,  85, 125,  2500 # runite ore

# TODO: rune essence object id = 2491
#   level 1, exp 5, rune ess = 1436, pure ess = 7936
