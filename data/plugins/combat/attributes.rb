module CombatModule
  ##
  # The delay a <i>Mob</i> must wait before attacking again.
  declare_attribute(:attack_delay, 0)

  ##
  # A flag indicating whether this <i>Mob</i> is currently in combat.
  declare_attribute(:attacking, false)

  ##
  # A flag indicating whether our <i>Mob</i> is dead.
  declare_attribute(:dead, false)

  ##
  # The amount of ticks a <i>Player</i> must wait before logging out after combat.
  declare_attribute(:logout_timer, Time.now.to_i)

  ##
  # The <i>CombatStyle</i> offset that a <i>Mob</i> is currently using.
  declare_attribute(:combat_style, 0, :persistent)

  ##
  # A flag indicating whether the special bar is flagged for the next attack.
  declare_attribute(:using_special, false, :persistent)

  ##
  # An integer between 0 and 100 indicating the amount of special energy a <i>Player</i> has.
  declare_attribute(:special_energy, 100, :persistent)
end