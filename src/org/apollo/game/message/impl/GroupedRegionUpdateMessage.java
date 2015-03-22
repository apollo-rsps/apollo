package org.apollo.game.message.impl;

import java.util.List;

import org.apollo.game.message.Message;
import org.apollo.game.model.Position;
import org.apollo.game.model.area.RegionCoordinates;

/**
 * A {@link Message} sent to the client that contains multiple
 *
 * @author Major
 */
public final class GroupedRegionUpdateMessage extends Message {

	/**
	 * The Position of the Player.
	 */
	private final Position player;

	/**
	 * The Position of the Region being updated.
	 */
	private final Position region;

	/**
	 * The List of RegionUpdateMessages to be sent.
	 */
	private final List<RegionUpdateMessage> messages;

	/**
	 * Creates the GroupedRegionUpdateMessage.
	 * 
	 * @param player The {@link Position} of the Player.
	 * @param coordinates The {@link RegionCoordinates} of the Region being updated.
	 * @param messages The {@link List} of {@link RegionUpdateMessage}s.
	 */
	public GroupedRegionUpdateMessage(Position player, RegionCoordinates coordinates, List<RegionUpdateMessage> messages) {
		this.player = player;
		this.region = new Position(coordinates.getAbsoluteX(), coordinates.getAbsoluteY());
		this.messages = messages;
	}

	/**
	 * Gets the {@link Position} of the Player.
	 * 
	 * @return The Position.
	 */
	public Position getPlayerPosition() {
		return player;
	}

	/**
	 * Gets the {@link List} of {@link RegionUpdateMessage}s.
	 * 
	 * @return The Collection.
	 */
	public List<RegionUpdateMessage> getMessages() {
		return messages;
	}

	/**
	 * Gets the {@link Position} of the Region these updates affect.
	 * 
	 * @return The Position.
	 */
	public Position getRegionPosition() {
		return region;
	}

}