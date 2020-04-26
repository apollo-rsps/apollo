require 'java'

java_import 'org.apollo.game.model.Animation'

# Animation constants.
ANGRY = Animation.new(859)
BECKON = Animation.new(864)
BLOW_KISS = Animation.new(1368)
BOW = Animation.new(858)
CHEER = Animation.new(862)
CLAP = Animation.new(865)
CLIMB_ROPE = Animation.new(1130)
CRY = Animation.new(860)
DANCE = Animation.new(866)
GLASS_BOX = Animation.new(1131)
GLASS_WALL = Animation.new(1128)
GOBLIN_BOW = Animation.new(2127)
GOBLIN_DANCE = Animation.new(2128)
HEAD_BANG = Animation.new(2108)
JIG = Animation.new(2106)
JOY_JUMP = Animation.new(2109)
LAUGH = Animation.new(861)
LEAN = Animation.new(1129)
NO = Animation.new(856)
PANIC = Animation.new(2105)
RASPBERRY = Animation.new(2110)
SALUTE = Animation.new(2112)
SHRUG = Animation.new(2113)
SPIN = Animation.new(2107)
THINKING = Animation.new(857)
WAVE = Animation.new(863)
YAWN = Animation.new(2111)
YES = Animation.new(855)

# A map of buttons to animations.
ANIMATIONS = {
  162 => THINKING, 6_503 => CLIMB_ROPE, 169 => NO,
  164 => BOW, 13_384 => GOBLIN_DANCE, 161 => CRY,
  170 => LAUGH, 171 => CHEER, 163 => WAVE,
  167 => BECKON, 3_362 => PANIC, 172 => CLAP,
  166 => DANCE, 13_363 => JIG, 13_364 => SPIN,
  13_365 => HEAD_BANG, 6_506 => LEAN, 165 => ANGRY,
  13_368 => YAWN, 13_366 => JOY_JUMP, 667 => GLASS_BOX,
  13_367 => RASPBERRY, 13_369 => SALUTE, 13_370 => SHRUG,
  11_100 => BLOW_KISS, 666 => GLASS_WALL, 168 => YES,
  13_383 => GOBLIN_BOW
}

# Intercept the button message.
on :message, :button do |player, message|
  anim = ANIMATIONS[message.widget_id]

  unless anim.nil?
    player.play_animation(anim)
    message.terminate
  end
end
