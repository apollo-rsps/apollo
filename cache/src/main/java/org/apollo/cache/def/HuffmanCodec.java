package org.apollo.cache.def;

/**
 * @author Khaled Abdeljaber
 */
public final class HuffmanCodec {

	private static HuffmanCodec codec;

	public static void init(int[] codes, byte[] codeWeights, int[] tree) {
		codec = new HuffmanCodec(codes, codeWeights, tree);
	}

	public static String decompress(byte[] compressed) {
		return codec.decompress_(compressed);
	}

	public static byte[] compress(String str, int length) {
		return codec.compress_(str, length);
	}

	public static byte[] compress(String str) {
		return codec.compress_(str, str.length());
	}

	/**
	 * The codes of the huffman.
	 */
	private final int[] codes;

	/**
	 * The frequencies of the codes.
	 */
	private final byte[] codeWeights;

	/**
	 * The indicies of the tree.
	 */
	private final int[] tree;

	public HuffmanCodec(int[] codes, byte[] codeWeights, int[] tree) {
		this.codes = codes;
		this.codeWeights = codeWeights;
		this.tree = tree;
	}

	/**
	 * Decompress a string.
	 *
	 * @param compressed the compressed string.
	 * @return the decompressed string.
	 */
	String decompress_(byte[] compressed) {
		if (compressed.length == 0) return "";

		var len = compressed[0] & 0xFF;
		var off = 1;
		StringBuilder builder = new StringBuilder();

		var index = 0;
		for (; ; ) {
			var compressedByte = compressed[off++];
			for (var mask = 0x80; mask != 0; mask >>= 1) {
				if ((compressedByte & mask) == 0) {
					index++;
				} else {
					index = tree[index];
				}

				var chr = tree[index];
				if (chr < 0) {
					builder.append((char) ((chr & 0xFF) ^ 0xFF));
					if (builder.length() >= len) return builder.toString();

					index = 0;
				}
			}
		}
	}

	/**
	 * Compress a string.
	 *
	 * @param str the str
	 * @return the length
	 */
	byte[] compress_(String str, int compressedLength) {
		var len = str.length();
		var compressed = new byte[compressedLength + 1];

		var key = 0;
		var bitPos = 8;
		var off = 0;
		compressed[0] = (byte) len;
		for (var i = 0; i < len; i++) {
			var chr = str.charAt(i) & 0xFF;
			var mask = codes[chr];
			var size = codeWeights[chr];

			off = bitPos >> 3;
			var remainder = bitPos & 0x7;
			key &= (-remainder >> 31);
			bitPos += size;
			var end = (((remainder + size) - 1) >> 3) + off;

			remainder += 24;
			compressed[off] = (byte) (key |= (mask >>> remainder));

			for (var j = 0; j < 4 && off < end; j++) {
				remainder -= 8;
				compressed[++off] = (byte) (key = mask >>> remainder);
			}
		}

		return compressed;
	}
}
