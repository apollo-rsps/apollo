java_import 'org.apollo.game.model.inter.EnterAmountListener'
java_import 'org.apollo.game.model.inter.dialogue.DialogueAdapter'

class SelectAmountDialogueListener < DialogueAdapter
  include EnterAmountListener

  attr_reader :player, :action
  def initialize(player, action)
    super()
    @player = player
    @action = action
  end

  # Called when a button has been clicked whilst the dialogue was opened.
  def buttonClicked(button)
    amount = get_amount(button)

    return false if amount == 0

    interfaces = @player.interface_set
    interfaces.close

    if amount == :enter_amount
      interfaces.open_enter_amount_dialogue(self)
      return true
    end

    amount = action.calculate_maximum_actions if amount == :all

    execute(amount)
    true
  end

  def execute(amount)
    @action.set_amount(amount)
    @player.start_action(@action)
  end

  # Called when an amount of mixing actions has been entered.
  def amountEntered(amount)
    execute(amount) if amount > 0
  end

  # Gets the amount of actions based on the specified button id.
  def get_amount(button)
    case button
    when 2799 then return 1
    when 2798 then return 5
    when 1748 then return :enter_amount
    when 1747 then return :all
    else return 0
    end
  end
end