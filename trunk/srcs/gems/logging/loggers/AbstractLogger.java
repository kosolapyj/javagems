package gems.logging.loggers;

import gems.logging.Logger;
import gems.logging.LoggingHandler;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A skeleton implementation of <em>logger</em>. It ensures the common functionality
 * of adding and providing underlaying <em>logging handlers</em>. A real handling of
 * <em>logging records</em> is still delegated to subclasses. It also adds an ability
 * to filter logging records.
 *
 * @author <a href="mailto:jozef.babjak@gmail.com">Jozef BABJAK</a>
 */
abstract class AbstractLogger implements Logger {

	/**
	 * Handlers. Unmodifiable defense copy is hold here, so it is safe to provide it outside.
	 */
	private volatile List<LoggingHandler> handlers;

	protected AbstractLogger() {
		handlers = Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException if {@code handler} is {@code null}.
	 */
	public final synchronized void addHandler(final LoggingHandler handler) {
		if (handler == null) {
			throw new IllegalArgumentException();
		}
		final List<LoggingHandler> modifiable = new LinkedList<LoggingHandler>(handlers);
		modifiable.add(handler);
		handlers = Collections.unmodifiableList(modifiable);
	}

	/**
	 * {@inheritDoc} Returned list is unmodifiable. This method never returns {@code null};
	 * at least an empty list returned, even if no handlers were added to the logger.
	 *
	 * @return handlers of the logger.
	 */
	public final List<LoggingHandler> getHandlers() {
		return handlers;
	}

}