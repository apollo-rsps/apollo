package org.apollo.game.action

import org.apollo.game.model.entity.Mob

abstract class AsyncAction<T : Mob> : Action<T>, AsyncActionTrait {
    override val runner: AsyncActionRunner

    constructor(delay: Int, immediate: Boolean, mob: T) : super(delay, immediate, mob) {
        this.runner = AsyncActionRunner({ this }, { executeActionAsync() })
    }

    abstract suspend fun executeActionAsync()

    override fun execute() {
        if (!runner.started()) {
            runner.start()
        }

        runner.pulse()
    }

    override fun stop() {
        super.stop()
        runner.stop()
    }
}