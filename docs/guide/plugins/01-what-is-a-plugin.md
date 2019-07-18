# What is a plugin?

Plugins in Apollo are a mechanism for extending the server with new functionality without having to change any of the core Java code.
Every plugin is a straight forward Kotlin project with `.plugin.kts` files containing the plugin extension behaviour and optional Kotlin source files containing generic reusable code.

## Plugin framework architecture

Under the hood, the Kotlin compiler transofmrs every `.plugin.kts` file into a class file that extends from @"org.apollo.game.plugin.kotlin.KotlinPluginScript", giving it access to the game @"org.apollo.game.model.World" and @"org.apollo.game.message.handler.MessageHandlerChainSet" the plugin framework is initialized with.