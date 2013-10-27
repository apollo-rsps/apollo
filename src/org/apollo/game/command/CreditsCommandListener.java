package org.apollo.game.command;

import java.util.Iterator;

import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;
import org.apollo.game.model.inter.quest.QuestConstants;
import org.apollo.util.plugin.PluginManager;

/**
 * Implements a {@code ::credits} command that lists the authors of all plugins
 * used in the server.
 * @author Graham
 */
public final class CreditsCommandListener implements CommandListener {

	/*
	 * If you are considering removing this command, please bear in mind that
	 * Apollo took several people thousands of hours to create. We released it
	 * to the world for free and it isn't much to ask to leave this command in.
	 * It isn't very obtrusive and gives us some well-deserved recognition for
	 * the work we have done. Thank you!
	 *
	 * The list of authors is generated from the plugin manager. If you create
	 * a custom plugin, make sure you add your name to the plugin.xml file and
	 * it'll appear here automatically!
	 */

	@Override
	public void execute(Player player, Command command) {
		PluginManager mgr = World.getWorld().getPluginManager();
		Iterator<String> it = mgr.createAuthorsIterator();

		int pos = 0;
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], "@dre@Apollo"));
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], "@dre@Introduction"));
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], ""));
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], "This server is based on Apollo, a lightweight, fast, secure"));
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], "and open-source RuneScape emulator. For more"));
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], "information about Apollo, visit the website at:"));
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], "@dbl@https://github.com/apollo-rsps/apollo"));
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], ""));
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], "Apollo is released under the terms of the ISC"));
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], "license, details can be found in the root folder of the "));
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], "Apollo distribution."));
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], ""));
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], "@dre@Credits"));
		player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos++], ""));

		for (; pos < QuestConstants.QUEST_TEXT.length; pos++) {
			String text = it.hasNext() ? it.next() : "";
			player.send(new SetInterfaceTextEvent(QuestConstants.QUEST_TEXT[pos], text));
		}

		player.getInterfaceSet().openWindow(QuestConstants.QUEST_INTERFACE);
	}

}
