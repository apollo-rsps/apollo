package org.apollo.game.plugin.skill.runecrafting

import org.apollo.game.message.impl.*
import org.apollo.game.model.entity.EquipmentConstants
import org.apollo.game.model.event.impl.LoginEvent

private val changeAltarObjectConfigId = 491

internal val RUNES = mutableListOf<Rune>()

fun List<Rune>.findById(id: Int): Rune? {
    return find { rune -> rune.id == id }
}

start {
    RUNES.addAll(DefaultRune.values())
}

on_player_event { LoginEvent::class }
    .then {
        val equippedHat = player.equipment.get(EquipmentConstants.HAT)
        val equippedTiaraConfig = equippedHat?.let { item -> Tiara.findById(item.id)?.configId } ?: 0
        val configValue = 1 shl equippedTiaraConfig

        player.send(ConfigMessage(changeAltarObjectConfigId, configValue))
    }

on { ObjectActionMessage::class }
    .where { option == 2 }
    .then {
        val tiara = Tiara.findByAltarId(id) ?: return@then
        val hat = it.equipment.get(EquipmentConstants.HAT) ?: return@then

        if (hat.id == tiara.id && tiara.altar.entranceId == id) {
            it.startAction(TeleportToAltarAction(it, position, 2, tiara.altar.entrance))
            terminate()
        }
    }

on { ItemActionMessage::class }
    .where { option == 1 }
    .then { player ->
        Tiara.findById(id)?.let {
            player.send(ConfigMessage(changeAltarObjectConfigId, 0))
            terminate()
        }
    }

on { ItemOnObjectMessage::class }
    .then {
        val tiara = Tiara.findByTalismanId(id) ?: return@then
        val altar = Altar.findByCraftingId(objectId) ?: return@then

        it.startAction(CreateTiaraAction(it, position, tiara, altar))
        terminate()
    }

on { ItemOptionMessage::class }
    .where { option == 4 }
    .then {
        val talisman = Talisman.findById(id) ?: return@then

        talisman.sendProximityMessageTo(it)
        terminate()
    }

on { ItemOnObjectMessage::class }
    .then {
        val talisman = Talisman.findById(id) ?: return@then
        val altar = Altar.findByEntranceId(objectId) ?: return@then

        it.startAction(TeleportToAltarAction(it, position, 2, altar.entrance))
        terminate()
    }

on { ObjectActionMessage::class }
    .where { option == 1 }
    .then {
        val altar = Altar.findByPortalId(id) ?: return@then

        it.startAction(TeleportToAltarAction(it, altar.entrance, 1, altar.exit))
        terminate()
    }

on { ObjectActionMessage::class }
    .where { option == 1 }
    .then {
        val rune = RUNES.findById(id) ?: return@then
        val craftingAltar = Altar.findByCraftingId(id) ?: return@then

        it.startAction(RunecraftingAction(it, rune, craftingAltar))
        terminate()
    }