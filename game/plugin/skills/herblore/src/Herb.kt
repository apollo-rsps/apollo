import org.apollo.game.plugin.api.Definitions

enum class Herb(
    val identified: Int,
    val unidentified: Int,
    val level: Int,
    val experience: Double
) {
    GUAM_LEAF(identified = 249, unidentified = 199, level = 1, experience = 2.5),
    MARRENTILL(identified = 251, unidentified = 201, level = 5, experience = 3.8),
    TARROMIN(identified = 253, unidentified = 203, level = 11, experience = 5.0),
    HARRALANDER(identified = 255, unidentified = 205, level = 20, experience = 6.3),
    RANARR(identified = 257, unidentified = 207, level = 25, experience = 7.5),
    TOADFLAX(identified = 2998, unidentified = 2998, level = 30, experience = 8.0),
    IRIT_LEAF(identified = 259, unidentified = 209, level = 40, experience = 8.8),
    AVANTOE(identified = 261, unidentified = 211, level = 48, experience = 10.0),
    KWUARM(identified = 263, unidentified = 213, level = 54, experience = 11.3),
    SNAPDRAGON(identified = 3000, unidentified = 3051, level = 59, experience = 11.8),
    CADANTINE(identified = 265, unidentified = 215, level = 65, experience = 12.5),
    LANTADYME(identified = 2481, unidentified = 2485, level = 67, experience = 13.1),
    DWARF_WEED(identified = 267, unidentified = 217, level = 70, experience = 13.8),
    TORSTOL(identified = 269, unidentified = 219, level = 75, experience = 15.0);

    val identifiedName by lazy { Definitions.item(identified)!!.name }

    companion object {
        private val identified = Herb.values().map(Herb::identified).toHashSet()
        private val herbs = Herb.values().associateBy(Herb::unidentified)

        operator fun get(id: Int): Herb? = herbs[id]
        internal fun Int.isUnidentified(): Boolean = this in herbs
        internal fun Int.isIdentified(): Boolean = this in identified
    }
}