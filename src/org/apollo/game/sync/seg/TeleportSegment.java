package org.apollo.game.sync.seg;

import org.apollo.game.model.Position;
import org.apollo.game.sync.block.SynchronizationBlockSet;

/**
 * A {@link SynchronizationSegment} where the mob is teleported to a new location.
 *
 * @author Graham
 */
public final class TeleportSegment extends SynchronizationSegment {

	/**
	 * The destination.
	 */
	private final Position destination;

	/**
	 * Creates the teleport segment.
	 *
	 * @param blockSet The block set.
	 * @param destination The destination.
	 */
	public TeleportSegment(SynchronizationBlockSet blockSet, Position destination) {
		super(blockSet);
		this.destination = destination;
	}

	/**
	 * Gets the destination.
	 *
	 * @return The destination.
	 */
	public Position getDestination() {
		return destination;
	}

	@Override
	public SegmentType getType() {
		return SegmentType.TELEPORT;
	}

}