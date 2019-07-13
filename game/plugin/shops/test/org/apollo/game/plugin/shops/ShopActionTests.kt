package org.apollo.game.plugin.shops

import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class ShopActionTests {

    @TestMock
    lateinit var player: Player

    @TestMock
    lateinit var action: ActionCapture




}