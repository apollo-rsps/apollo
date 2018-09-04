package org.apollo.plugin.locations.edgeville

import org.apollo.game.model.Direction
import org.apollo.game.plugin.entity.spawn.spawnNpc

// Generic npcs

spawnNpc("man", x = 3095, y = 3508)
spawnNpc("man", x = 3095, y = 3511)
spawnNpc("man", x = 3098, y = 3509)
spawnNpc("man", id = 2, x = 3093, y = 3511)
spawnNpc("man", id = 3, x = 3097, y = 3508)
spawnNpc("man", id = 3, x = 3092, y = 3508)
spawnNpc("man", id = 3, x = 3097, y = 3512)

spawnNpc("guard", x = 3086, y = 3516)
spawnNpc("guard", x = 3094, y = 3518)
spawnNpc("guard", x = 3108, y = 3514)
spawnNpc("guard", x = 3110, y = 3514)
spawnNpc("guard", x = 3113, y = 3514)
spawnNpc("guard", x = 3113, y = 3516)

spawnNpc("sheep", id = 43, x = 3050, y = 3516)
spawnNpc("sheep", id = 43, x = 3051, y = 3514)
spawnNpc("sheep", id = 43, x = 3056, y = 3517)
spawnNpc("ram", id = 3673, x = 3048, y = 3515)

spawnNpc("monk", x = 3044, y = 3491)
spawnNpc("monk", x = 3045, y = 3483)
spawnNpc("monk", x = 3045, y = 3497)
spawnNpc("monk", x = 3050, y = 3490)
spawnNpc("monk", x = 3054, y = 3490)
spawnNpc("monk", x = 3058, y = 3497)

// Functional npcs

spawnNpc("richard", x = 3098, y = 3516)
spawnNpc("doris", x = 3079, y = 3491)
spawnNpc("brother_jered", x = 3045, y = 3488)
spawnNpc("brother_althric", x = 3054, y = 3504)

spawnNpc("abbot_langley", x = 3059, y = 3484)
spawnNpc("oziach", x = 3067, y = 3518, facing = Direction.EAST)

spawnNpc("shop_keeper", id = 528, x = 3079, y = 3509)
spawnNpc("shop_assistant", id = 529, x = 3082, y = 3513)

spawnNpc("banker", x = 3096, y = 3489, facing = Direction.WEST)
spawnNpc("banker", x = 3096, y = 3491, facing = Direction.WEST)
spawnNpc("banker", x = 3096, y = 3492)
spawnNpc("banker", x = 3098, y = 3492)

spawnNpc("mage_of_zamorak", x = 3106, y = 3560)