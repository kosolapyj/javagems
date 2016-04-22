# Java Gems 2009.01 Release Notes #

Exactly two months after the previous release, a new version of Java Gems general-purpose library was released. In the very short history of this project, this release is the closest to our original goal to provide small tools, which are re-implemented again and again in many projects, only because they are too simple for its own library. Besides new features and improvements listed below, we also significantly increased test coverage behind the scene. Measuring by Emma, now we have 68% of classes and 65% of lines covered by unit tests.

[Download the latest version.](http://code.google.com/p/javagems/downloads/list)

## New Features ##

### Option ###

Using `gems.Option` wrapper around and arbitrary object return value, you can enforce client code to check presence of the return value. Thus, even if your method _may_ or _may not_ return a result, you can sleep well and be sure that client's code is robust enough to handle an empty return value.

[See the source code.](http://code.google.com/p/javagems/source/browse/trunk/srcs/gems/Option.java)

### Composed Comparator ###

Sometime is necessary to sort a collection according to several criteria. Defining one complex comparator leads to complex code, breaks design orthogonality and makes a code reuse harder. Instead of this, you can create several simple comparators and apply them using `gems.ComposedComparator`.

[See the source code.](http://code.google.com/p/javagems/source/browse/trunk/srcs/gems/ComposedComparator.java)

### IO Package ###

How many times did you write the following code:

```
final InputStream out = ...;
try {
    // do something
} catch (final IOException e) {
    // handle somehow
} finally {
    try {
        out.close();
    } catch (final IOException e) {
        throw new RuntimeException(e); // should never happen
    }
}
```

Or even the same without re-throwing a runtime exception (poor guy)? How many times did you check a file existence and reading permissions before actual reading, but you was still enforced to handle `IOException` thrown during the file reading, even if you cannot do anything reasonable about it? Do you also hate copy-pasting the `while` loop reading an input stream into a byte array among each and every project? Do you check `gems.io` package. At the moment, basic ideas are as follows:

  * `java.io.IOException` is hidden into `gems.io.RuntimeIOException`. A client code _may_ catch it, if it want to handle it, but it is _not enforced_ to do so.
  * A raw byte content is encapsulated into an array-like object -- an instance of `gems.io.ByteContent` interface, but there is no need to have a continuous memory for its storage.
  * A sub-content view upon that `gems.io.ByteContent` can be used without necessity to copy bytes.
  * A reading of input stream into `gems.io.ByteContent` is as straightforward as possible.

We are sure that you have _many_ ideas how this package may be extended.

[See the source code.](http://code.google.com/p/javagems/source/browse/trunk/srcs/gems/io/IOUtils.java)

### Mission Impossible? ###

If you have an unreachable code somewhere, explain it very clearly to your colleagues, and ensure that it is really unreachable using `gems.ShouldNeverHappenException`.

[See the source code.](http://code.google.com/p/javagems/source/browse/trunk/srcs/gems/ShouldNeverHappenException.java)

## Changes ##

`gems.logging.Logger` interface does not contain the `getHandlers()` method anymore. In fact, this functionality is not a logger responsibility and the presence of this method in the `Logger` interface only made implementations harder.

[See the source code.](http://code.google.com/p/javagems/source/browse/trunk/srcs/gems/logging/Logger.java)

## Improvements ##

A new implementation of `gems.logging.Logger` interface is provided: `gems.logging.loggers.ParallelLogger`. It is an asynchronous logger passing logging records to each of its handlers in own thread.

[See the source code.](http://code.google.com/p/javagems/source/browse/trunk/srcs/gems/logging/loggers/ParallelLogger.java)