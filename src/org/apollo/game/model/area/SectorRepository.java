package org.apollo.game.model.area;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apollo.game.model.Position;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * A repository of {@link Sector}s, backed by a {@link HashMap} of {@link SectorCoordinates} that correspond to their
 * appropriate sectors.
 * 
 * @author Major
 */
public final class SectorRepository {

	/**
	 * Whether or not sectors can be removed from this repository.
	 */
	private final boolean permitRemoval;

	/**
	 * The map of sector coordinates that correspond to the appropriate sectors.
	 */
	private final Map<SectorCoordinates, Sector> sectors = new HashMap<>();

	/**
	 * Creates a new sector repository.
	 * 
	 * @param permitRemoval If removal (of {@link Sector}s) from this repository should be permitted.
	 */
	public SectorRepository(boolean permitRemoval) {
		this.permitRemoval = permitRemoval;
	}

	/**
	 * Adds a {@link Sector} to the repository.
	 * 
	 * @param sector The sector.
	 * @throws IllegalArgumentException If the provided sector is null.
	 * @throws UnsupportedOperationException If the coordinates of the provided sector are already mapped (and hence the
	 *             existing sector would be replaced), and removal of sectors is not permitted.
	 */
	public void add(Sector sector) {
		Preconditions.checkNotNull(sector, "Sector cannot be null.");
		if (sectors.containsKey(sector.getCoordinates()) && !permitRemoval) {
			throw new UnsupportedOperationException("Cannot add a sector with the same coordinates as an existing sector.");
		}
		sectors.put(sector.getCoordinates(), sector);
	}

	/**
	 * Indicates whether the supplied value (i.e. the {@link Sector}) has a mapping.
	 * 
	 * @param sector The sector.
	 * @return {@code true} if this repository contains an entry with {@link SectorCoordinates} equal to the specified
	 *         sector, otherwise {@code false}.
	 */
	public boolean contains(Sector sector) {
		return contains(sector.getCoordinates());
	}

	/**
	 * Indicates whether the supplied key (i.e. the {@link SectorCoordinates}) has a mapping.
	 * 
	 * @param coordinates The coordinates.
	 * @return {@code true} if the key is already mapped to a value (i.e. a {@link Sector}), otherwise {@code false}.
	 */
	public boolean contains(SectorCoordinates coordinates) {
		return sectors.containsKey(coordinates);
	}

	/**
	 * Gets the {@link Sector} that contains the specified {@link Position}. If the sector does not exist in this
	 * repository then a new sector is created, submitted to the repository, and returned.
	 * 
	 * @param position The position.
	 * @return The sector.
	 */
	public Sector fromPosition(Position position) {
		return get(SectorCoordinates.fromPosition(position));
	}

	/**
	 * Gets a {@link Sector} with the specified {@link SectorCoordinates}. If the sector does not exist in this
	 * repository then a new sector is created, submitted to the repository, and returned.
	 * 
	 * @param coordinates The coordinates.
	 * @return The sector. Will never be null.
	 */
	public Sector get(SectorCoordinates coordinates) {
		Sector sector = sectors.get(coordinates);
		if (sector == null) {
			sector = new Sector(coordinates);
			add(sector);
		}
		return sector;
	}

	/**
	 * Gets a shallow copy of the {@link List} of {@link Sector}s. This will be an {@link ImmutableList}.
	 * 
	 * @return The list.
	 */
	public List<Sector> getSectors() {
		return ImmutableList.copyOf(sectors.values());
	}

	/**
	 * Removes a {@link Sector} from the repository, if permitted. This method removes the entry that has a key
	 * identical to the {@link SectorCoordinates} of the specified sector.
	 * 
	 * @param sector The sector to remove.
	 * @return Whether or not the sector was removed.
	 * @throws UnsupportedOperationException If this method is called on a repository that does not permit removal.
	 */
	public boolean remove(Sector sector) {
		if (!permitRemoval) {
			throw new UnsupportedOperationException("Cannot remove sectors from this repository.");
		}
		return sectors.remove(sector.getCoordinates()) != null;
	}

}