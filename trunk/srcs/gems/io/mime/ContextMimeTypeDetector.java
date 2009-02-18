package gems.io.mime;

import gems.Option;

/**
 * A detector of MIME type performing an analysis of a context.
 * It highly depends on a particular implementation what does
 * the 'context' mean.
 *
 * @author <a href="mailto:jozef.babjak@gmail.com">Jozef BABJAK</a>
 */
public interface ContextMimeTypeDetector {

	/**
	 * Analyses given context and tries to determine its MIME type.
	 * If it cannot determine a MIME type, returned option may not
	 * contain a value.
	 *
	 * @param context analysed context.
	 * @return a determined MIME type.
	 */
	Option<MimeType> detect(String context);

}
