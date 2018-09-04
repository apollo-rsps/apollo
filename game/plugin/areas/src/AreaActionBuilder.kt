package org.apollo.game.plugins.area

/**
 * A builder for ([Area], [AreaAction]) [Pair]s.
 */
class AreaActionBuilder internal constructor(val name: String, val area: Area) {

    private var entrance: AreaListener = { }

    private var inside: AreaListener = { }

    private var exit: AreaListener = { }

    /**
     * Places the contents of this builder into an ([Area], [AreaAction]) [Pair].
     */
    internal fun build(): Pair<Area, AreaAction> {
        return Pair(area, AreaAction(entrance, inside, exit))
    }

    /**
     * The [listener] to execute when a player enters the associated [Area].
     */
    fun entrance(listener: AreaListener) {
        this.entrance = listener
    }

    /**
     * The [listener] to execute when a player moves around inside the associated [Area].
     */
    fun inside(listener: AreaListener) {
        this.inside = listener
    }

    /**
     * The [listener] to execute when a player moves exits the associated [Area].
     */
    fun exit(listener: AreaListener) {
        this.exit = listener
    }
}