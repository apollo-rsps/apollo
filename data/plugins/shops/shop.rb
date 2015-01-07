require 'java'

java_import 'org.apollo.game.model.inv.Inventory'
java_import 'org.apollo.game.model.inv.InventoryConstants'
java_import 'org.apollo.game.model.inter.InterfaceListener'
java_import 'org.apollo.game.model.def.ItemDefinition'

# the shop constants.
@@SIDEBAR_INVENTORY_ID = 3823;
@@SHOP_INVENTORY_ID = 3900;
@@SHOP_WINDOW_ID = 3824;
@@SIDEBAR_ID = 3822;
@@WIDGET_TEXT_ID = 3901;

# shop inventory constants
@@SHOP_CAPACITY = 40
@@FULL_SHOP_MESSAGE = "This shop does not have enough space."

# the shops.
SHOPS = {}

# represents a single shop.
class Shop
	attr_reader :id, :name, :stock, :inventory
	attr_accessor :currency, :type, :npc_action

	def initialize(id, name, stock)
		@id = id
		@name = name
		@stock = stock
		@inventory = Inventory.new(@@SHOP_CAPACITY, Inventory::StackMode::STACK_ALWAYS)
		stock.each { |item|
			@inventory.add(item.id, item.amount)
		}
		@currency = @@DEFAULT_CURRENCY
		@type = :normal
		@npc_action = 2
	end

	# handles buying an item from a shop.
	def buy(player, slot, id, amount)
		item  = inventory.get(slot)
		total_currency = currency.total(player)
		value = currency.buy_value(item.id)
		if(amount == 0)
			player.send_message("#{item.definition.name} is currently valued at #{value} #{currency.name}.")
			return
		end
		if(player.inventory.free_slots == 0 and !(player.inventory.contains(item.id) and ItemDefinition.lookup(item.id).stackable))
			player.inventory.forceCapacityExceeded
			return
		end
		if(amount > item.amount and type != :supplier)
			amount = item.amount
			player.send_message("The shop has ran out of stock.")
		end
		total = amount * value
		if(total > total_currency)
			amount = (total_currency / value).floor
			total = amount * value
			player.send_message("You do not have enough #{currency.name}.")
		end
		if(amount > 0)
			if(currency.remove(player, total) == total)
				player.inventory.add(item.id, amount)
				unless(type == :supplier)
					if(item.amount == amount and stock[slot] != nil)
	                    inventory.set(slot, Item.new(item.id, 0))
					else
						inventory.remove(item.id, amount)
					end
				end
			end
		end
	end

	# handles selling an item to a shop.
	def sell(player, slot, id, amount)
		item  = player.inventory.get(slot)
		item_amount = player.inventory.amount(item.id)
		value = currency.sell_value(item.id)
		if(type == :closed or type == :supplier)
			player.send_message("You cannot sell items to this shop.")
			return
		end
		if(type == :normal and !inventory.contains(item.id))
			player.send_message("You cannot sell this item to this shop.")
			return
		end
		if(amount == 0)
			player.send_message("#{item.definition.name} is currently valued at #{value} #{currency.name}.")
			return
		end
		if(inventory.free_slots == 0 and !inventory.contains(item.id))
			inventory.forceCapacityExceeded
			return
		end
		if(amount > item_amount)
			amount = item_amount
		end
		total = value * amount
		if(player.inventory.remove(item.id, amount) == amount)
			currency.add(player, total)
			inventory.add(item.id, amount)
		end
	end
end

# the shop interface listener.
class ShopInterfaceListener 
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


