import org.apollo.game.model.Graphic
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Projectile

open class AmmoType(configurer: AmmoTypeBuilder.() -> Unit) {
    val items : Map<Int, Ammo>

    init {
        val ammo = AmmoTypeBuilder(this.javaClass.simpleName)
            .also(configurer)
            .build()

        items = HashMap(ammo)
    }
}

data class AmmoEnchantment(
    val graphic: Graphic,
    val effect: AmmoEnchantmentEffect,
    val chanceSupplier: AmmoEnchantmentChanceSupplier
)

data class Ammo(
    val name: String,
    val requiredLevel: Int,
    val dropChance: Double,
    val projectileFactory: AmmoProjectileFactory,
    val attack: Graphic? = null,
    val enchantment: AmmoEnchantment? = null
)

typealias AmmoEnchantmentEffect = Mob.() -> Unit
typealias AmmoEnchantmentChanceSupplier = Mob.() -> Double
typealias AmmoProjectileFactory = (World, Position, Mob) -> Projectile

/**
 * A [DslMarker] for the ammo DSL.
 */
@DslMarker annotation class AmmoDslMarker

/**
 * A builder for the graphics related to firing ammo.
 */
@AmmoDslMarker
class AmmoGraphicsBuilder {
    /**
     * The graphic used in the projectile fired.
     */
    var projectile: Int? = null

    /**
     * The graphic used when the attacker fires the shot.
     */
    var attack: Int? = null

    /**
     * The graphic used when a [Mob] is doAttack with an enchanted ammo effect.
     */
    var enchanted: Int? = null

    operator fun invoke(builder: AmmoGraphicsBuilder.() -> Unit) = builder(this)
}

@AmmoDslMarker
class AmmoDropChance

/**
 * A DSL builder for an ammo's ranged level requirements.
 */
@AmmoDslMarker
class AmmoRequirementBuilder {
    var level: Int = 1

    /**
     * Set the ranged level required to use this ammo.
     */
    infix fun level(requirement: Int): AmmoRequirementBuilder {
        this.level = requirement
        return this
    }
}

@AmmoDslMarker
class AmmoBuilder(val name: String) {
    /**
     * The chance that ammo of this type will be dropped rather than broken.
     */
    var dropChanceValue: Double = 1.0

    /**
     *  value to hold a dummy [AmmoDropChance] field that can be used by the overriden `%` operator.
     */
    val dropChance = AmmoDropChance()

    /**
     * `%` operator overload on [Int] for a fluent way to define an [Ammo]'s chance of dropping instead of breaking.
     */
    operator fun Int.rem(placeholder: AmmoDropChance) {
        dropChanceValue = this / 100.0
    }

    /**
     * The graphics played when this ammo is used.
     */
    val graphics = AmmoGraphicsBuilder()

    /**
     * An optional enchantment associated with this ammo.
     */
    val enchantment = AmmoEnchantmentBuilder()

    /**
     * The level requirements needed to use this ammo.
     */
    val requires = AmmoRequirementBuilder()
}

/**
 * A builder for effects applied to [Mob]'s doAttack with ammo that can be chanted.
 */
@AmmoDslMarker
class AmmoEnchantmentBuilder {
    var effect: AmmoEnchantmentEffect? = null
    var chance: AmmoEnchantmentChanceSupplier? = null

    /**
     * Set the effect that will be applied to a [Mob] whenever this enchantment
     * is succesfully applied.
     */
    infix fun effect(effect: AmmoEnchantmentEffect): AmmoEnchantmentBuilder {
        this.effect = effect
        return this
    }

    /**
     * Set the function used to calculate the chance of an enchantment triggering
     * on a projectile fired by a given [Mob].
     */
    infix fun chance(chanceSupplier: AmmoEnchantmentChanceSupplier): AmmoEnchantmentBuilder {
        this.chance = chanceSupplier
        return this
    }
}

/**
 * A builder for the weapon classes an [AmmoType] can be used with.
 */
@AmmoDslMarker
class AmmoTypeUseabilityBuilder {
    /**
     * A list of weapon class names that the [AmmoType] can be used with.
     */
    val weaponClasses = mutableListOf<String>()

