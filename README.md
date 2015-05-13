apollo
======

Apollo is a high-performance, modular RuneScape emulator with a collection of utilities for managing data files and plugins.

### Private server development today

How are most servers private servers developed today? Typically, a user might download a server, read through tutorials, and apply their modifications or write their own code on top of it. Eventually, this server gets released and the process repeats, resulting in a complete mess of servers, all created from cobbled-together code.

### What Apollo does different

At its core, Apollo provides the necessary features for a simple private server to operate. With the plugin system, it becomes easy to add extended functionality, much like a package manager. Instead of copying and pasting lines of code from a tutorial, new features can be implemented by using the package manager. A new feature is as simple as downloading a plugin into the appropriate folder, and configuring it. No need to modify any code -- the feature gets implemented straight away without any hassles. Plugins are currently written in Ruby, but more languages will be added later.

Another aspect of Apollo that is done differently is that the core server is easily upgradeable, only requiring the new jar to replace the old, and restarting the server. This makes the server less vulnerable to fatal mistakes at the core.

Some of the more prominent core features include:

- Packet encoding/decoding has been split from the representations of the packets themselves. This allows the potential for encoding/decoding to go on in parallel and also allows multiple revisions to be supported. Currently 317 and 377 are both completely supported.
-  Update server support (JAGGRAB, ondemand and HTTP).
-  Packet handler chaining: this allows multiple plugins to be able to intercept a single packet and deal with it appropriately. For example, a quest plugin could intercept searching a bookshelf for instance, if the behaviour needed to change in certain cases.
-  Parallel execution of player updating for multi-core machines.


Along with these features comes the bog standard stuff:

- Login
- Multiplayer
- Walking/running
- Rights management
- Travel back algorithm for movement
- Character design
- Full player updating
- Npcs (and full npc updating)
- Commands
- Inventory support
- Equipment support
- Action system
- Working distanced actions
- Task scheduler based on game ticks
- Saving/loading with a custom binary format
- Skill levels/experiences
- Plugin management
- Item, npc and object information reading (from the cache)
