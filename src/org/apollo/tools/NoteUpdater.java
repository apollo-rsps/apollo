package org.apollo.tools;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.parser.ItemDefinitionParser;
import org.apollo.game.model.def.ItemDefinition;

/**
 * A tool for updating the note data.
 * @author Graham
 */
public final class NoteUpdater {

	/**
	 * The entry point of the application.
	 * @param args The command line arguments.
	 * @throws Exception if an error occurs.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage:");
			System.err.println("  java -cp ... org.apollo.tools.NoteUpdater [release]");
			return;
		}
		String release = args[0];

		DataOutputStream os = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("data/note-" + release + ".dat")));
		try {
			IndexedFileSystem fs = new IndexedFileSystem(new File("data/fs/" + release), true);
			try {
				ItemDefinitionParser parser = new ItemDefinitionParser(fs);
				ItemDefinition[] defs = parser.parse();
				ItemDefinition.init(defs);

				os.writeShort(defs.length);

				Map<Integer, Integer> itemToNote = new HashMap<Integer, Integer>();

				for (int id = 0; id < defs.length; id++) {
					ItemDefinition def = ItemDefinition.forId(id);
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
			} finally {
				fs.close();
			}
		} finally {
			os.close();
		}
	}

}
