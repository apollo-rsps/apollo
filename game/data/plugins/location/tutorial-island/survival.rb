require 'java'

java_import 'org.apollo.game.message.impl.FlashTabInterfaceMessage'
java_import 'org.apollo.game.message.impl.FlashingTabClickedMessage'
java_import 'org.apollo.game.message.impl.SwitchTabInterfaceMessage'

private

# Contains Survival Expert-related constants.
module SurvivalConstants

  # The Survival Expert Npc.
  @survival_expert = spawn_npc name: :survival_expert, x: 3104, y: 3095, face: :north

  # The inventory tab index.
  INVENTORY_TAB_INDEX = 3

  # The inventory tab id.
  INVENTORY_TAB_ID = 3213

  # The id of the tree the Player will cut down.
  TREE_ID = 3033

  # The id of the bronze axe.
  BRONZE_AXE = lookup_item(:bronze_axe)

  # The id of the tinderbox.
  TINDERBOX = lookup_item(:tinderbox)

end

# The conversation with the Survival Expert, when on tutorial island.
conversation :tutorial_survival_expert do

  dialogue :introduction do
    type :npc_speech
    npc :survival_expert

    precondition { |player| player.tutorial_island_progress == :moving_around }

    text 'Hello there, newcomer. My name is Brynna. My job is to teach you a few survival tips and'\
           ' tricks. First off we\'re going to start with the most basic survival skill of all: '\
           'making a fire.'

    close { |player| add_survival_items(player) }
  end

  dialogue :hello_again do
    type :npc_speech
    npc :survival_expert

    precondition { |player| player.tutorial_island_progress == :moving_around }

    text 'Hello again. I\'m here to teach you a few survival tips and tricks. First off we\'re '\
           'going to start with the most basic survival skill of all: making a fire.'

    close { |player| add_survival_items(player) }
  end

  # The dialogue displayed when the Survival Expert gives the player a bronze axe.
  dialogue :give_bronze_axe do
    type :message_with_item
    item :bronze_axe

    text 'The Survival Expert gives you a bronze axe!'

    close { |player| TutorialInstructions.show_instruction(player) }
  end

  # The dialogue displayed when the Survival Expert gives the player a tinderbox.
  dialogue :give_tinderbox do
    type :message_with_item
    item :tinderbox

    text 'The Survival Expert gives you a tinderbox!'

    close { |player| TutorialInstructions.show_instruction(player) }
  end

  # The dialogue displayed when the Survival Expert gives the player both a bronze axe and a
  # tinderbox.
  dialogue :give_axe_and_tinderbox do
    type :message_with_item
    item :bronze_axe
    # TODO: the tinderbox is also displayed - find this dialogue id. Scale looks like the default
    # http://i.imgur.com/i1abN5X.png

    text 'The Survival Expert gives you a tinderbox and a bronze axe!'

    close do |player|
      if player.tutorial_island_progress < :given_axe
        player.tutorial_island_progress = :given_axe

        index = SurvivalConstants::INVENTORY_TAB_INDEX
        player.send(SwitchTabInterfaceMessage.new(index, SurvivalConstants::INVENTORY_TAB_ID))
        player.send(FlashTabInterfaceMessage.new(index))
      end

      TutorialInstructions.show_instruction(player)
    end
  end

  # The dialogue displayed when the player has succesfully cut down a tree.
  dialogue :get_logs do
    type :message_with_item
    item :logs

    text 'You get some logs.'
    close { |player| TutorialInstructions.show_instruction(player) }
  end

end

# Add the survival items (bronze axe and tinderbox) to the inventory of the player, if they do not
# already have them.
def add_survival_items(player)
  inventory = player.inventory

  unless inventory.contains(SurvivalConstants::BRONZE_AXE)
    inventory.add(SurvivalConstants::BRONZE_AXE)
    dialogue = :give_bronze_axe
  end

  unless inventory.contains(SurvivalConstants::TINDERBOX)
    inventory.add(SurvivalConstants::TINDERBOX)
    dialogue = (dialogue == :give_bronze_axe) ? :give_axe_and_tinderbox : :give_tinderbox
  end

  send_dialogue(player, get_dialogue(:tutorial_survival_expert, dialogue))
end

# Intercept the FirstObjectActionMessage to send tutorial-only events if the player is chopping
# down a tree.
on :message, :first_object_action do |player, message|
  if player.in_tutorial_island && message.id == SurvivalConstants::TREE_ID
    progress = player.tutorial_island_progress

    if progress < :cut_tree
      send_dialogue(player, get_dialogue(:tutorial_island_instructions, :try_cut_tree))
    elsif player.tutorial_island_progress == :cut_tree
      # Don't break the chain, so that the Woodcutting event actually happens.
      player.tutorial_island_progress = :cutting_tree
    end
  end
end

# Intercept the FlashingTabClickedMessage to update the player's progress, if applicable.
on :message, :flashing_tab_clicked do |player, message|
  if player.in_tutorial_island && message.tab == SurvivalConstants::INVENTORY_TAB_INDEX &&
     player.tutorial_island_progress == :given_axe
    player.tutorial_island_progress = :cut_tree
    message.terminate
  end
end
