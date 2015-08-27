require 'java'

java_import 'org.apollo.game.model.Animation'

ANIMATIONS = {
  162 => Animation::THINKING, 6_503 => Animation::CLIMB_ROPE, 169 => Animation::NO,
  164 => Animation::BOW, 13_384 => Animation::GOBLIN_DANCE, 161 => Animation::CRY,
  170 => Animation::LAUGH, 171 => Animation::CHEER, 163 => Animation::WAVE,
  167 => Animation::BECKON, 3_362 => Animation::PANIC, 172 => Animation::CLAP,
  166 => Animation::DANCE, 13_363 => Animation::JIG, 13_364 => Animation::SPIN,
  13_365 => Animation::HEAD_BANG, 6_506 => Animation::LEAN, 165 => Animation::ANGRY,
  13_368 => Animation::YAWN, 13_366 => Animation::JOY_JUMP, 667 => Animation::GLASS_BOX,
  13_367 => Animation::RASPBERRY, 13_369 => Animation::SALUTE, 13_370 => Animation::SHRUG,
  11_100 => Animation::BLOW_KISS, 666 => Animation::GLASS_WALL, 168 => Animation::YES,
  13_383 => Animation::GOBLIN_BOW
}

# Intercept the button message.
on :message, :button do |player, message|
  anim = ANIMATIONS[message.widget_id]

  unless anim.nil?
    player.play_animation(anim)
    message.terminate
  end
end
