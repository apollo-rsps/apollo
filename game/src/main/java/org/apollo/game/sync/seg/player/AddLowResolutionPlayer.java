package org.apollo.game.sync.seg.player;

import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.game.sync.seg.SegmentType;
import org.apollo.game.sync.seg.SynchronizationSegment;

/**
 * @author Khaled Abdeljaber
 */
public class AddLowResolutionPlayer extends SynchronizationSegment {
	/**
	 * Creates the segment.
	 *
	 * @param blockSet The block set.
	 */
	public AddLowResolutionPlayer(SynchronizationBlockSet blockSet) {
		super(blockSet);
	}

	@Override
	public SegmentType getType() {
		return SegmentType.REMOVE_MOB;
	}
}
