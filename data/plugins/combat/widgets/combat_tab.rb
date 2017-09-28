java_import 'org.apollo.game.model.inter.InterfaceConstants'
java_import 'org.apollo.game.model.entity.EquipmentConstants'
java_import 'org.apollo.game.message.impl.SetWidgetTextMessage'
java_import 'org.apollo.game.message.impl.SetInterfaceConfigMessage'
java_import 'org.apollo.game.message.impl.ConfigMessage'
java_import 'org.apollo.game.model.inv.SynchronizationInventoryListener'

on :message, :item_option do |player, message|
  update_combat_tab(player) if message.option == 2 && message.interface_id == SynchronizationInventoryListener::INVENTORY_ID
end

on :login do |event|
  update_combat_tab(event.player)
end

on :message, :item_action do |player, message|
  update_combat_tab(player) if message.interface_id == SynchronizationInventoryListener::EQUIPMENT_ID && message.slot == EquipmentConstants::WEAPON
end

on :message, :button do |player, msg|
  weapon              = EquipmentUtil.equipped_weapon player
  weapon_class        = weapon.weapon_class
  weapon_class_widget = weapon_class.widget

  next unless weapon_class_widget == msg.interface_id

  if weapon_class.special_bar? && msg.button == weapon_class.special_bar_button
    player.using_special = !player.using_special
    update_special_bar(player)
  else
    player.combat_style = msg.button
  end

  update_combat_style player
end

private

def update_combat_tab(player)
  weapon              = EquipmentUtil.equipped_weapon player
  weapon_name         = weapon.name
  weapon_class        = weapon.weapon_class
  weapon_class_widget = weapon_class.widget

  attack_style_interface_layer = InterfaceConstants::UserInterface::ATTACK_STYLE.layer

  player.interface_set.send_user_interface(attack_style_interface_layer, weapon_class_widget)
  player.send SetWidgetTextMessage.new(weapon_class_widget, 0, weapon_name)

  if weapon_class.special_bar?
    player.send SetInterfaceConfigMessage.new(weapon_class_widget, weapon_class.special_bar_config, !weapon.special_attack?)
  end

  update_combat_style player
end

def update_combat_style(player)
  weapon              = EquipmentUtil.equipped_weapon player
  combat_style        = weapon.weapon_class.selected_style player

  # Update the combat style in case we had an invalid style selected,
  # and therefore reverted to the first combat style
  player.combat_style = combat_style.button
  player.send ConfigMessage.new(43, combat_style.config)
end
