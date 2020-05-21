package org.apollo.game.sync.block;

/**
 * The effect of this mask only lasts a tick.
 * @author Khaled Abdeljaber
 */
public class TemporaryMovementTypeBlock extends MovementTypeBlock {

	/**
	 * Instantiates a new Movement type block.
	 *
	 * @param mode the mode
	 */
	public TemporaryMovementTypeBlock(MovementMode mode) {
		super(mode);
	}
}
