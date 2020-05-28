This is a short guide to getting a copy of Apollo from our VCS and
running a server able to accept connections from a game client. It
assumes you’re starting fresh and have no local copy of Apollo.

Requirements
============

You should be familiar with running programs on the UNIX shell or
Windows command prompt. There is also a short list of prerequisites
below needed to complete this guide.

-   Git
-   Gradle
-   Java 8
-   RuneScape r377 game data files [1]

Getting Apollo
==============

> **Note**
>
> Apollo is still in a development phase and has no current stable
> release, so to run the server we need to build it from sources first.

The URL for the Apollo git repository is
<https://github.com/apollo-rsps/apollo.git>. You can clone this using
the `git` command-line client or by using the [GitHub desktop
client](https://help.github.com/desktop/guides/contributing-to-projects/cloning-a-repository-from-github-desktop/).

```
> $ git clone <https://github.com/apollo-rsps/apollo.git>
```

If using the command line client, the repository will now be under a
folder named *apollo* and is ready to build. When complete open a shell
or Windows command prompt in that directory and move to the next step.

Building Apollo
===============

Apollo uses Gradle build scripts as it’s build system. To build it,
create a command prompt or shell in the Apollo repository folder and
run:

```
> $ ./gradlew assemble genRsa
```

This will build the core server with the content plugins and run their
respective tests. This process takes around a minute to complete and
when done will generate and output a set of RSA key parameters used by
connecting clients to encrypt their credentials. Save these for later.

Starting Apollo
===============

The last dependency is putting the game data in a location where the
server can find it. By default Apollo looks under `data/fs` in the root
directory for a folder matching the release number. Apollo supports
release 377, so in our case we want the directory structure to look like
this:

```
data/fs
└── 377
    ├── jingle1.mid
    ├── main_file_cache.dat
    ├── main_file_cache.idx0
    ├── main_file_cache.idx1
    ├── main_file_cache.idx2
    ├── main_file_cache.idxN
```

Now that everything is in place we can use the Gradle task to boot the
server.

```
> $ gradle server:run
```

After booting Apollo will have loaded the game data and be ready to accept connections.

[1] We are unable to provide user-end assets like the game data or
client due to copyright restrictions.
