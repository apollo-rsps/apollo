require 'java'
java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.Skill'

# TODO: this shouldn't use the punch animation/delay, but should use the one
# from the active weapon/attack style (according to Scu11 anyway).

DUMMY_ID = 823
DUMMY_SIZE = 1
PUNCH_ANIMATION = 422
ANIMATION_PULSES = 0 # TODO: might be more if hitting it actually works instead of showing 'nothing more' msg?
LEVEL_THRESHOLD = 8
EXP_PER_HIT = 5

class DummyAction < DistancedAction
  attr_reader :position

  def initialize(character, position)
    super ANIMATION_PULSES, true, character, position, DUMMY_SIZE

    @position = position
    @started = false
  end

  def executeAction
    if not @started
      @started = true

      character.send_message "You hit the dummy."
      character.turn_to @position
      character.play_animation Animation.new(PUNCH_ANIMATION)
    else
      skills = character.skill_set

      if skills.skill(Skill::ATTACK).maximum_level >= LEVEL_THRESHOLD then
        character.send_message "There is nothing more you can learn from hitting a dummy."
      else
        skills.add_experience Skill::ATTACK, EXP_PER_HIT
      end

      stop
    end
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position)
  end
end

on :event, :object_action do |ctx, player, event|
  if event.option == 2 and event.id == DUMMY_ID
    player.start_action DummyAction.new(player, event.position)
  end
end
