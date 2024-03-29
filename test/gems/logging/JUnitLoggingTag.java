package gems.logging;

import gems.UnexpectedNullException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@code LoggingTag} class.
 *
 * @author <a href="mailto:jozef.babjak@gmail.com">Jozef BABJAK</a>
 */
public final class JUnitLoggingTag {

	/**
	 * A testing logging severity.
	 */
	private static final LoggingSeverity ALERT = LoggingSeverity.ALERT;

	/**
	 * A tested instance.
	 */
	private LoggingTag tag;

	/**
	 * Initializes a tested instance.
	 */
	@Before public void setUp() {
		tag = new LoggingTag(LoggingFacility.NULL_FACILITY, ALERT);
	}

	/**
	 * Checks whether a {@code null} value as a severity is forbidden.
	 */
	@Test(expected = UnexpectedNullException.class) public void nullAsSeverityIsForbidden() {
		new LoggingTag(LoggingFacility.NULL_FACILITY, null);
	}

	/**
	 * Checks whether a {@code null} value as a facility is forbidden.
	 */
	@Test(expected = UnexpectedNullException.class) public void nullAsFacilityIsForbidden() {
		new LoggingTag(null, ALERT);
	}

	/**
	 * Checks if a facility retrieval works.
	 */
	@Test public void gettingFacilityWorks() {
		Assert.assertEquals(LoggingFacility.NULL_FACILITY, tag.getFacility());
	}

	/**
	 * Checks if a severity retrieval works.
	 */
	@Test public void gettingSeverityWorks() {
		Assert.assertEquals(ALERT, tag.getSeverity());
	}

}
