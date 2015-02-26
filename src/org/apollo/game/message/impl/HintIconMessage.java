package org.apollo.game.message.impl;

import java.util.Optional;

import org.apollo.game.message.Message;
import org.apollo.game.model.Position;

/**
 * A {@link Message} that displays a hint icon over an Npc, tile, or player.
 *
 * @author Major
 */
public final class HintIconMessage extends Message {

	// TODO identify the other types and use an enum.

	/**
	 * The hint icon type for Npcs.
	 */
	public static final int NPC_TYPE = 1;

	/**
	 * The hint icon type for Players.
	 */
	public static final int PLAYER_TYPE = 10;

	/**
	 * Creates a HintIconMessage for the Npc with the specified index.
	 * 
	 * @param index The index of the Npc.
	 * @return The HintIconMessage.
	 */
	public static HintIconMessage forNpc(int index) {
		return new HintIconMessage(NPC_TYPE, Optional.of(index), Optional.empty());
	}

	/**
	 * Creates a HintIconMessage for the Player with the specified index.
	 * 
	 * @param index The index of the Player.
	 * @return The HintIconMessage.
	 */
	public static HintIconMessage forPlayer(int index) {
		return new HintIconMessage(PLAYER_TYPE, Optional.of(index), Optional.empty());
	}

	/**
	 * The index of the entity, if applicable.
	 */
	private final Optional<Integer> index;

	/**
	 * The Position of the tile, if applicable.
	 */
	private final Optional<Position> position;

	/**
	 * The type of entity this HintIconMessage is directed at.
	 */
	private final int type;

	/**
	 * Creates the HintIconMessage.
	 *
	 * @param type The type of entity this HintIconMessage is directed at.
	 * @param index The index of the entity, if applicable.
	 * @param position The Position of the tile, if applicable.
	 */
	private HintIconMessage(int type, Optional<Integer> index, Optional<Position> position) {
		this.type = type;
		this.index = index;
		this.position = position;
	}

	/**
	 * Gets the index of the entity, if applicable.
	 *
	 * @return The index.
	 */
	public Optional<Integer> getIndex() {
		return index;
	}

	/**
	 * Gets the {@link Position} of the tile, if applicable.
	 *
	 * @return The Position.
	 */
	public Optional<Position> getPosition() {
		return position;
	}

	/**
	 * Gets the type this HintIconMessage is directed at.
	 *
	 * @return The type.
	 */
	public int getType() {
		return type;
	}

}