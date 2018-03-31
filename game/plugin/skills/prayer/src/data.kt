import com.google.common.collect.MultimapBuilder
import com.google.common.collect.SetMultimap
import org.apollo.game.message.impl.ConfigMessage
import org.apollo.game.model.Animation
import org.apollo.game.model.entity.Player

val BURY_BONE_ANIMATION = Animation(827)

val PLAYER_PRAYERS: SetMultimap<Player, Prayer> = MultimapBuilder.hashKeys()
    .enumSetValues(Prayer::class.java)
    .build<Player, Prayer>()

val Player.currentPrayers : Set<Prayer>
    get() = PLAYER_PRAYERS[this]

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
        private val BONES = Bone.values()

        operator fun get(id: Int) = BONES.find { bone -> bone.id == id }
    }
}

enum class Prayer(val button: Int, val level: Int, val setting: Int, val drain: Double) {
    THICK_SKIN(button = 5609, level = 1, setting = 83, drain = 0.01),
    BURST_OF_STRENGTH(button = 5610, level = 4, setting = 84, drain = 0.01),
    CLARITY_OF_THOUGHT(button = 5611, level = 7, setting = 85, drain = 0.01),
    ROCK_SKIN(button = 5612, level = 10, setting = 86, drain = 0.04),
    SUPERHUMAN_STRENGTH(button = 5613, level = 13, setting = 87, drain = 0.04),
    IMPROVED_REFLEXES(button = 5614, level = 16, setting = 88, drain = 0.04),
    RAPID_RESTORE(button = 5615, level = 19, setting = 89, drain = 0.01),
    RAPID_HEAL(button = 5615, level = 22, setting = 90, drain = 0.01),
    PROTECT_ITEM(button = 5617, level = 25, setting = 91, drain = 0.01),
    STEEL_SKIN(button = 5618, level = 28, setting = 92, drain = 0.1),
    ULTIMATE_STRENGTH(button = 5619, level = 31, setting = 93, drain = 0.1),
    INCREDIBLE_REFLEXES(button = 5620, level = 34, setting = 94, drain = 0.1),
    PROTECT_FROM_MAGIC(button = 5621, level = 37, setting = 95, drain = 0.15),
    PROTECT_FROM_MISSILES(button = 5622, level = 40, setting = 96, drain = 0.15),
    PROTECT_FROM_MELEE(button = 5623, level = 43, setting = 97, drain = 0.15),
    RETRIBUTION(button = 683, level = 46, setting = 98, drain = 0.15),
    REDEMPTION(button = 684, level = 49, setting = 99, drain = 0.15),
    SMITE(button = 685, level = 52, setting = 100, drain = 0.2);

    companion object {
        private val PRAYERS = Prayer.values()

        fun forButton(button: Int) = PRAYERS.find { it.button == button }
    }
}

fun updatePrayer(player: Player, prayer: Prayer) {
    val prayerSettingValue = if (player.currentPrayers.contains(prayer)) {
        1
    } else {
        0
    }

    player.send(ConfigMessage(prayer.setting, prayerSettingValue))
}