import CrushableIngredient.Companion.isCrushable
import NormalIngredient.Companion.isNormalIngredient
import org.apollo.game.plugin.api.Definitions

/**
 * A secondary ingredient in a potion.
 */
interface Ingredient {
    val id: Int

    companion object {
        internal fun Int.isIngredient(): Boolean = isNormalIngredient() || isCrushable()
    }
}

enum class CrushableIngredient(val uncrushed: Int, override val id: Int) : Ingredient {
    UNICORN_HORN(uncrushed = 237, id = 235),
    DRAGON_SCALE(uncrushed = 243, id = 241),
    CHOCOLATE_BAR(uncrushed = 1973, id = 1975),
    BIRDS_NEST(uncrushed = 5075, id = 6693);

    val uncrushedName by lazy { Definitions.item(uncrushed)!!.name }

    companion object {
        private const val PESTLE_AND_MORTAR = 233
        private const val KNIFE = 5605

        private val ingredients = CrushableIngredient.values().associateBy(CrushableIngredient::uncrushed)
        operator fun get(id: Int): CrushableIngredient? = ingredients[id]

        internal fun Int.isCrushable(): Boolean = this in ingredients
        internal fun Int.isGrindingTool(): Boolean = this == KNIFE || this == PESTLE_AND_MORTAR
    }
}

enum class NormalIngredient(override val id: Int) : Ingredient {
    EYE_NEWT(id = 221),
    RED_SPIDERS_EGGS(id = 223),
    LIMPWURT_ROOT(id = 225),
    SNAPE_GRASS(id = 231),
    WHITE_BERRIES(id = 239),
    WINE_ZAMORAK(id = 245),
    JANGERBERRIES(id = 247),
    TOADS_LEGS(id = 2152),
    MORT_MYRE_FUNGI(id = 2970),
    POTATO_CACTUS(id = 3138),
    PHOENIX_FEATHER(id = 4621),
    FROG_SPAWN(id = 5004),
    PAPAYA_FRUIT(id = 5972),
    POISON_IVY_BERRIES(id = 6018),
    YEW_ROOTS(id = 6049),
    MAGIC_ROOTS(id = 6051);

    companion object {
        private val ingredients = NormalIngredient.values().associateBy(NormalIngredient::id)
        operator fun get(id: Int): NormalIngredient? = ingredients[id]

        internal fun Int.isNormalIngredient(): Boolean = this in ingredients
    }
}