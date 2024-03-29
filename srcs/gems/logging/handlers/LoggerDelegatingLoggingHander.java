package gems.logging.handlers;

import gems.Checks;
import gems.UnexpectedNullException;
import gems.filtering.Filter;
import gems.logging.Logger;
import gems.logging.LoggingRecord;

/**
 * This <em>logging handler</em> delegates all passed <em>logging records</em> to
 * another logger. Doing so, it enables create a tree of hierarchical loggers for
 * more complex applications.
 *
 * @author <a href="mailto:jozef.babjak@gmail.com">Jozef BABJAK</a>
 */
public final class LoggerDelegatingLoggingHander extends AbstractFilteringLoggingHandler {

	/**
	 * An underlaying logger.
	 */
	private final Logger logger;

	/**
	 * Creates a new logging handler wrapping a given logger.
	 *
	 * @param logger a logger.
	 *
	 * @throws IllegalArgumentException if {@code logger} is {@code null}.
	 */
	public LoggerDelegatingLoggingHander(final Logger logger) {
		this(logger, Filter.ALLOW_ALL);
	}

	/**
	 * Creates a new filtering logging handler with given filter and logger.
	 *
	 * @param logger a logger.
	 * @param filter a filter.
	 *
	 * @throws UnexpectedNullException if any of arguments is {@code null}.
	 */
	public LoggerDelegatingLoggingHander(final Logger logger, final Filter<? super LoggingRecord> filter) {
		super(filter);
		this.logger = Checks.ensureNotNull(logger);
	}

	/**
	 * Passes a given record to the underlaying logger.
	 *
	 * @param record a logging record.
	 */
	@Override protected void doHandle(final LoggingRecord record) {
		logger.log(Checks.assertNotNull(record));
	}

}
