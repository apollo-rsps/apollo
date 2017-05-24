* [Contributing to Apollo](#contributing-to-apollo)
    * [Getting Apollo](#getting-apollo)
    * [Contributing A Code Change](#contributing-a-code-change)
    * [Style Guide](#style-guide)
    * [Coding Guidelines](#coding-guidelines)
* [Run the Server with the Client](#run-the-server-with-the-client)
* [Question / Problem](#question-problem)

# Contributing to Apollo

We are happy to have contributions whether for small bug fixes or major new
pieces of functionality. There's always something in the issue tracker to
take on.

The best way to help out would be to look at the issue tracker and check
what needs done. Code contributions are welcome but they are not the
only way to make a contribution to Apollo. Quality assurance is a very
important part of this project, as is documentation. Any help offered
with those is greatly appreciated.

## Getting Apollo

Apollo uses GitHub pull requests for reviewing and accepting changes,
and so to contribute a change to Apollo you should first fork the upstream
repository to your own GitHub account.

Once you have a copy of your own forked repository locally you should
add a new remote for `upstream`, so you can rebase any changes
you make and keep up to date with upstream.

```
git remote add upstream https://github.com/apollo-rsps/apollo.git
```

## Contributing A Code Change

* If the change is non-trivial please include some unit tests that cover the new functionality.
* If you are introducing a completely new feature or API it is a good idea to start a discussion on [IRC](#question-problem) and get consensus on the basic design first.
* Make sure you have observed the recommendations in the style guide below.
* It is our job to follow up on patches in a timely fashion. Nag us if we aren't doing our job (sometimes we drop things).

## Style Guide

The Apollo project uses a coding style based on the [Google
style guide](https://google.github.io/styleguide/javaguide.html). Please
use that as a reference for any code changes. Checkstyle rules will be
coming soon.

## Coding Guidelines

Additionally to the recommendations above, see the below guidelines
for code changes:

**Basic Stuff**

* Avoid cryptic abbreviations. Single letter variable names are fine in very short methods with few variables, otherwise make them informative.
* Clear code is preferable to comments. When possible make your naming so good you don't need comments. When that isn't possible comments should be thought of as mandatory, write them to be read.
* Logging, configuration, and public APIs are our "UI". Make them pretty, consistent, and usable.
* Don't be sloppy. Don't check in commented out code: we use version control, it is still there in the history. Don't leave TODOs in the code or FIXMEs if you can help it. Don't leave println statements in the code. Hopefully this is all obvious.
* We want people to use our stuff, which means we need clear, correct documentation. User documentation should be considered a part of any user-facing the feature, just like unit tests or performance results.
* Don't duplicate code (duh).

# Run the Server with the Client

* Download this [archive](http://www.mediafire.com/download/ew288b69ezlz97q/rs377_cache.7z) and extract it into `data/fs/377` (make the directories if they don't exist).
* Download this [archive](http://uppit.com/hkfdlo6tw8b3/377_client.tar.gz) and extract it where you want.
* Run `org.apollo.util.tools.RsaKeyGenerator` and replace the public key and the modulus with the result in `377_client/src/Client.java`.
```
private static final BigInteger RSA_EXPONENT = new BigInteger(public key);
private static final BigInteger RSA_MODULUS = new BigInteger(modulus);
```
* In the directory `377_client`, compile : `javac -sourcepath src src/*.java -d bin`.
* Run `org.apollo.Server`.
* In the directory `377_client`, execute `run.sh`.

# Question / Problem?

If you want to chat or need any assistance with a problem in Apollo feel free
to contact us on the IRC channel at #apollorsps on irc.freenode.net.

*most of the above is loosely borrowed from the great [Apache kafka](http://kafka.apache.org/contributing.html)
project*