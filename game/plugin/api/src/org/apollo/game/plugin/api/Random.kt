package org.apollo.game.plugin.api

import java.util.*

val RAND = Random()

fun rand(bounds: Int): Int {
    return RAND.nextInt(bounds)
}