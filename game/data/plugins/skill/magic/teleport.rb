# Thanks to phl0w <http://www.rune-server.org/members/phl0w> for providing
# the correct destination coordinates of the ancient teleports.

require 'java'

java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Graphic'
java_import 'org.apollo.game.model.Position'
java_import 'org.apollo.game.model.entity.Skill'

TELEPORT_SPELLS = {}

MODERN_TELE_ANIM = Animation.new(714)
MODERN_TELE_GRAPHIC = Graphic.new(111, 5, 100)

ANCIENT_TELE_END_GRAPHIC = Graphic.new(455)
ANCIENT_TELE_ANIM = Animation.new(1979)
ANCIENT_TELE_GRAPHIC = Graphic.new(392)

# A `Spell` that teleports a `Player` to another `Position`.
class TeleportSpell < Spell
  attr_reader :ancient, :destination, :experience, :name

  def initialize(ancient, level, elements, destination, experience, name)
    super(level, elements, experience)
    @ancient = ancient
    @destination = destination
    @name = name
  end

end

# A `SpellAction` for a `TeleportSpell`.
class TeleportingAction < SpellAction

  def initialize(mob, spell)
    super(mob, spell)
  end

  def execute_action
    @spell.ancient ? execute_ancient : execute_modern
  end

  def execute_modern
    if @pulses == 0
      mob.play_animation(MODERN_TELE_ANIM)
      mob.play_graphic(MODERN_TELE_GRAPHIC)
    elsif @pulses == 3
      mob.stop_graphic
      mob.stop_animation
      mob.teleport(@spell.destination)
      mob.skill_set.add_experience(Skill::MAGIC, @spell.experience)
      stop
    end
  end

  def execute_ancient
    if @pulses == 0
      mob.play_graphic(ANCIENT_TELE_GRAPHIC)
      mob.play_animation(ANCIENT_TELE_ANIM)
      set_delay(2)
    elsif @pulses == 2
      mob.stop_graphic
      mob.stop_animation
      mob.teleport(@spell.destination)
      mob.skill_set.add_experience(Skill::MAGIC, @spell.experience)
      stop
    end
  end

end

def tele(ancient = false, button, level, elements, x, y, experience, name)
  position = Position.new(x, y)
  TELEPORT_SPELLS[button] = TeleportSpell.new(ancient, level, elements, position, experience, name)
end

def ancient_tele(*args)
  tele(true, *args)
end

# Modern teleports
tele 1_164, 25, { LAW => 1, AIR => 3, FIRE => 1 }, 3213, 3424, 35, 'Varrock'
tele 1_167, 31, { LAW => 1, AIR => 3, EARTH => 1 }, 3222, 3219, 41, 'Lumbridge'
tele 1_170, 37, { LAW => 1, AIR => 3, WATER => 1 }, 2965, 3379, 47, 'Falador'
tele 1_174, 45, { LAW => 1, AIR => 5 }, 2757, 3478, 55.5, 'Camelot'
tele 1_540, 51, { LAW => 2, WATER => 2 }, 2662, 3306, 61, 'Ardougne'
tele 1_541, 58, { LAW => 2, EARTH => 2 }, 2549, 3114, 68, 'the Watchtower'
tele 7_455, 61, { LAW => 2, FIRE => 2 }, 2871, 3590, 68, 'Trollheim'
tele 18_470, 64, { Element.new([1963], nil, 'Banana') => 1, LAW => 2, WATER => 2, FIRE => 2 },
     2_754, 2_785, 76, 'Ape Atoll'

# Ancient teleports
ancient_tele 13_035, 54, { LAW => 2, FIRE => 1, AIR => 1 }, 3098, 9882, 64, 'Paddewwa'
ancient_tele 13_045, 60, { LAW => 2, SOUL => 2 }, 3320, 3338, 70, 'Senntisten'
ancient_tele 13_053, 66, { LAW => 2, BLOOD => 1 }, 3493, 3472, 76, 'Kharyll'
ancient_tele 13_061, 72, { LAW => 2, WATER => 4 }, 3003, 3470, 82, 'Lassar'
ancient_tele 13_069, 78, { LAW => 2, FIRE => 3, AIR => 2 }, 2966, 3_696, 88, 'Dareeyak'
ancient_tele 13_079, 84, { LAW => 2, SOUL => 2 }, 3163, 3664, 94, 'Carrallangar'
ancient_tele 13_087, 90, { LAW => 2, BLOOD => 2 }, 3287, 3883, 100, 'Annakarl'
ancient_tele 13_095, 96, { LAW => 2, WATER => 8 }, 2972, 3873, 106, 'Ghorrock'
