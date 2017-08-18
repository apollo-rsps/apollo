import org.apollo.game.model.Animation;

data class Pickaxe(val id: Int, val level: Int, val animation: Animation, val pulses: Int)

val PICKAXES = mutableMapOf<Int, Pickaxe>(
        1265 to Pickaxe(1265, 1, Animation(625), 8),  // bronze
        1267 to Pickaxe(1267, 1, Animation(626), 7),  // iron
        1269 to Pickaxe(1269, 1, Animation(627), 6),  // steel
        1273 to Pickaxe(1273, 21, Animation(629), 5), // mithril
        1271 to Pickaxe(1271, 31, Animation(628), 4), // adamant
        1275 to Pickaxe(1275, 41, Animation(624), 3)  // rune
)

val PICKAXE_IDS: IntArray = intArrayOf(
        1275, // rune
        1271,  // adamant
        1273, // mithril
        1269, // steel
        1267, // iron
        1265 // bronze
)