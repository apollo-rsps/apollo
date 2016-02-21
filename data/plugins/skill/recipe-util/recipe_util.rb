ITEM_RECIPES = {}
OBJECT_RECIPES = {}
SINGLE_ITEM_RECIPES = {}

# Creates a recipe for ITEM ON ITEM and ITEM ON OBJECT
def create_recipe(name, &block)
  recipe = Recipe.new &block

  requires_object = recipe.requires_object

  materieal_ids = recipe.material_requirements.keys
  materieal_ids.push(recipe.tool_requirement) unless recipe.tool_requirement.nil?

  list = requires_object ? recipe.object_requirements : materieal_ids

  hashes = []

  list.each do |key_one|
    materieal_ids.each do |key_two|

      unless key_one >= key_two
        small = [key_one, key_two].min
        big = [key_one, key_two].max

        hash = (big << 16) | small

        if requires_object
          OBJECT_RECIPES[hash] = recipe
        else
          ITEM_RECIPES[hash] = recipe
        end
      end
    end
  end

  recipe
end

# Creates a recipe for single click items. EX herbs
def create_single_item_recipe(name, option, &block)
  recipe = Recipe.new &block

  SINGLE_ITEM_RECIPES[(option << 16) | recipe.main_material.id] = recipe
end

# types: item, object, click
# item keys: primary, secondary
# object keys: object, item
# click keys: item, option
def get_recipe(type, key_one, key_two)
  if type == :click
    SINGLE_ITEM_RECIPES[(key_two << 16) | key_one]
  else
    small = [key_one, key_two].min
    big = [key_one, key_two].max

    hash = (big << 16) | small

    type == :object ? OBJECT_RECIPES[hash] : ITEM_RECIPES[hash]
  end
end

# types: item, object, click
# item keys: primary, secondary
# object keys: object, item
# click keys: item, option
def make_recipe(player, type, key_one, key_two)
  recipe = get_recipe(type, key_one, key_two)

  unless recipe.nil?
    action = RecipieAction.new(player, key_one, key_two, recipe)

    if recipe.action_type == :single
      player.start_action(action) unless action.nil?
    elsif recipe.action_type == :repeatable
      action.set_maximum_actions
      player.start_action(action) unless action.nil?
    elsif recipe.action_type == :selectable
      player.send(SetWidgetItemModelMessage.new(1746, recipe.product_reward.id, 170)) unless recipe.product_reward.nil?
      player.interface_set.open_dialogue(SelectAmountDialogueListener.new(player, action), 4429)
    else
      #No such action_type
    end
  end
end