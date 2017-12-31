import org.apollo.game.model.Animation

//@todo - attack factory maybe not necessary when factoring details out of attack
interface AttackFactory {
    fun createAttack(speed: Int, range: Int, type: AttackType, style: AttackStyle, animation: Animation): Attack
}