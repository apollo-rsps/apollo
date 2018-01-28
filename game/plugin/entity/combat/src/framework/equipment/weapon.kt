
import AttackStyle.*
import AttackType.Crush
import Weapons.createWeapon
import org.apollo.game.model.Animation

data class Weapon(val weaponClass: WeaponClass, val bonuses: CombatBonuses, val specialAttack: SpecialAttack? = null)

operator fun WeaponClass.invoke(name: String, configurer: WeaponBuilder.() -> Unit) {
    val weaponItem = lookup_item(name) ?: throw IllegalArgumentException("Invalid weapon name: ${name}")
    Weapons.weaponMap[weaponItem.id] = createWeapon(this, configurer)
}

object Weapons {
    internal val weaponMap = mutableMapOf<Int, Weapon>()

    operator fun get(itemId: Int?): Weapon {
        return weaponMap[itemId] ?: defaultWeapon
    }

    internal fun createWeapon(weaponClass: WeaponClass, configurer: WeaponBuilder.() -> Unit = {}): Weapon {
        return WeaponBuilder(weaponClass)
            .also(configurer)
            .build()
    }
}

object Unarmed : MeleeWeaponClass({
    widgetId = 5855

    defaults {
        attackSpeed = 4
        attackType = Crush
        attackAnimation = Animation(422)
    }

    Accurate {
        button = 5860
    }

    Aggressive {
        button = 5862
        attackAnimation = Animation(423)
    }

    Defensive {
        button = 5861
    }
})

private val defaultWeapon = Weapons.createWeapon(Unarmed)

class WeaponBuilder(private val weaponClass: WeaponClass) {
    private val combatBonusesBuilder = CombatBonusesBuilder()

    var specialAttack: SpecialAttack? = null
    var meleeStrength: Int
        get() = combatBonusesBuilder.meleeStrength
        set(value) {
            combatBonusesBuilder.meleeStrength = value
        }

    var rangedStrength: Int
        get() = combatBonusesBuilder.rangedStrength
        set(value) {
            combatBonusesBuilder.rangedStrength = value
        }

    var prayer: Int
        get() = combatBonusesBuilder.prayer
        set(value) {
            combatBonusesBuilder.prayer = value
        }

    fun attackBonuses(configurer: AttackBonusesBuilder.() -> Unit) = this.combatBonusesBuilder.attack(configurer)
    fun defenceBonuses(configurer: AttackBonusesBuilder.() -> Unit) = this.combatBonusesBuilder.defence(configurer)

    fun build() = Weapon(weaponClass, combatBonusesBuilder.build(), specialAttack)
}
