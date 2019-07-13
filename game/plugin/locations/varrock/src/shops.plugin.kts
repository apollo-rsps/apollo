package org.apollo.plugin.locations.varrock

import org.apollo.game.plugin.shops.builder.shop

shop("Aubury's Rune Shop.") {
    operated by "Aubury"

    category("runes") {
        sell(5000) of {
            -"Earth"
            -"Water"
            -"Fire"
            -"Air"
            -"Mind"
            -"Body"
        }

        sell(250) of {
            -"Chaos"
            -"Death"
        }
    }
}

shop("Lowe's Archery Emporium.") {
    operated by "Lowe"

    category("arrows") {
        sell(2000) of "Bronze"
        sell(1500) of "Iron"
        sell(1000) of "Steel"
        sell(800) of "Mithril"
        sell(600) of "Adamant"
    }

    category("normal weapons", affix = nothing) {
        sell(4) of "Shortbow"
        sell(4) of "Longbow"
        sell(2) of "Crossbow"
    }

    category("shortbows") {
        sell(3) of "Oak"
        sell(2) of "Willow"
        sell(1) of "Maple"
    }

    category("longbows") {
        sell(3) of "Oak"
        sell(2) of "Willow"
        sell(1) of "Maple"
    }
}

shop("Horvik's Armour Shop.") {
    operated by "Horvik"

    category("chainbody") {
        sell(5) of "Bronze"
        sell(3) of "Iron"
        sell(3) of "Steel"
        sell(1) of "Mithril"
    }

    category("platebody") {
        sell(3) of "Bronze"

        sell(1) of {
            -"Iron"
            -"Steel"
            -"Black"
            -"Mithril"
        }
    }

    sell(1) of {
        -"Iron platelegs"
        -"Studded body"
        -"Studded chaps"
    }
}

shop("Thessalia's Fine Clothes.") {
    operated by "Thessalia"

    category("apron") {
        sell(3) of "White"
        sell(1) of "Brown"
    }

    category("leather", affix = prefix) {
        sell(12) of "Body"
        sell(10) of "Gloves"
        sell(10) of "Boots"
    }

    category("skirt") {
        sell(5) of "Pink"
        sell(3) of "Black"
        sell(2) of "Blue"
    }

    sell(4) of "Cape"
    sell(5) of "Silk"

    sell(3) of {
        -"Priest gown"(426)
        -"Priest gown"(428)
    }
}

shop("Varrock General Store.") {
    operated by "Shopkeeper"(522) and "Shop assistant"(523)
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

shop("Varrock Swordshop.") {
    operated by "Shopkeeper"(551) and "Shop assistant"(552)

    category("swords") {
        sell(5) of "Bronze"
        sell(4) of "Iron"
        sell(4) of "Steel"
        sell(3) of "Black"
        sell(3) of "Mithril"
        sell(2) of "Adamant"
    }

    category("longswords") {
        sell(4) of "Bronze"
        sell(3) of "Iron"
        sell(3) of "Steel"
        sell(2) of "Black"
        sell(2) of "Mithril"
        sell(1) of "Adamant"
    }

    category("daggers") {
        sell(10) of "Bronze"
        sell(6) of "Iron"
        sell(5) of "Steel"
        sell(4) of "Black"
        sell(3) of "Mithril"
        sell(2) of "Adamant"
    }
}

shop("Zaff's Superior Staffs!") {
    operated by "Zaff"

    category("staves", affix = nothing) {
        sell(5) of {
            -"Battlestaff"
            -"Staff"
            -"Magic staff"
        }

        sell(2) of {
            -"Staff of air"
            -"Staff of water"
            -"Staff of earth"
            -"Staff of fire"
        }
    }
}
