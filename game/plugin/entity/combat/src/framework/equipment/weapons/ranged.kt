abstract class RangedWeaponClass(ammoType: AmmoType, detailsBuilder: WeaponClassConfigurer)
    : WeaponClass(RangeAttackFactory(ammoType), detailsBuilder)