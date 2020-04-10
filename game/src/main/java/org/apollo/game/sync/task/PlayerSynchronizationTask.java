package org.apollo.game.sync.task;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import org.apollo.game.message.impl.PlayerSynchronizationMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.PlayerUpdateInfo;
import org.apollo.game.sync.block.AppearanceBlock;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacketBuilder;

/**
 * A {@link SynchronizationTask} which synchronizes the specified {@link Player} .
 *
 * @author Graham
 */
public final class PlayerSynchronizationTask extends SynchronizationTask {

	/**
	 * The maximum amount of local players.
	 */
	private static final int MAXIMUM_LOCAL_PLAYERS = 255;

	/**
	 * The maximum number of players to load per cycle. This prevents the update packet from becoming too large (the
	 * client uses a 5000 byte buffer) and also stops old spec PCs from crashing when they login or teleport.
	 */
	private static final int NEW_PLAYERS_PER_CYCLE = 20;

	/**
	 * Contains packed location update data.
	 */
	private static final int[][] PACKED_LOCATION_UPDATE1 = new int[][]{{0, 3, 5}, {1, -1, 6}, {2, 4, 7}};
	/**
	 * Contains packed location update data.
	 */
	private static final int[][] PACKED_LOCATION_UPDATE3 = new int[][]{{0, 5, 7, 9, 11}, {1, -1, -1, -1, 12}, {2, -1, -1, -1, 13}, {3, -1, -1, -1, 14}, {4, 6, 8, 10, 15}};

	/**
	 * The Player.
	 */
	private final Player player;

	private final GamePacketBuilder[] nsnBuilders = new GamePacketBuilder[4];
	private final GamePacketBuilder blockBuilder = new GamePacketBuilder();

	/**
	 * Creates the {@link PlayerSynchronizationTask} for the specified {@link Player}.
	 *
	 * @param player The Player.
	 */
	public PlayerSynchronizationTask(Player player) {
		this.player = player;
		for (int index = 0; index < nsnBuilders.length; index++) {
			nsnBuilders[index] = new GamePacketBuilder();
		}
	}

	@Override
	public void run() {
		final var counts = new short[4];

		final var localPlayers = player.getLocalPlayerList();
		final var distance = player.getViewingDistance();
		final var appearanceTickets = player.getAppearanceTickets();

		final var repoPlayer = player.getWorld().getPlayerRepository();
		final var repoRegion = player.getWorld().getRegionRepository();

		final var current = repoRegion.fromPosition(player.getPosition());
		final var regions = current.getSurrounding();
		regions.add(current.getCoordinates());

		final var info = player.getPlayerUpdateInfo();

		for (var builder : nsnBuilders) {
			builder.switchToBitAccess();
		}

		// Determine which local players need to be removed.
		final var toRemove = new IntOpenHashSet();
		final var position = player.getPosition();
		for (var iterator = localPlayers.iterator(); iterator.hasNext(); ) {
			final var index = iterator.nextInt();
			final var remove = removable(position, distance, repoPlayer.get(index));
			if (remove) {
				toRemove.add(index);
			}
		}

		// Determine which local players need to be added.
		final var toAdd = new IntOpenHashSet();
		outer:
		for (var coordinates : regions) {
			var region = repoRegion.get(coordinates);
			var players = region.<Player>getEntities(EntityType.PLAYER);
			var iterator = players.iterator();
			while (iterator.hasNext()) {
				if (toAdd.size() + localPlayers.size() >= MAXIMUM_LOCAL_PLAYERS) {
					player.flagExcessivePlayers();
					break outer;
				}

				final var other = iterator.next();
				final var index = other.getIndex();
				if (index != player.getIndex() && other.getPosition()
						.isWithinDistance(position, distance) && !localPlayers.contains(index)) {
					toAdd.add(index);
				}
			}
		}

		for (var index = 0; index < 2048; index++) {
			final var firstPass = (info.getSkip(index) & 0x1) == 0;
			final var other = repoPlayer.get(index);
			final var nsn = other != null && (localPlayers.contains(index) || player
					.getIndex() == index) ? firstPass ? 0 : 1 : firstPass ? 2 : 3;
			final var low = nsn == 2 || nsn == 3;

			if (low) {
				if (toAdd.contains(index)) {
					SynchronizationBlockSet blockSet = other.getBlockSet();
					if (!blockSet.contains(AppearanceBlock.class) && !hasCachedAppearance(appearanceTickets, index,
							other.getAppearanceTicket())) {
						blockSet = blockSet.clone();
						blockSet.add(SynchronizationBlock.createAppearanceBlock(other));
					}
					addPlayer(info, nsn, index, other.getPosition(), blockSet);
				} else {
					if (other == null) {
						lowrez(nsn, index, info, null);
					} else {
						lowrez(nsn, index, info, other.getPosition());
					}
				}
			} else {
				SynchronizationBlockSet blockSet = other.getBlockSet();
				if (!blockSet.contains(AppearanceBlock.class) && !hasCachedAppearance(appearanceTickets, index,
						other.getAppearanceTicket())) {
					blockSet = blockSet.clone();
					blockSet.add(SynchronizationBlock.createAppearanceBlock(other));
				}

				final var remove = toRemove.contains(index);
				if (remove) {
					localPlayers.remove(index);
				}
				highrez(info, nsn, other, remove);
			}

			info.setSkip(index, (byte) (info.getSkip(index) >> 1));
		}

		for (var nsn = 0; nsn < nsnBuilders.length; nsn++) {
			skipFooter(info, nsn, counts[nsn]);
		}

		player.send(new PlayerSynchronizationMessage(nsnBuilders, blockBuilder));
	}

