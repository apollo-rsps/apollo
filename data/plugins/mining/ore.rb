# Thanks to Mikey` <http://www.rune-server.org/members/mikey%60/> for helping
# to find some of the item/object IDs, minimum levels and experiences.
#
# Thanks to Clifton <http://www.rune-server.org/members/clifton/> for helping
# to find some of the expired object IDs.

ORES = {}
EXPIRED_ORES = {}

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
  2180  => 450  , 2109  => 451  , 14904 => 14896, 14905 => 14897
}

COPPER_OBJECTS = {
  11960 => 11555, 11961 => 11556, 11962 => 11557, 11936 => 11552,
  11937 => 11553, 11938 => 11554, 2090  => 450  , 2091  => 451  ,
  14906 => 14898, 14907 => 14899, 14856 => 14832, 14857 => 14833,
  14858 => 14834
}

TIN_OBJECTS = {
  11597 => 11555, 11958 => 11556, 11959 => 11557, 11933 => 11552,
  11934 => 11553, 11935 => 11554, 2094  => 450  , 2095  => 451  ,
  14092 => 14894, 14903 => 14895
}

IRON_OBJECTS = {
  11954 => 11555, 11955 => 11556, 11956 => 11557, 2092  => 450  ,
  2093  => 451  , 14900 => 14892, 14901 => 14893, 14913 => 14915,
  14914 => 14916
}

COAL_OBJECTS = {
  11963 => 11555, 11964 => 11556, 11965 => 11557, 11930 => 11552,
  11931 => 11553, 11932 => 11554, 2096  => 450  , 2097  => 451  ,
  14850 => 14832, 14851 => 14833, 14852 => 14834
}

SILVER_OBJECTS = {
  11948 => 11555, 11949 => 11556, 11950 => 11557, 2100  => 450  ,
  2101  => 451
}

GOLD_OBJECTS = {
  11951 => 11555, 11952 => 11556, 11953 => 11557, 2098  => 450  ,
  2099  => 451
}

MITHRIL_OBJECTS = {
  11945 => 11555, 11946 => 11556, 11947 => 11557, 11942 => 11552,
  11943 => 11553, 11944 => 11554, 2102  => 450  , 2103  => 451  ,
  14853 => 14832, 14854 => 14833, 14855 => 14834
}

ADAMANT_OBJECTS = {
  11939 => 11552, 11940 => 11553, 11941 => 11554, 2104  => 450  ,
  2105  => 451  , 14862 => 14832, 14863 => 14833, 14864 => 14834
}

RUNITE_OBJECTS = {
  2106  => 450  , 2107  => 451  , 14859 => 14832, 14860 => 14833,
  14861 => 14834
}

append_ore Ore.new(434,  CLAY_OBJECTS,    1,  5,    3   ) # clay
append_ore Ore.new(436,  COPPER_OBJECTS,  1,  17.5, 6   ) # copper ore
append_ore Ore.new(438,  TIN_OBJECTS,     1,  17.5, 6   ) # tin ore
append_ore Ore.new(440,  IRON_OBJECTS,    15, 35,   16  ) # iron ore
append_ore Ore.new(453,  COAL_OBJECTS,    30, 50,   100 ) # coal
append_ore Ore.new(444,  GOLD_OBJECTS,    40, 65,   200 ) # gold ore
append_ore Ore.new(442,  SILVER_OBJECTS,  20, 40,   200 ) # silver ore
append_ore Ore.new(447,  MITHRIL_OBJECTS, 55, 80,   400 ) # mithril ore
append_ore Ore.new(449,  ADAMANT_OBJECTS, 70, 95,   800 ) # adamant ore
append_ore Ore.new(451,  RUNITE_OBJECTS,  85, 125,  2500) # runite ore

# TODO: rune essence object id = 2491
#   level 1, exp 5, rune ess = 1436, pure ess = 7936
