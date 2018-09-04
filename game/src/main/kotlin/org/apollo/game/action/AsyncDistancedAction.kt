package org.apollo.game.action

import org.apollo.game.model.Position
import org.apollo.game.model.entity.Mob

/**
 * A `DistancedAction` that uses `ActionCoroutine`s to run asynchronously.
 */
abstract class AsyncDistancedAction<T : Mob> : DistancedAction<T>, AsyncActionTrait {

    override var continuation: ActionCoroutine? = null

    constructor(delay: Int, immediate: Boolean, mob: T, position: Position, distance: Int) :
            super(delay, immediate, mob, position, distance)

    override fun executeAction() {
        if (update()) {
            stop()
        }
    }
}
