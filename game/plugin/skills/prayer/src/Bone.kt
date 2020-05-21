enum class Bone(val id: Int, val xp: Double) {
    REGULAR_BONES(id = 526, xp = 5.0),
    BURNT_BONES(id = 528, xp = 5.0),
    BAT_BONES(id = 530, xp = 4.0),
    BIG_BONES(id = 532, xp = 45.0),
    BABY_DRAGON_BONES(id = 534, xp = 30.0),
    DRAGON_BONES(id = 536, xp = 72.0),
    WOLF_BONES(id = 2859, xp = 14.0),
    SHAIKAHAN_BONES(id = 3123, xp = 25.0),
    JOGRE_BONES(id = 3125, xp = 15.0),
    BURNT_ZOGRE_BONES(id = 3127, xp = 25.0),
    MONKEY_BONES_SMALL_0(id = 3179, xp = 14.0),
    MONKEY_BONES_MEDIUM(id = 3180, xp = 14.0),
    MONKEY_BONES_LARGE_0(id = 3181, xp = 14.0),
    MONKEY_BONES_LARGE_1(id = 3182, xp = 14.0),
    MONKEY_BONES_SMALL_1(id = 3183, xp = 14.0),
    SHAKING_BONES(id = 3187, xp = 14.0),
    FAYRG_BONES(id = 4830, xp = 84.0),
    RAURG_BONES(id = 4832, xp = 96.0),
    OURG_BONES(id = 4834, xp = 140.0);

    companion object {
        private val BONES = Bone.values().associateBy(Bone::id)

        operator fun get(id: Int) = BONES[id]
        internal fun Int.isBone(): Boolean = this in BONES
    }
}