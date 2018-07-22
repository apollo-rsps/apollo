package org.apollo.game.model.entity;

import org.apollo.game.model.Position;

/**
 * The bounds of an {@link Entity}.
 *
 * @author Steve Soltys
 */
public class EntityBounds {

	/**
	 * The {@link Entity}.
	 */
	private final Entity entity;

	/**
	 * Creates an EntityBounds.
	 *
	 * @param entity The entity.
	 */
	EntityBounds(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Checks whether the given position is within the Entity's bounds.
	 *
	 * @param position The position.
	 * @return A flag indicating whether or not the position exists within the Entity's bounds.
	 */
	public boolean contains(Position position) {
		int positionX = position.getX();
		int positionY = position.getY();
		int positionHeight = position.getHeight();

		int entityX = entity.getPosition().getX();
		int entityY = entity.getPosition().getY();
		int entityHeight = entity.getPosition().getHeight();

		int width = entity.getWidth();
		int length = entity.getLength();

		return positionX >= entityX && positionX < entityX + width &&
			positionY >= entityY && positionY < entityY + length &&
			positionHeight == entityHeight;
	}
}
