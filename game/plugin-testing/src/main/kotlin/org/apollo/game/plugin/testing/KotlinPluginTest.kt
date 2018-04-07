package org.apollo.game.plugin.testing

import io.mockk.every
import io.mockk.spyk
import io.mockk.staticMockk
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.message.handler.MessageHandlerChainSet
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.KotlinPluginEnvironment
import org.apollo.game.plugin.PluginMetaData
import org.apollo.game.plugin.testing.fakes.FakePluginContextFactory
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.util.*


abstract class KotlinPluginTest : KotlinPluginTestHelpers() {
    override lateinit var world: World
    override lateinit var player: Player
    override lateinit var messageHandlers: MessageHandlerChainSet

    val items = mutableMapOf<Int, ItemDefinition>()

    @BeforeEach
    open fun setup() {
        messageHandlers = MessageHandlerChainSet()
        world = spyk(World())

        val pluginEnvironment = KotlinPluginEnvironment(world)
        pluginEnvironment.setContext(FakePluginContextFactory.create(messageHandlers))
        pluginEnvironment.load(ArrayList<PluginMetaData>())

        try {
            staticMockk("org.apollo.game.plugin.api.WorldKt").mock()
        } catch (ex: ClassNotFoundException) {
            // do nothing, plugin doesnt use this mock
            // @todo: yuck
        }
        staticMockk<ItemDefinition>().mock()
        every { ItemDefinition.lookup(any()) } answers { items[args[0] as Int] }

        player = world.spawnPlayer("testPlayer")
    }

    @AfterEach
    open fun teardown() {
        if (player.hasAction()) {
            player.waitForActionCompletion()
        }
    }
}
