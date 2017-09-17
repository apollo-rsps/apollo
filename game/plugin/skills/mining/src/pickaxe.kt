package org.apollo.game.plugin.skills.mining

import org.apollo.game.model.Animation;

enum class Pickaxe(val id: Int, val level: Int, val animation: Animation, val pulses: Int) {
    RUNE(1275, 41, Animation(624), 3),
    ADAMANT(1271, 31, Animation(628), 4),
    MITHRIL(1273, 21, Animation(629), 5),
    STEEL(1269, 1, Animation(627), 6),
    ITRON(1267, 1, Animation(626), 7),
    BRONZE(1265, 1, Animation(625), 8)
}

val PICKAXES = Pickaxe.values()



fun getPickaxes(): Array<Pickaxe> {
    return PICKAXES
}

fun lookupPickaxe(id: Int): Pickaxe? = PICKAXES.find { it.id == id }

