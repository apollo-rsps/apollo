require 'java'

java_import 'org.apollo.cache.def.ItemDefinition'

# A currency that can be used to purchase items in a Shop.
class Currency
  attr_reader :name

  # Creates the Currency.
  def initialize(id, name = ItemDefinition.lookup(id).name)
    fail 'Currency must have a name.' if name.nil?
    @id = id
    @name = name.to_s
  end

  # Adds the specified amount of this `Currency` to the specified `Player`'s inventory.
  def add(player, amount)
    player.inventory.add(@id, amount)
  end

  # Removes the specified amount of this `Currency` from the specified `Player`'s inventory.
  def remove(player, amount)
    player.inventory.remove(@id, amount)
  end

  # Gets the amount of this Currency in the specified player's inventory.
  def total(player)
    player.inventory.get_amount(@id)
  end

  def sell_value(id)
    (ItemDefinition.lookup(id).value * 0.60).floor
  end

end
