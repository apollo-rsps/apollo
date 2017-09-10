package org.apollo.game.plugin.skills.mining

enum class Gem(val id: Int, val chance: Int) {
    UNCUT_SAPPHIRE(1623, 0), // uncut sapphire
    UNCUT_EMERALD(1605, 0),// uncut emerald
    UNCUT_RUBY(1619, 0), // uncut ruby
    UNCUT_DIAMOND(1617, 0)  // uncut diamond
}

fun lookupGem(id: Int): Gem? {
    for (gem in Gem.values()) {
        if (gem.id == id) {
            return gem;
        }
    }
    return null;
}

