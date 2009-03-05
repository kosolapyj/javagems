package gems.caching;

import gems.Identifiable;
import gems.Option;

final class FlatCache<V extends Identifiable<K>, K> implements Cache<V, K> {

	private final CacheEvicter<?> evicter;

	private final SizeEstimator<V> sizer;

	private final CacheLimits limits;

	FlatCache(final CacheEvicter<?> evicter, final SizeEstimator<V> sizer, final CacheLimits limits) {
		assert evicter != null;
		assert sizer != null;
		assert limits != null;
		this.evicter = evicter;
		this.sizer = sizer;
		this.limits = new CacheLimits(limits);
	}

	@Override public synchronized void offer(V object) {
		// TODO: IMPLEMENT
	}

	@Override public synchronized Option<V> get(K id) {
		return null; // TODO: IMPLEMENT
	}
}