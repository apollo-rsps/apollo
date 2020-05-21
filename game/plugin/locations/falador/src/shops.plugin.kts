package org.apollo.plugin.locations.falador

import org.apollo.game.plugin.shops.builder.shop

shop("Falador General Store") {
    operated by "Shop keeper"(524) and "Shop assistant"( 525)
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

shop("Cassie's Shield Shop") {
    operated by "Cassie"

    sell(5) of "Wooden shield"
    sell(3) of "Bronze sq shield"
    sell(3) of "Bronze kiteshield"
    sell(2) of "Iron sq shield"
    sell(0) of "Iron kiteshield"
    sell(0) of "Steel sq shield"
    sell(0) of "Steel kiteshield"
    sell(0) of "Mithril sq shield"
}

shop("Flynn's Mace Market") {
    operated by "Flynn"

    category("mace") {
        sell(5) of "Bronze"
        sell(4) of "Iron"
        sell(3) of "Mithril"
        sell(2) of "Adamant"
    }
}

shop("Herquin's Gems") {
    operated by "Herquin"

    category("uncut", affix = prefix) {
        sell(1) of "Sapphire"
        sell(0) of {
            -"Emerald"
            -"Ruby"
            -"Diamond"
        }
    }

    sell(1) of "Sapphire"
    sell(0) of {
        -"Emerald"
        -"Ruby"
        -"Diamond"
    }
}

shop("Wayne's Chains - Chainmail Specialist") {
    operated by "Wayne"

    category("chainbody") {
        sell(3) of "Bronze"
        sell(2) of "Iron"
        sell(1) of "Steel"
        sell(1) of "Black"
        sell(1) of "Mithril"
        sell(1) of "Adamant"
    }
}