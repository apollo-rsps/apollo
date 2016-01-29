java_import 'org.apollo.cache.def.ItemDefinition'
java_import 'org.apollo.game.model.entity.EquipmentConstants'
java_import 'org.apollo.game.model.entity.Skill'
java_import 'org.apollo.cache.def.EquipmentDefinition'

##
# An {@code Exception} thrown by an {@code AttackRequirement} if a player doesn't atch its requirements.
class AttackRequirementException < Exception
  attr_reader :message

  def initialize(message)
    @message = message
  end
end

##
# A requirement which is checked before an {@code Attack} is executed.
class AttackRequirement
  ##
  # Verify that {@code _player} meets the requirements, throwing an AttackRequirementException with an appropriate
  # message if not.
  def validate(_player)
    throw RuntimeError.new('validate! not implemented')
  end

  ##
  # Do any destructive actions (e.g., remove items) after all requirements for an {@code Attack} have been met.
  def apply!(_player)
    throw RuntimeError.new('apply not implemented')
  end
end

class AttackRequirementDSL
  RUNES = {
    air: 556, water: 555, earth: 557, fire: 554,
    mind: 558, chaos: 562, death: 560, blood: 565,
    cosmic: 564, law: 563, nature: 561, soul: 566
  }

  attr_reader :requirements

  def initialize(&block)
    @requirements = []

    instance_eval(&block)
  end

  def rune(type, amount: 1)
    fail 'Invalid rune type' unless RUNES.key? type
    requirements << ItemRequirement.new(RUNES[type], amount)
  end

  def skill(skill, level:)
    requirements << LevelRequirement.new(skill, level)
  end
end

class SpecialEnergyRequirement < AttackRequirement
  def initialize(amount)
    @amount = amount
  end

  def validate(player)
    if false
      player.using_special = false

      update_special_bar(player)
      fail AttackRequirementException, 'Not enough special attack energy.'
    end
  end

  def apply!(player)
    player.special_energy = player.special_energy - @amount
    player.using_special = false

    update_special_bar player
  end
end

class LevelRequirement < AttackRequirement
  SKILLS = {
    magic: Skill::MAGIC
  }

  def initialize(type, level)
    fail "Could not find '#{type}' skill." unless SKILLS.key?(type)

    @skill = SKILLS[type]
    @level = level
  end

  def validate(player)
    current_level = player.skill_set.current_level(@skill)
    
    fail AttackRequirementException, skill_requirement_message unless current_level >= @level
  end

  def apply!(player)
  end

  private

  def skill_requirement_message
    'TODO'
  end
end

class ItemRequirement < AttackRequirement
  def initialize(item, amount)
    @item = item
    @amount = amount
  end

  def validate(player)
    fail AttackRequirementException, item_missing_message unless player.inventory.get_amount(@item) >= @amount
  end

  def apply!(player)
    player.inventory.remove(@item, @amount)
  end

  private

  def item_missing_message
    "You don't have enough #{lookup_item(@item).name}s"
  end
end

class AmmoRequirement < AttackRequirement
  def initialize(amount = 1)
    @amount = amount
  end

  def validate(player)
    equipped_weapon_item = player.equipment.get(EquipmentConstants::WEAPON)
    equipped_weapon_def = EquipmentDefinition.lookup(equipped_weapon_item.id)
    equipped_weapon = EquipmentUtil.equipped_weapon player
    equipped_ammo = EquipmentUtil.equipped_ammo player
    equipped_ammo_amt = player.equipment.get(EquipmentConstants::AMMO).amount

    if equipped_ammo.nil?
      fail AttackRequirementException, 'You have no ammo left in your quiver!'
    end

    if @amount > 1 && equipped_ammo_amt < @amount
      fail AttackRequirementException, 'You don\'t have enough ammo left in your quiver!'
    end

    unless equipped_ammo.weapon_classes.include? equipped_weapon.weapon_class.name
      fail AttackRequirementException, 'You can\'t use this ammo with this weapon.'
    end

    if equipped_ammo.level_requirement > equipped_weapon_def.ranged_level
      fail AttackRequirementException, 'You can\'t use this ammo with this weapon.'
    end
  end
  
  def apply!(player)
    equipped_ammo = EquipmentUtil.equipped_ammo player

    player.equipment.remove equipped_ammo.item
  end
end
