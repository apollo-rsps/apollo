package org.apollo.game.plugins.testing

import org.apollo.cache.def.NpcDefinition
import org.apollo.game.action.Action
import org.apollo.game.message.handler.MessageHandlerChainSet
import org.apollo.game.message.impl.*
import org.apollo.game.model.*
import org.apollo.game.model.entity.*
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.model.entity.obj.StaticGameObject
import org.apollo.net.message.Message
import org.junit.Assert
import org.mockito.ArgumentCaptor
import org.mockito.Mockito


class KotlinPluginTestContext(val world: World, val activePlayer: Player, val messageHandlers: MessageHandlerChainSet) {

    fun interactWith(entity: Entity, option: Int = 1) {
        activePlayer.position = entity.position.step(1, Direction.NORTH)

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

        world.register(npc)
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
        messageHandlers.notify(activePlayer, message)
    }

    fun shutdown() {

    }

    fun waitForActionCompletion(predicate: (Action<Player>) -> Boolean = { _ -> true }, timeout: Int = 15) {
        val actionCaptor: ArgumentCaptor<Action<*>> = ArgumentCaptor.forClass(Action::class.java)
        Mockito.verify(activePlayer).startAction(actionCaptor.capture())

        val action: Action<Player> = actionCaptor.value as Action<Player>
        Assert.assertTrue("Found wrong action type", predicate.invoke(action))

        var pulses = 0

        do {
            action.pulse()
        } while (action.isRunning && pulses++ < timeout)

        Assert.assertFalse("Exceeded timeout waiting for action completion", pulses > timeout)
    }

}
