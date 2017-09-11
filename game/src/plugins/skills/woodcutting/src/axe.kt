package org.apollo.game.plugin.skills.mining

import org.apollo.game.model.Animation;

enum class Axe(val id: Int, val level: Int, val animation: Animation, val pulses: Int) {
    RUNE(1275, 41, Animation(624), 3),  // rune
    ADAMANT(1271, 31, Animation(628), 4), // adamant
    MITHRIL(1273, 21, Animation(629), 5), // mithril
    STEEL(1269, 1, Animation(627), 6),  // steel
    ITRON(1267, 1, Animation(626), 7),  // iron
    BRONZE(1265, 1, Animation(625), 8)  // bronze
}



fun getAxes(): Array<Axe> {
    return Axe.values()
}

fun lookupPickaxe(id: Int): Axe? {
    for (axe in Axe.values()) {
        if (axe.id == id) {
            return axe;
        }
    }
    return null
}

