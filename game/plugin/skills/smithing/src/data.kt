import org.apollo.game.message.handler.ItemVerificationHandler
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.game.model.inv.Inventory

//For Reference: http://oldschoolrunescape.wikia.com/wiki/Smithing.
/*
My implementation of smithing does not include anything from any quests.
Quests will have to add the smithing items as needed.
 */

//Listeners for smelting
data class SmeltingWrapper(val player: Player, var bar: Bar?, val pos: Position)
val waitingForAmount = HashSet<SmeltingWrapper>()

fun getPlayerWaiting(player: Player): SmeltingWrapper? {
    if (waitingForAmount.any {it.player == player } ) {
        return waitingForAmount.first { it.player == player }
    }
    return null
}

enum class Ore(val id: Int) {
    CLAY(434),
    COPPER(436),
    TIN(438),
    IRON(440),
    COAL(453),
    GOLD(444),
    SILVER(442),
    MITHRIL(447),
    ADAMANT(449),
    RUNITE(451)
}

val furnaces = intArrayOf(2781, 14921, 9390, 2785, 2966, 3044, 3294, 3413, 4304, 4305, 6189, 6190, 11009, 11010, 11666, 12100, 12809)

val anvils = intArrayOf(2783, 2782)

val HAMMER_ITEM = 2347

val SMELTING_ANIMATION = 899
val SMELTING_SOUND = 469

val SMITHING_ANIMATION = 898
val SMITHING_SOUND = 468

data class OreAmount(val ore: Ore, val amount: Int);

enum class Bar(val id: Int, val xp: Double, val level: Int, val ores: Array<OreAmount>) {
    BRONZE(2349, 6.25, 1, arrayOf(OreAmount(Ore.TIN, 1), OreAmount(Ore.COPPER, 1))),
    IRON(2351, 12.5, 15, arrayOf(OreAmount(Ore.IRON, 1))),
    SILVER(2355, 13.67, 20, arrayOf(OreAmount(Ore.SILVER, 1))),
    STEEL(2353, 17.5, 30, arrayOf(OreAmount(Ore.IRON, 1), OreAmount(Ore.COAL, 2))),
    GOLD(2357, 22.5, 40, arrayOf(OreAmount(Ore.GOLD, 1))),
    MITHRIL(2359, 30.0, 50, arrayOf(OreAmount(Ore.MITHRIL, 1), OreAmount(Ore.COAL, 4))),
    ADAMANT(2361, 37.5, 70, arrayOf(OreAmount(Ore.ADAMANT, 1), OreAmount(Ore.COAL, 6))),
    RUNITE(2363, 50.0, 85, arrayOf(OreAmount(Ore.RUNITE, 1), OreAmount(Ore.COAL, 8)))
}

enum class Smithable(val bar: Bar) {
    BRONZE(Bar.BRONZE),
    IRON(Bar.IRON),
    STEEL(Bar.STEEL),
    MITHRIL(Bar.MITHRIL),
    ADAMANT(Bar.ADAMANT),
    RUNITE(Bar.RUNITE)
}


data class BarAmount(val bar: Bar, val amount: Int)

