# Java Gems #

Java Gems are general purpose utilities for Java. Yes, Java Gems are those simple code snippets copied again and again from one project to another, often from your private project to several work projects, those things too small for own library you cannot find in `java.util` and its subpackages, but you cannot live without them.

## News ##

  * 2009/11/03 - **The version 2009.11 was [released](http://code.google.com/p/javagems/wiki/ReleaseNotes2009_11).**
  * 2009/02/11 - The version 2009.02 was [released](http://code.google.com/p/javagems/wiki/ReleaseNotes2009_02).
  * 2009/01/11 - The version 2009.01 was [released](http://code.google.com/p/javagems/wiki/ReleaseNotes2009_01)
  * 2008/11/11 - The version 2008.11 was released.
  * 2008/08/11 - The version 2008.08 was released. This was the first release ever.

## Mission ##

An usual Java project has a lot of dependencies on 3rd party libraries. Managing them sometimes turns into a nightmare. Tools for dependency management exist, but unfortunately, their black magic sometime causes more problems than it solves. Surprisingly often we can found a 3rd party library with only a single simple feature used in a project. **Java Gems has intention to provide as many of those simple functions as possible in the only one library.**

It is not intended to replace all Java libraries ever made, especially huge stable and widely spread frameworks. **Java Gems is intended to remove common code from them and make them available as well designed, documented and testet building blocks.** Yep, some single-package libraries can be reimplemented or merged. A cycle can appear here: if a significant amount of a library is moved to Java Gems, the library will become simple, and it can be merged to or reimplemented in Java Gems subsequently. In fact, the main goal is to provide Java developers a way how to share tools and utilities without a necessity to reinvent them in each project or when moving to another team or changing a job.

A 3rd party libraries usage in a project can vary a lot. I know about projects which do not use 3r party libraries at all and also I know projects where huge amount of 3rd party libraries management takes a significant portion of developers' working time. Just a rough example: Let's say that J2SE libraries now covers 50% of a project functionality (part A), 30% is covered by _N_ 3rd party libraries (part B) and resting 20% is project own code (part C). Goals of Java Gems are to reduce the number _N_ in the part B and to reduce a portion of the part C.

Many 3rd party libraries are dead now. Unfortunately, have to say. They were implemented by a few passionate people - very often by only one person - for their purpose, they were maintained for a while, but abandoned later for many reasons. Many of them are frozen in Java 1.4 times, lacking all those cool Java 5 features like generics or varargs. **Java Gems want to be a living library for living projects.** We will design our interfaces very carefully and with extensions and changes in mind, but we will not hesitate to make things `@Deprecated` and even remove them later.

## Contribution ##

  1. **Send your feedback** to `gems.for.java@gmail.com`. But, even better...
  1. **... send your code** to `gems.for.java@gmail.com`. Before you do so, check _Contributing Recommendations_ below.

### What will be done with code you submit? ###

  1. **Code will be refused completely.** Very unlikely, if you at least tried to satisfy _Contributing recommendations_ written below.
  1. **Code will be accepted as is.** Very unlikely, even if you tried to satisfy _Contributing recommendations_ written below as much as possible.
  1. **Something in-between.** Very likely. Your code might be refactored, documentation might be improved and design might be changed. Or in the first round, _you_ will be asked to do so, with explanation why it is considered to be important. Maybe only your _idea_ will be adopted and completely new implementation will be provided.

### Contributing Recommendations ###

  1. **Code should be common enough.** Often used features and reausable tools gathering is a main aim of Java Gems project.
  1. **Single class utilities are perfect**; single package utilities might be accepted.
  1. **Dependency** only on Java 6 libraries and current release of Java Gems is allowed.
  1. **Code should be fully portable**, i.e. written in pure Java. No JNI, please.
  1. **Good documentation on all levels.** Explain, why your code should be merged. Explain, what it provides. Explain, how it works.
  1. **Current J2SE code should not be duplicated** even if there is _very_ good reason for that.
  1. **Usage example should be provided.**

Yes, you _may_ violate any of rules above, if you think that there is a reason to do so.
