FIRST_OPTION = 1

create_single_item_recipe FIRST_OPTION, :guam_leaf_249 do
  requires :main_material, name: :guam_leaf_199
  requires :skill, id: Skill::HERBLORE, level: 1
  set_action_type :selectable
  register_to_skill_menu Skill::HERBLORE, "Herb"
end

on :command, :herb, RIGHTS_ADMIN do |player, command|
  player.inventory.add(249, 1)
  player.inventory.add(227, 1)
  player.inventory.add(221, 1)
end