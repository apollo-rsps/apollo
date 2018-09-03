require 'java'

java_import 'org.apollo.cache.def.ItemDefinition'

java_import 'org.apollo.game.model.Item'

# An Item in a Shop.
class ShopItem
  attr_reader :amount, :cost, :id, :name

  # Creates the ShopItem.
  def initialize(id, amount, cost = nil)
    definition = ItemDefinition.lookup(id)
    @id = id
    @amount = amount
    @cost = cost.nil? ? definition.value : cost
    @name = definition.name
  end

end
