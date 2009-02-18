package gems.io.mime;

import gems.Option;
import gems.io.ByteContent;

public final class ContentMimeTypeDetectorSentinelWrapper extends AbstractMimeTypeDetectorSentinelWrapper implements ContentMimeTypeDetector {

	private final ContentMimeTypeDetector detector;

	public ContentMimeTypeDetectorSentinelWrapper(final ContentMimeTypeDetector detector) {
		this(detector, MimeUtils.GLOBAL_DEFAULT_MIME_TYPE);
	}

	public ContentMimeTypeDetectorSentinelWrapper(final ContentMimeTypeDetector detector, final MimeType defaultType) {
		super(defaultType);
		if (detector == null) {
			throw new IllegalArgumentException();
		}
		this.detector = detector;
	}

	// TODO: EXPLAINT IN JavaDoc THAT RETURNED Option IS ALREADY CHECKED.
	@Override public Option<MimeType> detect(final ByteContent content) {
		return ensureDefault(detector.detect(content));
	}
	
}