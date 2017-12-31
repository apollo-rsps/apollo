import org.apollo.game.model.entity.EquipmentConstants
import org.apollo.game.model.entity.Mob

val Mob.weapon: Weapon
    get() = Weapons[this.equipment[EquipmentConstants.WEAPON]?.id]

fun scale(oldScale: Pair<Double, Double>, newScale: Pair<Double, Double>, value: Double): Int {
    val oldMin = oldScale.first
    val oldMax = oldScale.second
    val newMin = newScale.first
    val newMax = newScale.second

    return Math.round((newMax - newMin) * (value - oldMin) / (oldMax - oldMin) + newMin).toInt();
}