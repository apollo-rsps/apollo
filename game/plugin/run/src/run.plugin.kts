import org.apollo.game.message.impl.ButtonMessage

val WALK_BUTTON_ID = 152
val RUN_BUTTON_ID = 153

on { ButtonMessage::class }
    .where { widgetId == WALK_BUTTON_ID || widgetId == RUN_BUTTON_ID }
    .then {
        it.toggleRunning()
    }