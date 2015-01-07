require 'java'

java_import 'org.apollo.game.action.DistancedAction'

# A distanced action to open a shop.
class ShopAction < DistancedAction
  attr_reader :player, :position, :id

  def initialize(player, position, id)
    super(0, true, player, position, 1)
    @player = player
    @position = position
    @id = id
  end

  def executeAction
    player.turn_to(position)
    open_shop(player, id)
    stop
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position)
  end
end

on :message, :npc_action do |ctx, player, message|
  npc = $world.npc_repository.get(message.index)
  if SHOPS.has_key?(npc.id)
    shop = SHOPS[npc.id]
    if shop.npc_action == message.option
      player.start_action(ShopAction.new(player, npc.position, npc.id))
    end
  end
end

def optionToAmount(option)
  case option
    when 1 then 0
    when 2 then 1
    when 3 then 5
    when 4 then 10
  end
end

on :message, :item_action do |ctx, player, message|
  amount = optionToAmount(message.option)
  shop = SHOPS[player.open_shop]
  if player.open_shop == -1 or !SHOPS.has_key?(player.open_shop) or !valid_inventory_action(message.interface_id == @@SIDEBAR_INVENTORY_ID ? player.inventory : SHOPS[player.open_shop].inventory, message)
    ctx.break_handler_chain()
    return
  end
  if player.interface_set.contains(@@SHOP_WINDOW_ID)
    if(message.interface_id == @@SIDEBAR_INVENTORY_ID)
      shop.sell(player, message.slot, message.id, amount)
      ctx.break_handler_chain()
    elsif(message.interface_id == @@SHOP_INVENTORY_ID)
      shop.buy(player, message.slot, message.id, amount)
      ctx.break_handler_chain()
    end
  end
end

on :logout do |player|
  player.interface_set.close if player.open_shop > -1
end
