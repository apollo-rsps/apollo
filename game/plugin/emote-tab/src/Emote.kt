import org.apollo.game.model.Animation

enum class Emote(val button: Int, animation: Int) {
    ANGRY_EMOTE(button = 165, animation = 859),
    BECKON_EMOTE(button = 167, animation = 864),
    BLOW_KISS_EMOTE(button = 11_100, animation = 1368),
    BOW_EMOTE(button = 164, animation = 858),
    CHEER_EMOTE(button = 171, animation = 862),
    CLAP_EMOTE(button = 172, animation = 865),
    CLIMB_ROPE_EMOTE(button = 6503, animation = 1130),
    CRY_EMOTE(button = 161, animation = 860),
    DANCE_EMOTE(button = 166, animation = 866),
    GLASS_BOX_EMOTE(button = 667, animation = 1131),
    GLASS_WALL_EMOTE(button = 666, animation = 1128),
    GOBLIN_BOW_EMOTE(button = 13_383, animation = 2127),
    GOBLIN_DANCE_EMOTE(button = 13_384, animation = 2128),
    HEAD_BANG_EMOTE(button = 13_365, animation = 2108),
    JIG_EMOTE(button = 13_363, animation = 2106),
    JOY_JUMP_EMOTE(button = 13_366, animation = 2109),
    LAUGH_EMOTE(button = 170, animation = 861),
    LEAN_EMOTE(button = 6_506, animation = 1129),
    NO_EMOTE(button = 169, animation = 856),
    PANIC_EMOTE(button = 3_362, animation = 2105),
    RASPBERRY_EMOTE(button = 13_367, animation = 2110),
    SALUTE_EMOTE(button = 13_369, animation = 2112),
    SHRUG_EMOTE(button = 13_370, animation = 2113),
    SPIN_EMOTE(button = 13_364, animation = 2107),
    THINKING_EMOTE(button = 162, animation = 857),
    WAVE_EMOTE(button = 163, animation = 863),
    YAWN_EMOTE(button = 13_368, animation = 2111),
    YES_EMOTE(button = 168, animation = 855);

    val animation = Animation(animation)

    companion object {
        internal val MAP = Emote.values().associateBy { it.button }

        fun fromButton(button: Int): Emote? = MAP[button]
    }
}