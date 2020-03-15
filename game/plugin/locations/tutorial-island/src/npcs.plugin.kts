package org.apollo.plugin.locations.tutorialIsland

import org.apollo.game.model.Direction
import org.apollo.game.plugin.entity.spawn.spawnNpc

// Functional npcs

// 'Above-ground' npcs

spawnNpc("master_chef", x = 3076, y = 3085)
spawnNpc("quest_guide", x = 3086, y = 3122)
spawnNpc("financial_advisor", x = 3127, y = 3124, facing = Direction.WEST)
spawnNpc("brother_brace", x = 3124, y = 3107, facing = Direction.EAST)
spawnNpc("magic_instructor", x = 3140, y = 3085)

// 'Below-ground' npcs
// Note: They aren't actually on a different plane, they're just in a different location that
// pretends to be underground.

spawnNpc("mining_instructor", x = 3081, y = 9504)
spawnNpc("combat_instructor", x = 3104, y = 9506)

// Non-humanoid npcs

spawnNpc("fishing_spot", id = 316, x = 3102, y = 3093)

spawnNpc("chicken", x = 3140, y = 3095)
spawnNpc("chicken", x = 3140, y = 3093)
spawnNpc("chicken", x = 3138, y = 3092)
spawnNpc("chicken", x = 3137, y = 3094)
spawnNpc("chicken", x = 3138, y = 3095)

// 'Below-ground' npcs
// Note: They aren't actually on a different plane, they're just in a different location that
// pretends to be underground.

spawnNpc("giant_rat", id = 87, x = 3105, y = 9514)
spawnNpc("giant_rat", id = 87, x = 3105, y = 9517)
spawnNpc("giant_rat", id = 87, x = 3106, y = 9514)
spawnNpc("giant_rat", id = 87, x = 3104, y = 9514)
spawnNpc("giant_rat", id = 87, x = 3105, y = 9519)
spawnNpc("giant_rat", id = 87, x = 3109, y = 9516)
spawnNpc("giant_rat", id = 87, x = 3108, y = 9520)
spawnNpc("giant_rat", id = 87, x = 3102, y = 9517)