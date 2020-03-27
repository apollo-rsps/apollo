import org.apollo.game.message.impl.decode.IfActionMessage

val WALK_BUTTON_ID = 152
val RUN_BUTTON_ID = 153

on { IfActionMessage::class }
    .where { componentId == WALK_BUTTON_ID || componentId == RUN_BUTTON_ID }
    .then {
        it.toggleRunning()
    }