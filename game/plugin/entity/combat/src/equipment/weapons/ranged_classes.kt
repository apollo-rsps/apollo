import AttackStyle.*
import AttackType.Ranged
import org.apollo.game.model.Animation

object Bow : RangedWeaponClass(Arrows, {
    widgetId = 1764

    defaults {
        attackType = Ranged
        attackAnimation = Animation(426)
        attackSpeed = 4
        attackRange = 7
    }

    Accurate {
        button = 1772
    }

    Rapid {
        button = 1770
        attackSpeed = 3

    }

    LongRanged {
        button = 1771
    }
})
