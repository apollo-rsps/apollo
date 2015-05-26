package org.apollo.game.message.impl;

import java.util.List;

import org.apollo.game.model.Position;
import org.apollo.game.model.entity.Npc;
import org.apollo.game.sync.seg.SynchronizationSegment;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to synchronize npcs with players.
 *
 * @author Major
 */
public final class NpcSynchronizationMessage extends Message {

	/**
	 * The amount of local npcs.
	 */
	private final int localNpcs;

	/**
	 * The npc's position.
	 */
	private final Position position;

	/**
	 * A list of segments.
	 */
	private final List<SynchronizationSegment> segments;

	/**
	 * Creates a new {@link NpcSynchronizationMessage}.
	 *
	 * @param position The position of the {@link Npc}.
	 * @param segments The list of segments.
	 * @param localNpcs The amount of local npcs.
	 */
	public NpcSynchronizationMessage(Position position, List<SynchronizationSegment> segments, int localNpcs) {
		this.position = position;
		this.segments = segments;
		this.localNpcs = localNpcs;
	}

	/**
	 * Gets the number of local npcs.
	 *
	 * @return The number of local npcs.
	 */
	public int getLocalNpcCount() {
		return localNpcs;
	}

	/**
	 * Gets the npc's position.
	 *
	 * @return The npc's position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the synchronization segments.
	 *
	 * @return The segments.
	 */
	public List<SynchronizationSegment> getSegments() {
		return segments;
	}

}