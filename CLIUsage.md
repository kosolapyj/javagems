# Command Line Interface #

This part of Java Gems project provides a _command line interface_ library, i.e. handling of options typed by user when executing an application from command line. This CLI library has following features:

  * A separation of options _as designed_ by the programmer (aka `CliOption`) and _as typed_ by the user (aka `CliActuator`). This provides clear and unambiguous meaning to objects.
  * Clear definition of command line options performed by a single constructor with two arguments. There are no magic numbers or misleading setters.
  * Every user input _is_ parsed, i.e. no excepions should be thrown by parsing process. So, client code is not forced to control a program flow by exception handling.

The library _does not_ provide any kind of help printing.

## CLI Usage Example ##

### Problem definition ###

Let's say that we need to implement CD burning tool used from command line, pretty similar to `cdrecord` or `burncd`. For a sake of simplicity, suppose that it is going to be use as follows:

```
# java CdBurner [optons] track [track ...]
```

where available options are these:

  * `-verbose` - A switch without any value, turning on verbose output.
  * `-speed #` - An option with an integer value specifying a burning speed.
  * `-filter name` - An option with a string value specifying a track preprocessing filter. It is required to be able to use many filters in the same time.

### Options definition ###

The first what we need to do is to create options, as designed by a programmer and expected by the applicaiton:

```
private static final CliOption OPTION_VERBOSE = new CliOption("verbose", CliOptionType.NONE);
private static final CliOption OPTION_SPEED = new CliOption("speed", CliOptionType.SINGLE);
private static final CliOption OPTION_FILTER = new CliOption("filter", CliOptionType.MULTIPLE);
```

Please note that each option has a different type, see the 2nd constructor parameter. Also take care about options' names: they are written without any dash prefix. In fact, it is up to parser how to recognize command line options, not up to options themselves. Now, we need to group options together for a further processing:

```
private static final CliOptions OPTIONS = new CliOptions();
static {
    OPTIONS.add(OPTION_VERBOSE);
    OPTIONS.add(OPTION_SPEED);
    OPTIONS.add(OPTION_FILTER);
}
```

At this moment, command line interface desing is complete.

### User input parsing ###

Now, let's handle users input:

```
public static void main(final String[] args) {
    final CliActuators actuators = new CliParserImpl("-", "--").parse(args, OPTIONS);
    // ...
}
```

Ok, game over. New CLI parser was created and command line arguments typed by the user were parsed against designed options. There is no need to handle magic parsing exception, hopefully it is possible to parse each user input, regardless how dumb _it_ is. (User or input? Do you choose as you wish.) The parser's constructor has two string arguments:

  * Prefix - specifies a prefix identifying an option typed by the user.
  * Stopword - special option stopping arguments parsing.

### Actuators investigation ###

Ok, let's see what is recognized in different usage scenarios. The following code will do it for `-verbose` switch:

```
if (actuators.getActuatorById(OPTION_VERBOSE.getId()) != null) {
    System.out.println("Using verbose output.");
}

```

The following code will do it for `-speed` option:

```
final CliActuator speed = actuators.getActuatorById(OPTION_SPEED.getId());
if (speed != null) {
    System.out.println("User requires this speed: " + speed.getValue());
} else {
    System.out.println("Using a default speed.");
}
```

The last snippet for the `-filter` multivalue option:

```
final CliActuator filters = actuators.getActuatorById(OPTION_FILTER.getId());
if (filters != null) {
    System.out.print("User requires following filters: ");
    for (final String filter : filters.getValues()) {
        System.out.print("'" + filter + "' ");
    }
    System.out.println();
}
```

It is also important to know, what was _not_ recognized:

```
System.out.print("The resting arguments are: ");
for (final String resting : actuators.getRests()) {
    System.out.print("'" + resting + "' ");
}
System.out.println();
```

A complete source code of this example is a [part](http://code.google.com/p/javagems/source/browse/trunk/docs/examples/srcs/cli/CdBurner.java) of Java Gems.

### Let's burn! ###

At first, try to start program without any arguments:
```
# java CdBurner
Using a default speed.
The resting arguments are:
#
```
Nothing interesting happens: no options nor resting arguments here.

Let's add `-verbose` argument. We use dash prefix, because we designed a parser so:
```
#java CdBurner -verbose
Using verbose output.
Using a default speed.
The resting arguments are: 
#
```
Yes, verbose argument was recognized.

What about speed?
```
#java CdBurner -speed 4
User requires this speed: 4
The resting arguments are: 
#
```
Yes, we caught it.

The `-speed` option is designed to be single-value. What if the user type it twice?
```
#java CdBurner -speed 4 -speed 8
User requires this speed: 8
The resting arguments are: 
#
```
The last occurence wins.

Unfortunately, our CD ripping tool produced a little bit stupid names of track files, their names start with dash, so they seem like options. How is the parser going to deal with these _unrecognizable options_?
```
#java CdBurner -track -track2 -track3
Using a default speed.
The resting arguments are: '-track' '-track2' '-track3' 
#
```
Yeah! Names seem like options, but no such options were designed. So it is easy: they are resting arguments.

Yep, but what if track files' names _are_ exactly as options expected? We can use _stopword_ specified during a parser creation:
```
#java CdBurner -- -speed -speed2 -speed3
Using a default speed.
The resting arguments are: '-speed' '-speed2' '-speed3' 
#
```
A stopword `--` prevents all subsequent arguments to be interpreted as options.

The last feature is track preprocessing. Let's use a normalization preprocessor:
```
#java CdBurner -filter normalization track track2 track3
Using a default speed.
User requires following filters: 'normalization' 
The resting arguments are: 'track' 'track2' 'track3' 
#
```
Yes, the filter is recognized.

But an original requirement was to be able to use more filters:
```
#java CdBurner -filter normalization -filter noisereduction track track2 track3
Using a default speed.
User requires following filters: 'normalization' 'noisereduction' 
The resting arguments are: 'track' 'track2' 'track3' 
#
```
The both filters are here, an order specified on command line is kept.

Putting all together:
```
#java CdBurner -speed 4 -filter normalization -speed 8 -filter noisereduction -verbose -- -speed -speed2 -speed3
Using verbose output.
User requires this speed: 8
User requires following filters: 'normalization' 'noisereduction' 
The resting arguments are: '-speed' '-speed2' '-speed3' 
#
```

## Best practices ##

In complex program with many command line options, not all this stuff will be placed in one class. Maybe it is good idea to dedicate one final utility class defining and holding options. Afterwards `main()` function will be responsible only for arguments parsing and maybe for very basic checks (Are there required resting arguments? Does user require help printing instead of real operation? Etc.). Recognized actuators will be subsequently provided to specialized classes responsible for application launching or subsystems configuration.