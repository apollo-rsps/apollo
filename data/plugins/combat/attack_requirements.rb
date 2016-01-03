java_import 'org.apollo.cache.def.ItemDefinition'
java_import 'org.apollo.game.model.entity.EquipmentConstants'
java_import 'org.apollo.cache.def.EquipmentDefinition'

class AttackRequirementException < Exception
  attr_reader :message

  def initialize(message)
    @message = message
  end
end

class AttackRequirement
  def validate(_player)
    throw RuntimeError.new('validate! not implemented')
  end

  def apply!(_player)
    throw RuntimeError.new('apply not implemented')
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
      fail AttackRequirementException.new('Not enough special attack energy.')
    end
  end

  def apply!(player)
    player.special_energy = player.special_energy - @amount
    player.using_special  = false

    update_special_bar player
  end
end

class ItemRequirement < AttackRequirement
  def initialize(item, amount)
    @item   = item
    @amount = amount
  end

  def validate(player)
    throw AttackRequirementException.new(item_missing_message) unless player.inventory.get_amount(@item) >= @amount
  end

  def apply!(player)
    player.inventory.remove(@item, @amount)
  end

  private

  def item_missing_message
    definition = ItemDefinition.lookup(@item)

    "You don't have enough #{lookup_item(@item).name}s"
  end
end

class AmmoRequirement < AttackRequirement
  def initialize(amount = 1)
    @amount = amount
  end

  def validate(player)
    equipped_weapon_item = player.equipment.get(EquipmentConstants::WEAPON)
    equipped_weapon_def  = EquipmentDefinition.lookup(equipped_weapon_item.id)
    equipped_weapon      = EquipmentUtil.equipped_weapon player
    equipped_ammo        = EquipmentUtil.equipped_ammo player
    equipped_ammo_amt    = player.equipment.get(EquipmentConstants::AMMO).amount

    if equipped_ammo.nil?
      fail AttackRequirementException.new('You have no ammo left in your quiver!')
    end

    if @amount > 1 && equipped_ammo_amt < @amount
      fail AttackRequirementException.new('You don\'t have enough ammo left in your quiver!')
    end

    unless equipped_ammo.weapon_classes.include? equipped_weapon.weapon_class.name
      fail AttackRequirementException.new('You can\'t use this ammo with this weapon.')
    end

    if equipped_ammo.level_requirement > equipped_weapon_def.ranged_level
      fail AttackRequirementException.new('You can\'t use this ammo with this weapon.')
    end
  end

  def apply!(player)
    equipped_ammo = EquipmentUtil.equipped_ammo player

    player.equipment.remove equipped_ammo.item
  end
end
