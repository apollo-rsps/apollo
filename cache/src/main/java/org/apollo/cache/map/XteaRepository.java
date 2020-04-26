package org.apollo.cache.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class XteaRepository implements Runnable {

	private static final int[] DEFAULT = {0, 0, 0, 0};

	public static class Xtea {
		private final int region;
		private final int[] keys;

		public Xtea(@JsonProperty("region") int region, @JsonProperty("keys") int[] keys) {
			this.region = region;
			this.keys = keys;
		}

		public int getRegion() {
			return region;
		}

		public int[] getKeys() {
			return keys;
		}
	}

	private final int release;
	private final Int2ObjectArrayMap<int[]> xteas;

	public XteaRepository(int release) {
		this.xteas = new Int2ObjectArrayMap<>();
		this.release = release;
	}

	@Override
	public void run() {
		File file = new File("./data/fs/" + release + "/xtea.json");
		try {
			var mapper = new ObjectMapper();
			var xteas = mapper.readValue(file, Xtea[].class);
			for (var xtea : xteas) {
				this.xteas.put(xtea.region, xtea.getKeys());
			}
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	public int[] get(int region) {
		return xteas.getOrDefault(region, DEFAULT);
	}

	public int[] get(int x, int y) {
		return get(x << 8 | y);
	}

	public Int2ObjectMap.FastEntrySet<int[]> getAll() {
		return xteas.int2ObjectEntrySet();
	}
}
