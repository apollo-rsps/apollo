package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a player reports another player.
 *
 * @author Lmctruck30
 */

public final class ReportAbuseMessage extends Message {

	/**
	 * The reported player username in long format.
	 */
	private final long reported;

	/**
	 * The rule id broken.
	 */
	private final int rule;

	/**
	 * Mute the reported player.
	 */
	private final boolean mute;

	/**
	 * Creates a new report abuse message.
	 *
	 * @param reported The reported name.
	 * @param rule The rule that was broken.
	 * @param mute The mute flag.
	 */
	public ReportAbuseMessage(long reported, int rule, boolean mute) {
		this.reported = reported;
		this.rule = rule;
		this.mute = mute;
	}

	/**
	 * Gets the username of the player being reported.
	 *
	 * @return The reported.
	 */
	public long getReported() {
		return reported;
	}

	/**
	 * Gets the rule that was broken.
	 *
	 * @return The rule.
	 */
	public int getRule() {
		return rule;
	}

	/**
	 * Gets this mute flag.
	 *
	 * @return mute.
	 */
	public boolean getMute() {
		return mute;
	}

}
