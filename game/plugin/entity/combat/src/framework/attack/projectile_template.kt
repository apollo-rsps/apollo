import org.apollo.game.model.Graphic

//@todo - generalize for magic projectiles
data class ProjectileTemplate(
    val factory: AmmoProjectileFactory,
    val castGraphic: Graphic? = null,
    val fumbleGraphic: Graphic? = null,
    val hitGraphic: Graphic? = null
)