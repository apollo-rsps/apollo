require 'java'

java_import 'org.apollo.game.model.entity.Player'

# Declare the tutorial island progress attribute.
declare_attribute(:tutorial_island_progress, :not_started, :persistent)

# The existing player class.
class Player

  # Returns whether or not this Player is currently on tutorial island.
  def in_tutorial_island
    x = position.x
    y = position.y
    above_ground(x, y) || below_ground(x, y)
  end

end

private

# Returns whether or not the specified coordinate pair is above ground on tutorial island.
def above_ground(x, y)
  x >= 3053 && x <= 3156 && y >= 3056 && y <= 3136
end

# Returns whether or not the specified coordinate pair is 'below' ground on tutorial island.
def below_ground(x, y)
  x >= 3072 && x <= 3118 && y >= 9492 && y <= 9535
end
