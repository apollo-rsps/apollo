package org.apollo.game.sync.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

import com.google.common.collect.ImmutableSet;

/**
 * A {@link SynchronizationTask} which does pre-synchronization work for the specified {@link Player}.
 *
 * @author Graham
 * @author Major
 */
public final class PrePlayerSynchronizationTask extends SynchronizationTask {

	/**
	 * The update mode used when sending a {@link GroupedRegionUpdateMessage}.
	 */
	private enum RegionUpdateMode {

		/**
		 * The difference update mode, which only sends updates for changes that have occurred in the last pulse.
		 */
		DIFFERENCE,

		/**
		 * The full update mode, which sends everything in the Region.
		 */
		FULL;

	}

	/**
	 * The amount of Regions that are in view of a player, in one direction.
	 */
	private static final int REGION_COUNT = (int) Math.ceil(Position.MAX_DISTANCE / Region.SIZE);

	/**
	 * The width of the viewport of every Player, in tiles.
	 */
	private static final int VIEWPORT_WIDTH = Region.SIZE * 13;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The Map of RegionCoordinates to Sets of RegionUpdateMessages, which contain all of the Entity information for a
	 * Region.
	 */
	private final Map<RegionCoordinates, List<RegionUpdateMessage>> snapshots;

	/**
	 * The Map of RegionCoordinates to Sets of RegionUpdateMessages, which contain the updates for a Region a Player can
	 * already view.
	 */
	private final Map<RegionCoordinates, List<RegionUpdateMessage>> updates;

	/**
	 * Creates the {@link PrePlayerSynchronizationTask} for the specified {@link Player}.
	 *
	 * @param player The Player.
	 * @param updates The {@link Map} containing {@link Region} updates.
	 * @param snapshots The Map containing Region snapshots.
	 */
	public PrePlayerSynchronizationTask(Player player, Map<RegionCoordinates, List<RegionUpdateMessage>> updates, Map<RegionCoordinates, List<RegionUpdateMessage>> snapshots) {
		this.player = player;
		this.updates = updates;
		this.snapshots = snapshots;
	}

	@Override
	public void run() {
		Position old = player.getPosition();
		player.getWalkingQueue().pulse();

		RegionUpdateMode mode = RegionUpdateMode.DIFFERENCE;

		if (player.isTeleporting()) {
			player.resetViewingDistance();
			mode = RegionUpdateMode.FULL;
		}

		boolean hasKnownRegion = player.hasLastKnownRegion();
		if (!hasKnownRegion) {
			mode = RegionUpdateMode.FULL;
		}

		if (!hasKnownRegion || isRegionUpdateRequired()) {
			player.setRegionChanged(true);

			Position position = player.getPosition();
			player.setLastKnownRegion(position);
			player.send(new RegionChangeMessage(position));
		}

		Set<RegionCoordinates> newRegions = getNewRegions(old, player.getPosition());
		sendRegionUpdates(mode, newRegions);
	}

	/**
	 * Gets the {@link Set} of {@link RegionCoordinates} of {@link Region}s that the {@link Player} in this task has
	 * only just became able to view.
	 *
	 * @param old The old {@link Position} of the Player.
	 * @param next The new Position of the Player.
	 * @return The Set of RegionCoordinates. Will not be {@code null}, but may be empty.
	 */
	private Set<RegionCoordinates> getNewRegions(Position old, Position next) {
		RegionCoordinates oldRegion = old.getRegionCoordinates();
		RegionCoordinates nextRegion = next.getRegionCoordinates();

		if (oldRegion.equals(nextRegion)) {
			return ImmutableSet.of();
		}

		Set<RegionCoordinates> coordinates = new HashSet<>(9);
		int oldX = oldRegion.getX(), oldY = oldRegion.getY();

		int dx = nextRegion.getX() - oldX;
		int dy = nextRegion.getY() - oldY;

		if (dx != 0) {
			int x = oldX + dx;
			int maxY = oldY + REGION_COUNT;

			for (int y = oldY - REGION_COUNT; y <= maxY; y++) {
				coordinates.add(new RegionCoordinates(x, y));
			}
		}

		if (dy != 0) {
			int y = oldY + dy;
			int maxX = oldX + REGION_COUNT;

			for (int x = oldX - REGION_COUNT; x <= maxX; x++) {
				coordinates.add(new RegionCoordinates(x, y));
			}
		}

		return coordinates;
	}

