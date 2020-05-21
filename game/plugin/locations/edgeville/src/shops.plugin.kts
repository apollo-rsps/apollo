package org.apollo.plugin.locations.edgeville

import org.apollo.game.plugin.shops.builder.shop

shop("Edgeville General Store") {
    operated by "Shop keeper"(528) and "Shop assistant"(529)
    buys any items

    sell(5) of "Pot"
    sell(2) of "Jug"
    sell(2) of "Shears"
    sell(3) of "Bucket"
    sell(2) of "Bowl"
    sell(2) of "Cake tin"
    sell(2) of "Tinderbox"
    sell(2) of "Chisel"
    sell(5) of "Hammer"
    sell(5) of "Newcomer map"
}

/**
 * TODO make a way to have requirements to open shops. Players have to have finished Dragon Slayer to access
 * "Oziach's Armour"
 */
shop("Oziach's Armour") {
    operated by "Oziach"

    sell(2) of "Rune platebody"
    sell(2) of "Green d'hide body"
    sell(35) of "Anti-dragon shield"
}