require 'java'

java_import 'org.apollo.cache.def.ItemDefinition'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.message.impl.SetWidgetTextMessage'
java_import 'org.apollo.game.message.handler.ItemVerificationHandler'
java_import 'org.apollo.game.model.inv.SynchronizationInventoryListener'
java_import 'org.apollo.game.model.inter.InterfaceListener'

# The hash of npc ids to Shops.
SHOPS = {}

# Creates the Shop from the specified Hash.
def create_shop(hash)
  unless hash.has_keys?(:items, :name, :npcs)
    fail 'Shop name, npcs, and items must be specified to create a shop.'
  end

  npcs, name = hash[:npcs], hash[:name]
  npcs = [npcs] unless npcs.is_a?(Array)
  currency = hash[:currency] || DEFAULT_CURRENCY

  options = hash[:npc_options] || [1]
  buys = hash[:buys] || :own

  items = hash.delete(:items).collect { |data| ShopItem.new(lookup_item(data[0]), data[1]) }
  shop = Shop.new(name, items, currency, options, buys)

  npcs.map { |name| lookup_npc(name) }.each { |npc| SHOPS[npc] = shop }
end

private

# The sidebar id for the inventory, when a Shop window is open.
INVENTORY_SIDEBAR = 3822

# The container id for the above inventory, when a Shop window is open.
INVENTORY_CONTAINER = 3823

# The Shop interface id.
SHOP_INTERFACE = 3824

# The container id for the Shop interface.
SHOP_CONTAINER = 3900

# The widget that displays the shop name.
SHOP_NAME_WIDGET = 3901

# The delay before a Shop is opened when the Player is in range of the Npc, in ticks.
SHOP_OPEN_DELAY = 0

# The distance, in tiles, the Player must reach before a Shop can be opened.
SHOP_DISTANCE = 1

# An `InventorySupplier` for a `Shop`.
class ShopInventorySupplier
  java_implements ItemVerificationHandler::InventorySupplier

  def getInventory(player)
    shop = player.open_shop
    shop == -1 ? nil : SHOPS[shop].inventory
  end

end

# An `InventorySupplier` for a `Player` with the shop window open.
class PlayerInventorySupplier
  java_implements ItemVerificationHandler::InventorySupplier

  def getInventory(player)
    player.open_shop == -1 ? nil : player.inventory
  end

end

ItemVerificationHandler.add_inventory(SHOP_CONTAINER, ShopInventorySupplier.new)
ItemVerificationHandler.add_inventory(INVENTORY_CONTAINER, PlayerInventorySupplier.new)

# A DistancedAction causing a Player to open a shop.
class OpenShopAction < DistancedAction
  attr_reader :player, :npc, :shop

  # Creates the OpenShopAction.
  def initialize(player, npc, shop)
    super(SHOP_OPEN_DELAY, true, player, npc.position, 1)
    @npc = npc
    @shop = shop
  end

  # Executes this DistancedAction, opening the shop.
  def executeAction
    mob.interacting_mob = @npc
    open_shop(mob, @npc.id)
    stop
  end

  # Returns whether or not this DistancedAction is equal to the specified Object.
  def equals(other)
    get_class == other.get_class && @npc == other.npc && @shop == other.shop
  end

end

# An InterfaceListener for when a Shop is closed.
class ShopCloseInterfaceListener
  java_implements InterfaceListener

  # Creates the ShopCloseInterfaceListener.
  def initialize(player, inventory_listener, shop_listener)
    @player = player
    @inventory_listener = inventory_listener
    @shop_listener = shop_listener
  end

  # Executed when the Shop interface is closed.
  def interface_closed
    @player.inventory.remove_listener(@inventory_listener)
    SHOPS[@player.open_shop].inventory.remove_listener(@shop_listener)

    @player.open_shop = -1
    @player.reset_interacting_mob
  end

end

# Intercept the npc action message.
on :message, :first_npc_action do |player, message|
  npc = $world.npc_repository.get(message.index)

  if SHOPS.key?(npc.id)
    shop = SHOPS[npc.id]

    valid = shop.npc_options.empty? || shop.npc_options.include?(message.option)
    player.start_action(OpenShopAction.new(player, npc, SHOPS[npc.id])) if valid
  end
end

