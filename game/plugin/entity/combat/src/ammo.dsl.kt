import org.apollo.game.model.Graphic
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Projectile

val AMMO = mutableMapOf<Int, Ammo>()

/**
 * Create and register a new collection of [Ammo] based on the given
 * [AmmoTypeBuilder].
 */
public fun ammo(name: String, builder: AmmoTypeBuilder.() -> Unit) {
    val ammoType = AmmoTypeBuilder(name)
    builder(ammoType)

    val ammoList = ammoType.build()
    ammoList.forEach { (id, ammo) -> AMMO[id] = ammo }
}

open class Ammo(
    val requiredLevel: Int,
    val dropChance: Double,
    val projectileFactory: AmmoProjectileFactory,
    val attack: Graphic? = null
) {
    fun toEnchanted(
        enchantment: Graphic,
        enchantmentEffect: AmmoEnchantmentEffect,
        enchantmentChanceSupplier: AmmoEnchantmentChanceSupplier
    ): EnchantedAmmo {
        return EnchantedAmmo(
            enchantment,
            enchantmentEffect,
            enchantmentChanceSupplier,
            requiredLevel,
            dropChance,
            projectileFactory
        )
    }
}

class EnchantedAmmo(
    val enchantment: Graphic,
    val enchantmentEffect: AmmoEnchantmentEffect,
    val enchantmentChanceSupplier: AmmoEnchantmentChanceSupplier,
    requiredLevel: Int,
    dropChance: Double,
    projectileFactory: AmmoProjectileFactory,
    attack: Graphic? = null
) : Ammo(
    requiredLevel,
    dropChance,
    projectileFactory,
    attack
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
     * The graphic used when a [Mob] is hit with an enchanted ammo effect.
     */
    var enchanted: Int? = null

    operator fun invoke(builder: AmmoGraphicsBuilder.() -> Unit) = builder(this)
}

@AmmoDslMarker
class AmmoDropChance()

/**
 * A DSL builder for an ammo's ranged level requirements.
 */
@AmmoDslMarker
class AmmoRequirementBuilder {
    internal var level: Int = 1

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
    internal var dropChanceValue: Double = 1.0

    /**
     * Internal value to hold a dummy [AmmoDropChance] field that can be used by the overriden `%` operator.
     */
    internal val dropChance = AmmoDropChance()

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
 * A builder for effects applied to [Mob]'s hit with ammo that can be chanted.
 */
@AmmoDslMarker
class AmmoEnchantmentBuilder {
    internal var effect: AmmoEnchantmentEffect? = null
    internal var chance: AmmoEnchantmentChanceSupplier? = null

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
    internal val fired = AmmoTypeUseabilityBuilder()

    /**
     * The base projectile factory for projectiles of this ammo type.
     */
    internal val projectile = AmmoProjectileFactoryBuilder()

    /**
     * The variants of ammo that belong to this ammo type.
     */
    internal val variants = mutableListOf<AmmoBuilder>()

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
    fun build(): List<Pair<Int, Ammo>> {
        val ammoTypeSingular = name.removeSuffix("s")
        val ammoList = mutableListOf<Pair<Int, Ammo>>()

        variants.forEach {
            val requiredLevel = it.requires.level
            val dropChance = it.dropChanceValue

            val projectileGraphicId = it.graphics.projectile ?: throw RuntimeException("Every ammo requires a projectile id")
            val projectileFactory = projectile.build(projectileGraphicId)
            val attack = it.graphics.attack?.let(::Graphic)

            val ammoName = "${it.name} $ammoTypeSingular"
            val ammo = Ammo(requiredLevel, dropChance, projectileFactory, attack)
            val ammoId = lookup_item(ammoName)?.id ?: throw RuntimeException("Unable to find ammo named $ammoName")

            ammoList.add(Pair(ammoId, ammo))

            val enchantment = it.graphics.enchanted?.let(::Graphic)
            val enchantmentEffect = it.enchantment.effect
            val enchantmentChanceSupplier = it.enchantment.chance

            if (enchantment != null && enchantmentEffect != null && enchantmentChanceSupplier != null) {
                val enchantedAmmoName = "$ammoName (e)"
                val enchantedAmmo = ammo.toEnchanted(enchantment, enchantmentEffect, enchantmentChanceSupplier)
                val enchantedAmmoId = lookup_item(enchantedAmmoName)?.id ?: throw RuntimeException("Unable to find ammo named $enchantedAmmoName")

                ammoList.add(Pair(enchantedAmmoId, enchantedAmmo))
            }
        }

        return ammoList
    }
}
