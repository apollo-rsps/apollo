package org.apollo.game.plugins.testing

import org.apollo.cache.def.NpcDefinition
import org.apollo.game.action.Action
import org.apollo.game.message.handler.MessageHandler
import org.apollo.game.message.handler.MessageHandlerChainSet
import org.apollo.game.message.impl.*
import org.apollo.game.model.*
import org.apollo.game.model.entity.*
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.model.entity.obj.StaticGameObject
import org.apollo.game.plugin.*
import org.apollo.net.message.Message
import org.apollo.util.security.PlayerCredentials
import org.junit.Assert
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Matchers.any
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*

@RunWith(PowerMockRunner::class)
@PrepareForTest(World::class, PluginContext::class, Player::class)
abstract class KotlinPluginTest {
    lateinit var world: World
    lateinit var player: Player
    lateinit var messageHandlers: MessageHandlerChainSet

    @Before
    fun setup() {
        messageHandlers = MessageHandlerChainSet()
        world = PowerMockito.spy(World())

        val answer = Answer<Any?> { invocation: InvocationOnMock ->
            messageHandlers.putHandler(
                    invocation.arguments[0] as Class<Message>,
                    invocation.arguments[1] as MessageHandler<*>)
        }

        val pluginContext = PowerMockito.mock<PluginContext>(PluginContext::class.java, answer)
        val pluginEnvironment = KotlinPluginEnvironment(world)

        pluginEnvironment.setContext(pluginContext)
        pluginEnvironment.load(ArrayList<PluginMetaData>())

        val credentials = PlayerCredentials("test", "test", 1, 1, "0.0.0.0")
        val position = Position(3200, 3200, 0)
        val region = world.regionRepository.fromPosition(position)

        player = PowerMockito.spy(Player(world, credentials, position))
        world.register(player)
        region.addEntity(player)

        PowerMockito.doNothing().`when`(player).send(any())
    }

    fun interactWith(entity: Entity, option: Int = 1) {
        player.position = entity.position.step(1, Direction.NORTH)

        when (entity) {
            is GameObject -> notify(ObjectActionMessage(option, entity.id, entity.position))
            is Npc -> notify(NpcActionMessage(option, entity.index))
            is Player -> notify(PlayerActionMessage(option, entity.index))
        }
    }

    fun spawnNpc(id: Int, position: Position): Npc {
        val definition = NpcDefinition(id)
        val npc = Npc(world, position, definition, arrayOfNulls(4))
        val region = world.regionRepository.fromPosition(position)
        val npcs = world.npcRepository

        npcs.add(npc)
        region.addEntity(npc)

        return npc
    }

    fun spawnObject(id: Int, position: Position): GameObject {
        val obj = StaticGameObject(world, id, position, 0, 0)

        world.spawn(obj)

        return obj
    }

    fun notify(message: Message) {
        messageHandlers.notify(player, message)
    }

    fun waitForActionCompletion(predicate: (Action<Player>) -> Boolean = { _ -> true }, timeout: Int = 15) {
        val actionCaptor: ArgumentCaptor<Action<*>> = ArgumentCaptor.forClass(Action::class.java)
        Mockito.verify(player).startAction(actionCaptor.capture())

        val action: Action<Player> = actionCaptor.value as Action<Player>
        Assert.assertTrue("Found wrong action type", predicate.invoke(action))

        var pulses = 0

        do {
            action.pulse()
        } while (action.isRunning && pulses++ < timeout)

        Assert.assertFalse("Exceeded timeout waiting for action completion", pulses > timeout)
    }

}
