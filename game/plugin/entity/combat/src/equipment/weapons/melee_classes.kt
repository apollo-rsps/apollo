import AttackStyle.*
import org.apollo.game.model.Animation

object Scimitar : MeleeWeaponClass({
    widgetId = 2423
    defaults {
        attackSpeed = 4
        attackAnimation = Animation(390)
        attackType = AttackType.Slash
    }

    Accurate { button = 2 }
    Aggressive { button = 3 }
    AltAggressive {
        button = 4
        attackType = AttackType.Stab
        attackAnimation = Animation(391)
    }

    Defensive { button = 5 }
})
