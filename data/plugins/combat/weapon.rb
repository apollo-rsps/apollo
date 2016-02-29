java_import 'org.apollo.cache.def.ItemDefinition'
java_import 'org.apollo.game.model.inv.InventoryAdapter'
java_import 'org.apollo.game.model.entity.EquipmentConstants'
java_import 'org.apollo.game.model.entity.AnimationSet'
java_import 'org.apollo.game.model.Animation'
java_import 'org.apollo.game.model.inv.SynchronizationInventoryListener'

WEAPONS       = {}
NAMED_WEAPONS = {}

COMBAT_STYLES = [
  :accurate,
  :aggressive,
  :defensive,
  :controlled,
  :alt_aggressive
]

def create_weapon(identifier, class_name = nil, named: false, &block)
  if named
    create_named_weapon(identifier, class_name, &block)
  else
    create_normal_weapon(identifier, class_name, &block)
  end
end


private

def create_normal_weapon(item_matcher, class_name = nil, &block)
  items = find_entities :item, item_matcher.to_s.gsub(/_/, ' '), -1
  items.each do |item_id|
    definition      = ItemDefinition.lookup(item_id)
    definition_name = definition.name.downcase.to_s

    if class_name.nil?
      class_name =
        case definition_name
          when /[a-zA-Z]+ 2h sword/
            :two_handed_sword
          when /[a-zA-Z]+ scimitar/
            :scimitar
          else
            raise "Couldn't find a suitable weapon class for the given weapon."
        end
    end

    WEAPONS[item_id] = Weapon.new(WEAPON_CLASSES[class_name])
    WEAPONS[item_id].instance_eval &block
  end
end

def create_named_weapon(name, class_name, &block)
  NAMED_WEAPONS[name] = Weapon.new WEAPON_CLASSES[class_name]
  NAMED_WEAPONS[name].instance_eval &block
end

# Represents an equippable weapon, and the class it belongs to.
# 
# * has an optional special_attack
# * belongs to a certain WeaponClass, and inherits bonuses from it.
class Weapon
  attr_reader :weapon_class, :special_attack

  include Combat::BonusContainer

  def initialize(weapon_class)
    @weapon_class   = weapon_class
    @special_attack = nil
  end

  def special_attack?
    not special_attack.nil?
  end

  def set_special_attack(energy_requirement:, animation:, graphic: nil, &block)

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
    animation = weapon_class.other_animation(key)
    puts animation

    puts key
    unless animation.nil?
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
        else
          # type code here
      end
    end
  end
end

on :message, :item_option do |player, message|
  update_weapon_animations(player) if message.option == 2 and message.interface_id == SynchronizationInventoryListener::INVENTORY_ID
end

on :login do |event|
  update_weapon_animations(event.player)
end

on :message, :item_action do |player, message|
  update_weapon_animations(player) if message.interface_id == SynchronizationInventoryListener::EQUIPMENT_ID and message.slot == EquipmentConstants::WEAPON
end
