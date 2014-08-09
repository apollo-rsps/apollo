require 'java'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.Animation'

DUMMY_ID = 823
DUMMY_SIZE = 1
PUNCH_ANIMATION = Animation.new(422)
ANIMATION_PULSES = 0
LEVEL_THRESHOLD = 8
EXP_PER_HIT = 5

class DummyAction < DistancedAction
  attr_reader :position

  def initialize(mob, position)
    super(ANIMATION_PULSES, true, mob, position, DUMMY_SIZE)

    @position = position
    @started = false
  end

  def executeAction
    unless @started
      @started = true

      mob.send_message('You hit the dummy.', true)
      mob.turn_to(position)
      mob.play_animation(PUNCH_ANIMATION)
    else
      skills = mob.skill_set

      if (skills.skill(ATTACK_SKILL_ID).maximum_level >= LEVEL_THRESHOLD)
        mob.send_message('There is nothing more you can learn from hitting a dummy.')
      else
        skills.add_experience(ATTACK_SKILL_ID, EXP_PER_HIT)
      end

      stop
    end
  end

  def equals(other)
    return (get_class == other.get_class and @position == other.position)
  end
end

on :message, :second_object_action do |ctx, player, message|
  player.start_action(DummyAction.new(player, message.position)) if message.id == DUMMY_ID
end