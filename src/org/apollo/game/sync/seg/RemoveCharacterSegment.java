package org.apollo.game.sync.seg;

import org.apollo.game.sync.block.SynchronizationBlockSet;

/**
 * A {@link SynchronizationSegment} which removes a character.
 * @author Graham
 */
public final class RemoveCharacterSegment extends SynchronizationSegment {

	/**
	 * An empty {@link SynchronizationBlockSet}.
	 */
	private static final SynchronizationBlockSet EMPTY_BLOCK_SET = new SynchronizationBlockSet();

	/**
	 * Creates the remove character segment.
	 */
	public RemoveCharacterSegment() {
		super(EMPTY_BLOCK_SET);
	}

	@Override
	public SegmentType getType() {
		return SegmentType.REMOVE_CHARACTER;
	}

}
