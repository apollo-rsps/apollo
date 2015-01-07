require 'java'

java_import 'org.apollo.game.model.def.ItemDefinition'

class Currency
	attr_reader :id, :currency, :name

	def initialize(id)
		@currency = ItemDefinition.lookup(id)
	end

	def name()
		if not currency.name.end_with?('s') then return currency.name.downcase+'s' end
		return currency.name.downcase
	end

	def buy_value(item)
		return (ItemDefinition.lookup(item).value / currency.value).floor
	end

	def sell_value(item)
		return ((ItemDefinition.lookup(item).value / currency.value) * 0.60).floor
	end

	def total(player)
		return player.inventory.amount(currency.id)
	end

	def add(player, amount)
		return player.inventory.add(currency.id, amount)
	end

	def remove(player, amount)
		return player.inventory.remove(currency.id, amount)
	end
end

@@DEFAULT_CURRENCY = Currency.new(995)
