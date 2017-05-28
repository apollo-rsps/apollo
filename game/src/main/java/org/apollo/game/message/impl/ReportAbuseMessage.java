package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a player reports another player.
 *
 * @author Lmctruck30
 */
public final class ReportAbuseMessage extends Message {

	/**
	 * The username of the player who was reported.
	 */
	private final String name;

	/**
	 * The id of the rule that was broken.
	 */
	private final int rule; // TODO enum for this?

	/**
	 * Whether or not a mute was requested.
	 */
	private final boolean mute;

	/**
	 * Creates the ReportAbuseMessage.
	 *
	 * @param name The username of the reported player.
	 * @param rule The id of the rule that was broken.
	 * @param mute The mute flag.
	 */
	public ReportAbuseMessage(String name, int rule, boolean mute) {
		this.name = name;
		this.rule = rule;
		this.mute = mute;
	}

	/**
	 * Gets the username of the reported player.
	 *
	 * @return The reported.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the id of rule that was broken.
	 *
	 * @return The rule.
	 */
	public int getRule() {
		return rule;
	}

	/**
	 * Gets whether or not a mute was requested.
	 *
	 * @return {@code true} iff a mute was requested.
	 */
	public boolean getMute() {
		return mute;
	}

}
