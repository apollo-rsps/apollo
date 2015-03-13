package org.apollo.game.message.impl;

import org.apollo.game.message.Message;

/**
 * A {@link Message} sent from a player to report another player
 * 
 * @author Lmctruck30
 */
public final class ReportAbuseMessage extends Message {
	
	private String name;
	
	private byte rule;
	
	private boolean mute;
	
	public ReportAbuseMessage(String name, byte rule, boolean mute) {
		this.name = name;
		this.rule = rule;
		this.mute = mute;
	}
	
	public String getName() {
		return name;
	}
	
	public byte getRule() {
		return rule;
	}
	
	public boolean getMute() {
		return mute;
	}

}
