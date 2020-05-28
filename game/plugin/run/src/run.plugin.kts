import org.apollo.game.message.impl.ButtonMessage
import org.apollo.game.plugin.kotlin.message.ButtonClick
import org.apollo.game.plugin.kotlin.message.on

val WALK_BUTTON_ID = 152
val RUN_BUTTON_ID = 153

on(ButtonClick, WALK_BUTTON_ID) { player.toggleRunning() }
on(ButtonClick, RUN_BUTTON_ID) { player.toggleRunning() }
