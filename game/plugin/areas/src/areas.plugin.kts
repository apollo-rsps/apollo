
import org.apollo.game.model.entity.Player
import org.apollo.game.model.event.impl.MobPositionUpdateEvent
import org.apollo.game.plugins.area.actions

/**
 * Intercepts the [MobPositionUpdateEvent] and invokes area actions if necessary.
 */
on_event { MobPositionUpdateEvent::class }
    .where { mob is Player }
    .then {
        for ((area, action) in actions) {
            if (mob.position in area) {
                if (next in area) {
                    action.inside(mob as Player, next)
                } else {
                    action.exit(mob as Player, next)
                }
            } else if (next in area) {
                action.entrance(mob as Player, next)
            }
        }
    }
