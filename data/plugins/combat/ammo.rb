AMMO = {}

def create_ammo(item_matcher, properties, &block)
  items = find_entities :item, item_matcher, -1
  fail "Unable to find ammo matching #{item_matcher}" unless items.size > 0

  projectile_type = properties[:projectile_type]
  fail "Unable to find projectile type #{projectile_type}" unless PROJECTILE_TYPES.key? projectile_type

  properties[:projectile_type] = PROJECTILE_TYPES[projectile_type]

  items.each do |item_id|
    AMMO[item_id] = Ammo.new(item_id, properties)
    AMMO[item_id].instance_eval(&block) if block_given?
  end
end

private

class Ammo
  attr_reader :item, :weapon_classes, :projectile, :projectile_type, :level_requirement, :drop_rate, :graphic

  include Combat::BonusContainer

  def initialize(item, weapon_classes:, projectile:, projectile_type:, level_requirement:, drop_rate:, graphic: nil)
    @item              = item
    @weapon_classes    = weapon_classes
    @projectile        = projectile
    @projectile_type   = projectile_type
    @level_requirement = level_requirement
    @drop_rate         = drop_rate
    @graphic           = graphic
  end
end
