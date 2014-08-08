require 'java'

java_import 'org.apollo.game.model.Animation'

ANIMATIONS = {
  162 => Animation::THINKING,     6503 => Animation::CLIMB_ROPE,  169 => Animation::NO,          164 => Animation::BOW,      13384 => Animation::GOBLIN_DANCE,
  161 => Animation::CRY,          170 => Animation::LAUGH,        171 => Animation::CHEER,       163 => Animation::WAVE,     167 => Animation::BECKON, 
  3362 => Animation::PANIC,       172 => Animation::CLAP,         166 => Animation::DANCE,       13363 => Animation::JIG,    13364 => Animation::SPIN,
  13365 => Animation::HEAD_BANG,  6506 => Animation::LEAN,        165 => Animation::ANGRY,       13368 => Animation::YAWN,   13366 => Animation::JOY_JUMP, 
  667 => Animation::GLASS_BOX,    13367 => Animation::RASPBERRY,  13369 => Animation::SALUTE,    13370 => Animation::SHRUG,  11100 => Animation::BLOW_KISS, 
  666 => Animation::GLASS_WALL,   168 => Animation::YES,          13383 => Animation::GOBLIN_BOW 
}

# Intercept the button message.
on :message, :button do |ctx, player, message|
  anim = ANIMATIONS[message.widget_id]
  unless anim == nil
    player.play_animation(anim)
    ctx.break_handler_chain
  end
end