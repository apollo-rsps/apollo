BOW_WIDGET_ID         = 77
BOW_SPECIAL_CONFIG_ID = 10
BOW_SPECIAL_BUTTON_ID = 8

create_weapon_class :shortbow, widget: BOW_WIDGET_ID, type: :ranged do
  defaults animation: 426
  special_bar BOW_SPECIAL_CONFIG_ID, BOW_SPECIAL_BUTTON_ID

  style :accurate, speed: 4, range: 7, button: 2
  style :rapid, speed: 3, range: 7, button: 3
  style :long_range, speed: 4, range: 9, button: 4
end

create_weapon :shortbow

create_weapon_class :longbow, widget: BOW_WIDGET_ID, type: :ranged do
  defaults animation: 426
  special_bar BOW_SPECIAL_CONFIG_ID, BOW_SPECIAL_BUTTON_ID

  style :accurate, speed: 6, range: 7, button: 2
  style :rapid, speed: 6, range: 7, button: 3
  style :long_range, speed: 6, range: 9, button: 4
end

create_weapon :longbow
