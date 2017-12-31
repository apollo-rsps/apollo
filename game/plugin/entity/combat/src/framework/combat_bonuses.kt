data class DamageBonuses(val stab: Int, val slash: Int, val crush: Int, val magic: Int, val range: Int)
data class CombatBonuses(
  val attack: DamageBonuses,
  val defence: DamageBonuses,
  val meleeStrength: Int,
  val rangedStrength: Int,
  val prayer: Int
)

class CombatBonusesBuilder {
  var meleeStrength = 0
  var rangedStrength = 0
  var prayer = 0
  var attackBonuses = DamageBonuses(0, 0, 0, 0, 0)
  var defenceBonuses = DamageBonuses(0, 0, 0, 0, 0)

  fun attack(configurer: DamageBonusesBuilder.() -> Unit) {
    val builder = DamageBonusesBuilder()
    builder.configurer()

    attackBonuses = builder.build()
  }

  fun defence(configurer: DamageBonusesBuilder.() -> Unit) {
    val builder = DamageBonusesBuilder()
    builder.configurer()

    defenceBonuses = builder.build()
  }

  fun build(): CombatBonuses {
    return CombatBonuses(attackBonuses, defenceBonuses, meleeStrength, rangedStrength, prayer)
  }
}

class DamageBonusesBuilder(
  var stab: Int = 0,
  var slash: Int = 0,
  var crush: Int = 0,
  var magic: Int = 0,
  var range: Int = 0
) {
  fun build(): DamageBonuses {
    return DamageBonuses(stab, slash, crush, magic, range)
  }
}