require 'java'

java_import 'org.apollo.game.model.Item'

class ShopItem
  attr_reader :item, :cost, :general

  def initialize(id, amount, cost = -1)
    @item = Item.new id, amount
    @cost = cost == -1 ? @item.definition.value : cost
    @general = false
  end

  def id
    return @item.id
  end

  def amount
    return @item.amount
  end

  def name
    return @item.definition.name
  end

end