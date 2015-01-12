require 'java'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.util.TextUtil'

java_import 'org.apollo.game.message.impl.SetPlayerActionMessage'
java_import 'org.apollo.game.message.impl.PrivacyOptionMessage'

# the player interaction.
on :message, :fourth_player_action do |ctx, player, message|
  acquaintance = $world.player_repository.get(message.index)
  puts player.wants_to_trade
  puts player.trade_stage
  if player.trade_stage == -1 and acquaintance.trade_stage == -1 
    if player.trade == nil and acquaintance.trade == nil
      player.startAction TradeAction.new(player, acquaintance)
    end
  end
end

# A distanced action to start a trade.
class TradeAction < DistancedAction
  attr_reader :player, :acquaintance
  
  def initialize(player, acquaintance)
    super 0, true, player, acquaintance.position, 1
    @player = player
    @acquaintance = acquaintance
  end
  
  def executeAction
    if (acquaintance.interface_set.size > 0)
      player.send_message("#{acquaintance.username} is busy at the moment.")
    elsif acquaintance.wants_to_trade
      if player.trade == nil and acquaintance.trade == nil
        player.trade = Trade.new(player, acquaintance)
        acquaintance.trade = Trade.new(acquaintance, player)
        if player.trade_stage == 0 && acquaintance.trade_stage == 0
          player.trade.start_trade
          acquaintance.trade.start_trade
        end
      end
    elsif !player.wants_to_trade and !acquaintance.wants_to_trade
        acquaintance.send_message TextUtil.capitalize(player.username)+":tradereq:"
        player.send_message "Sending trade offer..."
        player.wants_to_trade = true;
    end
    stop
  end
end

# gets the amount to offer based on the option.
def optionToOffer(option)
  case option
    when 1 then return 1
    when 2 then return 5
    when 3 then return 10
    when 4 then return 2147483647
    when 5 then return -1
  end
end

# the item interaction.
on :message, :item_action do |ctx, player, message|
  if player.interface_set.contains(3323)
    acquaintance = player.trade.acquaintance
    unless player.trade_stage == 1 or acquaintance.trade_stage == 1
      ctx.break_handler_chain()
      return
    end
    unless valid_inventory_action(message.interface_id == 3322 ? player.inventory : player.trade.inventory, message)
      ctx.break_handler_chain()
      return
    end
    amount = optionToOffer(message.option)
    if message.interface_id == 3322
      if (amount == -1)
        player.get_interface_set.open_enter_amount_dialogue(TradeOfferEnterAmountListener.new(player, message.slot, message.id))
      else
        player.trade.offer(player, message.slot, message.id, amount)
      end
      ctx.break_handler_chain()
    elsif message.interface_id == 3415
      if (amount == -1)
        player.get_interface_set.open_enter_amount_dialogue(TradeWithdrawEnterAmountListener.new(player, message.slot, message.id))
      else
        player.trade.withdraw(player, message.slot, message.id, amount)
      end
      ctx.break_handler_chain()
    end
  end
end

# Intercept the button message.
on :message, :button do |ctx, player, message|
  if message.widget_id == 3420 or message.widget_id == 3546
    player.trade.accept
    ctx.break_handler_chain
  end
end

#handles the decline button.
on :message, :closed_interface do |ctx, player, evt|
  unless player.trade == nil
    player.trade.decline
  end
end

# sets the trade menu
on :login do |player|
  player.send(SetPlayerActionMessage.new("Trade with", 4, false));
end

# handles logouts
on :logout do |player|
  unless player.trade == nil
    player.trade.decline
  end
end