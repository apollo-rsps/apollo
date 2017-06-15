import org.apollo.cache.def.ItemDefinition
import org.apollo.cache.def.NpcDefinition
import org.apollo.cache.def.ObjectDefinition
import org.apollo.game.model.entity.setting.PrivilegeLevel

on_command("iteminfo", PrivilegeLevel.ADMINISTRATOR)
        .then { player ->
            if (!valid_arg_length(arguments, 1, player, "Invalid syntax - ::npcinfo [npc id]")) {
                return@then
            }

            val id = arguments[0].toInt()
            val definition = ItemDefinition.lookup(id)
            val members = if (definition.isMembersOnly) "members" else "not members"

            player.sendMessage("Item $id is called ${definition.name}, is $members only, and has a " +
                    "team of ${definition.team}.")
            player.sendMessage("Its description is \"${definition.description}\".")
        }

on_command("npcinfo", PrivilegeLevel.ADMINISTRATOR)
        .then { player ->
            if (!valid_arg_length(arguments, 1, player, "Invalid syntax - ::npcinfo [npc id]")) {
                return@then
            }

            val id = arguments[0].toInt()
            val definition = NpcDefinition.lookup(id)
            val isCombative = if (definition.hasCombatLevel()) "has a combat level of ${definition.combatLevel}" else
                "does not have a combat level"

            player.sendMessage("Npc $id is called ${definition.name} and $isCombative.")
            player.sendMessage("Its description is \"${definition.description}\".")
        }

on_command("objectinfo", PrivilegeLevel.ADMINISTRATOR)
        .then { player ->
            if (!valid_arg_length(arguments, 1, player, "Invalid syntax - ::objectinfo [object id]")) {
                return@then
            }

            val id = arguments[0].toInt()
            val definition = ObjectDefinition.lookup(id)
            player.sendMessage("Object $id is called ${definition.name} and its description is " +
                    "\"${definition.description}\".")
            player.sendMessage("Its width is ${definition.width} and its length is ${definition.length}.")
        }