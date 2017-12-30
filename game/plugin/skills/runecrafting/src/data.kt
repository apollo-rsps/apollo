import javafx.geometry.Pos
import org.apollo.game.model.Animation
import org.apollo.game.model.Graphic
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import java.lang.Math.floor

val CHANGE_ALTER_OBJECT_CONFIG = 491
val TIARA_ITEM_ID = 5525 //Blank Tiara
val ANIMATION = Animation(791)
val GRAPHIC = Graphic(186, 0, 100)
val RUNE_ESSENCE_ID = 1436

enum class Alter(val entranceId: Int, val craftingId: Int, val portalId: Int, val entrance: Position, val exit: Position, val center: Position) {
    AIR_ALTER(2452, 2478, 2465, Position(2841, 4829), Position(2983, 3292), Position(2844, 4834)),
    MIND_ALTER(2453, 2479, 2466, Position(2793, 4828), Position(2980, 3514), Position(2786, 4841)),
    WATER_ALTER(2454, 2480, 2467, Position(2726, 4832), Position(3187, 3166), Position(2716, 4836)),
    EARTH_ALTER(2455, 2481, 2468, Position(2655, 4830), Position(3304, 3474), Position(2658, 4841)),
    FIRE_ALTER(2456, 2482, 2469, Position(2574, 4849), Position(3311, 3256), Position(2585, 4838)),
    BODY_ALTER(2457, 2483, 2470, Position(2524, 4825), Position(3051, 3445), Position(2525, 4832)),
    COSMIC_ALTER(2458, 2484, 2471,Position(2142, 4813), Position(2408, 4379), Position(2142, 4833)),
    LAW_ALTER(2459, 2485, 2472, Position(2464, 4818), Position(2858, 3379), Position(2464, 4832)),
    NATURE_ALTER(2460, 2486, 2473, Position(2400, 4835), Position(2867, 3019), Position(2400, 4841)),
    CHAOS_ALTER(2461, 2487, 2474, Position(2268, 4842), Position(3058, 3591), Position(2271, 4842)),
    DEATH_ALTER(2462, 2488, 2475, Position(2208, 4830), Position(3222, 3222), Position(2205, 4836));

    companion object {
        fun findByEntranceId(id: Int): Alter? = Alter.values().find { alter ->  alter.entranceId == id }
        fun findByPortalId(id: Int): Alter? = Alter.values().find { alter ->  alter.portalId == id }
        fun findByCraftingId(id: Int): Alter? = Alter.values().find { alter ->  alter.craftingId == id }
    }
}

enum class Rune(val id: Int, val alter: Alter, val level: Int, val xp: Double) {
    AIR_RUNE(556, Alter.AIR_ALTER, 1, 5.0),
    MIND_RUNE(558, Alter.MIND_ALTER, 1, 5.5),
    WATER_RUNE(555, Alter.WATER_ALTER, 5, 6.0),
    EARTH_RUNE(557, Alter.EARTH_ALTER, 9, 6.5),
    FIRE_RUNE(554, Alter.FIRE_ALTER, 14, 7.0),
    BODY_RUNE(559, Alter.BODY_ALTER, 20, 7.5),
    COSMIC_RUNE(564, Alter.COSMIC_ALTER, 27, 8.0),
    CHAOS_RUNE(562, Alter.CHAOS_ALTER, 35, 8.5),
    NATURE_RUNE(561, Alter.NATURE_ALTER, 44, 9.0),
    LAW_RUNE(563, Alter.LAW_ALTER, 54, 9.5),
    DEATH_RUNE(560, Alter.DEATH_ALTER, 65, 10.0);

    companion object {
        fun findById(id: Int): Rune? = Rune.values().find { rune ->  rune.id == id }
        fun findByAlterId(id: Int): Rune? = Rune.values().find { rune ->  rune.alter.craftingId == id }
    }

