package org.apollo.game.plugin.testing

import io.mockk.MockKVerificationScope
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import org.apollo.cache.def.NpcDefinition
import org.apollo.game.action.Action
import org.apollo.game.message.handler.MessageHandlerChainSet
import org.apollo.game.message.impl.ItemOptionMessage
import org.apollo.game.message.impl.NpcActionMessage
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.message.impl.PlayerActionMessage
import org.apollo.game.model.Direction
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Entity
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.model.entity.obj.StaticGameObject
import org.apollo.net.message.Message
import org.apollo.util.security.PlayerCredentials
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

sealed class DelayMode {
    class AfterTicks(val ticks: Int) : DelayMode()
    object AfterAction : DelayMode()
}

data class DelayedVerifier(val mode: DelayMode, val verifier: () -> Unit)

fun ticks(num: Int): DelayMode {
    return DelayMode.AfterTicks(num)
}

fun actionCompleted(): DelayMode {
    return DelayMode.AfterAction
}

/**
 * A base class containing a set of helper methods to be used within plugin tests.
 */
abstract class KotlinPluginTestHelpers {
    abstract var world: World
    abstract var player: Player
    abstract var messageHandlers: MessageHandlerChainSet

    val delayedVerifiers = mutableListOf<DelayedVerifier>()

    /**
     * Waits for an [Action] to complete within a specified number of pulses, and with an optional predicate
     * to test the [Action] against.
     */
    fun Player.waitForActionCompletion(predicate: (Action<Player>) -> Boolean = { _ -> true }, timeout: Int = 15) {
        val action = player.action as Action<Player>

        assertTrue(predicate.invoke(action), "Found wrong action type")

        var pulses = 0

        do {
            action.pulse()

            val verifiers = delayedVerifiers.filter {
                when (it.mode) {
                    is DelayMode.AfterTicks -> pulses == it.mode.ticks - 1
                    else -> false
                }
            }

            verifiers.forEach { it.verifier.invoke() }
        } while (action.isRunning && pulses++ < timeout)

        val verifiers = delayedVerifiers.filter { it.mode == DelayMode.AfterAction }
        verifiers.forEach { it.verifier.invoke() }

        assertFalse(pulses > timeout, "Exceeded timeout waiting for action completion")
    }

    /**
     * Verify some expectations on a [mock] after a delayed event (specified by [DelayMode]).
     */
    fun verifyAfter(mode: DelayMode, verifier: MockKVerificationScope.() -> Unit) {
        after(mode) { verify(verifyBlock = verifier) }
    }

    /**
     * Run a [callback] after a given delay, specified by [DelayMode].
     */
    fun after(mode: DelayMode, callback: () -> Unit) {
        delayedVerifiers.add(DelayedVerifier(mode, callback))
    }

    /**
     * Send an [ItemOptionMessage] for the given [id], [option], [slot], and [interfaceId], simulating a
     * player interacting with an item.
     */
    fun Player.interactWithItem(id: Int, option: Int, slot: Int? = null, interfaceId: Int? = null) {
        this.notify(ItemOptionMessage(option, interfaceId ?: -1, id, slot ?: player.inventory.slotOf(id)))
    }

    /**
     * Spawn a new object (defaulting to in-front of the player) and immediately interact with it.
     */
    fun Player.interactWithObject(id: Int, option: Int, at: Position? = null) {
        val obj = world.spawnObject(id, at ?: position.step(1, Direction.NORTH))
        interactWith(obj, option)
    }

    /**
     * Spawns a new NPC with the minimum set of dependencies required to function correctly in the world.
     */
    fun World.spawnNpc(id: Int, position: Position): Npc {
        val definition = NpcDefinition(id)
        val npc = Npc(this, position, definition, arrayOfNulls(4))
        val region = regionRepository.fromPosition(position)
        val npcs = npcRepository

        npcs.add(npc)
        region.addEntity(npc)

        return npc
    }

    /**
     * Spawn a new player stub in the world, with a dummy game session.
     */
    fun World.spawnPlayer(username: String, position: Position = Position(3200, 3200, 0)): Player {
        val credentials = PlayerCredentials(username, "test", 1, 1, "0.0.0.0")
        val region = regionRepository.fromPosition(position)

        val player = spyk(Player(this, credentials, position))
        register(player)
        region.addEntity(player)

        every { player.send(any()) } answers { }

        return player
    }

    /**
     * Spawn a new static game object into the world with the given id and position.
     */
    fun World.spawnObject(id: Int, position: Position): GameObject {
        val obj = StaticGameObject(this, id, position, 0, 0)

        spawn(obj)

        return obj
    }

    /**
     * Fake a client [Message] originating from a player and send it to the relevant
     * message handlers.
     */
    fun Player.notify(message: Message) {
        messageHandlers.notify(this, message)
    }

    /**
     * Move the player within interaction distance to the given [Entity] and fake an action
     * message.
     */
    fun Player.interactWith(entity: Entity, option: Int = 1) {
        position = entity.position.step(1, Direction.NORTH)

        when (entity) {
            is GameObject -> notify(ObjectActionMessage(option, entity.id, entity.position))
            is Npc -> notify(NpcActionMessage(option, entity.index))
            is Player -> notify(PlayerActionMessage(option, entity.index))
        }
    }

}

