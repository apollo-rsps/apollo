package org.apollo.game.sync.seg;

import org.apollo.game.model.Position;
import org.apollo.game.sync.block.SynchronizationBlockSet;

/**
 * A {@link SynchronizationSegment} that adds an npc.
 *
 * @author Major
 */
public final class AddNpcSegment extends SynchronizationSegment {

	/**
	 * The index.
	 */
	private final int index;

	/**
	 * The id of the npc.
	 */
	private final int npcId;

	/**
	 * The position.
	 */
	private final Position position;

	/**
	 * Creates the add npc segment.
	 *
	 * @param blockSet The block set.
	 * @param index The npcs's index.
	 * @param position The position.
	 * @param npcId The id of the npc.
	 */
	public AddNpcSegment(SynchronizationBlockSet blockSet, int index, Position position, int npcId) {
		super(blockSet);
		this.index = index;
		this.position = position;
		this.npcId = npcId;
	}

	/**
	 * Gets the npc's index.
	 *
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the npc id.
	 *
	 * @return The npcId
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * Gets the position.
	 *
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

	@Override
	public SegmentType getType() {
		return SegmentType.ADD_MOB;
	}

}