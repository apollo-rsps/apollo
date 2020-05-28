import org.apollo.game.message.impl.InventoryItemMessage

on { InventoryItemMessage::class }
        .where { option == 5 }
        .then {
            // This is just a stub, for now.
            // Several other things need to be done here:
            // - Items may only be dropped from your inventory
            // - Items dropped must be removed from your inventory
            // - Items must be checked to ensure they have a 'drop' option
            val item = it.inventory.get(slot)
            it.addGroundItem(item, it.position)
        }