package org.apollo.game.sync.block;

import org.apollo.game.model.Graphic;

/**
 * The graphic {@link SynchronizationBlock}.
 * @author Graham
 */
public final class GraphicBlock extends SynchronizationBlock {

	/**
	 * The graphic.
	 */
	private final Graphic graphic;

	/**
	 * Creates the graphic block.
	 * @param graphic The graphic.
	 */
	GraphicBlock(Graphic graphic) {
		this.graphic = graphic;
	}

	/**
	 * Gets the graphic.
	 * @return The graphic.
	 */
	public Graphic getGraphic() {
		return graphic;
	}

}
