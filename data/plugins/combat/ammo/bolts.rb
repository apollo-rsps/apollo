def create_bolt(item, hash)
  hash[:projectile_type] = :bolt
  hash[:weapon_classes]  = [:crossbow]
  hash[:projectile]      = 27 unless hash.key? :projectile
  create_ammo item, hash
end

create_projectile_type :bolt, start_height: 44, end_height: 44, delay: 41, speed: 5, slope: 5, radius: 10

create_bolt :iron_bolts, level_requirement: 1, drop_rate: 0.35
