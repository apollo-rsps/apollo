package org.apollo.util;

import io.netty.buffer.ByteBuf;

public class XteaUtil {


	/**
	 * The golden ratio XTEA uses.
	 */
	private static final int GOLDEN_RATIO = -1640531527;

	/**
	 * The number of rounds XTEA uses.
	 */
	private static final int ROUNDS = 32;

	public static void decipher(ByteBuf buffer, int start, int end, int[] key) {
		if (key.length != 4) throw new IllegalArgumentException();

		int numQuads = (end - start) / 8;
		for (int i = 0; i < numQuads; i++) {
			int sum = GOLDEN_RATIO * ROUNDS;
			int v0 = buffer.getInt(start + i * 8);
			int v1 = buffer.getInt(start + i * 8 + 4);
			for (int j = 0; j < ROUNDS; j++) {
				v1 -= (((v0 << 4) ^ (v0 >>> 5)) + v0) ^ (sum + key[(sum >>> 11) & 3]);
				sum -= GOLDEN_RATIO;
				v0 -= (((v1 << 4) ^ (v1 >>> 5)) + v1) ^ (sum + key[sum & 3]);
			}
			buffer.setInt(start + i * 8, v0);
			buffer.setInt(start + i * 8 + 4, v1);
		}
	}

	public static void encypher(ByteBuf buffer, int start, int end, int[] key) {
		if (key.length != 4) throw new IllegalArgumentException();

		int numQuads = (end - start) / 8;
		for (int i = 0; i < numQuads; i++) {
			int sum = 0;
			int v0 = buffer.getInt(start + i * 8);
			int v1 = buffer.getInt(start + i * 8 + 4);
			for (int j = 0; j < ROUNDS; j++) {
				v0 += (((v1 << 4) ^ (v1 >>> 5)) + v1) ^ (sum + key[sum & 3]);
				sum += GOLDEN_RATIO;
				v1 += (((v0 << 4) ^ (v0 >>> 5)) + v0) ^ (sum + key[(sum >>> 11) & 3]);
			}
			buffer.setInt(start + i * 8, v0);
			buffer.setInt(start + i * 8 + 4, v1);
		}
	}
}
