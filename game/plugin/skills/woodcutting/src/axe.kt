package org.apollo.game.plugin.skills.woodcutting

import org.apollo.game.model.Animation;

//Animation IDs thanks to Deadly A G S at rune-server.ee
enum class Axe(val id: Int, val level: Int, val animation: Animation, val pulses: Int) {
    RUNE(1359, 41, Animation(867), 3),
    ADAMANT(1357, 31, Animation(869), 4),
    MITHRIL(1355, 21, Animation(871), 5),
    BLACK(1361, 11, Animation(873), 6),
    STEEL(1353, 6, Animation(875), 6),
    IRON(1349, 1, Animation(877), 7),
    BRONZE(1351, 1, Animation(879), 8);

    companion object {
        private val AXES = Axe.values()
        fun getAxes(): Array<Axe> {
            return AXES
        }
    }
}


