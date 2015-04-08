package org.apollo.game.sync.seg;

import org.apollo.game.sync.block.SynchronizationBlockSet;

/**
 * A {@link SynchronizationSegment} which removes a mob.
 *
 * @author Graham
 */
public final class RemoveMobSegment extends SynchronizationSegment {

	/**
	 * An empty {@link SynchronizationBlockSet}.
	 */
	private static final SynchronizationBlockSet EMPTY_BLOCK_SET = new SynchronizationBlockSet();

	/**
	 * Creates the remove mob segment.
	 */
	public RemoveMobSegment() {
		super(EMPTY_BLOCK_SET);
	}

	@Override
	public SegmentType getType() {
		return SegmentType.REMOVE_MOB;
	}

}