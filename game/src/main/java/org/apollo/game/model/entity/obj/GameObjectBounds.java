package org.apollo.game.model.entity.obj;

import com.google.common.collect.ImmutableMap;
import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
import org.apollo.game.model.entity.EntityBounds;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The bounds of a {@link GameObject}.
 *
 * @author Steve Soltys
 */
public class GameObjectBounds extends EntityBounds {

	/**
	 * An {@link ImmutableMap} of Directions mapped to their mask bits for a {@link GameObject} interaction mask.
	 */
	private static final Map<Direction, Integer> INTERACTION_MASK_BITS = ImmutableMap.<Direction, Integer>builder()
		.put(Direction.NORTH, 0x1)
		.put(Direction.EAST, 0x8)
		.put(Direction.SOUTH, 0x4)
		.put(Direction.WEST, 0x2)
		.build();

	/**
	 * The GameObject.
	 */
	private final GameObject gameObject;

	/**
	 * The set of positions where the GameObject can be interacted with.
	 */
	private Set<Position> interactionPositions;

	/**
	 * Creates an EntityBounds.
	 *
	 * @param gameObject The GameObject.
	 */
	GameObjectBounds(GameObject gameObject) {
		super(gameObject);

		this.gameObject = gameObject;
	}

	/**
	 * Gets the set of positions where the GameObject can be interacted with.
	 *
	 * @return The set of positions.
	 */
	public Set<Position> getInteractionPositions() {
		if (interactionPositions == null) {
			interactionPositions = computeInteractionPositions();
		}

		return interactionPositions;
	}

	/**
	 * Computes the set of positions where the GameObject can be interacted with.
	 *
	 * @return The set of positions.
	 */
	private Set<Position> computeInteractionPositions() {

		switch (ObjectType.valueOf(gameObject.getType())) {
			case DIAGONAL_WALL:
				return getDiagonalWallInteractionPositions();

			case LENGTHWISE_WALL:
				return getWallInteractionPositions();

			case INTERACTABLE:
				return getInteractablePositions();

			// TODO: Figure out the rest.
		}

		return Collections.emptySet();
	}

	/**
	 * Gets the set of positions for a diagonal wall where the GameObject can be interacted with.
	 *
	 * @return The set of interaction positions.
	 */
	private Set<Position> getDiagonalWallInteractionPositions() {
		Set<Position> positions = new HashSet<>();
		Direction direction = Direction.WNES[gameObject.getOrientation()];
		Position position = gameObject.getPosition();

		// TODO: I think this either is missing one, or has one too many. Need to confirm these directions.
		positions.add(position.step(1, direction));
		positions.add(position.step(1, direction.counterClockwise()));
		positions.add(position.step(1, direction.counterClockwise(2)));
		positions.add(position.step(1, direction.clockwise()));
		positions.add(position.step(1, direction.clockwise(2)));
		positions.add(position.step(1, direction.opposite()));
		positions.add(position.step(1, direction.opposite().counterClockwise()));
		return positions;
	}

	/**
	 * Gets the set of Directions from which the GameObject can be interacted with.
	 *
	 * @return The interactable directions.
	 */
	private Set<Direction> getInteractableDirections() {
		Set<Direction> interactableDirections = new HashSet<>();

		int interactionMask = gameObject.getDefinition().getInteractionMask();
		int rotatedInteractionMask = (interactionMask << gameObject.getOrientation() & 0xf) +
			(interactionMask >> 4 - gameObject.getOrientation());

		for (Direction direction : Direction.NESW) {
			boolean interactive = (rotatedInteractionMask & INTERACTION_MASK_BITS.get(direction)) == 0;

			if (interactive) {
				interactableDirections.add(direction);
			}
		}

		return interactableDirections;
	}

	/**
	 * Gets the set of positions where the GameObject can be interacted with.
	 *
	 * @return The set of interaction positions.
	 */
	private Set<Position> getInteractablePositions() {
		Set<Direction> interactableDirections = getInteractableDirections();
		Set<Position> interactablePositions = new HashSet<>();

		Position position = gameObject.getPosition();
		int width = gameObject.getWidth();
		int length = gameObject.getLength();

		for (int deltaX = position.getX(); deltaX < position.getX() + width; deltaX++) {
			for (int deltaY = position.getY(); deltaY < position.getY() + length; deltaY++) {

				for(Direction direction : interactableDirections) {
					Position deltaPosition = new Position(deltaX, deltaY, position.getHeight()).step(1, direction);

					if(!contains(deltaPosition)) {
						interactablePositions.add(deltaPosition);
					}
				}
			}
		}

		return interactablePositions;
	}

	/**
	 * Gets the set of positions for a wall where the GameObject can be interacted with.
	 *
	 * @return The set of interaction positions.
	 */
	private Set<Position> getWallInteractionPositions() {
		Set<Position> positions = new HashSet<>();
		Direction objectDirection = Direction.WNES[gameObject.getOrientation()];
		Position position = gameObject.getPosition();

		positions.add(position);
		positions.add(position.step(1, objectDirection));
		positions.add(position.step(1, objectDirection.clockwise(2)));
		positions.add(position.step(1, objectDirection.counterClockwise(2)));
		return positions;
	}
}
