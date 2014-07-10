package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.entity.setting.PrivilegeLevel;

/**
 * An {@link Event} sent to the client that forwards a private message.
 * 
 * @author Major
 */
public final class ForwardPrivateMessageEvent extends Event {

    /**
     * The username of the player sending the message.
     */
    private final String username;

    /**
     * The privilege level of the player.
     */
    private final PrivilegeLevel privilege;

    /**
     * The message.
     */
    private final byte[] message;

    /**
     * Creates a new forward private message event.
     * 
     * @param sender The player sending the message.
     * @param message The compressed message.
     */
    public ForwardPrivateMessageEvent(String username, PrivilegeLevel level, byte[] message) {
	this.username = username;
	this.privilege = level;
	this.message = message;
    }

    /**
     * Gets the username of the sender.
     * 
     * @return The username.
     */
    public String getSenderUsername() {
	return username;
    }

    /**
     * Gets the {@link PrivilegeLevel} of the sender.
     * 
     * @return The privilege level.
     */
    public PrivilegeLevel getSenderPrivilege() {
	return privilege;
    }

    /**
     * Gets the compressed message.
     * 
     * @return The message.
     */
    public byte[] getCompressedMessage() {
	return message;
    }

}