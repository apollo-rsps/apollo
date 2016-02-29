create_weapon_class :unarmed, widget: -1 do
  default_speed 4

  add_style :accurate, animation: 422, block_animation: 424
  add_style :aggressive, animation: 423, block_animation: 424
  add_style :defensive, animation: 422, block_animation: 424
end

create_weapon :no_weapon, :unarmed, named: true do
  # Todo factor out empty blocks
end