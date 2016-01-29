CROSSBOW_WIDGET_ID = 79

create_weapon_class :crossbow, widget: CROSSBOW_WIDGET_ID, type: :ranged do
  defaults animation: 426, speed: 6, range: 7

  style :accurate, range: 7, button: 2
  style :rapid, speed: 5, button: 3
  style :long_range, range: 9, button: 4
end

create_weapon(/rune c'bow/, :crossbow)
