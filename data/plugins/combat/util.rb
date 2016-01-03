java_import 'org.apollo.game.model.entity.EquipmentConstants'

class CombatUtil
  def self.current_speed(_mob)
  end

  def self.calculate_max_hit(source)
    strength      = source.skill_set.get_skill(Skill::STRENGTH)
    strength_stat = 5 # source.bonus_stat(:other, :strength)

    effective_strength_damage = (strength.current_level) # * prayer_multiplier

    if [:aggressive, :alt_aggressive].include? source.combat_style
      effective_strength_damage += 3
    end

    (1.3 + (effective_strength_damage / 10) + (strength_stat / 80) +
      ((effective_strength_damage * strength_stat) / 640))
  end

  def self.calculate_accuracy(source, target)
    weapon       = EquipmentUtil.equipped_weapon source
    weapon_class = weapon.weapon_class
    combat_style = weapon_class.selected_style source
    attack_type  = combat_style.attack_type

    attack_stat  = [1, 1].max
    defence_stat = [1, 1].max

    attack  = source.skill_set.get_skill(Skill::ATTACK).current_level.to_f
    defence = target.skill_set.get_skill(Skill::DEFENCE).current_level.to_f

    attack_prayer_multiplier = 1 # TODO: Prayer
    attack_accuracy          = attack_stat * attack * attack_prayer_multiplier
    attack_accuracy          = 0.1 if attack_accuracy < 0

    defence_prayer_multiplier = 1 # TODO: Prayer
    defence_accuracy          = defence_stat * defence * defence_prayer_multiplier
    defence_accuracy          = 1 if defence_accuracy < 0

    base = attack_accuracy / defence_accuracy
    base > 1 ? 0.9 : base * 0.9
  end

  # Calculates a hit for the given <i>Mob</i> and special attack flag.
  def self.calculate_hit(source, target)
    accuracy = calculate_accuracy(source, target)
    max_hit  = calculate_max_hit(source) + 1

    if rand <= accuracy
      return rand(max_hit)
    else
      return 0
    end
  end
end

class EquipmentUtil
  def self.equipped_weapon(source)
    item = source.equipment.get(EquipmentConstants::WEAPON)

    return NAMED_WEAPONS[:unarmed] if item.nil?

    WEAPONS[item.id]
  end

  def self.equipped_ammo(source)
    item = source.equipment.get(EquipmentConstants::ARROWS)

    return nil if item.nil?

    AMMO[item.id]
  end
end
