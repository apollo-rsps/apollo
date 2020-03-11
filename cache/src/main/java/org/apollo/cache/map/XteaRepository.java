package org.apollo.cache.map;

import com.google.gson.Gson;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class XteaRepository implements Runnable {

	private static final Gson GSON = new Gson();

	public static class Xtea {
		private final int region;
		private final int[] keys;

		public Xtea(int region, int[] keys) {
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
			var xteas = GSON.fromJson(Files.newBufferedReader(file.toPath()), Xtea[].class);
			for (var xtea : xteas) {
				this.xteas.put(xtea.region, xtea.getKeys());
			}
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	public int[] get(int region) {
		return xteas.getOrDefault(region, null);
	}

	public int[] get(int x, int y) {
		return get(x << 8 | y);
	}

	public Int2ObjectMap.FastEntrySet<int[]> getAll() {
		return xteas.int2ObjectEntrySet();
	}
}
