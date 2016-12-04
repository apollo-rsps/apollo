package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a player reports another player.
 *
 * @author Lmctruck30
 */

public final class ReportAbuseMessage extends Message {

	private final long reported;

	private final int rule;

	private final boolean mute;

	public ReportAbuseMessage(long reported, int rule, boolean mute) {
		this.reported = reported;
		this.rule = rule;
		this.mute = mute;
	}

	public long getReported() {
		return reported;
	}

	public int getRule() {
		return rule;
	}

	public boolean getMute() {
		return mute;
	}

}
