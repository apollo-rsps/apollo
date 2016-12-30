package org.apollo.game.model.area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apollo.game.model.Position;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * A repository of {@link Region}s, backed by a {@link HashMap} of {@link RegionCoordinates} that correspond to their
 * appropriate regions.
 *
 * @author Major
 */
public final class RegionRepository {

	/**
	 * Returns an immutable RegionRepository, where {@link Region}s cannot be added or removed.
	 * <p>
	 * Note that, internally, regions are added lazily (i.e. only when necessary). As such, repositories are (again,
	 * internally) not actually immutable, so do not rely on such behaviour.
	 *
	 * @return The RegionRepository.
	 */
	public static RegionRepository immutable() {
		return new RegionRepository(false);
	}

	/**
	 * Returns a mutable RegionRepository, where {@link Region}s may be removed.
	 *
	 * @return The RegionRepository.
	 */
	public static RegionRepository mutable() {
		return new RegionRepository(true);
	}

	/**
	 * Whether or not regions can be removed from this repository.
	 */
	private final boolean permitRemoval;

	/**
	 * The map of RegionCoordinates that correspond to the appropriate Regions.
	 */
	private final Map<RegionCoordinates, Region> regions = new HashMap<>();

	/**
	 * A list of default {@link RegionListener}s which will be added to {@link Region}s upon creation.
	 */
	private final List<RegionListener> defaultRegionListeners = new ArrayList<>();

	/**
	 * Creates a new RegionRepository.
	 *
	 * @param permitRemoval If removal (of {@link Region}s) from this repository should be permitted.
	 */
	private RegionRepository(boolean permitRemoval) {
		this.permitRemoval = permitRemoval;
	}

	/**
	 * Adds a {@link Region} to the repository.
	 *
	 * @param region The Region.
	 * @throws IllegalArgumentException If the provided Region is null.
	 * @throws UnsupportedOperationException If the coordinates of the provided Region are already mapped (and hence the
	 *             existing Region would be replaced), and removal of regions is not permitted.
	 */
	private void add(Region region) {
		Preconditions.checkNotNull(region, "Region cannot be null.");
		if (regions.containsKey(region.getCoordinates()) && !permitRemoval) {
			throw new UnsupportedOperationException("Cannot add a Region with the same coordinates as an existing Region.");
		}

		defaultRegionListeners.forEach(region::addListener);
		regions.put(region.getCoordinates(), region);
	}

	/**
	 * Adds a {@link RegionListener} to be registered as a default listener with all newly created {@link Region}s and
	 * associated with any existing instances.
	 *
	 * @param listener The listener to add.
	 */
	public void addRegionListener(RegionListener listener) {
		for (Region region : regions.values()) {
			region.addListener(listener);
		}

		defaultRegionListeners.add(listener);
	}

	/**
	 * Indicates whether the supplied value (i.e. the {@link Region}) has a mapping.
	 *
	 * @param region The Region.
	 * @return {@code true} if this repository contains an entry with {@link RegionCoordinates} equal to the specified
	 *         Region, otherwise {@code false}.
	 */
	public boolean contains(Region region) {
		return contains(region.getCoordinates());
	}

	/**
	 * Indicates whether the supplied key (i.e. the {@link RegionCoordinates}) has a mapping.
	 *
	 * @param coordinates The coordinates.
	 * @return {@code true} if the key is already mapped to a value (i.e. a {@link Region}), otherwise {@code false}.
	 */
	public boolean contains(RegionCoordinates coordinates) {
		return regions.containsKey(coordinates);
	}

	/**
	 * Gets the {@link Region} that contains the specified {@link Position}. If the Region does not exist in this
	 * repository then a new Region is created, submitted to the repository, and returned.
	 *
	 * @param position The position.
	 * @return The Region.
	 */
	public Region fromPosition(Position position) {
		return get(RegionCoordinates.fromPosition(position));
	}

	/**
	 * Gets a {@link Region} with the specified {@link RegionCoordinates}. If the Region does not exist in this
	 * repository then a new Region is created, submitted to the repository, and returned.
	 *
	 * @param coordinates The RegionCoordinates.
	 * @return The Region. Will never be null.
	 */
	public Region get(RegionCoordinates coordinates) {
		Region region = regions.get(coordinates);
		if (region == null) {
			region = new Region(coordinates);
			add(region);
		}

		return region;
	}

	/**
	 * Gets a shallow copy of the {@link List} of {@link Region}s. This will be an {@link ImmutableList}.
	 *
	 * @return The List.
	 */
	public List<Region> getRegions() {
		return ImmutableList.copyOf(regions.values());
	}

	/**
	 * Removes a {@link Region} from the repository, if permitted. This method removes the entry that has a key
	 * identical to the {@link RegionCoordinates} of the specified Region.
	 *
	 * @param region The Region to remove.
	 * @return {@code true} if the specified Region existed and was removed, {@code false} if not.
	 * @throws UnsupportedOperationException If this method is called on a repository that does not permit removal.
	 */
	public boolean remove(Region region) {
		if (!permitRemoval) {
			throw new UnsupportedOperationException("Cannot remove regions from this repository.");
		}

		return regions.remove(region.getCoordinates()) != null;
	}

}