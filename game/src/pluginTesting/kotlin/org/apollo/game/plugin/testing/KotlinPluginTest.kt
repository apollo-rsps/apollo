package org.apollo.game.plugin.testing

import org.apollo.game.message.handler.MessageHandlerChainSet
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.*
import org.apollo.game.plugin.testing.fakes.FakePluginContextFactory
import org.junit.Before
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*

@RunWith(PowerMockRunner::class)
@PrepareForTest(World::class, PluginContext::class, Player::class)
abstract class KotlinPluginTest: KotlinPluginTestHelpers() {
    override lateinit var world: World
    override lateinit var player: Player
    override lateinit var messageHandlers: MessageHandlerChainSet

    @Before
    fun setup() {
        messageHandlers = MessageHandlerChainSet()
        world = PowerMockito.spy(World())

        val pluginEnvironment = KotlinPluginEnvironment(world)
        pluginEnvironment.setContext(FakePluginContextFactory.create(messageHandlers))
        pluginEnvironment.load(ArrayList<PluginMetaData>())

        player = world.spawnPlayer("testPlayer")
    }

}