    /**
     * Include [Ammo] under the current ammo type as useable by the given `weaponClass`.
     */
    infix fun from(weaponClass: String): AmmoTypeUseabilityBuilder = and(weaponClass)

    /**
     * Include [Ammo] under the current ammo type as useable by the given `weaponClass`.
     */
    infix fun and(weaponClass: String): AmmoTypeUseabilityBuilder {
        weaponClasses.add(weaponClass)
        return this
    }
}

/**
 * A builder DSL for an [AmmoProjectileFactory].
 */
@AmmoDslMarker
class AmmoProjectileFactoryBuilder {

    var startHeight: Int = 40
    var endHeight: Int = 35
    var delay: Int? = null
    var lifetime: Int? = null
    var pitch: Int? = null

    operator fun invoke(builder: AmmoProjectileFactoryBuilder.() -> Unit) = builder(this)

    /**
     * Build an [AmmoProjectileFactory] for the [Ammo] variant with the given graphic ID.
     */
    fun build(variant: Int): AmmoProjectileFactory = { world, source, target ->
        Projectile.ProjectileBuilder(world)
            .startHeight(startHeight)
            .endHeight(endHeight)
            .delay(delay!!)
            .lifetime(lifetime!!)
            .pitch(pitch!!)
            .graphic(variant)
            .source(source)
            .target(target)
            .build()
    }
}

/**
 * A builder for a collection of [Ammo] of the same type.
 */
@AmmoDslMarker
class AmmoTypeBuilder(val name: String) {
    /**
     * The constraints on what weapon classes this ammo type can be fired from.
     */
    val fired = AmmoTypeUseabilityBuilder()

    /**
     * The base projectile factory for projectiles of this ammo type.
     */
    val projectile = AmmoProjectileFactoryBuilder()

    /**
     * The variants of ammo that belong to this ammo type.
     */
    val variants = mutableListOf<AmmoBuilder>()

    /**
     * String invocation overload that creates a new ammo variant
     * given an [AmmoBuilder]
     */
    operator fun String.invoke(builder: AmmoBuilder.() -> Unit) {
        val variant = AmmoBuilder(this)
        builder(variant)

        variants.add(variant)
    }

    /**
     * Build a list of ItemID -> [Ammo] pairs based on the variants in this [AmmoTypeBuilder].
     */
    fun build(): Map<Int, Ammo> {
        val ammoTypeSingular = name.removeSuffix("s")
        val ammoMap = mutableMapOf<Int, Ammo>()

        variants.forEach {
            val requiredLevel = it.requires.level
            val dropChance = it.dropChanceValue

            val projectileGraphicId = it.graphics.projectile ?: throw RuntimeException("Every ammo requires a projectile id")
            val projectileFactory = projectile.build(projectileGraphicId)
            val attack = it.graphics.attack?.let({ Graphic(it, 0, 100) })

            val ammoName = "${it.name} $ammoTypeSingular"
            val ammo = Ammo(name, requiredLevel, dropChance, projectileFactory, attack)
            val ammoId = lookup_item(ammoName)?.id ?: throw RuntimeException("Unable to find ammo named $ammoName")

            ammoMap[ammoId] = ammo

            val enchantmentGraphic = it.graphics.enchanted?.let(::Graphic)
            val enchantmentEffect = it.enchantment.effect
            val enchantmentChanceSupplier = it.enchantment.chance

            if (enchantmentGraphic != null && enchantmentEffect != null && enchantmentChanceSupplier != null) {
                val enchantment = AmmoEnchantment(enchantmentGraphic, enchantmentEffect, enchantmentChanceSupplier)
                val enchantedAmmoName = "$ammoName (e)"
                val enchantedAmmo = Ammo(name, requiredLevel, dropChance, projectileFactory, attack, enchantment)
                val enchantedAmmoId = lookup_item(enchantedAmmoName)?.id ?: throw RuntimeException("Unable to find ammo named $enchantedAmmoName")

                ammoMap[enchantedAmmoId] = enchantedAmmo
            }
        }

        return ammoMap
    }
}
