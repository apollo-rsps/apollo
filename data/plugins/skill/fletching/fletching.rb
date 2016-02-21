create_recipe :arrow_shaft, 15 do
  requires :skill, id: Skill::FLETCHING, level: 1
  requires :tool, name: :knife_946
  requires :material, name: :logs_1511
  
  set_action_type :selectable
end

create_recipe :headless_arrow, 15 do
  requires :skill, id: Skill::FLETCHING, level: 1
  requires :material, name: :arrow_shaft, amount: 15
  requires :material, name: :feather, amount: 15
  
  set_action_type :selectable
end

create_recipe :bronze_arrow, 15 do
  requires :skill, id: Skill::FLETCHING, level: 1
  requires :material, name: :headless_arrow, amount: 15
  requires :material, name: :bronze_arrowtips, amount: 15
  
  set_action_type :selectable
end

on :command, :fletch, RIGHTS_ADMIN do |player, command|
  player.inventory.add(946, 1)
  player.inventory.add(1511, 10)
  player.inventory.add(314, 10000)
  player.inventory.add(39, 10000)
end
