require 'java'

java_import 'org.apollo.game.message.impl.HintIconMessage'
java_import 'org.apollo.game.message.impl.SwitchTabInterfaceMessage'


private

# The ids of tabs that are displayed when the player has yet to start the tutorial.
INITIAL_TABS = [ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2449, 904, -1, -1 ]

# The character design interface id.
CHARACTER_DESIGN = 3559

# The Runescape guide Npc
@runescape_guide = spawn_npc :name => :runescape_guide, :x => 3093, :y => 3107

# Sends the appropriate data to the client when the player logs in to the game.
on :login do |event, player|
  if player.in_tutorial_island
    TutorialInstructions::show_instruction(player)
   # INITIAL_TABS.each_with_index { |tab, index| player.send(SwitchTabInterfaceMessage.new(index, tab)) } # This is commented so the lame hack works

    if (player.tutorial_island_progress == :not_started)
      show_hint_icon(player)
      player.interface_set.open_window(CHARACTER_DESIGN)
    end
  end
end

## TODO lame hack to get round the lack of door support - must remove


on :message, :fifth_item_option do |ctx, player, message|
    player.teleport(Position.new(3100, 3107)) if message.id == 1
    player.tutorial_island_progress = :moving_around
    player.interface_set.open_dialogue_overlay(-1)
end

on :login do |event, player|
  player.inventory.add(1)
end

## END lame hack


# The conversation with the Runescape Guide, when on tutorial island.
conversation :tutorial_runescape_guide do

  # The first dialogue of the Runescape Guide, greeting the Player.
  dialogue :greetings do
    type :npc_speech
    npc :runescape_guide
    
    precondition { |player| player.tutorial_island_progress == :not_started }

    text "Greetings! I see you are a new arrival to this land. My job is to welcome all new visitors. So welcome!"

    continue :dialogue => :go_through_door do |player|
      player.tutorial_island_progress = :talk_to_people
    end
  end


  # The Guide welcomes back the Player if they speak to him after they have already gone through the conversation once.
  dialogue :welcome_back do
    type :npc_speech
    npc :runescape_guide

    precondition { |player| player.tutorial_island_progress != :not_started }

    text "Welcome back."

    continue :dialogue => :talk_to_people
  end


  # The Guide tells Players to speak to people in order to succeed.
  dialogue :talk_to_people do
    type :npc_speech
    npc :runescape_guide

    text "You have already learned the first thing you need to succeed in this world: talking to people!",
         "You will find many inhabitants of this world have useful things to say to you. By clicking on them with your mouse you can talk to them.",
         "I would also suggest reading through some of the supporting information on the website. There you can find maps, a bestiary, and much more."

    continue :dialogue => :go_through_door
    end

  # The Guide tells Players to go through the door, advancing the tutorial progress if this is the first time the Player has heard this.
  dialogue :go_through_door do
    type :npc_speech
    npc :runescape_guide

    text "To continue the tutorial go through that door over there, and speak to your first instructor."

    continue :close => true do |player|
      if (player.tutorial_island_progress == :not_started)
        reset_hint_icon(player)
        # TODO door hint icon
        player.tutorial_island_progress = :runescape_guide_finished
      end

      TutorialInstructions.show_instruction(player)
    end
  end

end


# Enables the hint icon above the Runescape guide.
def show_hint_icon(player)
  player.send(HintIconMessage.for_npc(@runescape_guide.index))
end

# Resets the hint icon.
def reset_hint_icon(player)
  player.send(HintIconMessage.reset_npc())
end