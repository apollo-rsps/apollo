package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} fired when a player submits the Report Abuse form in-game.
 *
 * @author Kyle Stevenson
 */
public class ReportAbuseEvent extends Event {

	/**
	 * The players name as a long.
	 */
	private final long encodedUsername;

	/**
	 * The rule that's being reported.
	 */
	private final byte ruleBroken;

	/**
	 * Mute for 48 hours - sent as either 1 or 0 for a boolean client-side.
	 */
	private final boolean mute;

	/**
	 * Creates a new instance of the ReportAbuseEvent.
	 *
	 * @param encodedUsername The players name as a long.
	 * @param ruleBroken The rule that's being reported.
	 * @param mute Mute for 48 hours.
	 */
	public ReportAbuseEvent(long encodedUsername, byte ruleBroken, byte mute) {
		this.encodedUsername = encodedUsername;
		this.ruleBroken = ruleBroken;
		this.mute = (mute == 1);
	}

	/**
	 * The players name as a long.
	 *
	 * @return The players name as a long.
	 */
	public long getEncodedUsername() {
		return encodedUsername;
	}

	/**
	 * The rule that's being reported.
	 *
	 * @return The rule that's being reported.
	 */
	public byte getRuleBroken() {
		return ruleBroken;
	}

	/**
	 * Mute for 48 hours - sent as either 1 or 0 for a boolean client-side.
	 *
	 * @return Mute for 48 hours.
	 */
	public boolean isMuting() {
		return mute;
	}

}