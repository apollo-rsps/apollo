# Creating a plugin

Apollo's plugins are written in [Kotlin](http://kotlinlang.org) and are
primarily for content, not core code (if you aren't sure where your code
should go, ask in irc). Note that this tutorial assumes some familiarity
with Kotlin, although good Java knowledge will probably be enough.

Note: This tutorial is for developing Plugins for the kotlin-experiments
branch prior to the release of the Kotlin plugin system for Apollo.

## Create a working environment



## Create the plugin metadata

Apollo's plugins are stored in */game/plugin*, and each plugin has its
own directory. Create one for your plugin - something like 'myplugin'.

Inside that, create a directory called 'src', then right click it and
'Mark Directory as > Sources Root'. It should turn blue. +
Inside your plugin's directory (*not* in src) you'll want a
*build.gradle* file, containing something like:

```groovy
plugin {
    name = "myplugin"
    authors = [ "your name" ]
}
```


Sometimes you need to use code from another plugin, which can be done
like so: `dependencies = [ "util:lookup" ]`

This imports the `lookup` plugin from `util`.

## Write the plugin

Plugins are written in kotlin script (_.kts_), which is then transpiled
into Java (bytecode) at compile time. Kotlin script is designed to be
executed like a scripting language: you do *not* need a main function (a
kotlin script consisting of nothing more than `println("Hello, world!")`
will indeed compile and print "Hello, world!").

Apollo uses the _.plugin.kts_ extension to mark files as plugin scripts.

Add a file named 'myplugin.plugin.kts' inside `src` and add the
following code:

```kotlin
import org.apollo.game.action.Action
import org.apollo.game.message.impl.InventoryItemMessage
import org.apollo.game.model.Item
import org.apollo.game.model.entity.Entity
import org.apollo.game.model.entity.EntityType
import org.apollo.game.model.entity.GroundItem
import org.apollo.game.model.entity.Player

class DropItemAction(val player: Player, val slot: Int): Action<Player>(delay = 0, immediate = true, player) {

    override fun execute() {
        val region = player.world.regionRepository.fromPosition(player.position)

        if (region.getEntities<Entity>(player.position, EntityType.DYNAMIC_OBJECT, EntityType.STATIC_OBJECT).isEmpty()) {
            val amount = player.inventory.reset(slot)?.amount
            if (amount == null) {
                return
            }

            val item = GroundItem.create(player.world, player.position, Item(item, amount), player)
            player.world.spawn(item)
        } else {
            player.sendMessage("You cannot drop this here.")
        }

        stop()
    }

}

val DROP_OPTION_ID = 5
val INVENTORY_INTERFACE_ID = 3214

on { InventoryItemMessage::class }
    .where { option == DROP_OPTION_ID && interfaceId == INVENTORY_INTERFACE_ID }
    .then { player ->
        player.startAction(DropItemAction(player, slot))
        terminate()
    }
```

Here we have an *action*, and a *listener*, the two core features of
plugins.

The `on {...}` lambda at the end is the listener, and listens for
specific event types (typically a *Message* subclass) Here we are
listening to `InventoryItemMessage`s, which are called whenever a player
performs an action on an item in an inventory. +
The `where` clause is used to filter out requests that don't match what
we're looking for: here, we only care about messages where the player
selected the fifth option (used for dropping), and when they selected
that option on an item in the inventory (i.e. the interfaceId matches
the inventory interface id). `where` is executed from the context of the
intercepted message, so `option` and `interfaceId` are actually fields
inside `InventoryItemMessage`. +
The 'then' clause is executed if the `where` lambda evaluates to `true`.

*Actions* are used to schedule player (or NPC)-related code to be
executed in the future (and optionally, periodically). Plugins also have
actions that can be used to suspend/asynchronously execute code. Here
we're creating an `Action` that removes an item from the player's
inventory and spawns a `GroundItem`. Because `Action`s are scheduled,
`execute()` will be called every server pulse (tick), until the `stop()`
function is called.

Now you can build it by running `gradle build` in the command line, or
in IntelliJ via 'View > Tool Windows > Gradle > Execute Gradle Task'
(type 'build' for the command).

Voila!
