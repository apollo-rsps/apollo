create_recipe :arrow_shaft do
  requires :skill, id: Skill::FLETCHING, level: 1
  requires :tool, name: :knife_946
  requires :material, name: :logs_1511

  rewards :product, name: :arrow_shaft, amount: 15
  
  set_action_type :selectable
end

create_recipe :headles_arrows do
  requires :skill, id: Skill::FLETCHING, level: 1
  requires :material, name: :arrow_shaft, amount: 15
  requires :material, name: :feather, amount: 15

  rewards :product, name: :headless_arrow, amount: 15
  
  set_action_type :selectable
end

create_recipe :bronze_arrow do
  requires :skill, id: Skill::FLETCHING, level: 1
  requires :material, name: :headless_arrow, amount: 15
  requires :material, name: :bronze_arrowtips, amount: 15

  rewards :product, name: :bronze_arrow, amount: 15
  
  set_action_type :selectable
end

on :command, :fletch, RIGHTS_ADMIN do |player, command|
  player.inventory.add(946, 1)
  player.inventory.add(1511, 10)
  player.inventory.add(314, 10000)
  player.inventory.add(39, 10000)
end
