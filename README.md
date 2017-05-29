# Apollo

Apollo is a high-performance, modular RuneScape emulator with a collection of utilities for managing data files and plugins.

### Developer information

Most discussion related to the development of Apollo happens on the IRC channel _#apollorsps_ on _irc.freenode.net_. If you have a problem and can't get in touch with anyone, create a GitHub issue. If making a pull request, please make sure all tests are still passing after making your changes, and that your code style is consistent with the rest of Apollo.

### Getting started

Apollo uses the [gradle build tool](https://gradle.org), so this must be installed in order to build Apollo.

1. Run `gradle build` in the Apollo directory to download dependencies and build Apollo.
2. Place the full cache of the client revision you want to target in `data/fs/[revision]` - by default Apollo is compatible with the 377 revision, so the cache would be placed in `data/fs/377`.
3. Place an `rsa.pem` file containing the RSA private key to be used by the server in `data/`. Apollo comes with a generator that will write the pem file to the appropriate location - see `org.apollo.util.tools.RsaKeyGenerator`. If you use the generator you must replace the public key and modulus used by your client (the tool will print the new values).
4. If you are targeting a different client revision, run the `EquipmentUpdater` (in `org.apollo.cache.tools`), which identifies the appropriate combat skill levels required to wear equipment. Note that you may have to update the tool yourself if the revision contains equipment not found in 377.

Apollo is now ready, and can be started using `gradle run`. You may wish to change the player serializer defined in `login.xml` - by default player data is not saved, and all users who log in will have administrator rights.

### Contributing

Please see [contributing to Apollo](CONTRIBUTING.md).
