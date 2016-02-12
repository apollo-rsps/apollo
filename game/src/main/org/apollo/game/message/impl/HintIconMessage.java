package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} that displays a hint icon over an Npc, tile, or player.
 *
 * @author Major
 */
public abstract class HintIconMessage extends Message {

	/**
	 * The type of a HintIcon.
	 */
	public enum Type {

		/**
		 * A HintIcon that hovers over an Npc.
		 */
		NPC(1),

		/**
		 * A HintIcon that hovers directly over a Position.
		 */
		CENTER(2),

		/**
		 * A HintIcon that hovers west over a Position.
		 */
		WEST(3),

		/**
		 * A HintIcon that hovers east over a Position.
		 */
		EAST(4),

		/**
		 * A HintIcon that hovers south over a Position.
		 */
		SOUTH(5),

		/**
		 * A HintIcon that hovers north over a Position.
		 */
		NORTH(6),

		/**
		 * A HintIcon that hovers over a Player.
		 */
		PLAYER(10);

		/**
		 * The integer value of this type.
		 */
		private final int value;

		/**
		 * Creates the Type.
		 *
		 * @param value The value.
		 */
		private Type(int value) {
			this.value = value;
		}

		/**
		 * Gets the value of this type.
		 *
		 * @return The value.
		 */
		public int getValue() {
			return value;
		}

	}

	/**
	 * The Type of entity this HintIconMessage is directed at.
	 */
	private final Type type;

	/**
	 * Creates the HintIconMessage.
	 *
	 * @param type The {@link Type} of this HintIconMessage.
	 */
	protected HintIconMessage(Type type) {
		this.type = type;
	}

	/**
	 * Gets the type this HintIconMessage is directed at.
	 *
	 * @return The type.
	 */
	public final Type getType() {
		return type;
	}

}