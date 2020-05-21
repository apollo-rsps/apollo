package org.apollo.game.plugin.testing.junit

import io.mockk.every
import io.mockk.slot
import io.mockk.spyk
import kotlin.reflect.KClass
import org.apollo.game.action.Action
import org.apollo.game.message.handler.MessageHandlerChainSet
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.mocking.StubPrototype
import org.apollo.game.plugin.testing.junit.stubs.PlayerStubInfo
import org.apollo.net.message.Message
import org.apollo.util.security.PlayerCredentials

data class ApolloTestState(val handlers: MessageHandlerChainSet, val world: World) {
    val players = mutableListOf<Player>()
    var actionCapture: ActionCapture? = null

    fun createActionCapture(type: KClass<out Action<*>>): ActionCapture {
        if (actionCapture != null) {
            throw IllegalStateException("Cannot specify more than one ActionCapture")
        }

        actionCapture = ActionCapture(type)
        return actionCapture!!
    }

    fun <T : Any> createStub(proto: StubPrototype<T>): T {
        val annotations = proto.annotations

        return when (proto.type) {
            Player::class -> createPlayer(PlayerStubInfo.create(annotations)) as T
            World::class -> world as T
            ActionCapture::class -> createActionCapture(Action::class) as T
            else -> throw IllegalArgumentException("Can't stub ${proto.type.qualifiedName}")
        }
    }

    fun createPlayer(info: PlayerStubInfo): Player {
        val credentials = PlayerCredentials(info.name, "test", 1, 1, "0.0.0.0")
        val region = world.regionRepository.fromPosition(info.position)

        val player = spyk(Player(world, credentials, info.position))

        world.register(player)
        region.addEntity(player)
        players.add(player)

        val actionSlot = slot<Action<*>>()
        val messageSlot = slot<Message>()

        every { player.send(capture(messageSlot)) } answers { handlers.notify(player, messageSlot.captured) }
        every { player.startAction(capture(actionSlot)) } answers {
            actionCapture?.capture(actionSlot.captured)
            true
        }

        return player
    }

    fun reset() {
        actionCapture = null
        players.forEach {
            it.stopAction()
            world.unregister(it)
        }

        players.clear()
    }
}