enum class SmithingItem(val id: Int,val makes: Int, val xp: Double, val level: Int, val bars: BarAmount, val barsText: Int, val nameText: Int, val columnId: Int, val slotId: Int) {
    BRONZE_DAGGER(1205, 1, 12.5, 1, BarAmount(Bar.BRONZE, 1 ), Interface.TEXT_DAGGER_BARS, Interface.TEXT_DAGGER_NAME, Interface.COLUMN_0, Interface.SLOT_0),
    BRONZE_AXE(1351, 1, 12.5, 1, BarAmount(Bar.BRONZE, 1 ), Interface.TEXT_AXE_BARS, Interface.TEXT_AXE_NAME, Interface.COLUMN_1, Interface.SLOT_0),
    BRONZE_MACE(1422, 1, 12.5, 2, BarAmount(Bar.BRONZE, 1 ), Interface.TEXT_MACE_BARS, Interface.TEXT_MACE_NAME, Interface.COLUMN_1, Interface.SLOT_1),
    BRONZE_MED_HELM(1139, 1, 12.5, 3, BarAmount(Bar.BRONZE, 1 ), Interface.TEXT_MED_HELM_BARS, Interface.TEXT_MED_HELM_NAME, Interface.COLUMN_3, Interface.SLOT_0),
    BRONZE_SWORD(1277, 1, 12.5, 4, BarAmount(Bar.BRONZE, 1 ), Interface.TEXT_SWORD_BARS, Interface.TEXT_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_1),
    BRONZE_WIRE(1794, 1, 12.5, 4, BarAmount(Bar.BRONZE, 1 ), Interface.TEXT_WIRE_BARS, Interface.TEXT_WIRE_NAME, Interface.COLUMN_4, Interface.SLOT_3),
    BRONZE_DART_TIP(819, 10, 12.5, 4, BarAmount(Bar.BRONZE, 1 ), Interface.TEXT_DART_TIPS_BARS, Interface.TEXT_DART_TIPS_NAME, Interface.COLUMN_4, Interface.SLOT_0),
    BRONZE_NAILS(4819, 15, 12.5, 4, BarAmount(Bar.BRONZE, 1 ), Interface.TEXT_NAILS_BARS, Interface.TEXT_NAILS_NAME, Interface.COLUMN_3, Interface.SLOT_4),
    BRONZE_ARROWTIPS(39, 15, 12.5, 5, BarAmount(Bar.BRONZE, 1 ), Interface.TEXT_ARROW_TIPS_BARS, Interface.TEXT_ARROW_TIPS_NAME, Interface.COLUMN_4, Interface.SLOT_1),
    BRONZE_SCIMITAR(1321, 1, 25.0, 5, BarAmount(Bar.BRONZE, 2 ), Interface.TEXT_SCIMITAR_BARS, Interface.TEXT_SCIMITAR_NAME, Interface.COLUMN_0, Interface.SLOT_2),
    BRONZE_LONGSWORD(1291, 1, 25.0, 6, BarAmount(Bar.BRONZE, 2 ), Interface.TEXT_LONG_SWORD_BARS, Interface.TEXT_LONG_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_3),
    BRONZE_FULL_HELM(1155, 1, 25.0, 7, BarAmount(Bar.BRONZE, 2 ), Interface.TEXT_FULL_HELM_BARS, Interface.TEXT_FULL_HELM_NAME, Interface.COLUMN_3, Interface.SLOT_1),
    BRONZE_KNIFE(864, 5, 12.5, 7, BarAmount(Bar.BRONZE, 1 ), Interface.TEXT_KNIFE_BARS, Interface.TEXT_KNIFE_NAME, Interface.COLUMN_4, Interface.SLOT_2),
    BRONZE_SQ_SHIELD(1173, 1, 25.0, 8, BarAmount(Bar.BRONZE, 2 ), Interface.TEXT_SQ_SHIELD_BARS, Interface.TEXT_SQ_SHIELD_NAME, Interface.COLUMN_3, Interface.SLOT_2),
    BRONZE_WARHAMMER(1337, 1, 37.5, 9, BarAmount(Bar.BRONZE, 3 ), Interface.TEXT_WAR_HAMMER_BARS, Interface.TEXT_WAR_HAMMER_NAME, Interface.COLUMN_1, Interface.SLOT_2),
    BRONZE_BATTLEAXE(1375, 1, 37.5, 10, BarAmount(Bar.BRONZE, 3 ), Interface.TEXT_BATTLE_AXE_BARS, Interface.TEXT_BATTLE_AXE_NAME, Interface.COLUMN_1, Interface.SLOT_3),
    BRONZE_CHAINBODY(1103, 1, 37.5, 11, BarAmount(Bar.BRONZE, 3 ), Interface.TEXT_CHAIN_BODY_BARS, Interface.TEXT_CHAIN_BODY_NAME, Interface.COLUMN_2, Interface.SLOT_0),
    BRONZE_KITESHIELD(1189, 1, 37.5, 12, BarAmount(Bar.BRONZE, 3 ), Interface.TEXT_KITE_SHIELD_BARS, Interface.TEXT_KITE_SHIELD_NAME, Interface.COLUMN_3, Interface.SLOT_3),
    BRONZE_CLAWS(3095, 1, 25.0, 13, BarAmount(Bar.BRONZE, 2 ), Interface.TEXT_CLAWS_BARS, Interface.TEXT_CLAWS_NAME, Interface.COLUMN_1, Interface.SLOT_4),
    BRONZE_2H_SWORD(1307, 1, 37.5, 14, BarAmount(Bar.BRONZE, 3 ), Interface.TEXT_2H_SWORD_BARS, Interface.TEXT_2H_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_4),
    BRONZE_PLATELEGS(1075, 1, 37.5, 16, BarAmount(Bar.BRONZE, 3 ), Interface.TEXT_PLATE_LEGS_BARS, Interface.TEXT_PLATE_LEGS_NAME, Interface.COLUMN_2, Interface.SLOT_1),
    BRONZE_PLATESKIRT(1087, 1, 37.5, 16, BarAmount(Bar.BRONZE, 3 ), Interface.TEXT_PLATE_SKIRT_BARS, Interface.TEXT_PLATE_SKIRT_NAME, Interface.COLUMN_2, Interface.SLOT_2),
    BRONZE_PLATEBODY(1117, 1, 62.5, 18, BarAmount(Bar.BRONZE, 5 ), Interface.TEXT_PLATE_BODY_BARS, Interface.TEXT_PLATE_BODY_NAME, Interface.COLUMN_2, Interface.SLOT_3),
    //
    IRON_DAGGER(1203, 1, 25.0, 15, BarAmount(Bar.IRON, 1 ), Interface.TEXT_DAGGER_BARS, Interface.TEXT_DAGGER_NAME, Interface.COLUMN_0, Interface.SLOT_0),
    IRON_AXE(1349, 1, 25.0, 16, BarAmount(Bar.IRON, 1 ), Interface.TEXT_AXE_BARS, Interface.TEXT_AXE_NAME, Interface.COLUMN_1, Interface.SLOT_0),
    IRON_MACE(1420, 1, 25.0, 17, BarAmount(Bar.IRON, 1 ), Interface.TEXT_MACE_BARS, Interface.TEXT_MACE_NAME, Interface.COLUMN_1, Interface.SLOT_1),
    IRON_SPIT(7225, 1, 25.0, 16, BarAmount(Bar.IRON, 1 ), Interface.TEXT_SPIT_BARS, Interface.TEXT_SPIT_NAME, Interface.COLUMN_4, Interface.SLOT_3),
    IRON_MED_HELM(1137, 1, 25.0, 18, BarAmount(Bar.IRON, 1 ), Interface.TEXT_MED_HELM_BARS, Interface.TEXT_MED_HELM_NAME, Interface.COLUMN_3, Interface.SLOT_0),
    IRON_SWORD(1279, 1, 25.0, 19, BarAmount(Bar.IRON, 1 ), Interface.TEXT_SWORD_BARS, Interface.TEXT_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_1),
    IRON_DART_TIP(807, 10, 25.0, 19, BarAmount(Bar.IRON, 1 ), Interface.TEXT_DART_TIPS_BARS, Interface.TEXT_DART_TIPS_NAME, Interface.COLUMN_4, Interface.SLOT_0),
    IRON_NAILS(4820, 15, 25.0, 19, BarAmount(Bar.IRON, 1 ), Interface.TEXT_NAILS_BARS, Interface.TEXT_NAILS_NAME, Interface.COLUMN_3, Interface.SLOT_4),
    IRON_ARROWTIPS(40, 15, 25.0, 20, BarAmount(Bar.IRON, 1 ), Interface.TEXT_ARROW_TIPS_BARS, Interface.TEXT_ARROW_TIPS_NAME, Interface.COLUMN_4, Interface.SLOT_1),
    IRON_SCIMITAR(1323, 1, 50.0, 20, BarAmount(Bar.IRON, 2 ), Interface.TEXT_SCIMITAR_BARS, Interface.TEXT_SCIMITAR_NAME, Interface.COLUMN_0, Interface.SLOT_2),
    IRON_LONGSWORD(1293, 1, 50.0, 21, BarAmount(Bar.IRON, 2 ), Interface.TEXT_LONG_SWORD_BARS, Interface.TEXT_LONG_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_3),
    IRON_FULL_HELM(1153, 1, 50.0, 22, BarAmount(Bar.IRON, 2 ), Interface.TEXT_FULL_HELM_BARS, Interface.TEXT_FULL_HELM_NAME, Interface.COLUMN_3, Interface.SLOT_1),
    IRON_KNIFE(863, 5, 25.0, 22, BarAmount(Bar.IRON, 1 ), Interface.TEXT_KNIFE_BARS, Interface.TEXT_KNIFE_NAME, Interface.COLUMN_4, Interface.SLOT_2),
    IRON_SQ_SHIELD(1175, 1, 50.0, 23, BarAmount(Bar.IRON, 2 ), Interface.TEXT_SQ_SHIELD_BARS, Interface.TEXT_SQ_SHIELD_NAME, Interface.COLUMN_3, Interface.SLOT_2),
    IRON_WARHAMMER(1335, 1, 75.0, 24, BarAmount(Bar.IRON, 3 ), Interface.TEXT_WAR_HAMMER_BARS, Interface.TEXT_WAR_HAMMER_NAME, Interface.COLUMN_1, Interface.SLOT_2),
    IRON_BATTLEAXE(1363, 1, 75.0, 25, BarAmount(Bar.IRON, 3 ), Interface.TEXT_BATTLE_AXE_BARS, Interface.TEXT_BATTLE_AXE_NAME, Interface.COLUMN_1, Interface.SLOT_3),
    IRON_OIL_LANTERN(4540, 1, 25.0, 26, BarAmount(Bar.IRON, 1 ), Interface.TEXT_OIL_LANTERN_BARS, Interface.TEXT_OIL_LANTERN_NAME, Interface.COLUMN_2, Interface.SLOT_4),
    IRON_CHAINBODY(1101, 1, 75.0, 26, BarAmount(Bar.IRON, 3 ), Interface.TEXT_CHAIN_BODY_BARS, Interface.TEXT_CHAIN_BODY_NAME, Interface.COLUMN_2, Interface.SLOT_0),
    IRON_KITESHIELD(1191, 1, 75.0, 27, BarAmount(Bar.IRON, 3 ), Interface.TEXT_KITE_SHIELD_BARS, Interface.TEXT_KITE_SHIELD_NAME, Interface.COLUMN_3, Interface.SLOT_3),
    IRON_CLAWS(3096, 1, 50.0, 28, BarAmount(Bar.IRON, 2 ), Interface.TEXT_CLAWS_BARS, Interface.TEXT_CLAWS_NAME, Interface.COLUMN_1, Interface.SLOT_4),
    IRON_2H_SWORD(1309, 1, 75.0, 29, BarAmount(Bar.IRON, 3 ), Interface.TEXT_2H_SWORD_BARS, Interface.TEXT_2H_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_4),
    IRON_PLATELEGS(1067, 1, 75.0, 31, BarAmount(Bar.IRON, 3 ), Interface.TEXT_PLATE_LEGS_BARS, Interface.TEXT_PLATE_LEGS_NAME, Interface.COLUMN_2, Interface.SLOT_1),
    IRON_PLATESKIRT(1081, 1, 75.0, 31, BarAmount(Bar.IRON, 3 ), Interface.TEXT_PLATE_SKIRT_BARS, Interface.TEXT_PLATE_SKIRT_NAME, Interface.COLUMN_2, Interface.SLOT_2),
    IRON_PLATEBODY(1115, 1, 125.0, 33, BarAmount(Bar.IRON, 5 ), Interface.TEXT_PLATE_BODY_BARS, Interface.TEXT_PLATE_BODY_NAME, Interface.COLUMN_2, Interface.SLOT_3),
    //
    STEEL_DAGGER(1207, 1, 37.5, 30, BarAmount(Bar.STEEL, 1 ), Interface.TEXT_DAGGER_BARS, Interface.TEXT_DAGGER_NAME, Interface.COLUMN_0, Interface.SLOT_0),
    STEEL_AXE(1353, 1, 37.5, 31, BarAmount(Bar.STEEL, 1 ), Interface.TEXT_AXE_BARS, Interface.TEXT_AXE_NAME, Interface.COLUMN_1, Interface.SLOT_0),
    STEEL_MACE(1424, 1, 37.5, 32, BarAmount(Bar.STEEL, 1 ), Interface.TEXT_MACE_BARS, Interface.TEXT_MACE_NAME, Interface.COLUMN_1, Interface.SLOT_1),
    STEEL_MED_HELM(1141, 1, 37.5, 33, BarAmount(Bar.STEEL, 1 ), Interface.TEXT_MED_HELM_BARS, Interface.TEXT_MED_HELM_NAME, Interface.COLUMN_3, Interface.SLOT_0),
    STEEL_SWORD(1281, 1, 37.5, 34, BarAmount(Bar.STEEL, 1) , Interface.TEXT_SWORD_BARS, Interface.TEXT_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_1),
    STEEL_DART_TIP(808, 10, 37.5, 34, BarAmount(Bar.STEEL, 1 ), Interface.TEXT_DART_TIPS_BARS, Interface.TEXT_DART_TIPS_NAME, Interface.COLUMN_4, Interface.SLOT_0),
    STEEL_NAILS(1539, 15, 37.5, 34, BarAmount(Bar.STEEL, 1 ), Interface.TEXT_NAILS_BARS, Interface.TEXT_NAILS_NAME, Interface.COLUMN_3, Interface.SLOT_4),
    STEEL_ARROWTIPS(41, 15, 37.5, 35, BarAmount(Bar.STEEL, 1 ), Interface.TEXT_ARROW_TIPS_BARS, Interface.TEXT_ARROW_TIPS_NAME, Interface.COLUMN_4, Interface.SLOT_1),
    STEEL_CANNONBALL(2, 1, 25.5, 35, BarAmount(Bar.STEEL, 1 ), Interface.TEXT_CANNON_BALL_BARS, Interface.TEXT_CANNON_BALL_NAME, Interface.COLUMN_4, Interface.SLOT_3),
    STEEL_SCIMITAR(1325, 1, 75.0, 35, BarAmount(Bar.STEEL, 2 ), Interface.TEXT_SCIMITAR_BARS, Interface.TEXT_SCIMITAR_NAME, Interface.COLUMN_0, Interface.SLOT_2),
    STEEL_LONGSWORD(1295, 1, 75.0, 36, BarAmount(Bar.STEEL, 2 ), Interface.TEXT_LONG_SWORD_BARS, Interface.TEXT_LONG_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_3),
    STEEL_STUDS(2370, 1, 37.5, 36, BarAmount(Bar.STEEL, 1 ), Interface.TEXT_STUDS_BARS, Interface.TEXT_STUDS_NAME, Interface.COLUMN_4, Interface.SLOT_4),
    STEEL_FULL_HELM(1157, 1, 75.0, 37, BarAmount(Bar.STEEL, 2 ), Interface.TEXT_FULL_HELM_BARS, Interface.TEXT_FULL_HELM_NAME, Interface.COLUMN_3, Interface.SLOT_1),
    STEEL_KNIFE(865, 5, 37.5, 37, BarAmount(Bar.STEEL, 1 ), Interface.TEXT_KNIFE_BARS, Interface.TEXT_KNIFE_NAME, Interface.COLUMN_4, Interface.SLOT_2),
    STEEL_SQ_SHIELD(1177, 1, 75.0, 38, BarAmount(Bar.STEEL, 2 ), Interface.TEXT_SQ_SHIELD_BARS, Interface.TEXT_SQ_SHIELD_NAME, Interface.COLUMN_3, Interface.SLOT_2),
    STEEL_WARHAMMER(1339, 1, 112.5, 39, BarAmount(Bar.STEEL, 3 ), Interface.TEXT_WAR_HAMMER_BARS, Interface.TEXT_WAR_HAMMER_NAME, Interface.COLUMN_1, Interface.SLOT_2),
    STEEL_BATTLEAXE(1365, 1, 112.5, 40, BarAmount(Bar.STEEL, 3 ), Interface.TEXT_BATTLE_AXE_BARS, Interface.TEXT_BATTLE_AXE_NAME, Interface.COLUMN_1, Interface.SLOT_3),
    STEEL_CHAINBODY(1105, 1, 112.5, 41, BarAmount(Bar.STEEL, 3 ), Interface.TEXT_CHAIN_BODY_BARS, Interface.TEXT_CHAIN_BODY_NAME, Interface.COLUMN_2, Interface.SLOT_0),
    STEEL_KITESHIELD(1193, 1, 112.5, 42, BarAmount(Bar.STEEL, 3 ), Interface.TEXT_KITE_SHIELD_BARS, Interface.TEXT_KITE_SHIELD_NAME, Interface.COLUMN_3, Interface.SLOT_3),
    STEEL_CLAWS(3097, 1, 75.0, 43, BarAmount(Bar.STEEL, 2 ), Interface.TEXT_CLAWS_BARS, Interface.TEXT_CLAWS_NAME, Interface.COLUMN_1, Interface.SLOT_4),
    STEEL_2H_SWORD(1311, 1, 112.5, 44, BarAmount(Bar.STEEL, 3 ), Interface.TEXT_2H_SWORD_BARS, Interface.TEXT_2H_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_4),
    STEEL_PLATELEGS(1069, 1, 112.5, 46, BarAmount(Bar.STEEL, 3 ), Interface.TEXT_PLATE_LEGS_BARS, Interface.TEXT_PLATE_LEGS_NAME, Interface.COLUMN_2, Interface.SLOT_1),
    STEEL_PLATESKIRT(1083, 1, 112.5, 46, BarAmount(Bar.STEEL, 3 ), Interface.TEXT_PLATE_SKIRT_BARS, Interface.TEXT_PLATE_SKIRT_NAME, Interface.COLUMN_2, Interface.SLOT_2),
    STEEL_PLATEBODY(1119, 1, 187.5, 48, BarAmount(Bar.STEEL, 5 ), Interface.TEXT_PLATE_BODY_BARS, Interface.TEXT_PLATE_BODY_NAME, Interface.COLUMN_2, Interface.SLOT_3),
    STEEL_BULLSEYE_LANTERN(4544, 1, 37.5, 49, BarAmount(Bar.STEEL, 1 ), Interface.TEXT_BULLSEYE_LANTERN_BARS, Interface.TEXT_BULLSEYE_LANTERN_NAME, Interface.COLUMN_2, Interface.SLOT_4),
    //
    MITHRIL_DAGGER(1209, 1, 50.0, 50, BarAmount(Bar.MITHRIL, 1 ), Interface.TEXT_DAGGER_BARS, Interface.TEXT_DAGGER_NAME, Interface.COLUMN_0, Interface.SLOT_0),
    MITHRIL_AXE(1355, 1, 50.0, 51, BarAmount(Bar.MITHRIL, 1 ), Interface.TEXT_AXE_BARS, Interface.TEXT_AXE_NAME, Interface.COLUMN_1, Interface.SLOT_0),
    MITHRIL_MACE(1428, 1, 50.0, 52, BarAmount(Bar.MITHRIL, 1 ), Interface.TEXT_MACE_BARS, Interface.TEXT_MACE_NAME, Interface.COLUMN_1, Interface.SLOT_1),
    MITHRIL_MED_HELM(1143, 1, 50.0, 53, BarAmount(Bar.MITHRIL, 1 ), Interface.TEXT_MED_HELM_BARS, Interface.TEXT_MED_HELM_NAME, Interface.COLUMN_3, Interface.SLOT_0),
    MITHRIL_SWORD(1285, 1, 50.0, 54, BarAmount(Bar.MITHRIL, 1 ), Interface.TEXT_SWORD_BARS, Interface.TEXT_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_1),
    MITHRIL_DART_TIP(809, 10, 50.0, 54, BarAmount(Bar.MITHRIL, 1 ), Interface.TEXT_DART_TIPS_BARS, Interface.TEXT_DART_TIPS_NAME, Interface.COLUMN_4, Interface.SLOT_0),
    MITHRIL_NAILS(4822, 15, 50.0, 54, BarAmount(Bar.MITHRIL, 1 ), Interface.TEXT_NAILS_BARS, Interface.TEXT_NAILS_NAME, Interface.COLUMN_3, Interface.SLOT_4),
    MITHRIL_ARROWTIPS(42, 15, 50.0, 55, BarAmount(Bar.MITHRIL, 1 ), Interface.TEXT_ARROW_TIPS_BARS, Interface.TEXT_ARROW_TIPS_NAME, Interface.COLUMN_4, Interface.SLOT_1),
    MITHRIL_SCIMITAR(1329, 1, 100.0, 55, BarAmount(Bar.MITHRIL, 2 ), Interface.TEXT_SCIMITAR_BARS, Interface.TEXT_SCIMITAR_NAME, Interface.COLUMN_0, Interface.SLOT_2),
    MITHRIL_LONGSWORD(1299, 1, 100.0, 56, BarAmount(Bar.MITHRIL, 2 ), Interface.TEXT_LONG_SWORD_BARS, Interface.TEXT_LONG_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_3),
    MITHRIL_FULL_HELM(1159, 1, 100.0, 57, BarAmount(Bar.MITHRIL, 2 ), Interface.TEXT_FULL_HELM_BARS, Interface.TEXT_FULL_HELM_NAME, Interface.COLUMN_3, Interface.SLOT_1),
    MITHRIL_KNIFE(866, 5, 50.0, 57, BarAmount(Bar.MITHRIL, 1 ), Interface.TEXT_KNIFE_BARS, Interface.TEXT_KNIFE_NAME, Interface.COLUMN_4, Interface.SLOT_2),
    MITHRIL_SQ_SHIELD(1181, 1, 100.0, 58, BarAmount(Bar.MITHRIL, 2 ), Interface.TEXT_SQ_SHIELD_BARS, Interface.TEXT_SQ_SHIELD_NAME, Interface.COLUMN_3, Interface.SLOT_2),
    MITHRIL_WARHAMMER(1343, 1, 150.0, 59, BarAmount(Bar.MITHRIL, 3 ), Interface.TEXT_WAR_HAMMER_BARS, Interface.TEXT_WAR_HAMMER_NAME, Interface.COLUMN_1, Interface.SLOT_2),
    MITHRIL_BATTLEAXE(1369, 1, 150.0, 60, BarAmount(Bar.MITHRIL, 3 ), Interface.TEXT_BATTLE_AXE_BARS, Interface.TEXT_BATTLE_AXE_NAME, Interface.COLUMN_1, Interface.SLOT_3),
    MITHRIL_CHAINBODY(1109, 1, 150.0, 61, BarAmount(Bar.MITHRIL, 3 ), Interface.TEXT_CHAIN_BODY_BARS, Interface.TEXT_CHAIN_BODY_NAME, Interface.COLUMN_2, Interface.SLOT_0),
    MITHRIL_KITESHIELD(1197, 1, 150.0, 62, BarAmount(Bar.MITHRIL, 3 ), Interface.TEXT_KITE_SHIELD_BARS, Interface.TEXT_KITE_SHIELD_NAME, Interface.COLUMN_3, Interface.SLOT_3),
    MITHRIL_CLAWS(3099, 1, 100.0, 63, BarAmount(Bar.MITHRIL, 2 ), Interface.TEXT_CLAWS_BARS, Interface.TEXT_CLAWS_NAME, Interface.COLUMN_1, Interface.SLOT_4),
    MITHRIL_2H_SWORD(1315, 1, 150.0, 64, BarAmount(Bar.MITHRIL, 3 ), Interface.TEXT_2H_SWORD_BARS, Interface.TEXT_2H_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_4),
    MITHRIL_PLATELEGS(1071, 1, 150.0, 66, BarAmount(Bar.MITHRIL, 3 ), Interface.TEXT_PLATE_LEGS_BARS, Interface.TEXT_PLATE_LEGS_NAME, Interface.COLUMN_2, Interface.SLOT_1),
    MITHRIL_PLATESKIRT(1085, 1, 150.0, 66, BarAmount(Bar.MITHRIL, 3 ), Interface.TEXT_PLATE_SKIRT_BARS, Interface.TEXT_PLATE_SKIRT_NAME, Interface.COLUMN_2, Interface.SLOT_2),
    MITHRIL_PLATEBODY(1121, 1, 250.0, 68, BarAmount(Bar.MITHRIL, 5 ), Interface.TEXT_PLATE_BODY_BARS, Interface.TEXT_PLATE_BODY_NAME, Interface.COLUMN_2, Interface.SLOT_3),
    //
    ADAMANT_DAGGER(1211, 1, 62.5, 70, BarAmount(Bar.ADAMANT, 1 ), Interface.TEXT_DAGGER_BARS, Interface.TEXT_DAGGER_NAME, Interface.COLUMN_0, Interface.SLOT_0),
    ADAMANT_AXE(1357, 1, 62.5, 71, BarAmount(Bar.ADAMANT, 1 ), Interface.TEXT_AXE_BARS, Interface.TEXT_AXE_NAME, Interface.COLUMN_1, Interface.SLOT_0),
    ADAMANT_MACE(1430, 1, 62.5, 72, BarAmount(Bar.ADAMANT, 1 ), Interface.TEXT_MACE_BARS, Interface.TEXT_MACE_NAME, Interface.COLUMN_1, Interface.SLOT_1),
    ADAMANT_MED_HELM(1145, 1, 62.5, 73, BarAmount(Bar.ADAMANT, 1 ), Interface.TEXT_MED_HELM_BARS, Interface.TEXT_MED_HELM_NAME, Interface.COLUMN_3, Interface.SLOT_0),
    ADAMANT_SWORD(1287, 1, 62.5, 74, BarAmount(Bar.ADAMANT, 1 ), Interface.TEXT_SWORD_BARS, Interface.TEXT_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_1),
    ADAMANT_DART_TIP(810, 10, 62.5, 74, BarAmount(Bar.ADAMANT, 1 ), Interface.TEXT_DART_TIPS_BARS, Interface.TEXT_DART_TIPS_NAME, Interface.COLUMN_4, Interface.SLOT_0),
    ADAMANT_NAILS(4823, 15, 62.5, 74, BarAmount(Bar.ADAMANT, 1 ), Interface.TEXT_NAILS_BARS, Interface.TEXT_NAILS_NAME, Interface.COLUMN_3, Interface.SLOT_4),
    ADAMANT_ARROWTIPS(43, 15, 62.5, 75, BarAmount(Bar.ADAMANT, 1 ), Interface.TEXT_ARROW_TIPS_BARS, Interface.TEXT_ARROW_TIPS_NAME, Interface.COLUMN_4, Interface.SLOT_1),
    ADAMANT_SCIMITAR(1331, 1, 125.0, 75, BarAmount(Bar.ADAMANT, 2 ), Interface.TEXT_SCIMITAR_BARS, Interface.TEXT_SCIMITAR_NAME, Interface.COLUMN_0, Interface.SLOT_2),
    ADAMANT_LONGSWORD(1301, 1, 125.0, 76, BarAmount(Bar.ADAMANT, 2 ), Interface.TEXT_LONG_SWORD_BARS, Interface.TEXT_LONG_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_3),
    ADAMANT_FULL_HELM(1161, 1, 125.0, 77, BarAmount(Bar.ADAMANT, 2 ), Interface.TEXT_FULL_HELM_BARS, Interface.TEXT_FULL_HELM_NAME, Interface.COLUMN_3, Interface.SLOT_1),
    ADAMANT_KNIFE(867, 5, 62.5, 77, BarAmount(Bar.ADAMANT, 1 ), Interface.TEXT_KNIFE_BARS, Interface.TEXT_KNIFE_NAME, Interface.COLUMN_4, Interface.SLOT_2),
    ADAMANT_SQ_SHIELD(1183, 1, 125.0, 78, BarAmount(Bar.ADAMANT, 2 ), Interface.TEXT_SQ_SHIELD_BARS, Interface.TEXT_SQ_SHIELD_NAME, Interface.COLUMN_3, Interface.SLOT_2),
    ADAMANT_WARHAMMER(1345, 1, 187.5, 79, BarAmount(Bar.ADAMANT, 3 ), Interface.TEXT_WAR_HAMMER_BARS, Interface.TEXT_WAR_HAMMER_NAME, Interface.COLUMN_1, Interface.SLOT_2),
    ADAMANT_BATTLEAXE(1371, 1, 187.5, 80, BarAmount(Bar.ADAMANT, 3 ), Interface.TEXT_BATTLE_AXE_BARS, Interface.TEXT_BATTLE_AXE_NAME, Interface.COLUMN_1, Interface.SLOT_3),
    ADAMANT_CHAINBODY(1111, 1, 187.5, 81, BarAmount(Bar.ADAMANT, 3 ), Interface.TEXT_CHAIN_BODY_BARS, Interface.TEXT_CHAIN_BODY_NAME, Interface.COLUMN_2, Interface.SLOT_0),
    ADAMANT_KITESHIELD(1199, 1, 187.5, 82, BarAmount(Bar.ADAMANT, 3 ), Interface.TEXT_KITE_SHIELD_BARS, Interface.TEXT_KITE_SHIELD_NAME, Interface.COLUMN_3, Interface.SLOT_3),
    ADAMANT_CLAWS(3100, 1, 125.0, 83, BarAmount(Bar.ADAMANT, 2 ), Interface.TEXT_CLAWS_BARS, Interface.TEXT_CLAWS_NAME, Interface.COLUMN_1, Interface.SLOT_4),
    ADAMANT_2H_SWORD(1317, 1, 187.5, 84, BarAmount(Bar.ADAMANT, 3 ), Interface.TEXT_2H_SWORD_BARS, Interface.TEXT_2H_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_4),
    ADAMANT_PLATELEGS(1073, 1, 187.5, 86, BarAmount(Bar.ADAMANT, 3 ), Interface.TEXT_PLATE_LEGS_BARS, Interface.TEXT_PLATE_LEGS_NAME, Interface.COLUMN_2, Interface.SLOT_1),
    ADAMANT_PLATESKIRT(1091, 1, 187.5, 86, BarAmount(Bar.ADAMANT, 3 ), Interface.TEXT_PLATE_SKIRT_BARS, Interface.TEXT_PLATE_SKIRT_NAME, Interface.COLUMN_2, Interface.SLOT_2),
    ADAMANT_PLATEBODY(1123, 1, 312.5, 88, BarAmount(Bar.ADAMANT, 5 ), Interface.TEXT_PLATE_BODY_BARS, Interface.TEXT_PLATE_BODY_NAME, Interface.COLUMN_2, Interface.SLOT_3),
    //
    RUNITE_DAGGER(1213, 1, 75.0, 85, BarAmount(Bar.RUNITE, 1 ), Interface.TEXT_DAGGER_BARS, Interface.TEXT_DAGGER_NAME, Interface.COLUMN_0, Interface.SLOT_0),
    RUNITE_AXE(1359, 1, 75.0, 86, BarAmount(Bar.RUNITE, 1 ), Interface.TEXT_AXE_BARS, Interface.TEXT_AXE_NAME, Interface.COLUMN_1, Interface.SLOT_0),
    RUNITE_MACE(1432, 1, 75.0, 87, BarAmount(Bar.RUNITE, 1 ), Interface.TEXT_MACE_BARS, Interface.TEXT_MACE_NAME, Interface.COLUMN_1, Interface.SLOT_1),
    RUNITE_MED_HELM(1147, 1, 75.0, 88, BarAmount(Bar.RUNITE, 1 ), Interface.TEXT_MED_HELM_BARS, Interface.TEXT_MED_HELM_NAME, Interface.COLUMN_3, Interface.SLOT_0),
    RUNITE_SWORD(1289, 1, 75.0, 89, BarAmount(Bar.RUNITE, 1 ), Interface.TEXT_SWORD_BARS, Interface.TEXT_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_1),
    RUNITE_DART_TIP(824, 10, 75.0, 89, BarAmount(Bar.RUNITE, 1 ), Interface.TEXT_DART_TIPS_BARS, Interface.TEXT_DART_TIPS_NAME, Interface.COLUMN_4, Interface.SLOT_0),
    RUNITE_NAILS(4824, 15, 75.0, 89, BarAmount(Bar.RUNITE, 1 ), Interface.TEXT_NAILS_BARS, Interface.TEXT_NAILS_NAME, Interface.COLUMN_3, Interface.SLOT_4),
    RUNITE_ARROWTIPS(44, 15, 75.0, 90, BarAmount(Bar.RUNITE, 1 ), Interface.TEXT_ARROW_TIPS_BARS, Interface.TEXT_ARROW_TIPS_NAME, Interface.COLUMN_4, Interface.SLOT_1),
    RUNITE_SCIMITAR(1333, 1, 150.0, 90, BarAmount(Bar.RUNITE, 2 ), Interface.TEXT_SCIMITAR_BARS, Interface.TEXT_SCIMITAR_NAME, Interface.COLUMN_0, Interface.SLOT_2),
    RUNITE_LONGSWORD(1303, 1, 150.0, 91, BarAmount(Bar.RUNITE, 2 ), Interface.TEXT_LONG_SWORD_BARS, Interface.TEXT_LONG_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_3),
    RUNITE_FULL_HELM(1163, 1, 150.0, 92, BarAmount(Bar.RUNITE, 2 ), Interface.TEXT_FULL_HELM_BARS, Interface.TEXT_FULL_HELM_NAME, Interface.COLUMN_3, Interface.SLOT_1),
    RUNITE_KNIFE(868, 5, 75.0, 92, BarAmount(Bar.RUNITE, 1 ), Interface.TEXT_KNIFE_BARS, Interface.TEXT_KNIFE_NAME, Interface.COLUMN_4, Interface.SLOT_2),
    RUNITE_SQ_SHIELD(1185, 1, 150.0, 93, BarAmount(Bar.RUNITE, 2 ), Interface.TEXT_SQ_SHIELD_BARS, Interface.TEXT_SQ_SHIELD_NAME, Interface.COLUMN_3, Interface.SLOT_2),
    RUNITE_WARHAMMER(1347, 1, 225.0, 94, BarAmount(Bar.RUNITE, 3 ), Interface.TEXT_WAR_HAMMER_BARS, Interface.TEXT_WAR_HAMMER_NAME, Interface.COLUMN_1, Interface.SLOT_2),
    RUNITE_BATTLEAXE(1373, 1, 225.0, 95, BarAmount(Bar.RUNITE, 3 ), Interface.TEXT_BATTLE_AXE_BARS, Interface.TEXT_BATTLE_AXE_NAME, Interface.COLUMN_1, Interface.SLOT_3),
    RUNITE_CHAINBODY(1113, 1, 225.0, 96, BarAmount(Bar.RUNITE, 3 ), Interface.TEXT_CHAIN_BODY_BARS, Interface.TEXT_CHAIN_BODY_NAME, Interface.COLUMN_2, Interface.SLOT_0),
    RUNITE_KITESHIELD(1201, 1, 225.0, 97, BarAmount(Bar.RUNITE, 3 ), Interface.TEXT_KITE_SHIELD_BARS, Interface.TEXT_KITE_SHIELD_NAME, Interface.COLUMN_3, Interface.SLOT_3),
    RUNITE_CLAWS(3101, 1, 150.0, 98, BarAmount(Bar.RUNITE, 2 ), Interface.TEXT_CLAWS_BARS, Interface.TEXT_CLAWS_NAME, Interface.COLUMN_1, Interface.SLOT_4),
    RUNITE_2H_SWORD(1319, 1, 225.0, 99, BarAmount(Bar.RUNITE, 3 ), Interface.TEXT_2H_SWORD_BARS, Interface.TEXT_2H_SWORD_NAME, Interface.COLUMN_0, Interface.SLOT_4),
    RUNITE_PLATELEGS(1079, 1, 225.0, 99, BarAmount(Bar.RUNITE, 3 ), Interface.TEXT_PLATE_LEGS_BARS, Interface.TEXT_PLATE_LEGS_NAME, Interface.COLUMN_2, Interface.SLOT_1),
    RUNITE_PLATESKIRT(1093, 1, 225.0, 99, BarAmount(Bar.RUNITE, 3 ), Interface.TEXT_PLATE_SKIRT_BARS, Interface.TEXT_PLATE_SKIRT_NAME, Interface.COLUMN_2, Interface.SLOT_2),
    RUNITE_PLATEBODY(1127, 1, 375.0, 99, BarAmount(Bar.RUNITE, 5 ), Interface.TEXT_PLATE_BODY_BARS, Interface.TEXT_PLATE_BODY_NAME, Interface.COLUMN_2, Interface.SLOT_3),
}

