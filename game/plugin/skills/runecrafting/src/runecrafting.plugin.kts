import org.apollo.game.message.impl.*
import org.apollo.game.model.entity.EquipmentConstants
import org.apollo.game.model.event.impl.LoginEvent


on_player_event { LoginEvent::class }
        .then {
            val configValue = player.equipment.get(EquipmentConstants.HAT)?.let { Tiara.findById(it.id)?.let { 1 shl it.configId } ?: 0 } ?: 0
            player.send(ConfigMessage(CHANGE_ALTAR_OBJECT_CONFIG, configValue))
        }

on { ObjectActionMessage::class }
        .where { option == 2 }
        .then {
            val tiara = Tiara.findByAltarId(id)
            //Check if player has taria
            val hat = it.equipment.get(EquipmentConstants.HAT)
            if (tiara != null && hat != null && hat.id == tiara.id) {
                //Check if correct altar
                if (tiara.altar.entranceId == id) {
                    it.startAction(TeleportAction(it, position, 2, tiara.altar.entrance))
                    terminate()
                }
            }
        }

on { ItemActionMessage::class }
        .where { option == 1 }
        .then {
            val tiara = Tiara.findById(id)
            if (tiara != null) {
                it.send(ConfigMessage(CHANGE_ALTAR_OBJECT_CONFIG, 0))
                terminate()
            }
        }

on { ItemOnObjectMessage::class }
        .then {
            val tiara = Tiara.findByTalismanId(id)
            val altar = Altar.findByCraftingId(objectId)
            if (tiara != null && altar != null) {
                it.startAction(CreateTiaraAction(it, position, tiara, altar))
                terminate()
            }
        }

on { ItemOptionMessage::class }
        .where { option == 4 }
        .then {
            val talisman = Talisman.findById(id)
            if (talisman != null) {
                talisman.sendProximityMessageTo(it)
                terminate()
            }
        }

on { ItemOnObjectMessage::class }
        .then {
            val talisman = Talisman.findById(id)
            val altar = Altar.findByEntranceId(objectId)
            if (altar != null && talisman != null) {
                it.startAction(TeleportAction(it, position, 2, altar.entrance))
                terminate()
            }
        }

on { ObjectActionMessage::class }
        .where { option == 1 }
        .then {
            val altar = Altar.findByPortalId(id)
            if (altar != null) {
                it.startAction(TeleportAction(it, altar.entrance, 1, altar.exit))
                terminate()
            } else {
                val rune = Rune.findByAltarId(id)
                val craftingAltar = Altar.findByCraftingId(id)
                if (rune != null && craftingAltar != null) {
                    it.startAction(RunecraftingAction(it, rune, craftingAltar))
                    terminate()
                }
            }
        }