# Opens the Shop registered to the specified npc.
def open_shop(player, npc)
  shop = SHOPS[npc]
  fail "No shop registered to npc #{npc} exists." if shop.nil?

  player.open_shop = npc

  inventory_listener = SynchronizationInventoryListener.new(player, INVENTORY_CONTAINER)
  shop_listener = SynchronizationInventoryListener.new(player, SHOP_CONTAINER)

  player_inventory, shop_inventory = player.inventory, shop.inventory

  player_inventory.add_listener(inventory_listener)
  player_inventory.force_refresh

  shop_inventory.add_listener(shop_listener)
  shop_inventory.force_refresh

  player.send(SetWidgetTextMessage.new(SHOP_NAME_WIDGET, shop.name))

  listener = ShopCloseInterfaceListener.new(player, inventory_listener, shop_listener)
  player.interface_set.open_window_with_sidebar(listener, SHOP_INTERFACE, INVENTORY_SIDEBAR)
end

# Intercept the Item action.
on :message, :item_action do |player, message|
  interface = message.interface_id

  if player.open_shop == -1 || !SHOPS.key?(player.open_shop)
    message.terminate
    next
  end

  if interface != INVENTORY_CONTAINER && interface != SHOP_CONTAINER
    message.terminate
    next
  end

  shop = SHOPS[player.open_shop]
  inventory = shop.inventory
  currency = shop.currency
  slot = message.slot

  player_inventory = player.inventory

  if interface == INVENTORY_CONTAINER
    id = message.id
    contains = inventory.contains(id)

    if !shop.buys == :none || shop.buys == :own && !contains
      player.send_message('You can\'t sell this item to this shop.')
      message.terminate
      next
    end

    if !contains && inventory.free_slots == 0
      player.send_message('The shop is currently full at the moment.')
      message.terminate
      next
    end

    item = player_inventory.get(slot)
    value = currency.sell_value(id)

    option = message.option
    if option == 1
      player.send_message("#{item.definition.name}: shop will buy for #{value} #{currency.name}.")
      next
    end

    sell_amount = case option
                    when 2 then 1
                    when 3 then 5
                    when 4 then 10
                    else next
                  end

    available = player_inventory.get_amount(id)
    sell_amount = available if sell_amount > available

    total_value = (value * sell_amount).floor

    player_inventory.remove(id, sell_amount)
    inventory.add(id, sell_amount)
    currency.add(player, total_value) if total_value > 0

    message.terminate
  elsif interface == SHOP_CONTAINER
    buy(shop, player, message, currency)
  end
end

# Buys the item from the `Shop`.
def buy(shop, player, message, currency)
  inventory, slot = shop.inventory, message.slot
  shop_item, invent_item = shop.items[slot], inventory.get(slot)

  id = shop_item.id

  option = message.option
  if option == 1
    player.send_message("#{shop_item.name}: currently costs #{shop_item.cost} #{currency.name}.")
    return
  end

  buy_amount = case option
                 when 2 then 1
                 when 3 then 5
                 when 4 then 10
                 else next
               end

  no_stock = false
  if buy_amount > invent_item.amount
    buy_amount = invent_item.amount
    no_stock = true
  end

  player_inventory = player.inventory
  has_item = player_inventory.get_amount(id) == 0

  definition = invent_item.definition
  space_required = if definition.stackable && has_item then 0
                   elsif !definition.stackable then buy_amount
                   else 1
                   end

  free_slots = player_inventory.free_slots
  not_enough_space = false

  if space_required > free_slots
    not_enough_space = true
    buy_amount = free_slots
  end

  total_currency = shop.currency.total(player)
  too_poor = false
  total_cost = buy_amount * shop_item.cost

  if total_cost > total_currency
    buy_amount = (total_currency / shop_item.cost).floor
    too_poor = true
  end

  if buy_amount > 0
    currency.remove(player, buy_amount * shop_item.cost)
    player_inventory.add(id, buy_amount)

    keep = invent_item.amount == buy_amount && shop.buys == :own
    keep ? inventory.set(slot, Item.new(id, 0)) : inventory.remove(id, buy_amount)
  end

  warning = if too_poor then "You don't have enough #{currency.name}."
            elsif no_stock then 'The shop has run out of stock.'
            elsif not_enough_space then 'You don\'t have enough inventory space.'
            end

  player.send_message(warning) unless warning.nil?
  message.terminate
end

# Declares the open_shop attribute, which contains the id of the currently open shop.
declare_attribute(:open_shop, -1)
