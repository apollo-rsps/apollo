MAX_PLAYERS = 2000 # TODO: obtain from a Java class

# Calculates the number of pulses it takes for an ore to respawn based on the
# number of players currently online.
#
# The 'base' argument is the number of pulses it takes with no players online.
# The 'players' argument is the number of players currently logged into the
# current world.
#
# The base times can be found on this website:
#   http://runescape.salmoneus.net/mining.html#respawn
#
# These must be converted to pulses (seconds * 10 / 6) to work with this
# function. The rest of the mining plugin rounds the base respawn times in
# pulses down where appropriate.
def respawn_pulses(base, players)
  base - players * base / (MAX_PLAYERS * 2)
end