fun getSmithingItem(id: Int): SmithingItem? = SmithingItem.values().firstOrNull { it.id == id }

object Interface  {
    const val MODEL_BRONZE = 2405
    const val MODEL_IRON = 2406
    const val MODEL_SILVER = 2407
    const val MODEL_STEEL = 2409
    const val MODEL_GOLD = 2410
    const val MODEL_MITHRIL = 2411
    const val MODEL_ADAMANT = 2412
    const val MODEL_RUNEITE = 2413
    //
    const val WIDGET_FURNACE = 2400
    //
    const val INTERFACE_SMITHING = 994
    const val INVENTORY_CONTAINER = 3823
    //Bars Text in UI
    const val TEXT_PLATE_BODY_BARS = 1112
    const val TEXT_PLATE_LEGS_BARS = 1110
    const val TEXT_PLATE_SKIRT_BARS = 1111
    const val TEXT_2H_SWORD_BARS = 1090;
    const val TEXT_CLAWS_BARS = 8428;
    const val TEXT_KITE_SHIELD_BARS = 1115
    const val TEXT_CHAIN_BODY_BARS = 1109
    const val TEXT_BATTLE_AXE_BARS = 1095
    const val TEXT_WAR_HAMMER_BARS = 1118
    const val TEXT_SQ_SHIELD_BARS = 1114
    const val TEXT_KNIFE_BARS = 1131
    const val TEXT_FULL_HELM_BARS = 1113
    const val TEXT_LONG_SWORD_BARS = 1089
    const val TEXT_SCIMITAR_BARS = 1116
    const val TEXT_ARROW_TIPS_BARS = 1130
    const val TEXT_DART_TIPS_BARS = 1128
    const val TEXT_NAILS_BARS = 13357
    const val TEXT_SWORD_BARS = 1124
    const val TEXT_MED_HELM_BARS = 1127
    const val TEXT_MACE_BARS = 1129
    const val TEXT_AXE_BARS = 1126
    const val TEXT_DAGGER_BARS = 1125
    const val TEXT_OIL_LANTERN_BARS = 11459
    const val TEXT_BULLSEYE_LANTERN_BARS = 11459
    const val TEXT_STUDS_BARS = 1135
    //Optional slot items, text bars text id 1132
    const val TEXT_SPIT_BARS = 1132 //Iron
    const val TEXT_WIRE_BARS = 1132 //Bronze
    const val TEXT_CANNON_BALL_BARS = 1132 //Steel

