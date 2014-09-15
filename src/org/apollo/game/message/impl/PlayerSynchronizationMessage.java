package org.apollo.game.message.impl;

import java.util.List;

import org.apollo.game.message.Message;
import org.apollo.game.model.Position;
import org.apollo.game.sync.seg.SynchronizationSegment;

/**
 * A {@link Message} sent to the client to synchronize players.
 * 
 * @author Graham
 */
public final class PlayerSynchronizationMessage extends Message {

	/**
	 * The position in the last known sector.
	 */
	private final Position lastKnownSector;

	/**
	 * The number of local players.
	 */
	private final int localPlayers;

	/**
	 * The player's position.
	 */
	private final Position position;

	/**
	 * A flag indicating if the sector has changed.
	 */
	private final boolean sectorChanged;

	/**
	 * The current player's synchronization segment.
	 */
	private final SynchronizationSegment segment;

	/**
	 * A list of segments.
	 */
	private final List<SynchronizationSegment> segments;

	/**
	 * Creates the player synchronization message.
	 * 
	 * @param lastKnownSector The last known sector.
	 * @param position The player's current position.
	 * @param sectorChanged A flag indicating if the sector has changed.
	 * @param segment The current player's synchronization segment.
	 * @param localPlayers The number of local players.
	 * @param segments A list of segments.
	 */
	public PlayerSynchronizationMessage(Position lastKnownSector, Position position, boolean sectorChanged,
			SynchronizationSegment segment, int localPlayers, List<SynchronizationSegment> segments) {
		this.lastKnownSector = lastKnownSector;
		this.position = position;
		this.sectorChanged = sectorChanged;
		this.segment = segment;
		this.localPlayers = localPlayers;
		this.segments = segments;
	}

	/**
	 * Gets the last known sector.
	 * 
	 * @return The last known sector.
	 */
	public Position getLastKnownSector() {
		return lastKnownSector;
	}

	/**
	 * Gets the number of local players.
	 * 
	 * @return The number of local players.
	 */
	public int getLocalPlayers() {
		return localPlayers;
	}

	/**
	 * Gets the player's position.
	 * 
	 * @return The player's position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the current player's segment.
	 * 
	 * @return The current player's segment.
	 */
	public SynchronizationSegment getSegment() {
		return segment;
	}

	/**
	 * Gets the synchronization segments.
	 * 
	 * @return The segments.
	 */
	public List<SynchronizationSegment> getSegments() {
		return segments;
	}

	/**
	 * Checks if the sector has changed.
	 * 
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean hasSectorChanged() {
		return sectorChanged;
	}

}