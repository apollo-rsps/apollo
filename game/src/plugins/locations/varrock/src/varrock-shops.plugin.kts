//Aubury's Rune Shop
val auburys = createShop("Aubury's Rune Shop.", arrayOf<ShopItem>(
        createShopItem("fire_rune", 5000),
        createShopItem("water_rune", 5000),
        createShopItem("air_rune", 5000),
        createShopItem("earth_rune", 5000),
        createShopItem("mind_rune", 5000),
        createShopItem("body_rune", 5000),
        createShopItem("chaos_rune", 250),
        createShopItem("death_rune", 250)
))
provideShop("aubury", auburys)

// Lowe's Archery Emporium
val lowes = createShop("Lowe's Archery Emporium.", arrayOf<ShopItem>(
        createShopItem("bronze_arrow", 2000),
        createShopItem("iron_arrow", 1500),
        createShopItem("steel_arrow", 1000),
        createShopItem("mithril_arrow", 800),
        createShopItem("adamant_arrow", 600),
        createShopItem("shortbow", 4),
        createShopItem("longbow", 4),
        createShopItem("oak_shortbow", 3),
        createShopItem("oak_longbow", 3),
        createShopItem("willow_shortbow", 2),
        createShopItem("willow_longbow", 2),
        createShopItem("maple_shortbow", 1),
        createShopItem("maple_longbow", 1),
        createShopItem("crossbow", 2)
))
provideShop("lowe", lowes)

//Horvik's Armour Shop
val horviks = createShop("Horvik's Armour Shop.", arrayOf<ShopItem>(
        createShopItem("bronze_chainbody", 5),
        createShopItem("iron_chainbody", 3),
        createShopItem("steel_chainbody", 3),
        createShopItem("mithril_chainbody", 1),
        createShopItem("bronze_platebody", 3),
        createShopItem("iron_platebody", 1),
        createShopItem("steel_platebody", 1),
        createShopItem("black_platebody", 1),
        createShopItem("mithril_platebody", 1),
        createShopItem("iron_platelegs", 1),
        createShopItem("studded_body", 1),
        createShopItem("studded_chaps", 1)
))
provideShop("horvik", horviks)

//Thessalia's Fine Clothes
val thessalias = createShop("Thessalia's Fine Clothes.", arrayOf<ShopItem>(
        createShopItem("white_apron", 3),
        createShopItem("leather_body", 12),
        createShopItem("leather_gloves", 10),
        createShopItem("leather_boots", 10),
        createShopItem("brown_apron", 1),
        createShopItem("pink_skirt", 5),
        createShopItem("black_skirt", 3),
        createShopItem("blue_skirt", 2),
        createShopItem("cape", 4),
        createShopItem("silk", 5),
        createShopItem(428, 3), //priest gown
        createShopItem(426, 3)
))
provideShop("thessalia", thessalias)

//Varrock General Store
val varrock = createShop("Varrock General Store.", arrayOf<ShopItem>(
        createShopItem("pot", 5),
        createShopItem("jug", 2),
        createShopItem("shears", 2),
        createShopItem("bucket", 3),
        createShopItem("bowl", 2),
        createShopItem("cake_tin", 2),
        createShopItem("tinderbox", 2),
        createShopItem("chisel", 2),
        createShopItem("hammer", 5),
        createShopItem("newcomer_map", 5)
))
provideShop(551, varrock)
provideShop(552, varrock)

//Varrock Swordshop
val varrockSwords = createShop("Varrock Swordshop.", arrayOf<ShopItem>(
        createShopItem("bronze_sword", 5),
        createShopItem("iron_sword", 4),
        createShopItem("steel_sword", 4),
        createShopItem("black_sword", 3),
        createShopItem("mithril_sword", 3),
        createShopItem("adamant_sword", 2),
        createShopItem("bronze_longsword", 4),
        createShopItem("iron_longsword", 3),
        createShopItem("steel_longsword", 3),
        createShopItem("black_longsword", 2),
        createShopItem("mithril_longsword", 2),
        createShopItem("adamant_longsword", 1),
        createShopItem("bronze_dagger", 10),
        createShopItem("iron_dagger", 6),
        createShopItem("steel_dagger", 5),
        createShopItem("black_dagger", 4),
        createShopItem("mithril_dagger", 3),
        createShopItem("adamant_dagger", 2)
))
provideShop(522, varrockSwords)
provideShop(523, varrockSwords)

//Zaff's Superior Staffs!
val zaffs = createShop("Zaff's Superior Staffs!", arrayOf<ShopItem>(
        createShopItem("battlestaff", 5),
        createShopItem("staff", 5),
        createShopItem("magic_staff", 5),
        createShopItem("staff_of_air", 2),
        createShopItem("staff_of_water", 2),
        createShopItem("staff_of_earth", 2),
        createShopItem("staff_of_fire", 2)
))
provideShop("zaff", zaffs)