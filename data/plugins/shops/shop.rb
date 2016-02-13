require 'java'

java_import 'org.apollo.game.model.inv.Inventory'

# A shop containing items that can be sold.
class Shop
  attr_reader :buys, :currency, :items, :inventory, :name, :npc_options

  def initialize(name, items, currency, options, buys)
    @name = name
    @items = items
    @currency = currency
    @buys = buys
    @npc_options = options
    @inventory = Inventory.new(DEFAULT_CAPACITY, Inventory::StackMode::STACK_ALWAYS)

    items.each { |item| @inventory.add(item.id, item.amount) }
  end

end

private

# The `Currency` used by default.
DEFAULT_CURRENCY = Currency.new(995, 'coins')

# The default capacity of a shop.
DEFAULT_CAPACITY = 30
