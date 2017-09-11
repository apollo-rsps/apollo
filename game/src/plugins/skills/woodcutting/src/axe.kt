package org.apollo.game.plugin.skills.mining

import org.apollo.game.model.Animation;

//Animation IDs: https://www.rune-server.ee/runescape-development/rs2-client/configuration/272373-emote-gfx-id-list.html
enum class Axe(val id: Int, val level: Int, val animation: Animation, val pulses: Int) {
    RUNE(1359, 41, Animation(867), 3),  // rune
    ADAMANT(1357, 31, Animation(869), 4), // adamant
    MITHRIL(1355, 21, Animation(871), 5), // mithril
    BLACK(1361, 11, Animation(873), 6),  // steel
    STEEL(1353, 6, Animation(875), 6),  // steel
    IRON(1349, 1, Animation(877), 7),  // iron
    BRONZE(1351, 1, Animation(879), 8)  // bronze
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

