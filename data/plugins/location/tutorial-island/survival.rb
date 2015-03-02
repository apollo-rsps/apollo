require 'java'

java_import 'org.apollo.game.message.impl.FlashTabInterfaceMessage'
java_import 'org.apollo.game.message.impl.FlashingTabClickedMessage'
java_import 'org.apollo.game.message.impl.SwitchTabInterfaceMessage'

private

# The Survival Expert Npc.
@survival_expert = spawn_npc :name => :survival_expert, :x => 3104, :y => 3095, :face => :north

# The inventory tab index.
INVENTORY_TAB_INDEX = 3

# The inventory tab id.
INVENTORY_TAB_ID = 3213

# The id of the tree the Player will cut down.
TREE_ID = 3033

# TODO prevent axe equipping

# The conversation with the Survival Expert, when on tutorial island.
conversation :tutorial_surivival_expert do

  dialogue :introduction do
    type :npc_speech
    npc :survival_expert

    precondition { |player| player.tutorial_island_progress == :moving_around }

    text "Hello there, newcomer. My name is Brynna. My job is to teach you a few survival tips and tricks. First off we're "\
         "going to start with the most basic survival skill of all: making a fire."

    continue :dialogue => :items
  end


  dialogue :hello_again do
    type :npc_speech
    npc :survival_expert

    precondition { |player| player.tutorial_island_progress == :moving_around }

    text "Hello again. I'm here to teach you a few survival tips and tricks. First off we're going to start with the most "\
         "basic survival skill of all: making a fire."

    continue :close => true, &:add_survival_items
  end

  # The dialogue displayed when the Survival Guide hands both a bronze axe and a tinderbox to the player.
  dialogue :items do
    type :message_with_item
    item :bronze_axe, 200 # TODO the tinderbox is also displayed - find this dialogue id. Scale looks like the default http://i.imgur.com/i1abN5X.png

    text "The Survival Guide gives you a tinderbox and a bronze axe!"
    
    continue :close => true do |player|
      if (player.tutorial_island_progress != :given_axe)
        player.tutorial_island_progress = :given_axe
        player.send(SwitchTabInterfaceMessage.new(INVENTORY_TAB_INDEX, INVENTORY_TAB_ID))
        player.send(FlashTabInterfaceMessage.new(INVENTORY_TAB_INDEX))
      end

      add_survival_items(player)
    end
  end

  # The dialogue displayed when the player has succesfully cut down a tree.
  dialogue :get_logs do
    type :message_with_item
    item :logs

    text "You get some logs."
    continue :close => true do |player|
      TutorialInstructions.show_instruction(player)
    end
  end

end

# The id of the bronze axe.
BRONZE_AXE = lookup_item(:bronze_axe)

# The id of the tinderbox.
TINDERBOX = lookup_item(:tinderbox)

# Add the survival items (bronze axe and tinderbox) to the inventory of the player, if they do not already have them.
def add_survival_items(player)
  inventory = player.inventory
  [ BRONZE_AXE, TINDERBOX ].each { |item| inventory.add(item) unless inventory.contains(item) }
  TutorialInstructions.show_instruction(player)
end

on :message, :first_object_action do |ctx, player, message|
  # TODO display "You cannot cut down this tree; you must first follow the guide's instructions." if the player has yet to get the axe
  if (player.in_tutorial_island && player.tutorial_island_progress == :cut_tree && message.id == TREE_ID)
    player.tutorial_island_progress = :cutting_tree # Don't break the chain, so that the Woodcutting event actually happens.
  end
end

# Intercept the FlashingTabClickedMessage to update the player's progress, if applicable.
on :message, :flashing_tab_clicked do |ctx, player, message|
  if (player.in_tutorial_island && message.tab == INVENTORY_TAB_INDEX)
    player.tutorial_island_progress = :cut_tree
    ctx.break_handler_chain()
  end
end