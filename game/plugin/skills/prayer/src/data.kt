import org.apollo.game.message.impl.ConfigMessage
import org.apollo.game.model.Animation
import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Player

val BURY_BONE_ANIMATION = Animation(827)

val PLAYER_PRAYERS = hashMapOf<Player, Prayer?>()

fun Player.getCurrentPrayer(): Prayer? = PLAYER_PRAYERS[this]
fun Player.setCurrentPrayer(prayer: Prayer?) = {PLAYER_PRAYERS[this] = prayer}

enum class Bone(val id: Int, val xp: Double) {
    REGULAR_BONES(526, 5.0),
    BURNT_BONES(528, 5.0),
    BAT_BONES(530, 4.0),
    BIG_BONES(532, 45.0),
    BABYDRAGON_BONES(534, 30.0),
    DRAGON_BONES(536, 72.0),
    WOLF_BONES(2859, 14.0),
    SHAIKAHAN_BONES(3123, 25.0),
    JOGRE_BONES(3125, 15.0),
    BURNT_ZOGRE_BONES(3127, 25.0),
    MONKEY_BONES_SMALL_0(3179, 14.0),
    MONKEY_BONES_MEDIUM(3180, 14.0),
    MONKEY_BONES_LARGE_0(3181, 14.0),
    MONKEY_BONES_LARGE_1(3182, 14.0),
    MONKEY_BONES_SMALL_1(3183, 14.0),
    SHAKING_BONES(3187, 14.0),
    FAYRG_BONES(4830, 84.0),
    RAURG_BONES(4832, 96.0),
    OURG_BONES(4834, 140.0);

    companion object {
        fun findById(id: Int): Bone? = Bone.values().find { bone -> bone.id == id }
    }
}

enum class Prayer(val button: Int, val level: Int, val setting: Int, val drain: Double) {
    THICK_SKIN(5609, 1, 83, 0.01),
    BURST_OF_STRENGHT(5610, 4, 84, 0.01),
    CLARITY_OF_THOUGHT(5611, 7, 85, 0.01),
    ROCK_SKIN(5612, 10, 86, 0.04),
    SUPERHUMAN_STRENGTH(5613, 13, 87, 0.04),
    IMPROVED_REFLEXES(5614, 16, 88, 0.04),
    RAPID_RESORE(5615, 19, 89, 0.01),
    RAPID_HEAL(5615, 22, 90, 0.01),
    PROTECT_ITEM(5617, 25, 91, 0.01),
    STEEL_SKIN(5618, 28, 92, 0.1),
    ULTIMATE_STRENGTH(5619, 31, 93, 0.1),
    INCREDIBLE_REFLEXES(5620, 34, 94, 0.1),
    PROTECT_FROM_MAGIC(5621, 37, 95, 0.15),
    PROTECT_FROM_MISSILES(5622, 40, 96, 0.15),
    PROTECT_FROM_MELEE(5623, 43, 97, 0.15),
    RETRIBUTION(683, 46, 98, 0.15),
    REDEMPTION(684, 49, 99, 0.15),
    SMITE(685, 52, 100, 0.2);

    companion object {
        fun findByButton(button: Int): Prayer? = Prayer.values().find { prayer -> prayer.button == button }
    }
}

fun updatePrayer(player: Player, prayer: Prayer?) {
    //Clear active prayer
    if (prayer != null) {
        if (player.getCurrentPrayer() == prayer) {
            player.send(ConfigMessage(prayer.setting, 0))
        } else if (player.getCurrentPrayer() == null) {
            player.send(ConfigMessage(prayer.setting, 1))
        } else {
            val cprayer: Prayer = player.getCurrentPrayer()!!
            player.send(ConfigMessage(cprayer.setting, 0))
            player.send(ConfigMessage(prayer.setting, 1))
        }
    }
    player.setCurrentPrayer(prayer)
}