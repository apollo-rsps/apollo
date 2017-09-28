class CombatStyle
  attr_reader :button, :config, :attack_type, :block_animation
  attr_accessor :attack

  def initialize(button, config, attack_type, block_animation)
    @button          = button
    @config          = config
    @attack_type     = attack_type
    @block_animation = block_animation
  end
end
