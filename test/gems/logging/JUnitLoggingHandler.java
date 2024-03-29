package gems.logging;

import gems.UnexpectedNullException;
import org.junit.Test;

/**
 * @author <a href="mailto:jozef.babjak@gmail.com">Jozef BABJAK</a>
 */
public final class JUnitLoggingHandler {

	/**
	 * Checks whether a {@code null} logging record is forbidden by logging handler null-implementation.
	 */
	@Test(expected = UnexpectedNullException.class) public void nullRecordIsForbidden() {
		LoggingHandler.NULL_HANDLER.handle(null);
	}

}
