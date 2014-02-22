package org.apollo.game.model.sector;

import java.util.HashMap;
import java.util.Map;

/**
 * A repository of sectors, backed by a {@link HashMap} of {@link SectorCoordinates} that correspond to their
 * appropriate {@link Sector}s.
 * 
 * @author Major
 */
public final class SectorRepository {

	/**
	 * Indicates whether sectors can be removed from this repository.
	 */
	private final boolean permitRemoval;

	/**
	 * A {@link Map} of {@link SectorCoordinates} that correspond to the appropriate {@link Sector}s..
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
		if (sector == null) {
			throw new IllegalArgumentException("Sector cannot be null.");
		} else if (sectors.containsKey(sector.getCoordinates()) && !permitRemoval) {
			throw new UnsupportedOperationException(
					"Cannot add a sector with the same coordinates as an existing sector.");
		}
		sectors.put(sector.getCoordinates(), sector);
	}

	/**
	 * Indicates whether the supplied value (i.e. the {@link Sector}) has a mapping.
	 * 
	 * @param sector The sector.
	 * @return {@code true} if the value is mapped by a key (i.e. {@link SectorCoordinates}), otherwise {@code false}.
	 */
	public boolean contains(Sector sector) {
		return sectors.containsValue(sector);
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
	 * Gets a {@link Sector} with the specified {@link SectorCoordinates}. If the sector does not exist (i.e.
	 * {@link #sectors}{@code .get()} returns {@code null}) then a new sector is created, submitted to the repository,
	 * and returned.
	 * 
	 * @param coordinates The coordinates.
	 * @return The sector.
	 */
	public Sector get(SectorCoordinates coordinates) {
		Sector sector = sectors.get(coordinates);
		if (sector == null) {
			add(sector = new Sector(coordinates));
		}
		return sector;
	}

	/**
	 * Removes a {@link Sector} from the repository, if permitted.
	 * 
	 * @param sector The sector to remove.
	 * @throws UnsupportedOperationException If this method is called on a repository that does not permit removal.
	 */
	public void remove(Sector sector) {
		if (!permitRemoval) {
			throw new UnsupportedOperationException("Cannot remove sectors from this repository.");
		}
		sectors.remove(sector.getCoordinates());
	}

}