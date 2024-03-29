package gems.io;

import gems.Bounds;

/**
 * Objects implementing this interface encapsulates raw byte content.
 * This interface and its implementations are designed to allow aggregating
 * byte contents or using a subcontent of the byte content without necessity
 * to copy the byte content itself back and forth. Please note that there
 * are many ways how to implement a behavior specified by this interface.
 * Consult documentation of a particular implementation when in doubts.
 *
 * @author <a href="mailto:jozef.babjak@gmail.com">Jozef BABJAK</a>
 */
public interface ByteContent {

	/**
	 * An empty content. It is immutable, i.e. the implementation does not
	 * support {@code append(ByteContent)} and {@code prepend(ByteContent)}
	 * operations.
	 */
	ByteContent EMPTY_CONTENT = new AtomicByteContent(new byte[0]);

	/**
	 * Returns a length of the stored content in bytes.
	 *
	 * @return a length of the stored content in bytes.
	 */
	int length();

	/**
	 * Returns the stored content as a byte array.
	 *
	 * @return the stored content as a byte array.
	 */
	byte[] getBytes();

	/**
	 * Returns a byte of the content with a given index. The indexing is zero-based.
	 *
	 * @param index an index of the requested byte.
	 *
	 * @return a byte with a given index.
	 */
	byte getByteAt(int index);

	/**
	 * Sets a byte of the content with a given index. The indexing is zero-based.
	 *
	 * @param index an index of the set byte.
	 * @param b a new value of the byte.
	 */
	void setByteAt(int index, byte b);

	/**
	 * Returns a subcontent of the content specified by given bounds.
	 *
	 * @param bounds required bounds of a subcontent.
	 *
	 * @return a subcontent of the content specified by given bounds.
	 */
	ByteContent getSubcontent(Bounds bounds);

}