    //Names Text in UI
    const val TEXT_PLATE_BODY_NAME = 1101
    const val TEXT_PLATE_LEGS_NAME = 1099
    const val TEXT_PLATE_SKIRT_NAME = 1100
    const val TEXT_2H_SWORD_NAME = 1088;
    const val TEXT_CLAWS_NAME = 8429;
    const val TEXT_KITE_SHIELD_NAME = 1105
    const val TEXT_CHAIN_BODY_NAME = 1098
    const val TEXT_BATTLE_AXE_NAME = 1092
    const val TEXT_WAR_HAMMER_NAME = 1083
    const val TEXT_SQ_SHIELD_NAME = 1104
    const val TEXT_KNIFE_NAME = 1106
    const val TEXT_FULL_HELM_NAME = 1103
    const val TEXT_LONG_SWORD_NAME = 1086
    const val TEXT_SCIMITAR_NAME = 1087
    const val TEXT_ARROW_TIPS_NAME = 1108
    const val TEXT_DART_TIPS_NAME = 1107
    const val TEXT_NAILS_NAME = 13358
    const val TEXT_SWORD_NAME = 1085
    const val TEXT_MED_HELM_NAME = 1102
    const val TEXT_MACE_NAME = 1093
    const val TEXT_AXE_NAME = 1091
    const val TEXT_DAGGER_NAME = 1094
    const val TEXT_OIL_LANTERN_NAME = 11461
    const val TEXT_BULLSEYE_LANTERN_NAME = 11461
    const val TEXT_STUDS_NAME = 1134
    //Optional slot items, text name id 1096
    const val TEXT_SPIT_NAME = 1096 //Iron
    const val TEXT_WIRE_NAME = 1096 //Bronze
    const val TEXT_CANNON_BALL_NAME = 1096 //Steel


