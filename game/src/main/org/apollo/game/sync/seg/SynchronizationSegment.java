package org.apollo.game.sync.seg;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;

/**
 * A segment contains a set of {@link SynchronizationBlock}s, {@link Direction}s (or teleport {@link Position}s) and any
 * other things required for the update of a single player.
 *
 * @author Graham
 */
public abstract class SynchronizationSegment {

	/**
	 * The {@link SynchronizationBlockSet}.
	 */
	private final SynchronizationBlockSet blockSet;

	/**
	 * Creates the segment.
	 *
	 * @param blockSet The block set.
	 */
	public SynchronizationSegment(SynchronizationBlockSet blockSet) {
		this.blockSet = blockSet;
	}

	/**
	 * Gets the block set.
	 *
	 * @return The block set.
	 */
	public final SynchronizationBlockSet getBlockSet() {
		return blockSet;
	}

	/**
	 * Gets the type of segment.
	 *
	 * @return The type of segment.
	 */
	public abstract SegmentType getType();

}