import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.plugin.api.Definitions

on_command("iteminfo", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        arguments.firstOrNull()
            ?.let(String::toIntOrNull)
            ?.let(Definitions::item)
            ?.apply {
                val members = if (isMembersOnly) "members" else "not members"

                player.sendMessage("Item $id is called $name and is $members only.")
                player.sendMessage("Its description is `$description`.")

                return@then
            }

        player.sendMessage("Invalid syntax - ::iteminfo [item id]")
    }

on_command("npcinfo", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        arguments.firstOrNull()
            ?.let(String::toIntOrNull)
            ?.let(Definitions::npc)
            ?.apply {
                val combat = if (hasCombatLevel()) "has a combat level of $combatLevel" else
                    "does not have a combat level"

                player.sendMessage("Npc $id is called $name and $combat.")
                player.sendMessage("Its description is `$description`.")

                return@then
        }

        player.sendMessage("Invalid syntax - ::npcinfo [npc id]")
    }

on_command("objectinfo", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        arguments.firstOrNull()
            ?.let(String::toIntOrNull)
            ?.let(Definitions::obj)
            ?.apply {
                player.sendMessage("Object ${arguments[0]} is called $name (width=$width, length=$length).")
                player.sendMessage("Its description is `$description`.")

                return@then
            }

        player.sendMessage("Invalid syntax - ::objectinfo [object id]")
    }