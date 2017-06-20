package org.apollo.game.action

import org.apollo.game.model.Position
import org.apollo.game.model.entity.Mob

abstract class AsyncDistancedAction<T : Mob> : DistancedAction<T>, AsyncActionTrait {

    override val runner: AsyncActionRunner

    constructor(delay: Int, immediate: Boolean, mob: T, position: Position, distance: Int) :
            super(delay, immediate, mob, position, distance) {

        this.runner = AsyncActionRunner({ this }, { executeActionAsync() })
    }

    abstract suspend fun executeActionAsync()

    override fun stop() {
        super.stop()
        runner.stop()
    }

    override fun executeAction() {
        if (!runner.started()) {
            runner.start()
        }

        runner.pulse()
    }

}

