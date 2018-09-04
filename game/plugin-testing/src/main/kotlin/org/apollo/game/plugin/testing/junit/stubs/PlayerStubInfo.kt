package org.apollo.game.plugin.testing.junit.stubs

import org.apollo.game.model.Position
import org.apollo.game.plugin.testing.junit.api.annotations.Name
import org.apollo.game.plugin.testing.junit.api.annotations.Pos

class PlayerStubInfo {
    companion object {
        fun create(annotations: Collection<Annotation>): PlayerStubInfo {
            val info = PlayerStubInfo()

            annotations.forEach {
                when (it) {
                    is Name -> info.name = it.value
                    is Pos -> info.position = Position(it.x, it.y, it.height)
                }
            }

            return info
        }
    }

    var position = Position(3222, 3222)
    var name = "test"
}
