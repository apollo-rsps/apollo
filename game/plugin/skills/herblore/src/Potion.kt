import CrushableIngredient.CHOCOLATE_BAR
import CrushableIngredient.DRAGON_SCALE
import CrushableIngredient.UNICORN_HORN
import NormalIngredient.*
import UnfinishedPotion.*
import org.apollo.game.plugin.api.Definitions

const val VIAL_OF_WATER = 227

interface Potion {
    val id: Int
    val level: Int
}

enum class UnfinishedPotion(override val id: Int, herb: Herb, override val level: Int) : Potion {
    GUAM(id = 91, herb = Herb.GUAM_LEAF, level = 1),
    MARRENTILL(id = 93, herb = Herb.MARRENTILL, level = 5),
    TARROMIN(id = 95, herb = Herb.TARROMIN, level = 12),
    HARRALANDER(id = 97, herb = Herb.HARRALANDER, level = 22),
    RANARR(id = 99, herb = Herb.RANARR, level = 30),
    TOADFLAX(id = 3002, herb = Herb.TOADFLAX, level = 34),
    IRIT(id = 101, herb = Herb.IRIT_LEAF, level = 45),
    AVANTOE(id = 103, herb = Herb.AVANTOE, level = 50),
    KWUARM(id = 105, herb = Herb.KWUARM, level = 55),
    SNAPDRAGON(id = 3004, herb = Herb.SNAPDRAGON, level = 63),
    CADANTINE(id = 107, herb = Herb.CADANTINE, level = 66),
    LANTADYME(id = 2483, herb = Herb.LANTADYME, level = 69),
    DWARF_WEED(id = 109, herb = Herb.DWARF_WEED, level = 72),
    TORSTOL(id = 111, herb = Herb.TORSTOL, level = 78);

    val herb = herb.identified
    val herbName: String = Definitions.item(herb.identified)!!.name

    companion object {
        private val ids = values().map(UnfinishedPotion::id).toHashSet()
        private val potions = values().associateBy(UnfinishedPotion::herb)

        operator fun get(id: Int) = potions[id]
        internal fun Int.isUnfinished(): Boolean = this in ids
    }
}

enum class FinishedPotion(
    override val id: Int,
    val unfinished: UnfinishedPotion,
    ingredient: Ingredient,
    override val level: Int,
    val experience: Double
) : Potion {
    ATTACK(id = 121, unfinished = GUAM, ingredient = EYE_NEWT, level = 1, experience = 25.0),
    ANTIPOISON(id = 175, unfinished = MARRENTILL, ingredient = UNICORN_HORN, level = 5, experience = 37.5),
    STRENGTH(id = 115, unfinished = TARROMIN, ingredient = LIMPWURT_ROOT, level = 12, experience = 50.0),
    RESTORE(id = 127, unfinished = HARRALANDER, ingredient = RED_SPIDERS_EGGS, level = 18, experience = 62.5),
    ENERGY(id = 3010, unfinished = HARRALANDER, ingredient = CHOCOLATE_BAR, level = 26, experience = 67.5),
    DEFENCE(id = 133, unfinished = RANARR, ingredient = WHITE_BERRIES, level = 30, experience = 75.0),
    AGILITY(id = 3034, unfinished = TOADFLAX, ingredient = TOADS_LEGS, level = 34, experience = 80.0),
    PRAYER(id = 139, unfinished = RANARR, ingredient = SNAPE_GRASS, level = 38, experience = 87.5),
    SUPER_ATTACK(id = 145, unfinished = IRIT, ingredient = EYE_NEWT, level = 45, experience = 100.0),
    SUPER_ANTIPOISON(id = 181, unfinished = IRIT, ingredient = UNICORN_HORN, level = 48, experience = 106.3),
    FISHING(id = 151, unfinished = AVANTOE, ingredient = SNAPE_GRASS, level = 50, experience = 112.5),
    SUPER_ENERGY(id = 3018, unfinished = AVANTOE, ingredient = MORT_MYRE_FUNGI, level = 52, experience = 117.5),
    SUPER_STRENGTH(id = 157, unfinished = KWUARM, ingredient = LIMPWURT_ROOT, level = 55, experience = 125.0),
    WEAPON_POISON(id = 187, unfinished = KWUARM, ingredient = DRAGON_SCALE, level = 60, experience = 137.5),
    SUPER_RESTORE(id = 3026, unfinished = SNAPDRAGON, ingredient = RED_SPIDERS_EGGS, level = 63, experience = 142.5),
    SUPER_DEFENCE(id = 163, unfinished = CADANTINE, ingredient = WHITE_BERRIES, level = 66, experience = 150.0),
    ANTIFIRE(id = 2428, unfinished = LANTADYME, ingredient = DRAGON_SCALE, level = 69, experience = 157.5),
    RANGING(id = 169, unfinished = DWARF_WEED, ingredient = WINE_ZAMORAK, level = 72, experience = 162.5),
    MAGIC(id = 3042, unfinished = LANTADYME, ingredient = POTATO_CACTUS, level = 76, experience = 172.5),
    ZAMORAK_BREW(id = 189, unfinished = TORSTOL, ingredient = JANGERBERRIES, level = 78, experience = 175.0);

    val ingredientName = Definitions.item(ingredient.id)!!.name.toLowerCase()
    val ingredient = ingredient.id

    companion object {
        private val potions = FinishedPotion.values().associateBy { Pair(it.unfinished.id, it.ingredient) }
        operator fun get(key: Pair<Int, Int>) = potions[key]
    }
}
