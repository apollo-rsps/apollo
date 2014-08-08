package org.apollo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

/**
 * A utility class for performing compression/decompression.
 * 
 * @author Graham
 */
public final class CompressionUtil {

	/**
	 * Bzip2s the specified array.
	 * 
	 * @param bytes The uncompressed array.
	 * @return The compressed array.
	 * @throws IOException If an I/O error occurs.
	 */
	public static byte[] bzip2(byte[] bytes) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try (BZip2CompressorOutputStream os = new BZip2CompressorOutputStream(bout, 1)) {
			os.write(bytes);
			os.finish();
			byte[] compressed = bout.toByteArray();
			byte[] newCompressed = new byte[compressed.length - 4];
			System.arraycopy(compressed, 4, newCompressed, 0, newCompressed.length);
			return newCompressed;
		}
	}

	/**
	 * Gzips the specified array.
	 * 
	 * @param bytes The uncompressed array.
	 * @return The compressed array.
	 * @throws IOException If an I/O error occurs.
	 */
	public static byte[] gzip(byte[] bytes) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try (DeflaterOutputStream os = new GZIPOutputStream(bout)) {
			os.write(bytes);
			os.finish();
			return bout.toByteArray();
		}
	}

	/**
	 * Unbzip2s the compressed array and places the result into the decompressed array.
	 * 
	 * @param compressed The compressed array.
	 * @param uncompressed The decompressed array.
	 * @throws IOException If an I/O error occurs.
	 */
	public static void unbzip2(byte[] compressed, byte[] uncompressed) throws IOException {
		byte[] newCompressed = new byte[compressed.length + 4];
		newCompressed[0] = 'B';
		newCompressed[1] = 'Z';
		newCompressed[2] = 'h';
		newCompressed[3] = '1';
		System.arraycopy(compressed, 0, newCompressed, 4, compressed.length);

		try (DataInputStream is = new DataInputStream(new BZip2CompressorInputStream(new ByteArrayInputStream(
				newCompressed)))) {
			is.readFully(uncompressed);
		}
	}

	/**
	 * Ungzips the compressed array and places the results into the decompressed array.
	 * 
	 * @param compressed The compressed array.
	 * @param uncompressed The decompressed array.
	 * @throws IOException If an I/O error occurs.
	 */
	public static void ungzip(byte[] compressed, byte[] uncompressed) throws IOException {
		try (DataInputStream is = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(compressed)))) {
			is.readFully(uncompressed);
		}
	}

	/**
	 * Ungzips the compressed buffer and places the results into the returning array.
	 * 
	 * @param compressed The compressed buffer.
	 * @return The decompressed array.
	 * @throws IOException If an I/O error occurs.
	 */
	public static byte[] ungzip(ByteBuffer compressed) throws IOException {
		byte[] data = new byte[compressed.remaining()];
		compressed.get(data);

		try (InputStream is = new GZIPInputStream(new ByteArrayInputStream(data));
				ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			while (true) {
				byte[] buf = new byte[1024];
				int read = is.read(buf, 0, buf.length);
				if (read == -1) {
					break;
				}
				os.write(buf, 0, read);
			}

			return os.toByteArray();
		}
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private CompressionUtil() {

	}

}