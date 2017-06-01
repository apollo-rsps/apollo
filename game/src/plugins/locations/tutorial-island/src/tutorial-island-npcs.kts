import org.apollo.game.model.Direction

// Functional npcs

// 'Above-ground' npcs

npc_spawn("master_chef", x = 3076, y = 3085)
npc_spawn("quest_guide", x = 3086, y = 3122)
npc_spawn("financial_advisor", x = 3127, y = 3124, facing = Direction.WEST)
npc_spawn("brother_brace", x = 3124, y = 3107, facing = Direction.EAST)
npc_spawn("magic_instructor", x = 3140, y = 3085)

// 'Below-ground' npcs
// Note: They aren't actually on a different plane, they're just in a different location that
// pretends to be underground.

npc_spawn("mining_instructor", x = 3081, y = 9504)
npc_spawn("combat_instructor", x = 3104, y = 9506)

// Non-humanoid npcs

npc_spawn("fishing_spot", id = 316, x = 3102, y = 3093)

npc_spawn("chicken", x = 3140, y = 3095)
npc_spawn("chicken", x = 3140, y = 3093)
npc_spawn("chicken", x = 3138, y = 3092)
npc_spawn("chicken", x = 3137, y = 3094)
npc_spawn("chicken", x = 3138, y = 3095)

// 'Below-ground' npcs
// Note: They aren't actually on a different plane, they're just in a different location that
// pretends to be underground.

npc_spawn(":giant_rat", id = 87, x = 3105, y = 9514)
npc_spawn(":giant_rat", id = 87, x = 3105, y = 9517)
npc_spawn(":giant_rat", id = 87, x = 3106, y = 9514)
npc_spawn(":giant_rat", id = 87, x = 3104, y = 9514)
npc_spawn(":giant_rat", id = 87, x = 3105, y = 9519)
npc_spawn(":giant_rat", id = 87, x = 3109, y = 9516)
npc_spawn(":giant_rat", id = 87, x = 3108, y = 9520)
npc_spawn(":giant_rat", id = 87, x = 3102, y = 9517)