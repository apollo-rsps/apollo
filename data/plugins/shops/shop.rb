require 'java'

java_import 'org.apollo.game.model.Inventory'
java_import 'org.apollo.game.model.def.ItemDefinition'

class Shop
  attr_reader :name, :items, :inventory
  attr_accessor :currency, :buy_which_items, :npc_options

  def initialize(name, items)
    @name = name
    @items = items

    @npc_options = []
    @inventory = Inventory.new(30, Inventory::StackMode::STACK_ALWAYS)

    # populate our current inventory
    items.each { |i| @inventory.add(i.id, i.amount) }

    # set default values
    @currency = @@DEFAULT_CURRENCY
    @buy_which_items = :own
  end

end