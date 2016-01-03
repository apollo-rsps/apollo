PROJECTILE_TYPES = {}

def create_projectile_type(name, start_height:, end_height:, delay:, speed:, slope:, radius:)
  PROJECTILE_TYPES[name] = ProjectileType.new(start_height, end_height, delay, speed, slope, radius)
end

private

class ProjectileType
  attr_reader :start_height, :end_height, :delay, :speed, :slope, :radius

  def initialize(start_height, end_height, delay, speed, slope, radius)
    @start_height = start_height
    @end_height   = end_height
    @delay        = delay
    @speed        = speed
    @slope        = slope
    @radius       = radius
  end
end
