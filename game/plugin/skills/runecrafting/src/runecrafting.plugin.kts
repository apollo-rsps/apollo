
import org.apollo.game.message.impl.*
import org.apollo.game.model.entity.EquipmentConstants
import org.apollo.game.model.event.impl.LoginEvent


on_player_event {LoginEvent::class}
        .then {
            val hat = player.equipment.get(EquipmentConstants.HAT)
            if (hat != null) {
                val tiara = Tiara.findById(hat.id)
                if (tiara != null) {
                    player.send(ConfigMessage(CHANGE_ALTER_OBJECT_CONFIG, 1 shl tiara.bitshift))
                } else {
                    player.send(ConfigMessage(CHANGE_ALTER_OBJECT_CONFIG, 0))
                }
            } else {
                player.send(ConfigMessage(CHANGE_ALTER_OBJECT_CONFIG, 0))
            }
        }

on {ObjectActionMessage::class}
        .where {option == 2}
        .then {
            val taria = Tiara.findByAlterId(id)
            //Check if player has taria
            val hat = it.equipment.get(EquipmentConstants.HAT)
            if (taria != null && hat != null && hat.id == taria.id) {
                //Check if correct alter
                if (taria.alter.entranceId == id) {
                    it.startAction(TeleportAction(it, position, 2, taria.alter.entrance))
                    terminate()
                }
            }
        }

on {ItemActionMessage::class}
        .where { option == 1 }
        .then {
            val taria = Tiara.findById(id)
            if (taria != null) {
                it.send(ConfigMessage(CHANGE_ALTER_OBJECT_CONFIG, 0))
                terminate()
            }
        }

on {ItemOnObjectMessage::class}
        .then {
            val tiara = Tiara.findByTalismanId(id)
            val alter = Alter.findByCraftingId(objectId)
            if (tiara != null && alter != null) {
                it.startAction(CreateTiaraAction(it, position, tiara, alter))
                terminate()
            }
        }

on {ItemOptionMessage::class}
        .where { option == 4 }
        .then {
            val talisman = Talisman.findById(id)
            if (talisman != null) {
                talisman.sendMessage(it)
                terminate()
            }
        }

on {ItemOnObjectMessage::class}
        .then {
            val talisman = Talisman.findById(id)
            val alter = Alter.findByEntranceId(objectId)
            if (alter != null && talisman != null) {
                it.startAction(TeleportAction(it, position, 2, alter.entrance))
                terminate()
            }
        }

on {ObjectActionMessage::class}
        .where { option == 1 }
        .then {
            val alter = Alter.findByPortalId(id)
            if (alter != null) {
                it.startAction(TeleportAction(it, alter.entrance, 1, alter.exit))
                terminate()
            } else {
                val rune = Rune.findByAlterId(id)
                val craftingAlter = Alter.findByCraftingId(id)
                if (rune != null && craftingAlter != null) {
                    it.startAction(RunecraftingAction(it, rune, craftingAlter))
                    terminate()
                }
            }
        }