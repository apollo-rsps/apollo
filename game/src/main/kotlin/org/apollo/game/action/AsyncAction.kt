package org.apollo.game.action

import org.apollo.game.model.entity.Mob

abstract class AsyncAction<T : Mob> : Action<T>, AsyncActionTrait {

    override var continuation: ActionCoroutine? = null

    constructor(delay: Int, immediate: Boolean, mob: T) : super(delay, immediate, mob)

    override fun execute() {
        if (update()) {
            stop()
        }
    }
}