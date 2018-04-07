import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.plugin.skills.mining.Ore

on { ObjectActionMessage::class }
    .where { option == Actions.MINING }
    .then { player ->
        Ore.fromRock(id)?.let { ore ->
            MiningAction.start(this, player, ore)
        }
    }

on { ObjectActionMessage::class }
    .where { option == Actions.PROSPECTING }
    .then { player ->
        val ore = Ore.fromRock(id)

        if (ore != null) {
            ProspectingAction.start(this, player, ore)
        } else if (Ore.fromExpiredRock(id) != null) {
            ExpiredProspectingAction.start(this, player)
        }
    }

private object Actions {
    const val MINING = 1
    const val PROSPECTING = 2
}