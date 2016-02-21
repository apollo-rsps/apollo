
create_shop npcs: :aubury, name: "Aubury's Rune Shop.", items: [
            [:fire_rune, 5_000], [:water_rune, 5_000], [:air_rune, 5_000], [:earth_rune, 5_000],
            [:mind_rune, 5_000], [:body_rune, 5_000 ], [:chaos_rune, 250], [:death_rune, 250  ]
]

create_shop npcs: :lowe, name: "Lowe's Archery Emporium", items: [
            [:bronze_arrow, 2_000], [:iron_arrow, 1_500], [:steel_arrow, 1_000],
            [:mithril_arrow, 800], [:adamant_arrow, 600], [:shortbow, 4], [:longbow, 4],
            [:oak_shortbow, 3], [:oak_longbow, 3], [:willow_shortbow, 2], [:willow_longbow, 2],
            [:maple_shortbow, 1], [:maple_longbow, 1], [:crossbow, 2]
]

create_shop npcs: :horvik, buys: :all, name: "Horvik's Armour Shop.", items: [
            [:bronze_chainbody, 5], [:iron_chainbody, 3], [:steel_chainbody, 3],
            [:mithril_chainbody, 1], [:bronze_platebody, 3], [:iron_platebody, 1],
            [:steel_platebody, 1], [:black_platebody, 1], [:mithril_platebody, 1],
            [:iron_platelegs, 1], [:studded_body, 1], [:studded_chaps, 1]
]

create_shop npcs: :thessalia, name: "Thessalia's Fine Clothes.", items: [
            [:white_apron, 3], [:leather_body, 12], [:leather_gloves, 10], [:leather_boots, 10],
            [:brown_apron, 1], [:pink_skirt, 5], [:black_skirt, 3], [:blue_skirt, 2], [:cape, 4],
            [:silk, 5], [:priest_gown_428, 3], [:priest_gown_426, 3]
]

create_shop npcs: [:shop_keeper_522, :shop_assistant_523], buys: :all,
            name: 'Varrock General Store', items: [
            [:pot, 5], [:jug, 2], [:shears, 2], [:bucket, 3], [:bowl, 2], [:cake_tin, 2],
            [:tinderbox, 2], [:chisel, 2], [:hammer, 5], [:newcomer_map, 5]
]

create_shop npcs: [:shop_keeper_551, :shop_assistant_552], name: 'Varrock Swordshop', items: [
            [:bronze_sword, 5], [:iron_sword, 4], [:steel_sword, 4], [:black_sword, 3],
            [:mithril_sword, 3], [:adamant_sword, 2], [:bronze_longsword, 4], [:iron_longsword, 3],
            [:steel_longsword, 3], [:black_longsword, 2], [:mithril_longsword, 2],
            [:adamant_longsword, 1], [:bronze_dagger, 10], [:iron_dagger, 6], [:steel_dagger, 5],
            [:black_dagger, 4], [:mithril_dagger, 3], [:adamant_dagger, 2]
]

create_shop npcs: :zaff, name: "Zaff's Superior Staffs!", items: [
            [:battlestaff, 5], [:staff, 5], [:magic_staff, 5], [:staff_of_air, 2],
            [:staff_of_water, 2], [:staff_of_earth, 2], [:staff_of_fire, 2]
]
