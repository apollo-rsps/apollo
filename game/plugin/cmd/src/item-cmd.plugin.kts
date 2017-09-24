import com.google.common.primitives.Ints
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.plugin.util.command.valid_arg_length

on_command("item", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        val invalidSyntax = "Invalid syntax - ::item [id] [amount]"
        if (!valid_arg_length(arguments, 1..2, player, invalidSyntax)) {
            return@then
        }

        val id = Ints.tryParse(arguments[0])
        if (id == null) {
            player.sendMessage(invalidSyntax)
            return@then
        }

        var amount = 1
        if (arguments.size == 2) {
            val amt = Ints.tryParse(arguments[1])
            if (amt == null) {
                player.sendMessage(invalidSyntax)
                return@then
            }
            amount = amt
        }

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