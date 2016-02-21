java_import 'org.apollo.game.action.Action'

class RecipieAction < Action
  attr_reader :recipe, :amount, :started, :pulses, :action, :listener, :primary, :secondary
  def initialize(player, primary, secondary, recipe)
    super(1, true, player)

    @amount = 1
    @primary = primary
    @secondary = secondary
    @recipe = recipe
    @started = false
    @pulses = 0
  end

  def execute
    if @pulses == 0
      unless @started
        unless recipe.check_skill_requirements(mob)
          stop
          return
        end

        @started = true
      end

      unless recipe.check_material_requirements(mob)
        stop
        return
      end

      recipe.display_attempt_message(mob, primary, secondary)
    end

    mob.play_animation(recipe.animation) unless recipe.animation.nil?

    chance = rand(101) # 0-100

    recipe.remove_materials(mob)
    
    if chance <= recipe.fail_chance.call(mob, recipe.skill_requirements, primary, secondary)
      recipe.display_failure_message(mob, primary, secondary)
    else
      recipe.display_success_message(mob, primary, secondary)
      recipe.assign_rewards(mob)
    end

    @amount -= 1
    @amount > 0 ? @pulses = 0 : stop
  end

  def calculate_maximum_actions
    inventory = mob.inventory

    items = recipe.material_requirements.clone
    items.merge!(recipe.main_material.id => recipe.main_material.amount) unless recipe.main_material.nil?

    items.map do |id, amount|
      inventory.get_amount(id) / amount
    end.min
  end

  def set_maximum_actions
    @amount = calculate_maximum_actions
  end

  def stop
    super()
    mob.inventory.remove_listener(@listener) unless @listener.nil?
  end

  # Sets the amount of actions.
  def set_amount(amount)
    @amount = amount
  end

  def equals(other)
    get_class == other.get_class && @recipe == other.recipe
  end
end