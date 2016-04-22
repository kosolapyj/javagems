# Java Gems Logging #

This is just another logging framework. It was invented, because its author needed to log one message with different facilities and with different severities for the each of these facilities. For example, the rudimentary requirement was to log "Too many connections" message as "security warning", "performance info" and "general notice".

## The First Touch ##

Basic features of Java Gems Logging library are following:

  * A logging  _facility_ is separated from a logging _severity_. (In fact, what about `java.util.logging.Level.CONFIG`? It is a _severity_, because it is _the_ level, but it seems to be much more like a facility for me.)
  * Each logging severity has a _meaning_ assigned (see javadoc API documentation). So, severities can be used uniformly across the application regardless of a number of developers or teams involved.
  * Each logging _record_ can have several logging facilities assigned and each of them may have a different severity. So, what is a _warning_ from security point of view can be also a _debug_ information from performance point of view.
  * Java Gems `Logger` is an _interface_. Providing different implementations with a different behavior, different demands of client application are supported.

### Coding Example ###

At this moment, you should see [TheFirstTouch.java](http://code.google.com/p/javagems/source/browse/trunk/docs/examples/srcs/logging/TheFirstTouch.java) example.

The output of the example is as follows (except line numbers at the  beginning of lines):

```
01: 2008-09-02 21:06:40.733 <main(1)> {{NOTICE}}	[logging.TheFirstTouch.main(67)]	'null'
02: 2008-09-02 21:06:40.850 <main(1)> {{NOTICE}}	[logging.TheFirstTouch.main(68)]	' Hello, world! '
03: 2008-09-02 21:06:40.852 <main(1)> {{NOTICE}}	[logging.TheFirstTouch.main(69)]	'logging.TheFirstTouch@cdedfd'
04: 2008-09-02 21:06:40.855 <main(1)> {{WARNING}}	[logging.TheFirstTouch.main(74)]	'Something is wrong.'
05: 2008-09-02 21:06:40.857 <main(1)> {{PERFORMANCE:NOTICE}}	[logging.TheFirstTouch.main(81)]	'Load is too high.'
06: 2008-09-02 21:06:40.858 <main(1)> {{SECURITY:NOTICE}}	[logging.TheFirstTouch.main(82)]	'Bad boys are here.'
07: 2008-09-02 21:06:40.860 <main(1)> {{PERFORMANCE:INFO}}	[logging.TheFirstTouch.main(87)]	'Performance information.'
08: 2008-09-02 21:06:40.862 <main(1)> {{PERFORMANCE:INFO}{SECURITY:WARNING}}	[logging.TheFirstTouch.main(94)]	'Too many connections.'
09: 2008-09-02 21:06:40.864 <main(1)> {{FATAL}}	[logging.TheFirstTouch.main(102)]	'java.lang.RuntimeException: Ooops.
10: 	at logging.TheFirstTouch.main(TheFirstTouch.java:102)
11: Caused by: java.lang.IllegalStateException: I'm the cause.
12: 	... 1 more
13: '
```

The basic structure of a logging record is the following:
  1. At the beginning, there is a creation timestamp of the logging record.
  1. In angle brackets, there is a thread identification. It is a thread name and its numeric ID.
  1. In braces, there are _logging tags_, i.e. pairs of logging facility and corresponding severity. If a logging record was created without any facility, so-called "null facility" with an empty name is used, see lines 1-3. A default severity is `NOTICE` and may be changed when an instance of `LoggingEntryPoint` is created.
  1. In square brackets, there is a logging record creator identifications, i.e. a package, class, method and line where the logging record was created.
  1. Finally, there is a logging message itself. Please note that the message is quoted, so you can easily catch whitespaces at the beginning and end of message, see line 2.

The described format is a little bit verbose for simple usage. If this is a case, you can use `gems.logging.formatters.SimpleLoggingRecordFormatter` instead of `gems.logging.formatters.PlainLoggingRecordFormatter`, which is the default one. If you replace a line
```
final LoggingHandler handler = new PrintStreamLoggingHandler(System.out);
```

with

```
final LoggingHandler handler = new PrintStreamLoggingHandler(System.out, new SimpleLoggingRecordFormatter());
```

you will get an output similar to this:

```
2008-09-02 21:46:00 {NOTICE}	'null'
2008-09-02 21:46:00 {NOTICE}	' Hello, world! '
2008-09-02 21:46:00 {NOTICE}	'logging.TheFirstTouch@16cd7d5'
2008-09-02 21:46:00 {WARNING}	'Something is wrong.'
2008-09-02 21:46:00 {NOTICE}	'Load is too high.'
2008-09-02 21:46:00 {NOTICE}	'Bad boys are here.'
2008-09-02 21:46:00 {INFO}	'Performance information.'
2008-09-02 21:46:00 {WARNING}	'Too many connections.'
2008-09-02 21:46:00 {FATAL}	'java.lang.RuntimeException: Ooops.
	at logging.TheFirstTouch.main(TheFirstTouch.java:103)
Caused by: java.lang.IllegalStateException: I'm the cause.
	... 1 more
'
```

In this case, timestamps are formated with seconds precision, there are not any thread and creator information and only a maximal severity is reported instead of complete logging tags.

## Entities ##

### Loggers ###

Basically, loggers are entry points to the logging subsystem. In the Java Gems Logging library, a logger is an interface. There are multiple implementations of this interface and a client may decide, which implementation is more suitable for it. Basically, a logger interface is simple, only two operations are possible:

  1. To add a logging handler to a logger. This is mostly done early during an application initialization, when logging subsystem is configured. By default, a logger has not any logging handlers.
  1. To pass a logging record to a logger. This is used for each logging message during the entire application run. Even if it is possible to pass a logging record directly to logger, this is usually done via _logging entry point_, which can more or less hide a complexity of logging record creation.

Following Logger interface implementations are provided:

  1. **Synchronous logger**: A straightforward implementation of logger. It simply pass a given record to all underalying logging handlers. It operates in a thread of the caller, i.e. caller thread needs to wait until logging operation finishes.
  1. **Asynchronous logger**: Logging, i.e. passing logging records to underlaying handlers is performed asynchronously by a background thread. Logging procedure invoked by the caller is designed to return as soon as possible, so a caller thread is not blocked by logging process even if very slow logging backends are used.
  1. **Parallel logger**: A logging handler processing logging records in a separate thread for each logging handler.

### Handlers ###

Generally speaking, logging handlers are end points of the logging subsystem, as a opposite to loggers. In fact, some logging handler implementations provides another 'technical' functions as buffering, delegating, and so on. Actually, following logging handlers are provided:

  1. **`PrintStreamLoggingHandler`** - A logging handler writing formatted logging records into a print stream. This is the endpoint logging handler making possible to append logging records to a file or print them to the screen.
  1. **`LoggerDelegatingLoggingHander`** - This logging handler delegates all passed logging records to another logger. Doing so, it enables create a tree of hierarchical loggers for more complex applications.
  1. **`BufferingLoggingHandler`** - Buffering wrapper for another handler. It collects logging records in an internal buffer and flushes them into an underlaying handler. Buffered handler may run an internal thread ensuring that a buffer is flushed at least once per specified timeout. If a number of logging records in an internal buffer reaches a specified size limit, a buffer is flushed immediately.
  1. **`ExceptionBarrierLoggingHandler`** - An exception catching barrier for a logging hander. All logging records are passed to the wrapped logging handler. Exceptions thrown by the wrapped handler are caught and catching the exception leads to immediate stopping of the handler. All subsequently logged records are simply ignored. This allows to prevent application shutdown caused by poorly written handlers, as well as passing logging records to a handler unable to process them. If a delay is specified during a wrapper creation, logging will be re-enabled after this delay. Zero delay means that ones stopped handler will be never restarted. Because this effectivelly hides errors occured in a wrapped logging handler, it is possible to handle the stopping event by implementing [ExceptionHandler](http://code.google.com/p/javagems/source/browse/trunk/srcs/gems/ExceptionHandler.java) interface. By default, there is not any stopping event handling.

### Filtering ###

All loggers and end-point logging handlers have a filtering capability. It allows to specify which logging records are processed and which ones are ignored. Several filters based on logging record severity and facility are provided in `gems.logging.filters` package.

### Formatters ###

  1. **`PlainLoggingRecordFormatter`** - Formatting logging records to plain text using all available information from logging record.
  1. **`SimpleLoggingRecordFormatter`** - Formatting logging records to plain text using only basic information from logging record.

### Logging Entry Point ###

A convenient entry point to the logging subsystem. It encapsulates a logger, but provides a facade for creation of logging records and logging tags. It provides simplified ways how to create logging records, especially when logging facility or logging severity is not required.