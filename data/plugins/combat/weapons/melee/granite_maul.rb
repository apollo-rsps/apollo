create_weapon :granite_maul do
  attack_bonuses slash: 92, crush: 80
  other_bonuses melee_strength: 70

  set_special_attack speed: 0, energy_requirement: 60, animation: 3157, graphic: 1225 do |_source, _target|
    damage! delay: 0
    damage! delay: 1
  end
end
