enum class CombatBonus {
    MeleeStrength,
    RangedStrength,
    Prayer
}

data class AttackBonuses(private val bonuses: Map<AttackType, Int>) {
    companion object {
        fun default() = AttackBonusesBuilder().build()
    }

    operator fun get(key: AttackType): Int = bonuses[key]!!
}

data class CombatBonuses(
    val attack: AttackBonuses,
    val defence: AttackBonuses,
    private val combatBonuses: Map<CombatBonus, Int>
) {
    companion object {
        fun default() = CombatBonusesBuilder().build()
    }

    operator fun get(key: CombatBonus): Int = combatBonuses[key]!!
}

class CombatBonusesBuilder {
    var meleeStrength = 0
    var rangedStrength = 0
    var prayer = 0
    var attackBonuses = AttackBonuses.default()
    var defenceBonuses = AttackBonuses.default()

    fun attack(configurer: AttackBonusesBuilder.() -> Unit) {
        val builder = AttackBonusesBuilder()
        builder.configurer()

        attackBonuses = builder.build()
    }

    fun defence(configurer: AttackBonusesBuilder.() -> Unit) {
        val builder = AttackBonusesBuilder()
        builder.configurer()

        defenceBonuses = builder.build()
    }

    fun build(): CombatBonuses {
        return CombatBonuses(attackBonuses, defenceBonuses, mapOf(
            CombatBonus.MeleeStrength to meleeStrength,
            CombatBonus.RangedStrength to rangedStrength,
            CombatBonus.Prayer to prayer
        ))
    }
}

class AttackBonusesBuilder(
    var stab: Int = 0,
    var slash: Int = 0,
    var crush: Int = 0,
    var magic: Int = 0,
    var range: Int = 0
) {

    fun build(): AttackBonuses {
        return AttackBonuses(mapOf(
            AttackType.Stab to stab,
            AttackType.Slash to slash,
            AttackType.Crush to crush,
            AttackType.Magic to magic,
            AttackType.Ranged to range
        ))
    }
}