COMBAT_STYLES = [
  :accurate,
  :aggressive,
  :defensive,
  :controlled,
  :alt_aggressive,
  :accurate_ranged,
  :rapid,
  :long_range
]

MELEE_COMBAT_STYLES = [
  :accurate,
  :aggressive,
  :alt_aggressive,
  :controlled,
  :defensive
]

RANGE_COMBAT_STYLES = [
  :accurate_ranged,
  :rapid,
  :long_range
]

class WeaponClass
  attr_reader :widget, :name, :special_bar_button, :special_bar_config

  include Combat::BonusContainer

  def initialize(name, widget)
    @name          = name
    @widget        = widget
    @styles        = {}
    @style_attacks = {}
    @style_offsets = []
    @animations    = {}
  end

  def add_style(style, button: nil, attack_type: nil, speed: nil, animation: nil, block_animation: nil, range: 1)
    raise 'Invalid combat style given' unless COMBAT_STYLES.include? style

    @styles[style] = {
      :attack_type     => attack_type,
      :speed           => speed,
      :animation       => animation,
      :block_animation => block_animation,
      :range           => range,
      :button          => button == nil ? @styles.size + 1 : button
    }
    
    @style_offsets.push style
    
    if MELEE_COMBAT_STYLES.include? style
      @style_attacks[style] = Attack.new(animation: animation)
    end
  end

  def attack(style)
    @style_attacks[style]
  end

  def attack_type(style)
    @styles[style][:attack_type]
  end

  def block_animation(style)
    @styles[style][:block_animation]
  end

  def button(style)
    @styles[style][:button]
  end
  
  def config(style)
    @style_offsets.find_index {|v| v == style }
  end
  
  def other_animation(type)
    @animations[type]
  end

  def speed(style)
    @styles[style][:speed] || default_speed
  end

  def style_at(button)
    selected_style = @styles.select { |key, hash| hash[:button] == button }.keys[0]

    if selected_style == nil
      selected_style = @styles.min_by { |key, hash| hash[:button] }.first
    end
    
    selected_style
  end

  def default_speed(speed = nil)
    unless speed.nil?
      @default_speed = speed
    end

    @default_speed
  end

  def special_bar(config, button)
    @special_bar_config = config
    @special_bar_button = button
  end

  def special_bar?
    !@special_bar_button.nil? and !@special_bar_config.nil?
  end

  def animations(stand: nil, walk: nil, run: nil, idle_turn: nil, turn_around: nil, turn_left: nil, turn_right: nil)
    @animations = {
      :stand       => stand,
      :walk        => walk,
      :run         => run,
      :idle_turn   => idle_turn,
      :turn_around => turn_around,
      :turn_left   => turn_left,
      :turn_right  => turn_right
    }
  end
end

WEAPON_CLASSES             = {}
WEAPON_CLASS_INTERFACE_MAP = {}

def create_weapon_class(name, widget:, &block)
  weapon_class = WeaponClass.new(name, widget)
  weapon_class.instance_eval &block

  WEAPON_CLASSES[name.to_sym] = weapon_class
end
