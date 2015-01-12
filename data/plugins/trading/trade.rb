require 'java'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.Item'
java_import 'org.apollo.game.model.inter.InterfaceListener'
java_import 'org.apollo.game.model.inter.EnterAmountListener'
java_import 'org.apollo.game.message.impl.OpenInterfaceSidebarMessage'
java_import 'org.apollo.game.message.impl.SetWidgetTextMessage'
java_import 'org.apollo.game.message.impl.SetPlayerActionMessage'
java_import 'org.apollo.game.message.impl.UpdateItemsMessage'
java_import 'org.apollo.util.TextUtil'

class Trade
  attr_reader :player, :acquaintance, :inventory

  def initialize(player, acquaintance)
    @player = player
    @acquaintance = acquaintance
    @inventory = inventory = Inventory.new(28)
    player.trade_stage = 0
  end

  # starts trading by loading the trade interface.
  def start_trade()
    player.trade_stage = 1
    invent_listener = SynchronizationInventoryListener.new(player, 3322)
    trade_listener = SynchronizationInventoryListener.new(player, 3415)
    acquaintance_trade_listener = SynchronizationInventoryListener.new(acquaintance, 3416)
    player.inventory.add_listener(invent_listener)
    inventory.add_listener(trade_listener)
    inventory.add_listener(acquaintance_trade_listener)
    player.inventory.force_refresh()
    inventory.force_refresh()
    player.send SetWidgetTextMessage.new(3417, "Trading with "+TextUtil.capitalize(acquaintance.username))
    player.send SetWidgetTextMessage.new(3431, "")
    player.interface_set.open_window_with_sidebar TradeInterfaceListener.new(player, invent_listener, trade_listener), 3323, 3321
  end

  # sends an offer
  def offer(player, slot, id, amount)
    item = player.inventory.get(slot)
    item_amount = player.inventory.amount(item.id)
    if(amount == 0)
      return
    end
    if(item == nil)
      return
    end
    if(inventory.free_slots == 0 and !inventory.contains(item.id))
      inventory.forceCapacityExceeded();
      return
    end
    if(amount > item_amount)
      amount = item_amount
    end
    if(player.inventory.remove(item.id, amount) == amount)
      inventory.add(item.id, amount)
    end
  end

  # withdraws an offer
  def withdraw(player, slot, id, amount)
    item = inventory.get(slot)
    item_amount = inventory.amount(item.id)
    if(amount == 0)
      return
    end
    if(item == nil)
      return
    end
    if(amount >= item_amount)
      amount = item_amount
    end
    if(player.inventory.free_slots == 0 and !(player.inventory.contains(item.id) and ItemDefinition.lookup(item.id).stackable))
      player.inventory.forceCapacityExceeded
      return
    end
    if(inventory.remove(item.id, amount) == amount)
      player.inventory.add(item.id, amount)
    end
  end

  #confirms the trade
  def confirm()
    player.send SetWidgetTextMessage.new(3535, "Are you sure you want to make this trade?")
    player.send SetWidgetTextMessage.new(3558, "Absolutely nothing!")
    player.send SetWidgetTextMessage.new(3557, "Absolutely nothing!")
    if player.trade.inventory.size > 0
      player.send SetWidgetTextMessage.new(3557, item_list(player))
    end
    if acquaintance.trade.inventory.size > 0
      player.send SetWidgetTextMessage.new(3558, item_list(acquaintance))
    end
    player.interface_set.open_window_with_sidebar(3443, 3321);
    player.trade_stage = 3
  end

  # completes the trade.
  def complete()
    if (player.trade.inventory.size < acquaintance.inventory.free_slots)
      player.trade.give_out_items(player, acquaintance)
    else
      player.inventory.forceCapacityExceeded
      player.trade.give_out_items(player, player)
      player.trade.refresh
    end
    player.trade.refresh
    player.interface_set.close
  end

  # handles pressing accept on trade interface.
  def accept()
    if acquaintance.trade_stage == 2
      player.trade.confirm
      acquaintance.trade.confirm
    elsif acquaintance.trade_stage == 4
      player.trade.complete
      acquaintance.trade.complete
    elsif player.trade_stage == 3
      player.trade_stage = 4
      player.send(SetWidgetTextMessage.new(3535, "Waiting for other player."));
      acquaintance.send(SetWidgetTextMessage.new(3535, "Other player has accepted."));
    elsif player.trade_stage == 1
      player.trade_stage = 2
      player.send(SetWidgetTextMessage.new(3431, "Waiting for other player."));
      acquaintance.send(SetWidgetTextMessage.new(3431, "Other player has accepted."));
    end
  end

  # handles declines.
  def decline()
    acquaintance.send_message("#{player.username} declined the trade.")
    player.send_message("You declined the trade.")
    acquaintance.interface_set.close
    player.interface_set.close
    acquaintance.trade.give_out_items(acquaintance, acquaintance)
    player.trade.give_out_items(player, player)
    acquaintance.trade.refresh
    player.trade.refresh
  end

  # handles refreshing trade variables.
  def refresh()
    player.trade_stage = -1
    player.wants_to_trade = false
    player.trade = nil
  end

  # gives out the items.
  def give_out_items(from, to)
    from.trade.inventory.items.each do | item |
      unless item == nil
        to.inventory.add(item)
      end
    end
  end

  # handles the item list on confirm screen (thanks shamon king.)
  def item_list(player)
    list = []
    text = ""
    player.trade.inventory.items.each do | item |
      unless item == nil
        item_def = item.get_definition
        unless item_def == nil
          item_amount = player.trade.inventory.get_amount(item.id)
          formated_amount = item_amount.to_s.gsub(/(\d)(?=(\d\d\d)+(?!\d))/, "\\1#{','}")
          list << "#{item_def.name} x #{formated_amount}\\n"
        end
      end
    end
    list.uniq!
    list.each do | words |
      text << words;
    end
    return text
  end
end

# the trade interface listeners.
class TradeInterfaceListener 
  include InterfaceListener

  def initialize(player, inventory_listener, trade_listener)
    @player = player
    @inventory_listener = inventory_listener
    @trade_listener = trade_listener
  end

  def interface_closed
    @player.inventory.remove_listener(@inventory_listener)
    @player.trade.inventory.remove_listener(@trade_listener)
  end
end

class TradeOfferEnterAmountListener
  include EnterAmountListener

  def initialize(player, slot, id)
    @player = player
    @slot = slot
    @id = id
  end

  def amountEntered(amount)
    @player.trade.offer(@player, @slot, @id, amount)
  end
end

class TradeWithdrawEnterAmountListener
  include EnterAmountListener

  def initialize(player, slot, id)
    @player = player
    @slot = slot
    @id = id
  end

  def amountEntered(amount)
    @player.trade.withdraw(@player, @slot, @id, amount)
  end
end

class Player
  attr_accessor :trade
  @trade
end

declare_attribute(:trade_stage, -1)
declare_attribute(:wants_to_trade, false)
