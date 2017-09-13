package org.apollo.game.plugin.skills.mining

import org.apollo.game.model.Animation;

//Animation IDs: https://www.rune-server.ee/runescape-development/rs2-client/configuration/272373-emote-gfx-id-list.html
enum class Axe(val id: Int, val level: Int, val animation: Animation, val pulses: Int) {
    RUNE(1359, 41, Animation(867), 3),
    ADAMANT(1357, 31, Animation(869), 4),
    MITHRIL(1355, 21, Animation(871), 5),
    BLACK(1361, 11, Animation(873), 6),
    STEEL(1353, 6, Animation(875), 6),
    IRON(1349, 1, Animation(877), 7),
    BRONZE(1351, 1, Animation(879), 8)
}

fun getAxes(): Array<Axe> {
    return Axe.values()
}

fun lookupPickaxe(id: Int): Axe? = Axe.values().find { it.id == id }

