import org.apollo.game.model.Animation

enum class Log(val id: Int, val level: Int, val xp: Double) {
    NORMAL(1511, 1, 40.0),
    ACHEY(2862, 1, 40.0),
    OAK(1521, 15, 60.0),
    WILLOW(1519, 30, 90.0),
    TEAK(6333, 35, 105.0),
    MAPLE(1517, 45, 135.0),
    MAHOGANY(6332, 50, 157.5),
    YEW(1515, 60, 202.5),
    MAGIC(1513, 75, 303.8)
}

val TINDER_BOX = 590
val FIRE_OBJ = 2732
val ASH = 592
val LIGHT_ANIMATION = Animation(733)

fun lookupLog(id: Int): Log? {
    for (log in Log.values()) {
        if (log.id == id) {
            return log;
        }
    }
    return null
}