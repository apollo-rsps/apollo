##
# The number of ticks since a <i>Mob<i>s last attack.
declare_attribute(:attack_timer, 100)

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
# The <i>CombatSpell</i> offset that a <i>Mob</i> is currently using.
declare_attribute(:combat_spell, :none, :persistent)

##
# A flag indicating whether the special bar is flagged for the next attack.
declare_attribute(:using_special, false, :persistent)

##
# A flag indicating whether auto retaliation is enabled.
declare_attribute(:auto_retaliate, true, :persistent)

##
# An integer between 0 and 100 indicating the amount of special energy a <i>Player</i> has.
declare_attribute(:special_energy, 100, :persistent)
