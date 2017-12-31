import org.apollo.game.model.Animation

data class SpecialBar(val button: Int, val configId: Int)
data class WeaponClassDetails(val widget: Int, val specialBar: SpecialBar?, val styles: List<WeaponClassStyle>)
data class WeaponClassStyle(val button: Int, val configId: Int, val attackStyle: AttackStyle, val attack: Attack, val blockAnimation: Animation?)

typealias WeaponClassConfigurer = WeaponClassDetailsBuilder.() -> Unit

open class WeaponClass(attackFactory: AttackFactory, detailsBuilder: WeaponClassConfigurer) {
    val widget: Int
    val specialBar: SpecialBar?
    val styles: Array<WeaponClassStyle>

    init {
        val details = WeaponClassDetailsBuilder(attackFactory)
            .also(detailsBuilder)
            .build()

        widget = details.widget
        specialBar = details.specialBar
        styles = details.styles.toTypedArray()
    }
}

class WeaponClassDetailsBuilder(private val attackFactory: AttackFactory) {
    private var configureStyleDefaults: WeaponClassStyleBuilder.() -> Unit = {}
    private var specialBar: SpecialBar? = null
    private val styles = mutableListOf<WeaponClassStyle>()

    var widgetId: Int? = null

    fun defaults(configurer: WeaponClassStyleBuilder.() -> Unit) {
        configureStyleDefaults = configurer
    }

    fun specialBar(configurer: SpecialBarBuilder.() -> Unit) {
        specialBar = SpecialBarBuilder().also(configurer).build()
    }

    operator fun AttackStyle.invoke(configurer: WeaponClassStyleBuilder.() -> Unit) {
        val styleBuilder = WeaponClassStyleBuilder(this)
            .also(configureStyleDefaults)
            .also(configurer)

        styles.add(styleBuilder.build(attackFactory))
    }

    fun build(): WeaponClassDetails {
        return WeaponClassDetails(
            widgetId ?: throw IllegalStateException("Weapon class widget id is required"),
            specialBar,
            styles
        )
    }
}

class WeaponClassStyleBuilder(val attackStyle: AttackStyle) {
    var button: Int? = null
    var configId: Int? = null
    var attackRange: Int? = 1
    var attackSpeed: Int? = null
    var attackType: AttackType? = null
    var attackAnimation: Animation? = null
    var blockAnimation: Animation? = null
    var attack: Attack? = null

    fun build(attackFactory: AttackFactory): WeaponClassStyle {
        val attack = this.attack ?: attackFactory.createAttack(
            attackSpeed ?: throw IllegalStateException("BasicAttack speed is required"),
            attackRange ?: throw IllegalStateException("BasicAttack range is required"),
            attackType ?: throw IllegalStateException("BasicAttack type is required"),
            attackStyle,
            attackAnimation ?: throw IllegalStateException("BasicAttack animation is required")
        )

        return WeaponClassStyle(
            button ?: throw IllegalStateException("Combat style button is required"),
            0,
            attackStyle,
            attack,
            blockAnimation
        )
    }
}

class SpecialBarBuilder {
    var button: Int? = null
    var configId: Int? = null

    fun build(): SpecialBar {
        return SpecialBar(
            button ?: throw IllegalStateException("No button configured for special bar"),
            configId ?: throw IllegalStateException("No config id configured for special bar")
        )
    }
}