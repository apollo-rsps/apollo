package org.apollo.game.plugin.entity.actions

import io.mockk.verify
import org.apollo.game.message.impl.SetPlayerActionMessage
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@ExtendWith(ApolloTestingExtension::class)
class PlayerActionTests {

    @TestMock
    lateinit var player: Player

    @ParameterizedTest
    @EnumSource(PlayerActionType::class)
    fun `enabling and disabling PlayerActions sends SetPlayerActionMessages`(type: PlayerActionType) {
        player.enableAction(type)

        verify { player.send(eq(SetPlayerActionMessage(type.displayName, type.slot, type.primary))) }
        assertTrue(player.actionEnabled(type)) { "Action $type should have been enabled, but was not." }

        player.disableAction(type)

        verify { player.send(eq(SetPlayerActionMessage("null", type.slot, type.primary))) }
        assertFalse(player.actionEnabled(type)) { "Action $type should not have been enabled, but was." }
    }
}