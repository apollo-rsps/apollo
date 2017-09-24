package org.apollo.game.plugin.api

import java.util.Random

val RAND = Random()

fun rand(bounds: Int): Int {
    return RAND.nextInt(bounds)
}