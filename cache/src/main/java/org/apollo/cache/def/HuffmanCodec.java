package org.apollo.cache.def;

/**
 * @author Khaled Abdeljaber
 */
public final class HuffmanCodec {

	private static HuffmanCodec codec;

	public static void init(int[] codes, byte[] codeLengths, int[] tree) {
		codec = new HuffmanCodec(codes, codeLengths, tree);
	}

	public static String decompress(byte[] compressed) {
		return codec.decompress_(compressed);
	}

	public static byte[] compress(String str) {
		return codec.compress_(str, str.length() + 1);
	}

	/**
	 * The codes of the huffman.
	 */
	private final int[] codes;

	/**
	 * The frequencies of the codes.
	 */
	private final byte[] codeLengths;

	/**
	 * The indicies of the tree.
	 */
	private final int[] tree;

	public HuffmanCodec(int[] codes, byte[] codeLengths, int[] tree) {
		this.codes = codes;
		this.codeLengths = codeLengths;
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
		var uncompressedLength = str.length();
		var dest = new byte[compressedLength];

		var bitPos = 8;
		dest[0] = (byte) uncompressedLength;
		if (uncompressedLength >= 0xFF) {
			bitPos += 8;
			dest[1] = (byte) ((uncompressedLength >> 16) & 0xFF);
		}

		var key = 0;
		for (var i = 0; i < uncompressedLength; i++) {
			var character = lookup(str.charAt(i)) & 0xFF;
			var code = codes[character];
			var length = codeLengths[character];

			var off = bitPos >> 3;
			var remainder = bitPos & 0x7;
			var end = off + (length + remainder - 1 >> 3);

			key &= (-remainder >> 31);
			bitPos += length;

			remainder += 24;
			dest[off] = (byte) (key |= code >>> remainder);

			for (var j = 0; j < 4 && off < end; j++) {
				remainder -= 8;
				if (j == 3) {
					dest[++off] = (byte) (key = code << -remainder);
				} else {
					dest[++off] = (byte) (key = code >>> remainder);
				}
			}
		}

		return dest;
	}

	private static byte lookup(char character) {
		if (character > 0 && character < 128 || character >= 160 && character <= 255) {
			return (byte) character;
		} else if (character == 8364) {
			return -128;
		} else if (character == 8218) {
			return -126;
		} else if (character == 402) {
			return -125;
		} else if (character == 8222) {
			return -124;
		} else if (character == 8230) {
			return -123;
		} else if (character == 8224) {
			return -122;
		} else if (character == 8225) {
			return -121;
		} else if (character == 710) {
			return -120;
		} else if (character == 8240) {
			return -119;
		} else if (character == 352) {
			return -118;
		} else if (character == 8249) {
			return -117;
		} else if (character == 338) {
			return -116;
		} else if (character == 381) {
			return -114;
		} else if (character == 8216) {
			return -111;
		} else if (character == 8217) {
			return -110;
		} else if (character == 8220) {
			return -109;
		} else if (character == 8221) {
			return -108;
		} else if (character == 8226) {
			return -107;
		} else if (character == 8211) {
			return -106;
		} else if (character == 8212) {
			return -105;
		} else if (character == 732) {
			return -104;
		} else if (character == 8482) {
			return -103;
		} else if (character == 353) {
			return -102;
		} else if (character == 8250) {
			return -101;
		} else if (character == 339) {
			return -100;
		} else if (character == 382) {
			return -98;
		} else if (character == 376) {
			return -97;
		} else {
			return 63;
		}
	}
}
