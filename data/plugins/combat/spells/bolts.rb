create_combat_spell :wind_bolt do

  spellbook :modern, button: 10
  max_damage 8

  effects animation: 1162, graphic: { id: 117, height: 100 }, hit_graphic: { id: 119, delay: 100 }
  projectile id: 118, type: :bolt_spells

  requirements do
    rune :air, amount: 3
    rune :chaos, amount: 1

    skill :magic, level: 17
  end

end

create_projectile_type :bolt_spells, start_height: 46, end_height: 36, delay: 51, speed: 12, slope: 15, radius: 86
