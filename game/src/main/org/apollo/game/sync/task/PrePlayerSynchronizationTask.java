package org.apollo.game.sync.task;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apollo.game.message.impl.ClearRegionMessage;
import org.apollo.game.message.impl.GroupedRegionUpdateMessage;
import org.apollo.game.message.impl.RegionChangeMessage;
import org.apollo.game.message.impl.RegionUpdateMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionCoordinates;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.entity.Player;

/**
 * A {@link SynchronizationTask} which does pre-synchronization work for the specified {@link Player}.
 *
 * @author Graham
 * @author Major
 */
public final class PrePlayerSynchronizationTask extends SynchronizationTask {

	/**
	 * The radius of viewable regions.
	 */
	private static final int VIEWABLE_REGION_RADIUS = 3;

	/**
	 * The width of the viewport of every Player, in tiles.
	 */
	private static final int VIEWPORT_WIDTH = Region.SIZE * 13;

	/**
	 * The Map of RegionCoordinates to Sets of RegionUpdateMessages, which contain all of the Entity information for a
	 * Region.
	 */
	private final Map<RegionCoordinates, Set<RegionUpdateMessage>> encodes;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The Map of RegionCoordinates to Sets of RegionUpdateMessages, which contain the updates for a Region a Player
	 * can already view.
	 */
	private final Map<RegionCoordinates, Set<RegionUpdateMessage>> updates;

	/**
	 * Creates the {@link PrePlayerSynchronizationTask} for the specified {@link Player}.
	 *
	 * @param player The Player.
	 * @param encodes The Map containing Region encodes.
	 * @param updates The {@link Map} containing {@link Region} updates.
	 */
	public PrePlayerSynchronizationTask(Player player, Map<RegionCoordinates, Set<RegionUpdateMessage>> encodes,
	                                    Map<RegionCoordinates, Set<RegionUpdateMessage>> updates) {
		this.player = player;
		this.updates = updates;
		this.encodes = encodes;
	}

	@Override
	public void run() {
		Position old = player.getPosition();
		player.getWalkingQueue().pulse();

		if (player.isTeleporting()) {
			player.resetViewingDistance();
		}

		Position position = player.getPosition();
		if (!player.hasLastKnownRegion() || isRegionUpdateRequired()) {
			player.setRegionChanged(true);

			player.setLastKnownRegion(position);
			player.send(new RegionChangeMessage(position));
		}

		Set<RegionCoordinates> oldViewable = getViewableRegions(old), newViewable = getViewableRegions(position);

		Set<RegionCoordinates> differences = new HashSet<>(newViewable);
		differences.retainAll(oldViewable);

		Set<RegionCoordinates> full = new HashSet<>(newViewable);
		full.removeAll(oldViewable);

		sendUpdates(player.getLastKnownRegion(), differences, full);
	}

	/**
	 * Gets the {@link Set} of {@link RegionCoordinates} of Regions that are viewable from the specified {@link
	 * Position}.
	 *
	 * @param position The Position.
	 * @return The Set of RegionCoordinates.
	 */
	private Set<RegionCoordinates> getViewableRegions(Position position) { // TODO possibly more complicated than this
		RegionCoordinates local = position.getRegionCoordinates();
		int localX = local.getX(), localY = local.getY();
		int maxX = localX + VIEWABLE_REGION_RADIUS, maxY = localY + VIEWABLE_REGION_RADIUS;

		Set<RegionCoordinates> viewable = new HashSet<>();
		for (int x = localX - VIEWABLE_REGION_RADIUS; x < maxX; x++) {
			for (int y = localY - VIEWABLE_REGION_RADIUS; y < maxY; y++) {
				viewable.add(new RegionCoordinates(x, y));
			}
		}

		return viewable;
	}

	/**
	 * Checks if a region update is required.
	 *
	 * @return {@code true} if a Region update is required, {@code false} if not.
	 */
	private boolean isRegionUpdateRequired() {
		Position current = player.getPosition();
		Position last = player.getLastKnownRegion();

		int deltaX = current.getLocalX(last);
		int deltaY = current.getLocalY(last);

		return deltaX <= Position.MAX_DISTANCE || deltaX >= VIEWPORT_WIDTH - Position.MAX_DISTANCE - 1
				|| deltaY <= Position.MAX_DISTANCE || deltaY >= VIEWPORT_WIDTH - Position.MAX_DISTANCE - 1;
	}

	/**
	 * Sends the updates for a {@link Region}
	 *
	 * @param position The {@link Position} of the last known region.
	 * @param differences The {@link Set} of {@link RegionCoordinates} of Regions that changed.
	 * @param full The {@link Set} of {@link RegionCoordinates} of Regions that require a full update.
	 */
	private void sendUpdates(Position position, Set<RegionCoordinates> differences, Set<RegionCoordinates> full) {
		RegionRepository repository = player.getWorld().getRegionRepository();
		int height = position.getHeight();

		differences.stream().map(coordinates -> {
			Set<RegionUpdateMessage> messages = updates.computeIfAbsent(coordinates,
					coords -> repository.get(coords).getUpdates(height));

			return new GroupedRegionUpdateMessage(position, coordinates, messages);
		}).forEach(player::send);

		full.stream().map(coordinates -> {
			Set<RegionUpdateMessage> messages = encodes.computeIfAbsent(coordinates,
					coords -> repository.get(coords).encode(height));

			player.send(new ClearRegionMessage(position, coordinates));
			return new GroupedRegionUpdateMessage(position, coordinates, messages);
		}).forEach(player::send);
	}

}