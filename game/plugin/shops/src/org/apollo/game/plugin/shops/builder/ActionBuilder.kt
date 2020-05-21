package org.apollo.game.plugin.shops.builder

import org.apollo.cache.def.NpcDefinition

/**
 * A builder to provide the action id used to open the shop.
 */
@ShopDslMarker
class ActionBuilder {

    private var action: String = "Trade"

    private var actionId: Int? = null

    /**
     * Sets the name or id of the action used to open the shop interface with an npc. Defaults to "Trade".
     *
     * If specifying an id it must account for hidden npc menu actions (if any exist) - if "Open Shop" is the first
     * action displayed when the npc is right-clicked, it does not necessarily mean that the action id is `1`.
     *
     * @param action The `name` (as a [String]) or `id` (as an `Int`) of the npc's action menu, to open the shop.
     * @throws IllegalArgumentException If `action` is not a [String] or [Int].
     */ // TODO this is dumb, replace it
    override fun equals(@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") action: Any?): Boolean {
        if (action is String) {
            this.action = action
            return true
        } else if (action is Int) {
            actionId = action
            return true
        }

        throw IllegalArgumentException("The Npc option must be provided as a String (the option name) or an Int (the option index)\"")
    }

    /**
     * Returns the open shop action slot.
     *
     * @throws IllegalArgumentException If the action id or name is invalid.
     */
    internal fun slot(npc: NpcDefinition): Int {
        actionId?.let { action ->
            require(npc.hasInteraction(action - 1)) {
                "Npc ${npc.name} does not have an an action $action." // action - 1 because ActionMessages are 1-based
            }

            return action
        }

        val index = npc.interactions.indexOf(action)
        require(index != -1) { "Npc ${npc.name} does not have an an action $action." }

        return index + 1 // ActionMessages are 1-based
    }

    /**
     * Throws [UnsupportedOperationException].
     */
    override fun hashCode(): Int = throw UnsupportedOperationException("ActionBuilder is a utility class for a DSL " +
        "and improperly implements equals() - it should not be used anywhere outside of the DSL.")

}