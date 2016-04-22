TODO: RENAME

# Introduction #

  * concise
  * type safe


---


## Example 1 ##

Before:

```
	public CliParserImpl(final String prefix, final String stopword) {
		if (prefix == null) {
			throw new IllegalArgumentException();
		}
		if (stopword == null) {
			throw new IllegalArgumentException();
		}
		this.prefix = prefix;
		this.stopword = stopword;
	}
```

After:

```

	public CliParserImpl(final String prefix, final String stopword) {
		this.prefix = Checks.ensureNotNull(prefix);
		this.stopword = Checks.ensureNotNull(stopword);
	}

```



---


## Example 2 ##

Before:

```
	@Override public CliActuators parse(final String[] args, final CliOptions options) {
		if (args == null) {
			throw new IllegalArgumentException();
		}
		if (options == null) {
			throw new IllegalArgumentException();
		}
		return parseImpl(args, options);
	}
```

After:

```
	@Override public CliActuators parse(final String[] args, final CliOptions options) {
		return parseImpl(Checks.ensureNotNull(args), Checks.ensureNotNull(options));
	}

```


---


## Be cereful about this ##

Before:

```
	public final void log(final LoggingRecord record) {
		if (record == null) {
			throw new IllegalArgumentException();
		}
		if (filter.allows(record)) {
			doLog(record);
		}
	}
```

After:

```
	public final void log(final LoggingRecord record) {
		if (filter.allows(Checks.ensureNotNull(record))) {
			doLog(record);
		}
	}

```

Maybe more clear for someone, even if not so concise:

```
	public final void log(final LoggingRecord record) {
		if (record == null) {
			throw new UnexpectedNullException();
		}
		if (filter.allows(record)) {
			doLog(record);
		}
	}
```

## Don't do this ##

todo: ComposedComparator example or something similar.


---


Inputs checks:

  * Hard check: public and package local interfaces.
  * Assertions: protected methods implementations. Rarely inside private code.
  * Not chech: private code - make it clean without assertions.