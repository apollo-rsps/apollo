require 'java'

java_import 'org.apollo.game.model.Item'
java_import 'org.apollo.game.model.entity.Skill'
java_import 'org.apollo.game.message.impl.SetWidgetItemModelMessage'
java_import 'org.apollo.game.model.Animation'

class Recipe

  attr_reader :skill_requirements, :material_requirements, :object_requirements, :main_material, :tool_requirement,
  :experience_rewards, :item_rewards, :product_reward,
  :fail_chance, :messages, :action_type, :animation
  def initialize(&block)
    # Requirements
    @skill_requirements = {}
    @material_requirements = {}
    @object_requirements = []
    @tool_requirement = nil
    @action_type = :single
    @animation = nil

    # Rewards
    @experience_rewards = {}
    @item_rewards = []
    @product_reward = nil
    @main_material = nil

    # Other
    @messages = {}
    @fail_chance = 0
    @skill_menu = nil

    # Evalute the methods inside.
    self.instance_eval(&block)
  end

  def register_to_skill_menu(skill, tab)
    register_item(skill, tab: tab, level: skill_requirements[skill], id: product_reward.id)
  end

  # Set the animation id
  def set_animation(id)
    @animation = Animation.new(id)
  end

  # Sets the kind of action when creating.
  # single
  # repeatable
  # selectable
  def set_action_type(type)
    @action_type = type
  end

  # Add a mesage for a stage of the recipe.
  # success
  # failure
  # attempt
  # skill_req
  # item_req
  def message(name, value)
    @messages[name] = value.is_a?(Proc) ? value : Proc.new { value }
    #puts "#{@messages[name].call(nil, 2, 3)}"
  end

  # Set the chance to fial this recipie
  # percent - Chance to fail making the item.
  def set_fail_chance(value)
    @fail_chance = value.is_a?(Proc) ? value : Proc.new { value }
  end

  # Add a requirement to make the recipie.
  # skill - Skill requirement to make the item.
  # tool - Tool needed to make the product. This does not get removed.
  # material - Materials needed to make the product. These get removed.
  # object - Object needed to make the product.
  def requires(type, hash)

    if type == :skill
      @skill_requirements.merge!( hash[:id] => hash[:level] )
    elsif type == :tool
      id = hash[:name] ? lookup_item(hash[:name]) : hash[:id]
      @tool_requirement = id
    elsif type == :material
      id = hash[:name] ? lookup_item(hash[:name]) : hash[:id]
      amount = hash[:amount] || 1

      @material_requirements.merge!( id => amount )
    elsif type == :object
      id = find_entities(:object, hash[:name], 10) || hash[:id]

      @object_requirements.push(*id)
    elsif type == :main_material
      id = hash[:name] ? lookup_item(hash[:name]) : hash[:id]
      amount = hash[:amount] || 1
      @main_material = Item.new(id, amount)
    end
  end

  # Add a reward for completion of the recipie
  # expereince - Exp for making product.
  # product - The main item produced.
  # item - Additional Items if any produced.
  def rewards(type, hash)

    id = hash[:name] ? lookup_item(hash[:name]) : hash[:id]
    amount = hash[:amount] || 1

    if type == :experience
      @experience_rewards.merge!(id => amount)
    elsif type == :product
      @product_reward = Item.new(id, amount)
    elsif type == :item
      @item_rewards.push( Item.new(id, amount) )
    end
  end

  # Check if the player meets the skill requirements to perform this recipe.
  def check_skill_requirements(player)
    skills = player.skill_set

    # Check skills.
    skill_requirements.each do |skill, level|
      if skills.get_current_level(skill) < level
        skill_req_msg = messages[:skill_req].nil? ? "You need a #{Skill::get_name(skill)} level of #{level} to do this task." : messages[:skill_req].call(player, product_reward, Skill::get_name(skill), level)
        player.send_message("#{skill_req_msg}")
        return false
      end
    end

    true
  end

  # Check if the player meets the material requirements to perform this recipe.
  def check_material_requirements(player)
    inventory = player.inventory

    # Check Materials.
    material_requirements.each do |id, amount|
      if inventory.get_amount(id) < amount
        item_req_msg = messages[:item_req].nil? ? "You are missing the required materials to make this item." : messages[:item_req].call(player, product_reward, tool_requirement, material_requirements)
        player.send_message("#{item_req_msg}")
        return false
      end
    end

    unless tool_requirement.nil?
      return false unless inventory.contains(tool_requirement)
    end

    true
  end

  # Display the success message
  def display_success_message(player, primary, secondary)
    player.send_message("#{messages[:success].call(player, primary, secondary)}") unless messages[:success].nil?
  end

  # Display the attempt message
  def display_attempt_message(player, primary, secondary)
    player.send_message("#{messages[:attempt].call(player, primary, secondary)}") unless messages[:attempt].nil?
  end

  # Display the failure message
  def display_failure_message(player, primary, secondary)
    player.send_message("#{messages[:failure].call(player, primary, secondary)}") unless messages[:failure].nil?
  end

  # Remove the materials for making the recipe.
  def remove_materials(player)
    # Remove the materials that go into making the product.
    material_requirements.each do |item, amount|
      player.inventory.remove(item, amount)
    end

    player.inventory.remove(main_material) unless main_material.nil?
  end

  # Reward the player for making the recipe.
  def assign_rewards(player)
    # Add the main product.
    player.inventory.add(product_reward) unless product_reward.nil?

    # Add additional items.
    item_rewards.each do |item|
      player.inventory.add(item)
    end

    # Add experience.
    experience_rewards.each do |skill, amount|
      player.skill_set.add_experience(skill, amount)
    end
  end

  # Check if tool is required.
  def requires_tool
    !tool_requirement.nil?
  end

  # Check if any objects are required.
  def requires_object
    !object_requirements.empty?
  end

  # Get the amount of material needed for the id.
  def get_material_amount(id)
    material_requirements[id]
  end
end