	private void addPlayer(PlayerUpdateInfo info, int nsn, int index, Position position,
						   SynchronizationBlockSet blockSet) {
		final var main = nsnBuilders[nsn];
		main.putBits(2, 0);

		if (info.getExternalPosition(index) != position.hashCode()) {
			main.putBit(1);
			lowrez(nsn, index, info, position);
		} else {
			main.putBit(0);
		}

		main.putBits(26, position.getY() << 13 | position.getX());
		flagBlockUpdate(nsn, info, blockSet);
	}

	private void highrez(PlayerUpdateInfo info, int nsn, Player other, boolean remove) {
		final var main = nsnBuilders[nsn];
		final var directions = other.getDirections();
		final var nextUpdateType = other.isTeleporting() ? 3 : directions.length;
		final var position = other.getPosition();

		SynchronizationBlockSet blockSet = other.getBlockSet();
		if (!blockSet.contains(AppearanceBlock.class) && !hasCachedAppearance(other.getAppearanceTickets(),
				other.getIndex() - 1, other.getAppearanceTicket())) {
			blockSet = blockSet.clone();
			blockSet.add(SynchronizationBlock.createAppearanceBlock(other));
		}

		flagBlockUpdate(nsn, info, blockSet);
		main.putBits(2, nextUpdateType);

		if (remove) {
			main.putBit(1);
			lowrez(nsn, other.getIndex(), info, position);
		} else if (nextUpdateType == 1) {
			main.putBits(3, directions[0].toInteger());
		} else if (nextUpdateType == 2) {
			main.putBits(4, directions[1].toInteger());
		} else if (nextUpdateType == 3) {
			final var nextUpdateDeltaX = player.getPosition().getX() - position.getX();
			final var nextUpdateDeltaY = player.getPosition().getY() - position.getY();
			final var nextUpdateDeltaPlane = player.getPosition().getHeight() - position.getHeight();
			if (Math.abs(nextUpdateDeltaX) < 16 && Math.abs(nextUpdateDeltaY) < 16) {
				main.putBits(12,
						((nextUpdateDeltaX & 0x1f) << 5) + (nextUpdateDeltaY & 0x1f) + ((nextUpdateDeltaPlane & 0x3) << 10));
			} else {
				main.putBits(1, 1);
				main.putBits(30,
						((nextUpdateDeltaX & 0x3FFF) << 14) + (nextUpdateDeltaY & 0x3FFF) + ((nextUpdateDeltaPlane & 0x3) << 28));
			}
		}
	}

