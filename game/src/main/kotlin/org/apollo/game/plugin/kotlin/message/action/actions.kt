import org.apollo.cache.def.NpcDefinition
import org.apollo.game.message.impl.NpcActionMessage
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.plugin.kotlin.KotlinPluginScript
import org.apollo.game.plugin.kotlin.message.action.npc.NpcAction
import org.apollo.game.plugin.kotlin.message.action.npc.NpcActionPredicateContext
import org.apollo.game.plugin.kotlin.message.action.obj.InteractiveObject
import org.apollo.game.plugin.kotlin.message.action.obj.ObjectAction
import org.apollo.game.plugin.kotlin.message.action.obj.ObjectActionPredicateContext

/**
 * Registers a listener for [ObjectActionMessage]s that occur on any of the given [InteractiveObject]s using the
 * given [option] (case-insensitive).
 *
 * ```
 * on(ObjectAction, option = "Open", objects = DOORS.toList()) {
 *     player.sendMessage("You open the door.")
 * }
 * ```
 */
fun KotlinPluginScript.on(
    listenable: ObjectAction.Companion,
    option: String,
    callback: ObjectAction<*>.() -> Unit
) {
    registerListener(listenable, ObjectActionPredicateContext(option, emptyList()), callback)
}

fun <T : InteractiveObject> KotlinPluginScript.on(
    listenable: ObjectAction.Companion,
    option: String,
    interactives: List<T>,
    callback: ObjectAction<T>.() -> Unit
) {
    val cb = callback as ObjectAction<*>.() -> Unit
    registerListener(listenable, ObjectActionPredicateContext<T>(option, interactives.toList()), cb)
}


fun <T : InteractiveObject> KotlinPluginScript.on(
    listenable: ObjectAction.Companion,
    option: String,
    objects: Array<T>,
    callback: ObjectAction<T>.() -> Unit
) {
    on(listenable, option, objects.toList(), callback)
}

/**
 * Registers a listener for [NpcActionMessage]s that occur on any of the given [NpcDefinition]s using the
 * given [option] (case-insensitive).
 *
 * ```
 * on(NpcAction, option = "Talk-to", npcs = npcs("(Gnome )?Banker") {
 *     ...
 * }
 * ```
 */
fun KotlinPluginScript.on(
    listenable: NpcAction.Companion,
    option: String,
    npcs: Sequence<NpcDefinition>,
    callback: NpcAction.() -> Unit
) {
    registerListener(listenable, NpcActionPredicateContext(option, npcs.toList()), callback)
}