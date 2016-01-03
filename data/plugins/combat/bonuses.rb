module Combat
  # A module for units which can have their own bonuses. E.G., weapons, equipment, ammo.
  module BonusContainer
    def attack_bonus(type)
      @attack_bonuses[type]
    end

    def defence_bonus(type)
      @defence_bonuses[type]
    end

    def other_bonus(type)
      @other_bonuses[type]
    end

    def other_bonuses(melee_strength: 0, ranged_strength: 0, prayer: 0)
      @other_bonuses = {
        melee_strength: melee_strength,
        ranged_strength: ranged_strength,
        prayer: prayer
      }
    end

    def defence_bonuses(stab: 0, slash: 0, crush: 0, magic: 0, range: 0)
      @defence_bonuses = {
        stab: stab,
        slash: slash,
        crush: crush,
        magic: magic,
        range: range
      }
    end

    def attack_bonuses(stab: 0, slash: 0, crush: 0, magic: 0, range: 0)
      @attack_bonuses = {
        stab: stab,
        slash: slash,
        crush: crush,
        magic: magic,
        range: range
      }
    end
  end
end
