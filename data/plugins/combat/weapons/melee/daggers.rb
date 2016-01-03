DAGGER_WIDGET_ID         = 89
DAGGER_SPECIAL_CONFIG_ID = 12
DAGGER_SPECIAL_BUTTON_ID = 10

create_weapon_class :dagger, widget: DAGGER_WIDGET_ID do
  defaults speed: 4, animation: 7041, attack_type: :stab

  attack_bonuses crush: -4, magic: 1
  defence_bonuses magic: 1

  style :accurate, button: 2
  style :aggressive, button: 3
  style :alt_aggressive, attack_type: :slash, animation: 7048, button: 4
  style :defensive, animation: 7049, button: 5
end

# Need a separate WeaponClass for the dragon dagger because
# of the differing animations
create_weapon_class :dragon_dagger, widget: DAGGER_WIDGET_ID do
  defaults speed: 4, animation: 402, attack_type: :stab
  special_bar DAGGER_SPECIAL_CONFIG_ID, DAGGER_SPECIAL_BUTTON_ID

  attack_bonuses crush: -4, magic: 1
  defence_bonuses magic: 1

  style :accurate, button: 2
  style :aggressive, button: 3
  style :alt_aggressive, attack_type: :slash, button: 4
  style :defensive, button: 5
end

create_weapon /(?:drag|dragon) dagger.*/, :dragon_dagger do
  set_special_attack speed: 4, energy_requirement: 25, animation: 1062, graphic: { id: 252, height: 100 } do
    damage! delay: 0
    damage! delay: 1
  end
end
