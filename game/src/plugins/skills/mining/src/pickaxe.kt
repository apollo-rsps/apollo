import org.apollo.game.model.Animation;

data class Pickaxe(id: Int, level: Int, animation: Animation, pulses: Int)

val PICKAXES = Array(
        Pickaxe(1265, 1, 625, 8),  // bronze
        Pickaxe(1267, 1, 626, 7),  // iron
        Pickaxe(1269, 1, 627, 6),  // steel
        Pickaxe(1273, 21, 629, 5), // mithril
        Pickaxe(1271, 31, 628, 4), // adamant
        Pickaxe(1275, 41, 624, 3)  // rune
)

val PICKAXE_IDS: IntArray = intArrayOf(
        1275, // rune
        1271  // adamant
        1273, // mithril
        1269, // steel
        1267, // iron
        1265, // bronze
)