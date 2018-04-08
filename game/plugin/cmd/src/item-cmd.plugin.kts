import com.google.common.primitives.Ints
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.setting.PrivilegeLevel

on_command("item", PrivilegeLevel.ADMINISTRATOR)
    .then { player ->
        if (arguments.size !in 1..2) {
            player.sendMessage("Invalid syntax - ::item [id] [amount]")
            return@then
        }

        val id = Ints.tryParse(arguments[0])
        if (id == null) {
            player.sendMessage("Invalid syntax - ::item [id] [amount]")
            return@then
        }

        val amount = if (arguments.size == 2) {
            val amt = Ints.tryParse(arguments[1])

            if (amt == null) {
                player.sendMessage("Invalid syntax - ::item [id] [amount]")
                return@then
            }

            amt
        } else {
            1
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