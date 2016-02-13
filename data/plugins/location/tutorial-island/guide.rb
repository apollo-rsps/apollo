require 'java'

java_import 'org.apollo.game.message.impl.HintIconMessage'
java_import 'org.apollo.game.message.impl.MobHintIconMessage'
java_import 'org.apollo.game.message.impl.PositionHintIconMessage'
java_import 'org.apollo.game.message.impl.SwitchTabInterfaceMessage'
java_import 'org.apollo.game.model.entity.EntityType'
java_import 'org.apollo.game.model.Position'

private

# Contains constants used during the Runescape Guide part of Tutorial Island.
module GuideConstants

  # The Runescape Guide Npc.
  RUNESCAPE_GUIDE = spawn_npc name: :runescape_guide, x: 3093, y: 3107

  # The character design interface id.
  CHARACTER_DESIGN_INTERFACE = 3559

  # The id of the door of the house new players spawn in.
  DOOR_ID = 3014

  # The Position of the door of the house new players spawn in.
  DOOR_POSITION = Position.new(3098, 3107)

  # The HintIconMessage sent to display a hint arrow above the door of the house new players spawn
  # in.
  DOOR_HINT = PositionHintIconMessage.new(HintIconMessage::Type::WEST, DOOR_POSITION, 120)

  # The ids of tabs that are displayed when the player has yet to start the tutorial.
  INITIAL_TABS = [-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2449, 904, -1, -1].freeze

  # The HintIconMessage sent to remove a hint arrow from above the door.
  RESET_DOOR_HINT = PositionHintIconMessage.reset

  # The HintIconMessage sent to remove a hint arrow from an Npc.
  RESET_NPC_HINT = MobHintIconMessage.reset(EntityType::NPC)

end

# Sends the appropriate data to the client when the player logs in to the game.
on :login do |_event, player|
  if player.in_tutorial_island && player.privilege_level != RIGHTS_ADMIN
    TutorialInstructions.show_instruction(player)

    GuideConstants::INITIAL_TABS.each_with_index do |tab, index|
      player.send(SwitchTabInterfaceMessage.new(index, tab))
    end

    if player.tutorial_island_progress == :not_started
      player.interface_set.open_window(GuideConstants::CHARACTER_DESIGN_INTERFACE)
      player.send(MobHintIconMessage.create(GuideConstants::RUNESCAPE_GUIDE))
    end
  end
end

# The conversation with the Runescape Guide, when on tutorial island.
conversation :tutorial_runescape_guide do

  # The first dialogue of the Runescape Guide, greeting the Player.
  dialogue :greetings do
    type :npc_speech
    npc :runescape_guide
    emote :calm

    precondition { |player| player.tutorial_island_progress == :not_started }

    text 'Greetings! I see you are a new arrival to this land. My job is to welcome all new '\
           'visitors. So welcome!'

    continue dialogue: :talk_to_people do |player|
      player.tutorial_island_progress = :talk_to_people
    end
  end

  # The Guide welcomes back the Player if they speak to him after they have already gone through
  # the conversation once.
  dialogue :welcome_back do
    type :npc_speech
    npc :runescape_guide
    emote :calm

    precondition { |player| player.tutorial_island_progress != :not_started }

    text 'Welcome back.'

    continue dialogue: :talk_to_people
  end

  # The Guide tells Players to speak to people in order to succeed.
  dialogue :talk_to_people do
    type :npc_speech
    npc :runescape_guide
    emote :calm

    text 'You have already learned the first thing you need to succeed in this world: talking to '\
           'people!',
         'You will find many inhabitants of this world have useful things to say to you. By '\
           'clicking on them with your mouse you can talk to them.',
         'I would also suggest reading through some of the supporting information on the website.'\
           ' There you can find maps, a bestiary, and much more.'

    continue dialogue: :go_through_door
  end

  # The Guide tells Players to go through the door, advancing the tutorial progress if this is the
  # first time the Player has heard this.
  dialogue :go_through_door do
    type :npc_speech
    npc :runescape_guide
    emote :calm

    text 'To continue the tutorial go through that door over there, and speak to your first '\
           'instructor.'

    close do |player|
      if player.tutorial_island_progress < :runescape_guide_finished
        player.send(GuideConstants::RESET_NPC_HINT)

        player.send(GuideConstants::DOOR_HINT)
        player.tutorial_island_progress = :runescape_guide_finished
      end

      TutorialInstructions.show_instruction(player)
    end
  end

  # The dialogue displayed if the player attempts to leave the Runescape Guide's house before they
  # have spoken to him.
  dialogue :talk_to_guide do
    type :text

    text 'You need to talk to the Runescape Guide before you are allowed to proceed through this '\
          'door.'

    close do |player|
      TutorialInstructions.show_instruction(player)
    end
  end

end

on :open_door do |event, player|
  door = event.door

  if player.in_tutorial_island && door.position.equals(GuideConstants::DOOR_POSITION)
    if player.tutorial_island_progress < :runescape_guide_finished
      send_dialogue(player, get_dialogue(:tutorial_runescape_guide, :talk_to_guide))
      event.terminate
    elsif player.tutorial_island_progress == :runescape_guide_finished
      player.tutorial_island_progress = :moving_around
      player.send(GuideConstants::RESET_DOOR_HINT)
      TutorialInstructions.show_instruction(player)
    end
  end
end

