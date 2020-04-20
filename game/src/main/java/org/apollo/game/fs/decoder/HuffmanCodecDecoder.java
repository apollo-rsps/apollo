package org.apollo.game.fs.decoder;

import org.apollo.cache.Cache;
import org.apollo.cache.CacheBuffer;
import org.apollo.cache.def.HuffmanCodec;
import org.apollo.cache.map.MapFile;

/**
 * The type Huffman codec decoder.
 */
public class HuffmanCodecDecoder implements Runnable {

	/**
	 * The {@link Cache}.
	 */
	private final Cache cache;

	/**
	 * Create a new {@link WorldMapDecoder}.
	 *
	 * @param cache The {@link Cache} to load {@link MapFile}s. from.
	 */
	public HuffmanCodecDecoder(Cache cache) {
		this.cache = cache;
	}

	@Override
	public void run() {
		final var fs = cache.getArchive(10);
		final var group = fs.findFolderByName("huffman");
		final var file = group.findRSFileByID(0);

		buildHuffman(file.getData());
	}

	/**
	 * Builds the huffman fields.
	 * @param data the data containing the weights of the huffman.
	 */
	private void buildHuffman(CacheBuffer data) {
		data.setPosition(0);

		var nextCodes = new int[33];
		var codes = new int[data.getRemaining()];
		var tree = new int[8];
		var codeFrequencies = data.getBuffer();

		int largestVistedIndex = 0;
		while (data.getRemaining() > 0) {
			var position = data.getPosition();
			var len = data.readByte();
			if (len == 0) {
				continue;
			}

			var next = 0;
			var lenBit = 1 << 32 - len;
			var code = nextCodes[len];
			codes[position] = code;
			if ((code & lenBit) != 0) {
				next = nextCodes[len - 1];
			} else {
				next = code | lenBit;

				for (var index = len - 1; index >= 1; --index) {
					var nextCode = nextCodes[index];
					if (nextCode != code) {
						break;
					}

					var nextLenBit = 1 << 32 - index;
					if ((nextCode & nextLenBit) != 0) {
						nextCodes[index] = nextCodes[index - 1];
						break;
					}

					nextCodes[index] = nextCode | nextLenBit;
				}
			}

			nextCodes[len] = next;

			for (var index = len + 1; index <= 32; index++) {
				if (code == nextCodes[index]) {
					nextCodes[index] = next;
				}
			}

			var treeIndex = 0;
			for (var shift = 0; shift < len; shift++) {
				var bit = Integer.MIN_VALUE >>> shift;
				if ((code & bit) != 0) {
					if (tree[treeIndex] == 0) {
						tree[treeIndex] = largestVistedIndex;
					}

					treeIndex = tree[treeIndex];
				} else {
					++treeIndex;
				}

				if (treeIndex >= tree.length) {
					var nextTree = new int[tree.length * 2];
					System.arraycopy(tree, 0, nextTree, 0, tree.length);

					tree = nextTree;
				}
			}

			tree[treeIndex] = ~position;
			if (treeIndex >= largestVistedIndex) {
				largestVistedIndex = treeIndex + 1;
			}
		}

		HuffmanCodec.init(codes, codeFrequencies, tree);
	}
}
