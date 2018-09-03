require 'java'

java_import 'org.apollo.game.action.DistancedAction'
java_import 'org.apollo.game.model.Animation'

DUMMY_ID = 823
DUMMY_SIZE = 1
PUNCH_ANIMATION = Animation.new(422)
ANIMATION_PULSES = 0
LEVEL_THRESHOLD = 8
EXP_PER_HIT = 5

# A DistancedAction for attacking a training dummy.
class DummyAction < DistancedAction
  attr_reader :position

  def initialize(mob, position)
    super(ANIMATION_PULSES, true, mob, position, DUMMY_SIZE)

    @position = position
    @started = false
  end

  def executeAction
    if @started
      skills = mob.skill_set

      if (skills.skill(Skill::ATTACK).maximum_level >= LEVEL_THRESHOLD)
        mob.send_message('There is nothing more you can learn from hitting a dummy.')
      else
        skills.add_experience(Skill::ATTACK, EXP_PER_HIT)
      end

      stop
    else
      @started = true

      mob.send_message('You hit the dummy.')
      mob.turn_to(position)
      mob.play_animation(PUNCH_ANIMATION)
    end
  end

  def equals(other)
    get_class == other.get_class && @position == other.position
  end
end

on :message, :second_object_action do |player, message|
  player.start_action(DummyAction.new(player, message.position)) if message.id == DUMMY_ID
end
