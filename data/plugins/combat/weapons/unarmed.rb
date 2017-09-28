create_weapon_class :no_weapon, widget: 92 do
  defaults speed: 4, animation: 422

  style :accurate, button: 2
  style :aggressive, animation: 423, button: 3
  style :defensive, button: 4
end

create_weapon :unarmed, :no_weapon, named: true do
  # Todo factor out empty blocks
end
