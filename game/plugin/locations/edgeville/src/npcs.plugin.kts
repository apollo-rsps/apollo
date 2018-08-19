package org.apollo.plugin.locations.edgeville

import org.apollo.game.model.Direction
import org.apollo.game.plugin.entity.spawn.npc_spawn

// Generic npcs

npc_spawn("man", x = 3095, y = 3508)
npc_spawn("man", x = 3095, y = 3511)
npc_spawn("man", x = 3098, y = 3509)
npc_spawn("man", id = 2, x = 3093, y = 3511)
npc_spawn("man", id = 3, x = 3097, y = 3508)
npc_spawn("man", id = 3, x = 3092, y = 3508)
npc_spawn("man", id = 3, x = 3097, y = 3512)

npc_spawn("guard", x = 3086, y = 3516)
npc_spawn("guard", x = 3094, y = 3518)
npc_spawn("guard", x = 3108, y = 3514)
npc_spawn("guard", x = 3110, y = 3514)
npc_spawn("guard", x = 3113, y = 3514)
npc_spawn("guard", x = 3113, y = 3516)

npc_spawn("sheep", id = 43, x = 3050, y = 3516)
npc_spawn("sheep", id = 43, x = 3051, y = 3514)
npc_spawn("sheep", id = 43, x = 3056, y = 3517)
npc_spawn("ram", id = 3673, x = 3048, y = 3515)

npc_spawn("monk", x = 3044, y = 3491)
npc_spawn("monk", x = 3045, y = 3483)
npc_spawn("monk", x = 3045, y = 3497)
npc_spawn("monk", x = 3050, y = 3490)
npc_spawn("monk", x = 3054, y = 3490)
npc_spawn("monk", x = 3058, y = 3497)

// Functional npcs

npc_spawn("richard", x = 3098, y = 3516)
npc_spawn("doris", x = 3079, y = 3491)
npc_spawn("brother_jered", x = 3045, y = 3488)
npc_spawn("brother_althric", x = 3054, y = 3504)

npc_spawn("abbot_langley", x = 3059, y = 3484)
npc_spawn("oziach", x = 3067, y = 3518, facing = Direction.EAST)

npc_spawn("shop_keeper", id = 528, x = 3079, y = 3509)
npc_spawn("shop_assistant", id = 529, x = 3082, y = 3513)


npc_spawn("banker", x = 3096, y = 3489, facing = Direction.WEST)
npc_spawn("banker", x = 3096, y = 3491, facing = Direction.WEST)
npc_spawn("banker", x = 3096, y = 3492)
npc_spawn("banker", x = 3098, y = 3492)

npc_spawn("mage_of_zamorak", x = 3106, y = 3560)