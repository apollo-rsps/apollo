package org.apollo.game.plugin.testing

import org.apollo.cache.def.ItemDefinition
import org.apollo.game.message.handler.MessageHandlerChainSet
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.KotlinPluginEnvironment
import org.apollo.game.plugin.PluginContext
import org.apollo.game.plugin.PluginMetaData
import org.apollo.game.plugin.testing.fakes.FakePluginContextFactory
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Matchers.anyInt
import org.powermock.api.mockito.PowerMockito.mockStatic
import org.powermock.api.mockito.PowerMockito.spy
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*

@RunWith(PowerMockRunner::class)
@PrepareForTest(World::class, PluginContext::class, Player::class, ItemDefinition::class)
abstract class KotlinPluginTest : KotlinPluginTestHelpers() {
    override lateinit var world: World
    override lateinit var player: Player
    override lateinit var messageHandlers: MessageHandlerChainSet

    val items = mutableMapOf<Int, ItemDefinition>()

    @Before
    open fun setup() {
        messageHandlers = MessageHandlerChainSet()
        world = spy(World())

        val pluginEnvironment = KotlinPluginEnvironment(world)
        pluginEnvironment.setContext(FakePluginContextFactory.create(messageHandlers))
        pluginEnvironment.load(ArrayList<PluginMetaData>())

        mockStatic(ItemDefinition::class.java)
        given(ItemDefinition.lookup(anyInt())).willAnswer {
            items[it.arguments[0] as Int]
        }

        player = world.spawnPlayer("testPlayer")
    }

    @After
    open fun teardown() {
        if (player.hasAction()) {
            player.waitForActionCompletion()
        }
    }
}
