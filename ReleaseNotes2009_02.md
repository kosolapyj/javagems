# Java Gems 2009.02 Release Notes #

This is a maintenance release containing only a few changes. Thanks to all users providing a feedback and helping us to deliver even better building blocks.

## Bugfixes ##

  * A **bug preventing a logging record creator identification** was fixed.
  * A **possible swallowed exception behavior** in `gems.io.IOUtils.read()` was fixed.

## Enhancements ##

  * **Better usage of generics** was applied through codebase. Many interfaces are now more flexible but still type safe and almost all _unchecked assignments_ simply disapeared.
  * As part of bugfixing, a refactoring was performed. The result is that there is a new general-purpose interface named [ExceptionHandler](http://code.google.com/p/javagems/source/browse/trunk/srcs/gems/ExceptionHandler.java). It is useful for **a pluggable exception handling**.
  * **Test coverage was increased**. Actually, 72% of classes and 66% of code lines are covered by unit tests.

## Other changes ##

  * **The method `gems.filtering.FilterChain.isEmpty()` is now marked as _deprecated_.** This method is not a valid part of the class interface and your code should not depend on it. If you think opposite, please let us know about your use case. However, the method will not be removed before 2010.02.