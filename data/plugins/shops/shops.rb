require 'java'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.event.impl.SetWidgetTextEvent'
java_import 'org.apollo.game.event.impl.UpdateItemsEvent'
java_import 'org.apollo.game.event.impl.ServerMessageEvent'

java_import 'org.apollo.game.model.inter.InterfaceListener'
java_import 'org.apollo.game.model.inv.SynchronizationInventoryListener'
java_import 'org.apollo.game.model.def.ItemDefinition'

SHOPS = {}

def create_shop(hash)
  raise 'Shop name and items must be specified to create a shop.' unless hash.has_key?(:npc_id) && hash.has_key?(:name) && hash.has_key?(:items)

  npc_id = hash.delete(:npc_id)
  shop = Shop.new hash.delete(:name), hash.delete(:items).collect { |data| ShopItem.new(*data) }

  hash.each do |key, value|
    if key == :currency
      shop.currency = value
    elsif key == :npc_options
      shop.npc_options = value
    elsif key == :buy_which_items
      shop.buy_which_items = value
    end
  end

  SHOPS[npc_id] = shop
end

class OpenShopAction < DistancedAction
  attr_reader :player, :npc, :shop

  def initialize(player, npc, shop)
    super(0, true, player, npc.position, 1)

    @player = player
    @npc = npc
    @shop = shop
  end

  def executeAction
    @player.set_interacting_mob(@npc)
    open_shop(@player, @npc.id)
    stop
  end

  def equals(other)
    return (@npc == other.npc && @shop == other.shop)
  end

end

class ShopCloseInterfaceListener
  include InterfaceListener

  def initialize(player, inventory_listener, shop_listener)
    @player = player
    @inventory_listener = inventory_listener
    @shop_listener = shop_listener
  end

  def interface_closed
    @player.inventory.remove_listener(@inventory_listener)
    SHOPS[@player.open_shop].inventory.remove_listener(@shop_listener)
    @player.open_shop = -1
    @player.reset_interacting_mob()
  end

end

on :event, :npc_action do |ctx, player, event|
  npc = World.world.npc_repository.get(event.index)

  if SHOPS.has_key?(npc.id)
    shop = SHOPS[npc.id]

    if shop.npc_options.empty? or shop.npc_options.include?(event.option)
      player.start_action OpenShopAction.new(player, npc, SHOPS[npc.id])
    end
  end

end

def open_shop(player, npc_id)
  if(!SHOPS.has_key?(npc_id))
    raise "no shop with the npc id of #{npc_id} exists"
  end
  shop = SHOPS[npc_id]

  player.open_shop = npc_id

  inventory_listener = SynchronizationInventoryListener.new(player, 3823)
  shop_listener = SynchronizationInventoryListener.new(player, 3900)

  player.inventory.add_listener(inventory_listener)
  player.inventory.force_refresh()

  shop.inventory.add_listener(shop_listener)
  shop.inventory.force_refresh()

  player.send(SetWidgetTextEvent.new(3901, shop.name))
  player.interface_set.open_window_with_sidebar(ShopCloseInterfaceListener.new(@player, inventory_listener, shop_listener), 3824, 3822)
end

on :event, :item_action do |ctx, player, event|

  if event.interface_id == 3823 or event.interface_id == 3900
    if player.open_shop == -1 or !SHOPS.has_key?(player.open_shop) or !valid_inventory_action(event.interface_id == 3823 ? player.inventory : SHOPS[player.open_shop].inventory, event)
      ctx.break_handler_chain()
      return
    end
  end

  shop = SHOPS[player.open_shop]

  ##selling
  if event.interface_id == 3823

    if !shop.buy_which_items == :none or shop.buy_which_items == :own and !shop.inventory.contains(event.id)
      player.send_message("You can't sell this item to this shop.")
      ctx.break_handler_chain()
      return
    end

    if !shop.inventory.contains(event.id) and shop.inventory.free_space == 0
      player.send_message("The shop is currently full at the moment.")
      ctx.break_handler_chain()
      return
    end

    item = player.inventory.get(event.slot)
    value = shop.currency.sell_value(player, event.id)

    sell_amount = 0
    case event.option
      when 1
        player.send_message("#{item.definition.name}: shop will buy for #{value} #{shop.currency.name}.")
      when 2
        sell_amount = 1
      when 3
        sell_amount = 5
      when 4
        sell_amount = 10
    end

    shop.inventory.add(event.id, sell_amount)
    player.inventory.remove(event.id, sell_amount)

    total_value = (value * sell_amount).floor
    if total_value > 0
      shop.currency.add(player, total_value)
    end

    ctx.break_handler_chain()
  end

  ## buying
  if event.interface_id == 3900

    shop_item = shop.items[event.slot]
    invent_item = shop.inventory.get(event.slot)

    buy_amount = 0
    if event.option == 1
      player.send_message("#{shop_item.name}: currently costs #{shop_item.cost} #{shop.currency.name}.")
    elsif event.option == 2
      buy_amount = 1
    elsif event.option == 3
      buy_amount = 5
    elsif event.option == 4
      buy_amount = 10
    end

    # stock checking
    no_stock_warning = false
    if buy_amount > invent_item.amount
      buy_amount = invent_item.amount
      no_stock_warning = true
    end

    # inventory space checking
    has_item = player.inventory.get_amount(shop_item.id) == 0
    free_slots = player.inventory.free_slots()
    space_required = 1

    if invent_item.definition.stackable and has_item
      space_required = 0
    elsif !invent_item.definition.stackable
      space_required = buy_amount
    end

    not_enough_space = false
    if space_required > free_slots
      not_enough_space = true
      buy_amount = free_slots
    end

    # enough currency checking
    total_currency = shop.currency.total(player)
    not_enough_currency = false
    total_cost = buy_amount * shop_item.cost
    if(total_cost > total_currency)
      buy_amount = (total_currency / shop_item.cost).floor
      not_enough_currency = true
    end

    # buying of the item
    if buy_amount > 0
      shop.currency.remove(player, buy_amount * shop_item.cost)
      player.inventory.add(shop_item.id, buy_amount)
      if invent_item.amount == buy_amount and !shop_item.general
        shop.inventory.set(event.slot, Item.new(invent_item.id, 0))
      else
        shop.inventory.remove(shop_item.id, buy_amount)
      end
    end

    if no_stock_warning
      player.send_message("The shop has run out of stock.")
    elsif not_enough_currency
      player.send_message("You don't have enough #{shop.currency.name}.")
    elsif not_enough_space
      player.send_message("You don't have enough inventory space.")
    end

    ctx.break_handler_chain()
  end
end

on :logout do |player|
  if player.open_shop > -1
    player.interface_set.close()
  end
end

# shop attributes
declare_attribute(:open_shop, -1)

## shop examples

# regular shop
create_shop :npc_id => 0, :name => 'test shop', :items => [[1040, 10]]

# the shop will only open when the option 1(first click) of the npc is clicked otherwise open_shop(player, 1) would be
# used to open it
create_shop :npc_id => 1, npc_options => [1], :name => 'test shop', :items => [[1040, 10]]

# a shop which accepts santa hats. usage Currency.new(item_id) you can override the Currency class to accept your
# own type of payment for example pk points. the currency defaults to coins
create_shop :npc_id => 2, :currency => Currency.new(1050), :name => 'test shop', :items => [[1040, 10]]

# a shop which will buy all items using :buy_which_items flag. :buy_which_items accepts :none, :all and :own
create_shop :npc_id => 3, :buy_which_items => :all, :name => 'test shop', :items => [[1040, 10]]