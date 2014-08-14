package org.apollo.tools;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.decoder.ItemDefinitionDecoder;
import org.apollo.game.model.def.ItemDefinition;

/**
 * A tool for updating the note data.
 * 
 * @author Graham
 */
public final class NoteUpdater {

	/**
	 * The entry point of the application.
	 * 
	 * @param args The command line arguments.
	 * @throws Exception If an error occurs.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException("Usage:\njava -cp ... org.apollo.tools.NoteUpdater [release].");
		}
		String release = args[0];

		try (DataOutputStream os = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("data/note-"
				+ release + ".dat")));
				IndexedFileSystem fs = new IndexedFileSystem(Paths.get("data/fs/", release), true)) {
			ItemDefinitionDecoder decoder = new ItemDefinitionDecoder(fs);
			ItemDefinition[] defs = decoder.decode();
			ItemDefinition.init(defs);

			os.writeShort(defs.length);
			Map<Integer, Integer> itemToNote = new HashMap<>();

			for (int id = 0; id < defs.length; id++) {
				ItemDefinition def = ItemDefinition.lookup(id);
				if (def.isNote()) {
					itemToNote.put(def.getNoteInfoId(), def.getId());
				}
			}

			for (int id = 0; id < defs.length; id++) {
				if (itemToNote.containsKey(id)) {
					os.writeBoolean(true); // notable
					os.writeShort(itemToNote.get(id));
				} else {
					os.writeBoolean(false); // not notable
				}
			}
		}
	}

}