    //UI column and rows
    const val COLUMN_0 = 1119
    const val COLUMN_1 = 1120
    const val COLUMN_2 = 1121
    const val COLUMN_3 = 1122
    const val COLUMN_4 = 1123
    const val SLOT_0 = 0
    const val SLOT_1 = 1
    const val SLOT_2 = 2
    const val SLOT_3 = 3
    const val SLOT_4 = 4
}

enum class FurnaceSelection(val widget: Int, val bar: Bar) {
    BRONZE(3987, Bar.BRONZE),
    IRON(3991, Bar.IRON),
    SILVER(3995, Bar.SILVER),
    STEEL(3999, Bar.STEEL),
    GOLD(4003, Bar.GOLD),
    MITHRIL(7441, Bar.MITHRIL),
    ADAMANT(7446, Bar.ADAMANT),
    RUNITE(7450, Bar.RUNITE)
}

val playersActive = mutableListOf<Player>()
val playersInvs = mutableListOf<PlayerInvs>()
data class PlayerInvs(val player: Player, val inv0: Inventory, val inv1: Inventory, val inv2: Inventory, val inv3: Inventory, val inv4: Inventory, val pos: Position)

fun findPlayerInvs(player: Player): PlayerInvs? = playersInvs.firstOrNull { it.player == player }

fun amountFromOption(option: Int): Int = when (option) {
    1 -> 1
    2 -> 5
    3 -> 10
    else -> throw IllegalArgumentException("Option must be 1-3")
}

class PlayerInventorySupplier(val column: Int) : ItemVerificationHandler.InventorySupplier {

    override fun getInventory(player: Player): Inventory? {
        val invs = findPlayerInvs(player) ?: return null
        return when (column) {
            Interface.COLUMN_0 -> invs.inv0
            Interface.COLUMN_1 -> invs.inv1
            Interface.COLUMN_2 -> invs.inv2
            Interface.COLUMN_3 -> invs.inv3
            Interface.COLUMN_4 -> invs.inv4
            else -> null
        }

    }

}
