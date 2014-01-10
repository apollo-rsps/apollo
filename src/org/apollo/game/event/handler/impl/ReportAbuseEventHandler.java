package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ReportAbuseEvent;
import org.apollo.game.model.GameRule;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;
import org.apollo.util.NameUtil;

import java.util.logging.Logger;

/**
 * An {@link EventHandler} for {@link ReportAbuseEvent}s.
 *
 * @author Kyle Stevenson
 */
public class ReportAbuseEventHandler extends EventHandler<ReportAbuseEvent> {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(ReportAbuseEventHandler.class.getName());

	/**
	 * Determines if one player can mute another player based on a {@link ReportAbuseEvent}.
	 *
	 * @param reporter The player submitting the report.
	 * @param reportee The player being reported.
	 * @param event The event in question.
	 * @return true if the current player is a moderator or admin, the reporter and the reportee do not have the same
	 *         {@link Player.PrivilegeLevel} and if this offense can result in muting.
	 */
	private boolean canMute(Player reporter, Player reportee, ReportAbuseEvent event) {
		if (reporter.getPrivilegeLevel() == Player.PrivilegeLevel.STANDARD) {
			return false;
		} else if (reporter.getPrivilegeLevel().toInteger() <= reportee.getPrivilegeLevel().toInteger()) {
			return false;
		} else if (!isMutableOffense(event)) {
			return false;
		}

		return true;
	}

	@Override
	public void handle(EventHandlerContext ctx, Player player, ReportAbuseEvent event) {
		String reporterName = player.getName();
		String reporteeName = NameUtil.decodeBase37(event.getEncodedUsername());
		GameRule ruleBroken = GameRule.valueOf(event.getRuleBroken());
		boolean isMuting = event.isMuting();

		Player reportee = World.getWorld().getPlayer(reporteeName);

		if (reportee == null) {
			player.sendMessage("You cannot report players who do not exist!");
			return;
		} else if (reporterName.equalsIgnoreCase(reporteeName)) {
			player.sendMessage("You cannot report yourself!");
			return;
		}

		// TODO: do something with the reporting data. Maybe the handling should be done by a plugin?

		if (isMuting && canMute(player, reportee, event)) {
			// TODO: implement muting
			logger.info(player + " has just muted " + reportee + " for 48 hours.");
		}

		logger.info(player + " reported " + reportee + " for breaking rule " + ruleBroken);
	}

	/**
	 * Determines if the offense reported is capable of muting someone.
	 *
	 * @param event The event to check for mutable offenses.
	 * @return true if the offense can receive a mute.
	 */
	private boolean isMutableOffense(ReportAbuseEvent event) {
		GameRule ruleBroken = GameRule.valueOf(event.getRuleBroken());

		switch (ruleBroken) {
			case ADVERTISING_WEBSITE:
			case ASKING_FOR_PERSONAL_DETAILS:
			case BUG_ABUSE:
			case ENCOURAGING_OTHERS_TO_BREAK_RULES:
			case ITEM_SCAMMING:
			case JAGEX_STAFF_IMPERSONATION:
			case OFFENSIVE_LANGUAGE:
			case PASSWORD_SCAMMING:
			case REAL_WORLD_ITEM_TRADING:
				return true;
		}

		return false;
	}

}