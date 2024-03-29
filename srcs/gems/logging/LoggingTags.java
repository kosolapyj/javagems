package gems.logging;

import gems.Checks;
import gems.UnexpectedNullException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * An immutable holder of logging tags. It is created as a part
 * of each logging record and always contains at least one logging
 * tag. It provides several querying methods and it is also possible
 * to iterate over stored logging tags.
 *
 * @author <a href="mailto:jozef.babjak@gmail.com">Jozef BABJAK</a>
 */
public final class LoggingTags implements Iterable<LoggingTag> {

	/**
	 * A maximal severity.
	 */
	private final LoggingSeverity maximalSeverity;

	/**
	 * An internal representation of logging tags as facility-severity mapping.
	 */
	private final Map<LoggingFacility, LoggingSeverity> map = new HashMap<LoggingFacility, LoggingSeverity>();

	/**
	 * A list representation of logging tags published outside.
	 * It provides an ability to iterate over stored logging tags.
	 */
	private final List<LoggingTag> list;


	/**
	 * Creates a new logging tags holder for a given tags. At least one tag should be given.
	 * If there is more than one tag for the same facility, a tag with the highest severity
	 * is kept.
	 *
	 * @param tags logging tags.
	 *
	 * @throws UnexpectedNullException if {@code tags} or any of its elements is {@code null}.
	 */
	LoggingTags(final LoggingTag[] tags) {
		if (Checks.ensureNotNull(tags).length == 0) {
			throw new IllegalArgumentException();
		}
		LoggingSeverity auxMax = null;
		for (final LoggingTag tag : tags) {
			if (tag == null) {
				throw new UnexpectedNullException();
			}
			putConditionally(tag.getFacility(), tag.getSeverity());
			if (auxMax == null || tag.getSeverity().compareTo(auxMax) > 0) {
				auxMax = tag.getSeverity();
			}

		}
		maximalSeverity = auxMax;
		list = Collections.unmodifiableList(initList());
	}

	/**
	 * Creates a list representation of logging tags from internal facility-severity mapping.
	 *
	 * @return a list representation of logging tags from internal facility-severity mapping.
	 */
	private List<LoggingTag> initList() {
		final List<LoggingTag> result = new LinkedList<LoggingTag>();
		for (final LoggingFacility facility : map.keySet()) {
			result.add(new LoggingTag(facility, map.get(facility)));
		}
		return result;
	}

	/**
	 * Adds a given facility-severity mapping into the internal map
	 * if there is no mapping for a given facility, or if current
	 * mapping has lower severity as a given severity is.
	 *
	 * @param facility a facility.
	 * @param severity a severity.
	 */
	private void putConditionally(final LoggingFacility facility, final LoggingSeverity severity) {
		final LoggingSeverity known = map.get(facility);
		if (known == null) {
			map.put(facility, severity);
		} else {
			if (severity.compareTo(known) > 0) {
				map.put(facility, severity);
			}
		}
	}

	/**
	 * Returns a severity for a given facility, or {@code null} if no logging tag with a given facility was found.
	 *
	 * @param facility a checked facility.
	 *
	 * @return a severity for a given facility, or {@code null} if no logging tag with a given facility was found.
	 *
	 * @throws UnexpectedNullException if {@code facility} is {@code null}.
	 */
	public LoggingSeverity getSeverity(final LoggingFacility facility) {
		return map.get(Checks.ensureNotNull(facility));
	}

	/**
	 * Returns a maximal severity of tags. This method never returns {@code null}.
	 *
	 * @return a maximal severity of tags.
	 */
	public LoggingSeverity getMaximalSeverity() {
		return maximalSeverity;
	}

	/**
	 * Checks whether tags contains a tag for a given facility.
	 *
	 * @param facility a checked facility.
	 *
	 * @return {@code true} if tags contain a tag for a given facility, {@code false} otherwise.
	 *
	 * @throws UnexpectedNullException if {@code facility} is {@code null}.
	 */
	public boolean hasFacility(final LoggingFacility facility) {
		return map.containsKey(Checks.ensureNotNull(facility));
	}

	/**
	 * Checks whether tags' maximal severity is at least at a given level.
	 *
	 * @param level a checked level.
	 *
	 * @return {@code true} if tags' maximal severity is equal to or greater than a given level,
	 *         {@code false} otherwise.
	 *
	 * @throws UnexpectedNullException if {@code level} is {@code null}.
	 */
	public boolean isSevere(final LoggingSeverity level) {
		return maximalSeverity.compareTo(Checks.ensureNotNull(level)) >= 0;
	}

	/**
	 * Checks whether tags' severity for a given facility is at least at the given level.
	 * It implies that a {@code false} value is returned if tags do not contain any tag
	 * for a given facility.
	 *
	 * @param level a checked level.
	 * @param facility a checked facility.
	 *
	 * @return {@code true} if tags contain a tag for a given facility with a severity equal to or
	 *         greater than a given level, {@code false} otherwise.
	 *
	 * @throws UnexpectedNullException if any of arguments is {@code null}.
	 */
	public boolean isSevere(final LoggingSeverity level, final LoggingFacility facility) {
		if (level == null) {
			throw new UnexpectedNullException();
		}
		if (facility == null) {
			throw new UnexpectedNullException();
		}
		final LoggingSeverity found = map.get(facility);
		return found != null && found.compareTo(level) >= 0;
	}

	/**
	 * Checks whether tags contain at least one tag with a given severity.
	 *
	 * @param severity a checked severity.
	 *
	 * @return {@code true} if tags contain at least one tag with a given severity, {@code false} otherwise.
	 *
	 * @throws UnexpectedNullException if {@code severity} is {@code null}.
	 */
	public boolean hasSeverity(final LoggingSeverity severity) {
		if (severity == null) {
			throw new UnexpectedNullException();
		}
		return severity.equals(maximalSeverity) || map.values().contains(severity);
	}

	/**
	 * Returns an immutable iterator over stored logging tags. This method never returns {@code null}.
	 *
	 * @return an iterator over stored logging tags.
	 */
	public Iterator<LoggingTag> iterator() {
		return list.iterator();
	}

}
