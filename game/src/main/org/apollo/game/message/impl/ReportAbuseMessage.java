package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

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
