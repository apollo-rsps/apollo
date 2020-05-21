import org.apollo.game.message.impl.decode.ButtonMessage

val WALK_BUTTON_ID = 152
val RUN_BUTTON_ID = 153

on { ButtonMessage::class }
    .where { componentId == WALK_BUTTON_ID || componentId == RUN_BUTTON_ID }
    .then {
        it.toggleRunning()
    }