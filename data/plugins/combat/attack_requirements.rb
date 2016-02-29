java_import 'org.apollo.cache.def.ItemDefinition'

class AttackRequirementException < Exception
  attr_reader :message

  def initialize(message)
    @message = message
  end
end

class AttackRequirement
  def validate!(player)
    throw RuntimeError.new('validate! not implemented')
  end

  def apply(player)
    throw RuntimeError.new('apply not implemented')
  end
end

class SpecialEnergyRequirement < AttackRequirement
  def initialize(amount)
    @amount = amount
  end

  def validate!(player)
    throw AttackRequirementException.new('Not enough special attack energy.') unless player.special_energy >= @amount
  end

  def apply(player)
    player.special_energy = player.special_energy - @amount
  end
end

class ItemRequirement < AttackRequirement
  def initialize(item, amount)
    @item   = item
    @amount = amount
  end

  def validate!(player)
    throw AttackRequirementException.new(item_missing_message) unless player.inventory.get_amount(@item) >= @amount
  end

  def apply(player)
    player.inventory.remove(@item, @amount)
  end

  private

  def item_missing_message
    definition = ItemDefinition.lookup(@item)

    "You don't have enough #{lookup_item(@item).name}s"
  end
end
