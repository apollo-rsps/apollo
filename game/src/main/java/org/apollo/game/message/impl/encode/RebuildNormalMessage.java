package org.apollo.game.message.impl.encode;

import org.apollo.cache.map.XteaRepository;
import org.apollo.game.model.Position;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client instructing it to load the specified region.
 *
 * @author Graham
 */
public final class RebuildNormalMessage extends Message {

	/**
	 * The position of the region to load.
	 */
	private final Position position;

	/**
	 * The index of the player.
	 */
	private final int index;

	/**
	 * The repository containing xteas.
	 */
	private final XteaRepository repository;

	/**
	 * If the player has a last known region.
	 */
	private final boolean hasLastKnownRegion;

	/**
	 * Creates the region changed message.
	 *
	 * @param position           The position of the region.
	 * @param repository         the repository
	 * @param hasLastKnownRegion if the player has been in a region.
	 */
	public RebuildNormalMessage(Position position, int index, XteaRepository repository, boolean hasLastKnownRegion) {
		this.position = position;
		this.index = index;
		this.repository = repository;
		this.hasLastKnownRegion = hasLastKnownRegion;
	}

	/**
	 * Gets the position of the region to load.
	 *
	 * @return The position of the region to load.
	 */
	public Position getPosition() {
		return position;
	}


	/**
	 * Gets the index of the player.
	 *
	 * @return the index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets repository.
	 *
	 * @return the repository
	 */
	public XteaRepository getRepository() {
		return repository;
	}

	/**
	 * Is has last known region boolean.
	 *
	 * @return the boolean
	 */
	public boolean isHasLastKnownRegion() {
		return hasLastKnownRegion;
	}
}