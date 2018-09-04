package org.apollo.plugin.consumables

import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill

/**
 * An item that can be consumed to restore or buff stats.
 */
abstract class Consumable(val name: String, val id: Int, val sound: Int, val delay: Int, val replacement: Int?) {

    abstract fun addEffect(player: Player)

    fun consume(player: Player, slot: Int) {
        addEffect(player)
        player.inventory.reset(slot)

        if (replacement != null) {
            player.inventory.add(replacement)
        }
    }
}

private val consumables = mutableMapOf<Int, Consumable>()

fun isConsumable(itemId: Int) = consumables.containsKey(itemId)
fun lookupConsumable(itemId: Int): Consumable = consumables.get(itemId)!!
fun consumable(consumable: Consumable) = consumables.put(consumable.id, consumable)

enum class FoodOrDrinkType(val action: String) {
    FOOD("eat"), DRINK("drink")
}

class FoodOrDrink : Consumable {

    companion object {
        const val EAT_FOOD_SOUND = 317
    }

    val restoration: Int
    val type: FoodOrDrinkType

    constructor(
        name: String,
        id: Int,
        delay: Int,
        type: FoodOrDrinkType,
        restoration: Int,
        replacement: Int? = null
    ) : super(name, id, EAT_FOOD_SOUND, delay, replacement) {
        this.type = type
        this.restoration = restoration
    }

    override fun addEffect(player: Player) {
        val hitpoints = player.skillSet.getSkill(Skill.HITPOINTS)
        val hitpointsLevel = hitpoints.currentLevel
        val newHitpointsLevel = Math.min(hitpointsLevel + restoration, hitpoints.maximumLevel)

        player.sendMessage("You ${type.action} the $name.")
        if (newHitpointsLevel > hitpointsLevel) {
            player.sendMessage("It heals some health.")
        }

        player.skillSet.setCurrentLevel(Skill.HITPOINTS, newHitpointsLevel)
    }
}

/**
 * Define a new type of [Consumable] food.
 */
fun food(name: String, id: Int, restoration: Int, replacement: Int? = null, delay: Int = 3) {
    consumable(FoodOrDrink(name, id, delay, FoodOrDrinkType.FOOD, restoration, replacement))
}

/**
 * Define a new type of [Consumable] drink.
 */
fun drink(name: String, id: Int, restoration: Int, replacement: Int? = null, delay: Int = 3) {
    consumable(FoodOrDrink(name, id, delay, FoodOrDrinkType.DRINK, restoration, replacement))
}