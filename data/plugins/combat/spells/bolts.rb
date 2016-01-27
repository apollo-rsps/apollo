create_combat_spell :wind_bolt do
  interface spellbook: :modern, button: 10

  level_requirement 17
  max_damage 8
  runes {}

  effects animation: 1162, graphic: {id: 117, height: 100}, hit_graphic: {id: 119, delay: 100}
  projectile projectile: 118, projectile_type: :bolt_spells
end

create_projectile_type :bolt_spells, start_height: 46, end_height: 36, delay: 51, speed: 12, slope: 15, radius: 86