package org.apollo.game.plugin.skills.mining

import org.apollo.game.model.Animation;

data class Pickaxe(val id: Int, val level: Int, val animation: Animation, val pulses: Int)

private val PICKAXES = mutableMapOf<Int, Pickaxe>()

fun addPickaxe(id: Int, level: Int, animation: Animation, pulses: Int) {
    PICKAXES.put(id, Pickaxe(id, level, animation, pulses))
}

fun getPickaxes(): MutableSet<Int> {
    return PICKAXES.keys
}

fun lookupPickaxe(id: Int): Pickaxe? {
    return PICKAXES[id];
}

