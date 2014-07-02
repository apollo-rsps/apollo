require 'java'
java_import 'org.apollo.game.model.def.ItemDefinition'

class Currency

  def initialize(item_id)
    @item_id = item_id
  end

  def add(player, amount)
    return player.inventory.add(@item_id, amount)
  end

  def remove(player, amount)
    return player.inventory.remove(@item_id, amount)
  end

  def total(player)
    return player.inventory.get_amount(@item_id)
  end

  def name
    return ItemDefinition.lookup(@item_id).name
  end

  def sell_value(player, item_id)
    return (ItemDefinition.lookup(item_id).value * 0.60).floor
  end

end

@@DEFAULT_CURRENCY = Currency.new(995)