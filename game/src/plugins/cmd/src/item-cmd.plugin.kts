import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.setting.PrivilegeLevel

on_command("item", PrivilegeLevel.ADMINISTRATOR)
        .then { player ->
            if  (!valid_arg_length(arguments, 1..2, player, "Invalid syntax - ::item [id] [amount]")) {
                return@then
            }

            val id = arguments[0].toInt()
            val amount = if (arguments.size == 2) arguments[1].toInt() else 1

            if (id < 0 || id >= ItemDefinition.count()) {
                player.sendMessage("The item id you specified is out of bounds!")
                return@then
            }

            if (amount < 0) {
                player.sendMessage("The amount you specified is out of bounds!")
                return@then
            }

            player.inventory.add(id, amount)
        }