def create_arrow(item, hash)
  hash[:projectile_type] = :arrow
  hash[:weapon_classes]  = [:longbow, :shortbow]

  create_ammo item, hash
end

create_projectile_type :arrow, start_height: 41, end_height: 37, delay: 41, speed: 6, slope: 15, radius: 10

create_arrow :bronze_arrow, level_requirement: 1, projectile: 10, graphic: 19, drop_rate: 0.3
create_arrow :iron_arrow, level_requirement: 1, projectile: 9, graphic: 18, drop_rate: 0.35
create_arrow :steel_arrow, level_requirement: 5, projectile: 11, graphic: 20, drop_rate: 0.4
create_arrow :mithril_arrow, level_requirement: 20, projectile: 12, graphic: 21, drop_rate: 0.45
create_arrow :adamant_arrow, level_requirement: 30, projectile: 13, graphic: 22, drop_rate: 0.5
create_arrow :rune_arrow, level_requirement: 40, projectile: 15, graphic: 24, drop_rate: 0.6