	/**
	 * Gets the {@link List} of {@link GroupedRegionUpdateMessage}s.
	 *
	 * @param mode The {@link RegionUpdateMode} used when creating the Messages.
	 * @param newRegions The {@link Set} of {@link RegionCoordinates} that should be sent as a full update.
	 */
	private void sendRegionUpdates(RegionUpdateMode mode, Set<RegionCoordinates> newRegions) {
		Position position = player.getPosition();
		RegionCoordinates base = position.getRegionCoordinates();
		int baseX = base.getX(), baseY = base.getY();

		RegionRepository repository = player.getWorld().getRegionRepository();
		List<GroupedRegionUpdateMessage> messages = new ArrayList<>();

		for (int x = baseX - REGION_COUNT; x <= baseX + REGION_COUNT; x++) {
			for (int y = baseY - REGION_COUNT; y <= baseY + REGION_COUNT; y++) {
				RegionCoordinates coordinates = new RegionCoordinates(x, y);

				RegionUpdateMode local = mode;
				if (mode == RegionUpdateMode.DIFFERENCE && newRegions.contains(coordinates)) {
					local = RegionUpdateMode.FULL;

					player.send(new ClearRegionMessage(position, coordinates));
				}

				Optional<GroupedRegionUpdateMessage> message = toUpdateMessage(local, player.getLastKnownRegion(), coordinates, repository);
				if (message.isPresent()) {
					messages.add(message.get());
				}
			}
		}

		messages.forEach(player::send);
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

		return deltaX <= Position.MAX_DISTANCE || deltaX >= VIEWPORT_WIDTH - Position.MAX_DISTANCE - 1 || deltaY <= Position.MAX_DISTANCE || deltaY >= VIEWPORT_WIDTH - Position.MAX_DISTANCE - 1;
	}

	/**
	 * Creates a {@link GroupedRegionUpdateMessage} using the specified {@link RegionUpdateMode}, returning
	 * {@link Optional#empty()} if no update message is required.
	 *
	 * @param mode The RegionUpdateMode for the Message.
	 * @param lastKnownRegion The last known region {@link Position} of the Player.
	 * @param coordinates The {@link RegionCoordinates} of the {@link Region}.
	 * @param repository The {@link RegionRepository} containing the Regions.
	 * @return The Optional containing the GroupedRegionUpdateMessage.
	 */
	private Optional<GroupedRegionUpdateMessage> toUpdateMessage(RegionUpdateMode mode, Position lastKnownRegion, RegionCoordinates coordinates, RegionRepository repository) {
		List<RegionUpdateMessage> messages;

		/*
		 * Here we used Map#computeIfAbsent because the value may have been inserted into the Map by another thread
		 * after our Map#get call. This is done in two separate parts (rather than acquiring the lock every time, and
		 * just calling Map#computeIfAbsent immediately) for performance - once the List<RegionUpdateMessage> has been
		 * placed into the Map once, it will never be changed, and is therefore a read-only operation after this. The
		 * alternative, acquiring the lock for every access, would be very slow.
		 */

		switch (mode) {
			case DIFFERENCE:
				messages = updates.get(coordinates);
				if (messages == null) {
					synchronized (updates) {
						messages = updates.computeIfAbsent(coordinates, coords -> repository.get(coords).getUpdates(lastKnownRegion.getHeight()));
					}
				}

				break;
			case FULL:
				messages = snapshots.get(coordinates);
				if (messages == null) {
					synchronized (snapshots) {
						messages = snapshots.computeIfAbsent(coordinates, coords -> repository.get(coords).getSnapshot(lastKnownRegion.getHeight()));
					}
				}

				break;
			default:
				throw new IllegalArgumentException("Unrecognised RegionUpdateMode " + mode + ".");
		}

		return messages.isEmpty() ? Optional.empty() : Optional.of(new GroupedRegionUpdateMessage(lastKnownRegion, coordinates, messages));
	}

}