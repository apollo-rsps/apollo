enum class AttackStyle(val attackBonus: Int = 0, val defenceBonus: Int = 0, val strengthBonus: Int = 0) {
    Accurate(attackBonus = 3, strengthBonus = 3),
    Aggressive(strengthBonus = 3),
    Defensive(defenceBonus = 3),
    Controlled(attackBonus = 1, strengthBonus = 1, defenceBonus = 1),
    AltAggressive(strengthBonus = 3),
    Rapid,
    LongRanged(attackBonus = 1,strengthBonus = 1, defenceBonus = 3)
}

enum class AttackType {
    Stab,
    Slash,
    Crush,
    Magic,
    Ranged
}