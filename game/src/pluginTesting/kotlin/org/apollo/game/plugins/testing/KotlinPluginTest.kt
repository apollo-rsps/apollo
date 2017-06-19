package org.apollo.game.plugins.testing

import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.message.handler.MessageHandlerChainSet
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.*
import org.apollo.net.message.Message
import org.apollo.util.security.PlayerCredentials
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Matchers.any
import org.mockito.Mockito.mock
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.powermock.api.mockito.PowerMockito.spy
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*

@RunWith(PowerMockRunner::class)
@PrepareForTest(World::class, PluginContext::class, Player::class)
abstract class KotlinPluginTest {
    private var ctx: KotlinPluginTestContext? = null

    protected fun context(): KotlinPluginTestContext {
        return ctx ?: throw IllegalStateException("Setup method not called")
    }

    @Before
    fun setup() {
        val messageHandlerChainSet = MessageHandlerChainSet()
        val world = spy<World>(World())

        val answer = Answer<Any?> { invocation: InvocationOnMock ->
            messageHandlerChainSet.putHandler(
                    invocation.arguments[0] as Class<Message>,
                    invocation.arguments[1] as MessageHandler<*>)
        }

        val pluginContext = mock<PluginContext>(PluginContext::class.java, answer)
        val pluginEnvironment = KotlinPluginEnvironment(world)

        pluginEnvironment.setContext(pluginContext)
        pluginEnvironment.load(ArrayList<PluginMetaData>())

        val credentials = PlayerCredentials("test", "test", 1, 1, "0.0.0.0")
        val position = Position(3200, 3200, 0)

        val player = spy<Player>(Player(world, credentials, position))
        org.powermock.api.mockito.PowerMockito.doNothing().`when`(player).send(any<Message>())

        world.register(player)

        val region = world.regionRepository.fromPosition(position)
        region.addEntity(player)

        ctx = KotlinPluginTestContext(world, player, messageHandlerChainSet)
    }

    @After
    fun destroy() {
        ctx!!.shutdown()
    }

}
