COMBAT_STYLES = [
  :accurate,
  :aggressive,
  :defensive,
  :controlled,
  :alt_aggressive,
  :rapid,
  :long_range
]

##
# A model for a weapon class, which contains the necessary inform

class WeaponClass
  ##
  # Allow this class to set combat bonuses.

  include Combat::BonusContainer

  ##
  # The widget which is displayed on the combat tab when this weapon class
  # is in use.

  attr_reader :widget

  ##
  # A unique identifier for this class.

  attr_reader :name

  ##
  # The id of the special bar button for this weapon class, if applicable.

  attr_reader :special_bar_button

  ##
  # The id of the widget config used to hide this weapon classes special bar, if applicable.

  attr_reader :special_bar_config

  ##
  # Create a new WeaponClass with the given name and specified widget id.
  #
  # @param [Symbol] name The unique name to give this weapon class.
  # @param [Number] widget The id of the widget associated with this weapon class.

  def initialize(name, widget, type)
    @name       = name
    @widget     = widget
    @type       = type
    @styles     = {}
    @defaults   = {}
    @animations = {}
  end

  ##
  # Set default properties which are used for styles which don't have
  # those properties defined.
  #
  # @param [Hash] props
  #   * :attack_type [Symbol] The default attack type for styles in this class.
  #   * :speed       [Number] The default attack speed for styles in this class.
  #   * :animation   [Number] The default attack animation id for styles in this class.
  #   * :block_animation [Number] The default block animation id for styles in this class.
  #   * :range [Number] The default attack range for styles in this class.

  def defaults(props)
    @defaults = props
  end

  ##
  # Add a new {@link CombatStyle} to to this class.
  #
  # @param [Symbol] style The name of the style, one of {@link COMBAT_STYLES}.
  # @param [Hash] properties
  #   * :button      [Number] The id of the button which activates this style.
  #   * :attack_type [Symbol] The attack type for this style.
  #   * :speed       [Number] The attack speed for this style.
  #   * :animation   [Number] The attack animation id for this style.
  #   * :graphic     [Hash|Number] The id, or a hash specifying height and id for the graphic for this style.
  #   * :requirements [Array] An array of [AttackRequirement] objects to be applied to this styles attack.
  #   * :block_animation [Number] The block animation id for this style.
  #   * :range [Number] The attack range for this style.
  #
  # @param [Proc] block An optional block which is called in the context of an AttackDSL if present, to create an attack for
  #                     this style.

  def style(style, properties = {}, &block)
    fail 'Invalid combat style given' unless COMBAT_STYLES.include? style

    properties = @defaults.merge properties

    ## The config ID used to set the active combat style, typically 0-3. TODO: does this always work?
    config = @styles.size
    button, attack_type, block_animation = properties[:button], properties[:attack_type], properties[:block_animation]

    @styles[style] = CombatStyle.new(button, config, attack_type, block_animation)

    if block_given?
      attack_dsl              = AttackDSL.new
      attack_dsl.speed        = properties[:speed]
      attack_dsl.graphic      = properties[:graphic]
      attack_dsl.animation    = properties[:animation]
      attack_dsl.range        = properties[:range]
      properties[:requirements].each { |requirement| attack_dsl.add_requirement requirement }
      attack_dsl.instance_eval &block

      @styles[style].attack = attack_dsl.to_attack
    end

    return unless @styles[style].attack.nil?

    ## Get rid of any properties which aren't included in the keyword argument list
    properties.delete_if { |key| ![:speed, :animation, :range, :requirements, :graphic].include? key }

    if @type == :melee
      @styles[style].attack = Attack.new(properties)
    elsif @type == :ranged
      @styles[style].attack = RangedAttack.new(properties)
    end
  end

  ##
  # Get the currently selected combat style in this class for the given mob.
  # @param [Mob] mob
  # @return [CombatStyle]

  def selected_style(mob)
    style = @styles.find { |_key, value| value.button == mob.combat_style }

    style = @styles.min_by { |_key, value| value.button } if style.nil?

    # We want the last element because Hash#find returns [key, value]
    style.last
  end

  ##
  # Get the animation id for the given animation type.

  def animation(type)
    @animations[type]
  end

  ##
  # Set the widget config and button for this classes special attack bar.

  def special_bar(config, button)
    @special_bar_config = config
    @special_bar_button = button
  end

  ##
  # Check if this weapon class supports a special attack bar.

  def special_bar?
    !@special_bar_button.nil? && !@special_bar_config.nil?
  end

  ##
  # Set the character animations associated with this weapon class.
  
  def animations(stand: nil, walk: nil, run: nil, idle_turn: nil, turn_around: nil, turn_left: nil, turn_right: nil)
    @animations = {
      stand:       stand,
      walk:        walk,
      run:         run,
      idle_turn:   idle_turn,
      turn_around: turn_around,
      turn_left:   turn_left,
      turn_right:  turn_right
    }
  end
end

WEAPON_CLASSES             = {}
WEAPON_CLASS_INTERFACE_MAP = {}

def create_weapon_class(name, widget:, type: :melee, &block)
  weapon_class = WeaponClass.new(name, widget, type)
  weapon_class.instance_eval &block

  WEAPON_CLASSES[name.to_sym] = weapon_class
end
