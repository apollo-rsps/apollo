java_import 'org.apollo.cache.def.ItemDefinition'
java_import 'org.apollo.cache.def.EquipmentDefinition'
java_import 'org.apollo.game.model.inv.InventoryAdapter'
java_import 'org.apollo.game.model.entity.EquipmentConstants'
java_import 'org.apollo.game.model.entity.AnimationSet'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.inv.SynchronizationInventoryListener'

WEAPONS       = {}
NAMED_WEAPONS = {}
## 
# Create a new {@code Weapon} and configure it with the Weapon DSL.
#
# @param [Symbol] identifier A unique identifier for the weapon if {@code named} is true, otherwise the item name
#                            of the weapon
# @param [Symbol] class_name A symbol representing the {@code WeaponClass} this weapon belongs to.
# @param [Boolean]           True if this is a uniquely identified weapon, false if it belongs to an item.

def create_weapon(identifier, class_name = nil, named: false, &block)
  if named
    create_named_weapon(identifier, class_name, &block)
  else
    create_normal_weapon(identifier, class_name, &block)
  end
end

private

def create_normal_weapon(item_matcher, class_name = nil, &block)
  items = find_entities :item, item_matcher, -1
  fail "Unable to find weapon matching #{item_matcher}" if items.empty?

  items.each do |item_id|
    equipment_def = EquipmentDefinition.lookup(item_id)

    next if equipment_def.nil? || equipment_def.slot != EquipmentConstants::WEAPON

    definition      = ItemDefinition.lookup(item_id)
    definition_name = definition.name.downcase.to_s

    if class_name.nil?
      class_name =
        case definition_name
        when /[a-zA-Z]+ 2h sword/
          :two_handed_sword
        when /[a-zA-Z]+ scimitar/
          :scimitar
        when /[a-zA-Z]+ dagger/
          :dagger
        when /[a-z\s]*longbow/
          :longbow
        when /[a-z\s]*shortbow/
          :shortbow
        when /[a-z]+ c'bow/
          :crossbow
        else
          fail "Couldn't find a suitable weapon class for the given weapon."
        end
    end

    WEAPONS[item_id] = Weapon.new(definition.name, WEAPON_CLASSES[class_name])
    WEAPONS[item_id].instance_eval(&block) if block_given?
  end
end

def create_named_weapon(name, class_name, &block)
  NAMED_WEAPONS[name] = Weapon.new name.to_s.capitalize, WEAPON_CLASSES[class_name]
  NAMED_WEAPONS[name].instance_eval(&block) if block_given?
end

# Represents an equippable weapon, and the class it belongs to.
#
# * has an optional special_attack
# * belongs to a certain WeaponClass, and inherits bonuses from it.
class Weapon
  attr_reader :name, :weapon_class, :special_attack

  include Combat::BonusContainer

  def initialize(name, weapon_class)
    @name           = name
    @weapon_class   = weapon_class
    @special_attack = nil
  end

  def special_attack?
    !special_attack.nil?
  end

  def set_special_attack(speed:, range: 1, energy_requirement:, animation:, graphic: nil, &block)
    attack_dsl = AttackDSL.new
    attack_dsl.speed = speed
    attack_dsl.range = range
    attack_dsl.animation = animation
    attack_dsl.graphic = graphic
    attack_dsl.add_requirement SpecialEnergyRequirement.new(energy_requirement)
    attack_dsl.instance_eval(&block)

    @special_attack = attack_dsl.to_attack
  end
end

def update_weapon_animations(player)
  default_animations = AnimationSet::DEFAULT_ANIMATION_SET
  player_animations  = player.animation_set

  player_animations.stand       = default_animations.stand
  player_animations.walking     = default_animations.walking
  player_animations.running     = default_animations.running
  player_animations.idle_turn   = default_animations.idle_turn
  player_animations.turn_around = default_animations.turn_around
  player_animations.turn_left   = default_animations.turn_left
  player_animations.turn_right  = default_animations.turn_right

  weapon       = EquipmentUtil.equipped_weapon(player)
  weapon_class = weapon.weapon_class

  [:stand, :walk, :run, :idle_turn, :turn_around, :turn_left, :turn_right].each do |key|
    animation = weapon_class.animation(key)

    next if animation.nil?
    case key
    when :stand
      player_animations.stand = animation
    when :walk
      player_animations.walking = animation
    when :run
      player_animations.running = animation
    when :idle_turn
      player_animations.idle_turn = animation
    when :turn_around
      player_animations.turn_around = animation
    when :turn_left
      player_animations.turn_left = animation
    when :turn_right
      player_animations.turn_right = animation
    end
  end
end

on :message, :item_option do |player, message|
  next unless message.interface_id == SynchronizationInventoryListener::INVENTORY_ID &&
              message.option == 2

  update_weapon_animations(player)
end

on :login do |event|
  update_weapon_animations(event.player)
end

on :message, :item_action do |player, message|
  next unless message.interface_id == SynchronizationInventoryListener::EQUIPMENT_ID &&
              message.slot == EquipmentConstants::WEAPON

  update_weapon_animations(player)
end
