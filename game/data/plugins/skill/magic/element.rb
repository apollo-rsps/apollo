require 'java'

java_import 'org.apollo.game.model.entity.EquipmentConstants'

AIR_ELEMENTS = {}
WATER_ELEMENTS = {}
EARTH_ELEMENTS = {}
FIRE_ELEMENTS = {}

AIR_RUNE    = 556
WATER_RUNE  = 555
EARTH_RUNE  = 557
FIRE_RUNE   = 554

MIND_RUNE   = 558
CHAOS_RUNE  = 562
DEATH_RUNE  = 560
BLOOD_RUNE  = 565

COSMIC_RUNE = 564
LAW_RUNE    = 563
NATURE_RUNE = 561
SOUL_RUNE   = 566

MIST_RUNE   = 4695
DUST_RUNE   = 4696
SMOKE_RUNE  = 4697
MUD_RUNE    = 4698
STEAM_RUNE  = 4694
LAVA_RUNE   = 4699

# An element of a spell.
class Element
  attr_reader :runes, :staffs, :name

  def initialize(runes, staffs, name = 'Null')
    @runes = runes
    @staffs = staffs
    @name = name
  end

  def check_remove(player, amount, remove)
    weapon = player.equipment.get(EquipmentConstants::WEAPON)
    unless @staffs.nil? || weapon.nil?
      @staffs.each { |staff| return true if weapon.id == staff }
    end

    inventory = player.inventory

    found = {}
    counter = 0

    inventory.items.each do |item|
      break unless counter < amount
      next if item.nil?

      amt = item.amount
      @runes.each do |rune|
        break unless counter < amount

        id = item.id
        if id == rune
          if amt >= amount
            inventory.remove(id, amount) if remove
            return true
          else
            found[id] = amt
            counter += amt
          end
        end
      end
    end

    if counter >= amount
      found.each { |id, amt| inventory.remove(id, amt) } if remove
      return true
    end

    false
  end

end

AIR_RUNES    = [556, 4695, 4696, 4697]
WATER_RUNES  = [555, 4695, 4698, 4694]
EARTH_RUNES  = [557, 4696, 4697, 4698]
FIRE_RUNES   = [554, 4697, 4694, 4699]

AIR_STAFFS   = [1381, 1397, 1405]
WATER_STAFFS = [1383, 1395, 1403]
EARTH_STAFFS = [1385, 1399, 1407, 3053, 3054]
FIRE_STAFFS  = [1387, 1393, 1401, 3053, 3054]

AIR    = Element.new(AIR_RUNES,   AIR_STAFFS, 'Air rune')
WATER  = Element.new(WATER_RUNES, WATER_STAFFS, 'Water rune')
EARTH  = Element.new(EARTH_RUNES, EARTH_STAFFS, 'Earth rune')
FIRE   = Element.new(FIRE_RUNES,  FIRE_STAFFS, 'Fire rune')

MIND   = Element.new([MIND_RUNE],  nil, 'Mind rune')
CHAOS  = Element.new([CHAOS_RUNE], nil, 'Chaos rune')
DEATH  = Element.new([DEATH_RUNE], nil, 'Death rune')
BLOOD  = Element.new([BLOOD_RUNE], nil, 'Blood rune')

COSMIC = Element.new([COSMIC_RUNE], nil, 'Cosmic rune')
LAW    = Element.new([LAW_RUNE],    nil, 'Law rune')
NATURE = Element.new([NATURE_RUNE], nil, 'Nature rune')
SOUL   = Element.new([SOUL_RUNE],   nil, 'Soul rune')
