TWO_HANDED_SWORD_WIDGET_ID  = 82
TWO_HANDED_SWORD_SPECIAL_ID = 12

create_weapon_class :two_handed_sword, widget: TWO_HANDED_SWORD_WIDGET_ID do
  default_speed 7
  special_bar TWO_HANDED_SWORD_SPECIAL_ID
  
  animations stand: 7047, walk: 7046, run: 7039, idle_turn: 7044, turn_around: 7044, turn_left: 7043, turn_right: 7044
               
  attack_bonuses stab: -4, magic: -4
  defence_bonuses range: -1

  add_style :accurate, attack_type: :slash, animation: 7041
  add_style :aggressive, attack_type: :crush, animation: 7041
  add_style :alt_aggressive, attack_type: :crush, animation: 7048
  add_style :defensive, attack_type: :slash, animation: 7049
end

create_weapon :iron_2h_sword do
  attack_bonuses slash: 13, crush: 10
  other_bonuses melee_strength: 14
end

create_weapon :steel_2h_sword do
  attack_bonuses slash: 21, crush: 16
  other_bonuses melee_strength: 22
end

create_weapon /(?:black|white) 2h sword/ do
  attack_bonuses slash: 27, crush: 21
  other_bonuses melee_strength: 26
end

create_weapon :mithril_2h_sword do
  attack_bonuses slash: 30, crush: 24
  other_bonuses melee_strength: 26
end

create_weapon :adamant_2h_sword do
  attack_bonuses slash: 43, crush: 30
  other_bonuses melee_strength: 31
end

create_weapon :rune_2h_sword do
  attack_bonuses slash: 69, crush: 50
  other_bonuses melee_strength: 70
end

create_weapon :dragon_2h_sword do
  attack_bonuses slash: 92, crush: 80
  other_bonuses melee_strength: 70
end