    fun getBonus(): Double =
            when(this) {
                Rune.AIR_RUNE -> (floor((level / 11.0)) + 1)
                Rune.MIND_RUNE -> (floor((level / 14.0)) + 1)
                Rune.WATER_RUNE -> (floor((level / 19.0)) + 1)
                Rune.EARTH_RUNE -> (floor((level / 26.0)) + 1)
                Rune.FIRE_RUNE -> (floor((level / 35.0)) + 1)
                Rune.BODY_RUNE -> (floor((level / 46.0)) + 1)
                Rune.COSMIC_RUNE -> (floor((level / 59.0)) + 1)
                Rune.CHAOS_RUNE -> (floor((level / 74.0)) + 1)
                Rune.NATURE_RUNE -> (floor((level / 91.0)) + 1)
                Rune.LAW_RUNE -> 1.0
                Rune.DEATH_RUNE -> 1.0
            }
}

enum class Talisman(val id: Int, val alter: Position) {
    AIR_TALISMAN(1438, Position(2985, 3292)),
    EARTH_TALISMAN(1440, Position(3306, 3474)),
    FIRE_TALISMAN(1442, Position(3313, 3255)),
    WATER_TALISMAN(1444, Position(3185, 3165)),
    BODY_TALISMAN(1446, Position(3053, 3445)),
    MIND_TALISMAN(1448, Position(2982, 3514)),
    CHAOS_TALISMAN(1452, Position(3059, 3590)),
    COSMIC_TALISMAN(1454, Position(2408, 4377)),
    DEATH_TALISMAN(1456, Position(0, 0)),
    LAW_TALISMAN(1458, Position(2858, 3381)),
    NATURE_TALISMAN(1462, Position(2869, 3019));

    companion object {
        fun findById(id: Int): Talisman? = Talisman.values().find { talisman ->  talisman.id == id }
    }

    fun sendMessage(player: Player) {
        if (alter.isWithinDistance(player.position, 10)) {
            player.sendMessage("Your talisman glows brightly.");
        } else {
            var msg = if (player.position.y > alter.y) "North" else "South";
            msg += if (player.position.x > alter.x) "-East" else "-West";
            player.sendMessage("The talisman pulls toward the $msg");
        }
    }
}

enum class Tiara(val id: Int, val alter: Alter, val talisman: Talisman, val bitshift: Int, val xp: Double) {
    AIR_TIARA(5527, Alter.AIR_ALTER, Talisman.AIR_TALISMAN, 0, 25.0),
    MIND_TIARA(5529, Alter.MIND_ALTER, Talisman.MIND_TALISMAN, 1, 27.5),
    WATER_TIARA(5531, Alter.WATER_ALTER, Talisman.WATER_TALISMAN, 2, 30.0),
    BODY_TIARA(5533, Alter.BODY_ALTER, Talisman.BODY_TALISMAN, 5, 37.5),
    EARTH_TIARA(5535, Alter.EARTH_ALTER, Talisman.EARTH_TALISMAN, 3, 32.5),
    FIRE_TIARA(5537, Alter.FIRE_ALTER, Talisman.FIRE_TALISMAN, 4, 35.0),
    COSMIC_TIARA(5539, Alter.COSMIC_ALTER, Talisman.COSMIC_TALISMAN, 6, 40.0),
    NATURE_TIARA(5541, Alter.NATURE_ALTER, Talisman.NATURE_TALISMAN, 8, 45.0),
    CHAOS_TIARA(5543, Alter.CHAOS_ALTER, Talisman.CHAOS_TALISMAN, 9, 42.5),
    LAW_TIARA(5545, Alter.LAW_ALTER, Talisman.LAW_TALISMAN, 7, 47.5),
    DEATH_TIARA(5548, Alter.DEATH_ALTER, Talisman.DEATH_TALISMAN, 10, 50.0);

    companion object {
        fun findById(id: Int): Tiara? = Tiara.values().find { tiara ->  tiara.id == id }
        fun findByAlterId(id: Int): Tiara? = Tiara.values().find { tiara ->  tiara.alter.entranceId == id }
        fun findByTalismanId(id: Int): Tiara? = Tiara.values().find { tiara ->  tiara.talisman.id == id }
    }

}