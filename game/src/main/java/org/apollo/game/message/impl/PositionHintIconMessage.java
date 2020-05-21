package org.apollo.game.message.impl;

import com.google.common.base.Preconditions;
import org.apollo.game.model.Position;

/**
 * A {@link HintIconMessage} which displays a hint over a Position.
 */
public final class PositionHintIconMessage extends HintIconMessage {

	/**
	 * Represents the default Position a HintIcon is reset to.
	 */
	private static final Position DEFAULT_POSITION = new Position(0, 0);

	/**
	 * Tests if the specified Type if valid for a Position HintIcon.
	 *
	 * @param type The Type to test.
	 * @return The Type if it was valid.
	 */
	private static Type testType(Type type) {
		Preconditions.checkArgument(type != Type.NPC && type != Type.PLAYER,
			"Hint icons over a Position may not have a type of Player or Npc.");
		return type;
	}

	/**
	 * Creates a new {@link PositionHintIconMessage} which resets the current
	 * HintIcon.
	 *
	 * @return The new {@link PositionHintIconMessage}, never {@code null}.
	 */
	public static PositionHintIconMessage reset() {
		return new PositionHintIconMessage(Type.CENTER, DEFAULT_POSITION, 0);
	}

	/**
	 * The Position of the HintIcon.
	 */
	private final Position position;

	/**
	 * The display height of the HintIcon.
	 */
	private final int height;

	/**
	 * Constructs a new {@link PositionHintIconMessage}.
	 *
	 * @param type The Type of the HintIcon.
	 * @param position The Position of the hint icon.
	 * @param height The display height of the hint icon.
	 */
	public PositionHintIconMessage(Type type, Position position, int height) {
		super(testType(type));
		this.position = position;
		this.height = height;
	}

	/**
	 * Gets the Position of the HintIcon.
	 *
	 * @return The Position of the HintIcon.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the display height of the HintIcon.
	 *
	 * @return The display height of the HintIcon.
	 */
	public int getHeight() {
		return height;
	}

}