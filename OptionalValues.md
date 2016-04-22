# Optional Values #

This short article will show you a simple trick how to significantly improve robustness of client code which use methods returning _optional_ values. A method returning _optional_ value is such method usually returning an object, but sometimes returning ... simply _nothing_. A good example is a method returning object stored in a cache: usually it returns cached object identified by some kind of key, but sometime it has nothing to return, because the object was evicted from cache storage meantime.

## Problem and its ordinary solution ##

In our example, provider is a credit bank and consumer is a client requesting a loan. The business entity _loan_ is represented by a simple immutable class holding an amout of money:

```
public final class Loan {

	private final int amount;

	public Loan(final int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}
	
}
```

Many of sanity checks and javadoc comments are omitted here. See "References" section below to see where to find fully documented and runable code used in this example. In fact, internals of the `Loan` class are not important for this example. What we are interested in is a bank providing loans. Because there can be many of banks providing that service, let's design an interface for the intended interaction. At first, let's try an ordinary approach:

```
/**
 * A credit bank providing loans to clients.
 */
public interface CreditBank {

	/**
	 * Returns a new loan or {@code null} if a request is withheld.
	 *
	 * @param amount requested amount.
	 *
	 * @return  a new loan or {@code null} if a request is withheld.
	 */
	Loan getLoan(int amount);

}
```

Specified contract is as straightforward as possible: client requires a loan of given amount and bank _may_ or _may not_ satisfy it. Implementation might be for instance as follows:

```

private static final int LIMIT = 10000;

@Override public Loan getLoan(final int amount) {
	if (amount <= LIMIT) {
		return new Loan(amount >> 1);
	}
	return null;
}

```

In this example we can ignore that fact why the bank returns only a fragment of requested amount. At the moment, optimistic client code is hurry to say friends that it's ready for a new business:

```
final CreditBank bank = myFriend.getFavouriteBank();
final int amount = myWife.getNecessaryAmount();
final Loan loan = bank.getLoan(amount);
System.out.println("From credit bank I have this amount: " + loan.getAmount());
```

This is very easy and works very well when the loan is agreed. In oposite case, the last line will clearly throw `NullPointerException`. The proper usage needs a bit more careful approach:

```
final Loan loan = bank.getLoan(amount);
if (loan != null) {
	System.out.println("From credit bank I have this amount: " + loan.getAmount());
} else {
	System.out.println("I have nothing.");
}
```

Unfortunately, it is easy to forget to do this sanity check and if it is omitted, the application can run well many times, but it fails later; you know, directly when presented to a customer first time.

## How to enforce robust client code? ##

Anybody designing that sensitive `getLoan()` operation is aware that the case of loan agreement is only one of two possible results. The question is how to enforce client code to be aware of this, too. As shown above, an ordinary design is not sufficient. For this reason, we can design `CreditBank` interface differently:

```
/**
 * A credit bank providing loans to clients.
 */
public interface CreditBank {

	/**
	 * Returns a new loan. Returned option is empty if a request was witheld.
	 *
	 * @param amount requested amount.
	 *
	 * @return  a new loan.
	 */
	Option<Loan> getLoan(int amount);

}
```

The difference is very subtle: the only change is that return value of `getLoan()` method has been changed. Instead of `Loan` business entity, `Option` object is returned. `Option` is implemented as follows:

```
public final class Option<T> {

	private final T value;

	private volatile boolean checked;

	public Option(final T value) {
		this.value = value;
	}

	public boolean hasValue() {
		checked = true;
		return value != null;
	}

	public T getValue() {
		if (!checked) {
			throw new IllegalStateException();
		}
		return value;
	}

}
```

It is a type-safe wrapper for any value of type `T`, including `null`. It provides two methods, one for checking whether a stored value is real object (i.e. it is not `null`), and the second is for getting that value. The key point is that it ensures that value presence is checked before getting value, regardless of the value stored inside.

So, if the client code was lazily written:

```
final Option<Loan> loan = bank.getLoan(amount);
System.out.println("From credit bank I have this amount: " + loan.getValue().getAmount());
```

it throws `IllegalStateException` each time and it can be immediatelly revealed that something is wrong. Robust checking is enforced:

```
final Option<Loan> loan = bank.getLoan(amount);
if (loan.hasValue()) {
	System.out.println("From credit bank I have this amount: " + loan.getValue().getAmount());
} else {
	System.out.println("I have nothing.");
}
```

Just for completeness, a possible implementation of `getLoan()` method using `Option` wrapper around returned `Loan` might be as follows:

```

@Override public Option<Loan> getLoan(final int amount) {
	if (amount <= LIMIT) {
		return new Option<Loan>(new Loan(amount >> 1));
	}
	return new Option<Loan>(null);
}

```

## Best practices ##

Even if this design gem is useful, do not overuse it blindly. Using `Option` wrapper around a return value, all users of that API need to write a little bit more complex conditional code. Very often a better solution exists: empty collection or zero-lenght array can be returned as safe return value. Where an instance of an interface is required, its _null-implementation_ could be the perfect sentinel value. Nota bene, `Option` wrapper is quite good for a return value, but it is only seldom a smart choice for input attributes. However, this can be also reasonable, as you can see in ... (Later we will add a link to an example here when it is committed to repository. If you see this message here for a long time, you should notify us that we forgot it.)

Please note that `Option` is immutable from the stored value point of view, but its internal state is _not_ immutable and it changes when value presence is checked. Remember this when tempted to do (likely premature) performance optimization using wannabe constant for _none_ value:

```

private static final Option<Loan> NO_LOAN = new Option<Loan>(null);

@Override public Option<Loan> getLoan(final int amount) {
	if (amount <= LIMIT) {
		return new Option<Loan>(new Loan(amount >> 1));
	}
	return NO_LOAN;
}

```

Generally speaking, it is not a good idea.

## References ##

  * `Option<T>` wrapper [source code](http://code.google.com/p/javagems/source/browse/trunk/srcs/gems/Option.java)
  * [Runable and documented examples](http://code.google.com/p/javagems/source/browse/#svn/trunk/docs/examples/srcs/option)