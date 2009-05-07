package gems;

import java.util.EnumMap;

public final class StaticLimits<E extends Enum<E>> implements Limits<E> {

	private final EnumMap<E, Number> map;

	public StaticLimits(final Class<E> type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		map = new EnumMap<E, Number>(type);
	}

	public void setLimit(final E e, final Number value) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		if (value == null) {
			throw new IllegalArgumentException();
		}
		map.put(e, value);
	}

	@Override public Number getLimit(final E e) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		if (map.containsKey(e)) {
			return map.get(e);
		}
		return 0;
	}

}