package org.apollo.game.plugin.testing

import org.apollo.cache.def.ItemDefinition
import org.apollo.cache.def.NpcDefinition
import org.apollo.game.action.Action
import org.apollo.game.message.handler.MessageHandlerChainSet
import org.apollo.game.message.impl.*
import org.apollo.game.model.*
import org.apollo.game.model.entity.*
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.model.entity.obj.StaticGameObject
import org.apollo.net.message.Message
import org.apollo.util.security.PlayerCredentials
import org.junit.Assert
import org.mockito.*
import org.powermock.api.mockito.PowerMockito

/**
 * A base class containing a set of helper methods to be used within plugin tests.
 */
abstract class KotlinPluginTestHelpers {
    abstract var world: World
    abstract var player: Player
    abstract var messageHandlers: MessageHandlerChainSet

    /**
     * Waits for an [Action] to complete within a specified number of pulses, and with an optional predicate
     * to test the [Action] against.
     */
    fun Player.waitForActionCompletion(predicate: (Action<Player>) -> Boolean = { _ -> true }, timeout: Int = 15) {
        val actionCaptor: ArgumentCaptor<Action<*>> = ArgumentCaptor.forClass(Action::class.java)
        Mockito.verify(this).startAction(actionCaptor.capture())

        val action: Action<Player> = actionCaptor.value as Action<Player>
        Assert.assertTrue("Found wrong action type", predicate.invoke(action))

        var pulses = 0

        do {
            action.pulse()

            /**
             * Introducing an artificial delay is necessary to prevent the timeout being exceeded before
             * an asynchronous [Action] really starts.  When a job is submitted to a new coroutine context
             * there may be a delay before it is actually executed.
             *
             * This delay is typically sub-millisecond and is only incurred with startup.  Since game actions
             * have larger delays of their own this isn't a problem in practice.
             */
            Thread.sleep(50L)
        } while (action.isRunning && pulses++ < timeout)

        Assert.assertFalse("Exceeded timeout waiting for action completion", pulses > timeout)
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

        val player = PowerMockito.spy(Player(this, credentials, position))
        register(player)
        region.addEntity(player)

        PowerMockito.doNothing().`when`(player).send(Matchers.any())

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

