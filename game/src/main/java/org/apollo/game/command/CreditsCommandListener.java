package org.apollo.game.command;

import com.google.common.collect.ImmutableSet;
import org.apollo.game.model.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implements a {@code ::credits} command that lists the authors of all plugins used in the server.
 *
 * @author Graham
 */
public final class CreditsCommandListener extends CommandListener {

	/**
	 * The Set of authors.
	 */
	private final Set<String> authors;

	/**
	 * Creates the CreditsCommandListener.
	 *
	 * @param authors The {@link Set} of authors.
	 */
	public CreditsCommandListener(Set<String> authors) {
		this.authors = ImmutableSet.copyOf(authors);
	}

	/*
	 * If you are considering removing this command, please bear in mind that Apollo took several people thousands of
	 * hours to create. We released it to the world for free and it isn't much to ask to leave this command in. It isn't
	 * very obtrusive and gives us some well-deserved recognition for the work we have done. Thank you!
	 *
	 * The list of authors is generated from the plugin manager. If you create a custom plugin, make sure you add your
	 * name to the plugin.xml file and it'll appear here automatically!
	 */

	@Override
	public void execute(Player player, Command command) {
		List<String> text = new ArrayList<>(12 + authors.size());
		text.add("@dre@Apollo");
		text.add("@dre@Introduction");
		text.add("");
		text.add("This server is based on Apollo, a lightweight, fast, secure");
		text.add("and open-source RuneScape emulator. For more");
		text.add("information about Apollo, visit the website at:");
		text.add("@dbl@https://github.com/apollo-rsps/apollo");
		text.add("");
		text.add("Apollo is released under the terms of the ISC license.");
		text.add("");
		text.add("@dre@Credits");
		text.add("");
		text.addAll(authors);

		player.sendQuestInterface(text);
	}

}