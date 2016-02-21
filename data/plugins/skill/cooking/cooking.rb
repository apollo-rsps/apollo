java_import 'org.apollo.game.model.entity.Skill'

# Couple of Example usages
# Combines multiple items into one product
create_recipe :cake_recipe_combine do
  set_action_type :repeatable
  
  requires :skill, id: Skill::COOKING, level: 40

  requires :material, name: :pot_of_flour
  requires :material, name: :egg
  requires :material, name: :bucket_of_milk
  requires :material, name: :cake_tin

  rewards :product, name: :uncooked_cake

  set_fail_chance lambda { | player, skill_requirements, primary, secondary | # primary and secondary refer to object/item/option keys
     60
  }

  message :success, lambda { |player, primary, secondary|
    "You did it!"
  }

  message :attempt, lambda { |player, primary, secondary|
    "You attempt it!"
  }

  message :failure, lambda { |player, primary, secondary|
    "You failed!"
  }

  message :skill_req, lambda { |player, product_reward, skill_id, level |
    "You need skills!"
  }

  message :item_req, lambda { |player, product_reward, tool_requirement, material_requirements |
    "You need items!"
  }

  # Register has to come after product and skill requirements
  register_to_skill_menu Skill::COOKING, "Pastry"
end

# Recipe for cooking a cake on a stove.
# If it has an object requirement it's automatically a item on object packet.
create_recipe :cake_recipe_cook do
  set_fail_chance 50

  requires :skill, id: Skill::COOKING, level: 40

  requires :material, name: :uncooked_cake
  requires :object, name: "Range"

  message :attempt, "You attempt to make the item."
  message :success, "Yay You did it."
  message :failure, "Fuck you burnt it."

  rewards :product, name: :cake
  rewards :experience, id: Skill::COOKING, amount: 180

  set_action_type :repeatable
end

on :command, :cake, RIGHTS_ADMIN do |player, command|
  player.inventory.add(1933, 1)
  player.inventory.add(1944, 1)
  player.inventory.add(1927, 1)
  player.inventory.add(1887, 1)
end