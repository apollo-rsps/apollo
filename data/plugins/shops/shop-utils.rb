require 'java'

java_import 'org.apollo.game.model.Item'
java_import 'org.apollo.game.model.inter.InterfaceListener'
java_import 'org.apollo.game.model.inv.FullInventoryListener'
java_import 'org.apollo.game.model.inv.SynchronizationInventoryListener'
java_import 'org.apollo.game.message.impl.SetWidgetTextMessage'

# creates a shop (thanks stuart)
def create_shop(hash)
  raise 'Shop id, name, and stock must be specified to create a shop.' unless hash.has_key?(:id) && hash.has_key?(:name) && hash.has_key?(:stock)
	id = hash.delete(:id)
	shop = Shop.new id, hash.delete(:name), hash.delete(:stock).collect { |data| Item.new(*data) }
	hash.each do |key, value|
    if key == :currency
      shop.currency = value
    elsif key == :npc_action
     	shop.npc_action = value
    elsif key == :type
      shop.type = value
    end
  end
  SHOPS[id] = shop
end

# opens a shop by their id.
def open_shop(player, id)
	if(!SHOPS.has_key?(id))
    raise "no shop with the id of #{id} exists"
  end
  shop = SHOPS[id]
	player.open_shop = id
  inv_listener = SynchronizationInventoryListener.new(player, @@SIDEBAR_INVENTORY_ID)
  shop_listener = SynchronizationInventoryListener.new(player, @@SHOP_INVENTORY_ID)
  full_shop_listener = FullInventoryListener.new(player, @@FULL_SHOP_MESSAGE)
  player.inventory.add_listener(inv_listener)
  shop.inventory.add_listener(shop_listener)
  shop.inventory.add_listener(full_shop_listener)
  player.inventory.force_refresh()
  shop.inventory.force_refresh()
  player.send(SetWidgetTextMessage.new(@@WIDGET_TEXT_ID, shop.name))
  player.interface_set.open_window_with_sidebar(ShopInterfaceListener.new(player, inv_listener, shop_listener), @@SHOP_WINDOW_ID, @@SIDEBAR_ID)
end

# shop attributes
declare_attribute(:open_shop, -1)

# create shop examples
create_shop :id => 945, :name => 'Ranging Guild Ticket Exchange', :type => :supplier, :currency => Currency.new(1464), :npc_action => 1, :stock => [[47, 30], [1133, 1], [892, 50], [1169, 1], [1135, 1], [829, 20]]