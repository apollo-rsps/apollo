DAGGER_WIDGET_ID         = 89
DAGGER_SPECIAL_CONFIG_ID = 12
DAGGER_SPECIAL_BUTTON_ID = 10

create_weapon_class :dagger, widget: DAGGER_WIDGET_ID do
  default_speed 4

  attack_bonuses crush: -4, magic: 1
  defence_bonuses magic: 1

  add_style :accurate, attack_type: :stab, animation: 7041, button: 2
  add_style :aggressive, attack_type: :stab, animation: 7041, button: 3
  add_style :alt_aggressive, attack_type: :slash, animation: 7048, button: 4
  add_style :defensive, attack_type: :stab, animation: 7049, button: 5
end

# Need a separate WeaponClass for the dragon dagger because
# of the differing animations
create_weapon_class :dragon_dagger, widget: DAGGER_WIDGET_ID do
  default_speed 4
  special_bar DAGGER_SPECIAL_CONFIG_ID, DAGGER_SPECIAL_BUTTON_ID

  attack_bonuses crush: -4, magic: 1
  defence_bonuses magic: 1

  add_style :accurate, attack_type: :stab, animation: 402, button: 2
  add_style :aggressive, attack_type: :stab, animation: 402, button: 3
  add_style :alt_aggressive, attack_type: :slash, animation: 402, button: 4
  add_style :defensive, attack_type: :stab, animation: 402, button: 5
end

create_weapon /(?:drag|dragon) dagger.*/, :dragon_dagger do
  set_special_attack energy_requirement: 25, animation: 1062, graphic: {id: 252, height: 100} do |source, target|
    damage! source, target, CombatUtil::calculate_hit(source, target)
    damage! source, target, CombatUtil::calculate_hit(source, target), 1
  end
end