create_weapon_class :no_weapon, widget: 92 do
  default_speed 4

  add_style :accurate, animation: 422, block_animation: 424
  add_style :aggressive, animation: 423, block_animation: 424
  add_style :defensive, animation: 422, block_animation: 424
end

create_weapon :unarmed, :no_weapon, named: true do
  # Todo factor out empty blocks
end