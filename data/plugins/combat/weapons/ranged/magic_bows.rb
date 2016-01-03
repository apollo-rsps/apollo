create_projectile_type :msb, start_height: 41, end_height: 37, delay: 31, speed: 3, slope: 15, radius: 8

create_weapon :magic_shortbow do
  set_special_attack speed: 3, range: 7, energy_requirement: 60, animation: 1074, graphic: { id: 256, height: 100 } do
    range_damage! projectile: 249, projectile_type: PROJECTILE_TYPES[:msb]
    range_damage! projectile: 249, projectile_type: PROJECTILE_TYPES[:msb], projectile_graphic: 256, delay: 1
  end
end
