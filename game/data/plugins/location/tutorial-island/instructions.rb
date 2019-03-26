

# Contains members related to the instructions issues during tutorial island.
module TutorialInstructions

  # Sends the appropriate instruction to the player.
  def self.show_instruction(player)
    instructions = CONVERSATIONS[:tutorial_island_instructions]
    progress = player.tutorial_island_progress.name
    name = case progress
             # The Runescape Guide instructions.
             when :not_started then :getting_started
             when :runescape_guide_finished then :scenery
             when :moving_around then :moving_around

             # The Survival Guide instructions.
             when :given_axe then :viewing_items
             when :cut_tree then :cut_tree
             when :cutting_tree then :please_wait

             else raise "No dialogue for current stage #{progress} exists."
           end

    dialogue = instructions.part(name)
    send_dialogue(player, dialogue)
  end

  # The one-sided 'conversation' of instruction instructions.
  conversation :tutorial_island_instructions do

    # The initial instruction displayed when the player logs in, before they have spoken to the
    # guide.
    dialogue :getting_started do
      type :text

      title 'Getting started'
      text 'To start the tutorial, use your left mouse button to click on the Runescape Guide in '\
             'this room. He is indicated by a flashing yellow arrow above his head. If you can\'t '\
             'see him, use your keyboard\'s arrow keys to rotate the view.'
    end

    # The instruction displayed after the player has spoken to the Runescape Guide.
    dialogue :scenery do
      type :text

      title 'Interacting with scenery'
      text 'You can interact with many items of the scenery by simply clicking on them. Right '\
           'clicking will also give more options. Click on the door indicated with the yellow '\
           'arrow to go through to the next area and speak with your next instructor.'
    end


    # The instruction displayed after the player has left the initial building.
    dialogue :moving_around do
      type :text

      title 'Moving around'
      text 'Follow the path to find the next instructor. Clicking on the ground will walk you to '\
             'that point. Talk to the survival expert by the pond to continue the tutorial. '\
             'Remember you can rotate the view by pressing the arrow keys.'
    end

    # The instruction displayed after the player has been given the tinderbox and bronze axe by the
    # Survival Guide.
    dialogue :viewing_items do
      type :text

      title 'Viewing the items you were given'
      text 'Click on the flashing backpack icon to the right side of the main window to view your '\
             'inventory. Your inventory is a list of everything you have in your backpack.'
    end

    # The instruction displayed if the player tries to cut a tree before having the axe.
    dialogue :try_cut_tree do
      type :text

      title 'Follow the guide\'s instructions'
      text 'You cannot cut down this tree, you must first follow the guide\'s instructions.'
    end

    # The instruction displayed before the player has begun to cut the tree.
    dialogue :cut_tree do
      type :text

      title 'Cut down a tree'
      text 'You can click on the backpack icon at any time to view the items that you currently '\
             'have in your inventory. You will see that you now have an axe in your inventory. '\
             'Use this to get some logs by clicking on the indicated tree.'
    end

    # The instruction displayed when the player begins to cut the tree.
    dialogue :please_wait do
      type :text

      title 'Please wait...'
      text 'Your character is now attempting to cut down the tree. Sit back for a moment whilst '\
             'he does all the hard work.' # TODO: she instead of he if applicable
    end

    # The instruction displayed after the player has successfully cut logs from the tree.
    dialogue :make_a_fire do
      type :text

      title 'Making a fire'
      text 'Well done! You managed to cut some logs from the tree! Next, use the tinderbox in '\
             'your inventory to light the logs. First click on the tinderbox to \'use\' it.'\
             'Then click on the logs in your inventory to light them.'
    end

    # The instruction displayed when the player begins to light the fire.
    dialogue :lighting_fire do
      type :text

      title 'Please wait...'
      # TODO: she instead of he if applicable
      text 'Your character is now attempting to light the logs. Sit back for a moment whilst he '\
             'does all the hard work.'
    end

    # The instruction displayed when the has lit the logs.
    dialogue :gained_experience do
      type :text

      text 'You gained some experience.'\
           'Click on the flashing bar graph icon near the inventory button to see your skill '\
             'stats.'
    end

    # The dialogue displayed when the Player has clicked the flashing skill tab icon.
    dialogue :skill_stats do
      type :text

      title 'Your skill stats.'
      text 'Here you will see how good your skills are. As you move your mouse over any of the '\
           'icons in this panel, the small yellow popup box will show you the exact amount of '\
           'experience you have and how much is needed to get to the next level. Speak to the '\
           'Survival Expert to continue.'
    end
  end

end
