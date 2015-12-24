SCIMITAR_WIDGET_ID             = 81
SCIMITAR_SPECIAL_BAR_CONFIG_ID = 21
SCIMITAR_SPECIAL_BAR_BUTTON_ID = 21

create_weapon_class :scimitar, widget: SCIMITAR_WIDGET_ID do
  default_speed 4
  special_bar SCIMITAR_SPECIAL_BAR_CONFIG_ID, SCIMITAR_SPECIAL_BAR_BUTTON_ID

  attack_bonuses crush: -2
  defence_bonuses slash: -1

  add_style :accurate, attack_type: :slash, animation: 390, button: 2
  add_style :aggressive, attack_type: :slash, animation: 390, button: 3
  add_style :alt_aggressive, attack_type: :stab, animation: 391, button: 4
  add_style :defensive, attack_type: :slash, animation: 390, button: 5
end

create_weapon :iron_scimitar do
  attack_bonuses stab: 2, slash: 10
  other_bonuses melee_strength: 9
end

create_weapon :steel_scimitar do
  attack_bonuses stab: 3, slash: 15
  other_bonuses melee_strength: 14
end

create_weapon /(black|white) scimitar/ do
  attack_bonuses stab: 4, slash: 19
  other_bonuses melee_strength: 14
end

create_weapon :mithril_scimitar do
  attack_bonuses stab: 5, slash: 21
  other_bonuses melee_strength: 20
end

create_weapon :adamant_scimitar do
  attack_bonuses stab: 6, slash: 29
  other_bonuses melee_strength: 28
end

create_weapon :rune_scimitar do
  attack_bonuses stab: 7, slash: 45
  other_bonuses melee_strength: 44
end

create_weapon :dragon_scimitar do
  attack_bonuses :stab => 8, slash: 67
  other_bonuses melee_strength: 66
end