	private void lowrez(int nsn, int index, PlayerUpdateInfo info, Position position) {
		final var main = nsnBuilders[nsn];
		final var lastPosition = info.getExternalPosition(index);
		if (position != null) {
			final var currentPosition = position.hashCode();
			if (lastPosition != currentPosition) {
				info.setExternalPositions(index, currentPosition);

				final var lastY = lastPosition & 0xFF;
				final var lastX = lastPosition >> 8 & 0xFF;
				final var lastPlane = lastPosition >> 16;

				final var currentY = currentPosition & 0xFF;
				final var currentX = currentPosition >> 8 & 0xFF;
				final var currentPlane = currentPosition >> 16;

				final var yOffset = currentY - lastY;
				final var xOffset = currentX - lastX;
				final var planeOffset = (currentPlane - lastPlane) & 0x3;

				if (currentX == lastX && currentY == lastY) {
					main.putBits(2, 1);
					main.putBits(2, planeOffset);
				} else if (Math.abs(xOffset) <= 1 && Math.abs(yOffset) <= 1) {
					main.putBits(2, 2);
					main.putBits(5,
							(PACKED_LOCATION_UPDATE1[xOffset + 1][yOffset + 1] & 0x7) + ((planeOffset & 0x3) << 3));
				} else {
					main.putBits(2, 3);
					main.putBits(18, ((xOffset & 0xFF) << 8) + (yOffset & 0xFF) + ((planeOffset & 0x3) << 16));
				}
				return;
			}
		}

		main.putBits(2, 0);
	}

	private void flagBlockUpdate(int nsn, PlayerUpdateInfo info, SynchronizationBlockSet blockSet) {
		final var buffer = nsnBuilders[nsn];
		if (blockSet.size() == 0) {
			buffer.putBit(0);
			return;
		}

		buffer.putBit(1);

		var flag = 0;
		//accumulate flag here.

		if (flag >= 0xFF) {
			flag |= 0x8;
		}

		blockBuilder.put(DataType.BYTE, flag);

		if (flag >= 0xFF) {
			blockBuilder.put(DataType.BYTE, flag >> 8);
		}

		// write the blocks here.

	}

	private void skipBody(PlayerUpdateInfo info, int nsn, int skips) {
		final var builder = nsnBuilders[nsn];
		if (skips != 0) {
			builder.putBit(0);
			skip(builder, skips - 1);
		}
		builder.putBit(1);
	}

	private void skipFooter(PlayerUpdateInfo info, int nsn, int skips) {
		final var builder = nsnBuilders[nsn];
		if (skips == 0) {
			builder.switchToByteAccess();
			return;
		}
		builder.putBit(0);
		skip(builder, skips - 1);
		builder.switchToByteAccess();
	}

	private void skip(GamePacketBuilder builder, int skips) {
		if (skips < 1) {
			builder.putBits(2, 0);
		} else if (skips < 32) {
			builder.putBits(2, 1);
			builder.putBits(5, skips);
		} else if (skips < 256) {
			builder.putBits(2, 2);
			builder.putBits(8, skips);
		} else {
			builder.putBits(2, 3);
			builder.putBits(11, skips);
		}
	}

	/**
	 * Tests whether or not the specified Player has a cached appearance within
	 * the specified appearance ticket array.
	 *
	 * @param appearanceTickets The appearance tickets.
	 * @param index             The index of the Player.
	 * @param appearanceTicket  The current appearance ticket for the Player.
	 * @return {@code true} if the specified Player has a cached appearance
	 * otherwise {@code false}.
	 */
	private boolean hasCachedAppearance(int[] appearanceTickets, int index, int appearanceTicket) {
		if (appearanceTickets[index] != appearanceTicket) {
			appearanceTickets[index] = appearanceTicket;
			return false;
		}

		return true;
	}

	/**
	 * Returns whether or not the specified {@link Player} should be removed.
	 *
	 * @param position The {@link Position} of the Player being updated.
	 * @param other    The Player being tested.
	 * @return {@code true} iff the specified Player should be removed.
	 */
	private boolean removable(Position position, int distance, Player other) {
		if (other == null || other.isTeleporting() || !other.isActive()) {
			return true;
		}

		Position otherPosition = other.getPosition();
		return otherPosition.getLongestDelta(position) > distance || !otherPosition
				.isWithinDistance(position, distance);
	}

}