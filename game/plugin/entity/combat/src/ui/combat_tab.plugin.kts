
import org.apollo.game.message.impl.*
import org.apollo.game.model.entity.EquipmentConstants
import org.apollo.game.model.entity.Player
import org.apollo.game.model.event.impl.LoginEvent
import org.apollo.game.model.inv.SynchronizationInventoryListener

on_player_event { LoginEvent::class }.then { updateCombatTab(player) }

on { ItemOptionMessage::class }
    .where { interfaceId == SynchronizationInventoryListener.INVENTORY_ID && option == 2 }
    .then { updateCombatTab(it) }

on { ItemActionMessage::class }
    .where { interfaceId == SynchronizationInventoryListener.EQUIPMENT_ID && slot == EquipmentConstants.WEAPON }
    .then { updateCombatTab(it) }

on { ButtonMessage::class }
    .then {
        val weapon = it.weapon
        val weaponClass = weapon.weaponClass
        val newCombatStyle = weaponClass.styles.firstOrNull { it.button == widgetId } ?: return@then

        it.combatStyle = weaponClass.styles.indexOf(newCombatStyle)
        it.combatState.attack = newCombatStyle.attack
    }

fun updateCombatTab(player: Player) {
    val weaponItem = player.equipment[EquipmentConstants.WEAPON]
    val weaponName = weaponItem?.definition?.name ?: "Unarmed"
    val weapon = Weapons[weaponItem?.id]
    val weaponClass = weapon.weaponClass
    var widget = weaponClass.widget

    player.send(SwitchTabInterfaceMessage(0, widget))

    if (weaponItem != null) {
        player.send(SetWidgetItemModelMessage(++widget, weaponItem.id, 200))
    }
/*
    if weapon_class.special_bar?
    player.send SetWidgetVisibilityMessage.new(weapon_class.special_bar_config, weapon.special_attack?)
    end
*/
    player.send(SetWidgetTextMessage(widget + 2, weaponName))
    player.send(ConfigMessage(43, player.combatStyle)) //@todo - combat style offset
}
