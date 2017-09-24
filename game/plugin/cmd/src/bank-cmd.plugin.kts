import org.apollo.game.model.entity.setting.PrivilegeLevel

// Opens the player's bank if they are an administrator.
on_command("bank", PrivilegeLevel.ADMINISTRATOR)
    .then { player -> player.openBank() }