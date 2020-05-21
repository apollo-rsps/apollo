package org.apollo.plugin.locations.lumbridge

import org.apollo.game.plugin.shops.builder.shop

shop("Lumbridge General Store") {
    operated by "Shop keeper" and "Shop assistant"
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

shop("Bob's Brilliant Axes") {
    operated by "Bob"

    category("pickaxe") {
        sell(5) of "Bronze"
    }

    category("axe") {
        sell(10) of "Bronze"
        sell(5) of "Iron"
        sell(3) of "Steel"
    }

    category("battleaxe") {
        sell(5) of "Iron"
        sell(2) of "Steel"
        sell(1) of "Mithril"
    }
}

// TODO find out how to make objects be able to open stores for the Culinaromancer's Chest. Also links to TODO in
// Al-Kharid's shops plugin for "unlockable" items.