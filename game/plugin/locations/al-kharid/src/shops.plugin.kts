package org.apollo.plugin.locations.alKharid

import org.apollo.game.plugin.shops.builder.shop

shop("Al-Kharid General Store") {
    operated by "Shop keeper"(524) and "Shop assistant"(525)
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
 * TODO add a way to "unlock" items, as more of Ali Morrisane's items are unlocked by completing certain parts of
 * the Rogue Trader minigame progressively.
 *
 * TODO this shop can be accessed only through dialogue, so support for that should be added.
 */
/*shop("Ali's Discount Wares") {
    operated by "Ali Morrisane"

    sell(3) of "Pot"
    sell(2) of "Jug"
    sell(10) of { "Waterskin"(1825) }
    sell(3) of "Desert shirt"
    sell(2) of "Desert boots"
    sell(19) of "Bucket"
    sell(11) of "Fake beard"
    sell(12) of "Karidian headpiece"
    sell(50) of "Papyrus"
    sell(5) of "Knife"
    sell(11) of "Tinderbox"
    sell(23) of "Bronze pickaxe"
    sell(15) of "Raw chicken"
}*/

shop("Dommik's Crafting Store") {
    operated by "Dommik"

    sell(2) of "Chisel"
    category("mould") {
        sell(10) of "Ring"
        sell(2) of "Necklace"
        sell(10) of "Amulet"
    }
    sell(3) of "Needle"
    sell(100) of "Thread"
    category("mould") {
        sell(3) of "Holy"
        sell(10) of "Sickle"
        sell(10) of "Tiara"
    }
}

shop("Gem Trader") {
    operated by "Gem trader"

    category("uncut", affix = prefix) {
        sell(1) of {
            -"Sapphire"
            -"Emerald"
        }
        sell(0) of {
            -"Ruby"
            -"Diamond"
        }
    }

    sell(1) of {
        -"Sapphire"
        -"Emerald"
    }
    sell(0) of {
        -"Ruby"
        -"Diamond"
    }
}

shop("Louie's Armoured Legs Bazaar") {
    operated by "Louie Legs"

    category("platelegs", depluralise = false) {
        sell(5) of "Bronze"
        sell(3) of "Iron"
        sell(2) of "Steel"
        sell(1) of "Black"
        sell(1) of "Mithril"
        sell(1) of "Adamant"
    }
}

shop("Ranael's Super Skirt Store") {
    operated by "Ranael"

    category("plateskirt") {
        sell(5) of "Bronze"
        sell(3) of "Iron"
        sell(2) of "Steel"
        sell(1) of "Black"
        sell(1) of "Mithril"
        sell(1) of "Adamant"
    }
}

shop("Shantay Pass Shop") {
    operated by "Shantay"

    sell(100) of { "Waterskin"(1823) }
    sell(100) of { "Waterskin"(1831) }
    sell(10) of "Jug of water"
    sell(10) of "Bowl of water"
    sell(10) of "Bucket of water"
    sell(10) of "Knife"
    category("desert", affix = prefix) {
        sell(10) of "shirt"
        sell(10) of "robe"
        sell(10) of "boots"
    }
    sell(10) of "Bronze bar"
    sell(500) of "Feather"
    sell(10) of "Hammer"
    sell(0) of "Bucket"
    sell(0) of "Bowl"
    sell(0) of "Jug"
    sell(500) of "Shantay pass"
    sell(20) of "Rope"
}

shop("Zeke's Superior Scimitars") {
    operated by "Zeke"

    category("scimitar") {
        sell(5) of "Bronze"
        sell(3) of "Iron"
        sell(2) of "Steel"
        sell(1) of "Mithril"
    }
}