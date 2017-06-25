import org.apollo.game.message.impl.ButtonMessage
import org.apollo.game.model.Animation

val ANGRY_EMOTE = Animation(859)
val BECKON_EMOTE = Animation(864)
val BLOW_KISS_EMOTE = Animation(1368)
val BOW_EMOTE = Animation(858)
val CHEER_EMOTE = Animation(862)
val CLAP_EMOTE = Animation(865)
val CLIMB_ROPE_EMOTE = Animation(1130)
val CRY_EMOTE = Animation(860)
val DANCE_EMOTE = Animation(866)
val GLASS_BOX_EMOTE = Animation(1131)
val GLASS_WALL_EMOTE = Animation(1128)
val GOBLIN_BOW_EMOTE = Animation(2127)
val GOBLIN_DANCE_EMOTE = Animation(2128)
val HEAD_BANG_EMOTE = Animation(2108)
val JIG_EMOTE = Animation(2106)
val JOY_JUMP_EMOTE = Animation(2109)
val LAUGH_EMOTE = Animation(861)
val LEAN_EMOTE = Animation(1129)
val NO_EMOTE = Animation(856)
val PANIC_EMOTE = Animation(2105)
val RASPBERRY_EMOTE = Animation(2110)
val SALUTE_EMOTE = Animation(2112)
val SHRUG_EMOTE = Animation(2113)
val SPIN_EMOTE = Animation(2107)
val THINKING_EMOTE = Animation(857)
val WAVE_EMOTE = Animation(863)
val YAWN_EMOTE = Animation(2111)
val YES_EMOTE = Animation(855)

val EMOTE_MAP = mapOf<Int, Animation>(
	162 to THINKING_EMOTE, 6_503 to CLIMB_ROPE_EMOTE, 169 to NO_EMOTE,
	164 to BOW_EMOTE, 13_384 to GOBLIN_DANCE_EMOTE, 161 to CRY_EMOTE,
	170 to LAUGH_EMOTE, 171 to CHEER_EMOTE, 163 to WAVE_EMOTE,
	167 to BECKON_EMOTE, 3_362 to PANIC_EMOTE, 172 to CLAP_EMOTE,
	166 to DANCE_EMOTE, 13_363 to JIG_EMOTE, 13_364 to SPIN_EMOTE,
	13_365 to HEAD_BANG_EMOTE, 6_506 to LEAN_EMOTE, 165 to ANGRY_EMOTE,
	13_368 to YAWN_EMOTE, 13_366 to JOY_JUMP_EMOTE, 667 to GLASS_BOX_EMOTE,
	13_367 to RASPBERRY_EMOTE, 13_369 to SALUTE_EMOTE, 13_370 to SHRUG_EMOTE,
	11_100 to BLOW_KISS_EMOTE, 666 to GLASS_WALL_EMOTE, 168 to YES_EMOTE,
	13_383 to GOBLIN_BOW_EMOTE
)

on { ButtonMessage::class }
	.where { widgetId in EMOTE_MAP }
	.then {
		it.playAnimation(EMOTE_MAP[widgetId])
	}