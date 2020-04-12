package org.apollo.game.sync.task;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.apollo.cache.def.EquipmentDefinition;
import org.apollo.game.message.impl.PlayerSynchronizationMessage;
import org.apollo.game.model.*;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.EquipmentConstants;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.PlayerUpdateInfo;
import org.apollo.game.model.entity.setting.Gender;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.sync.block.*;
import org.apollo.game.sync.block.TitleBlock;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.util.BufferUtil;
import org.jetbrains.annotations.NotNull;

import static org.apollo.game.model.entity.PlayerUpdateInfo.MAXIMUM_LOCAL_PLAYERS;

/**
 * A {@link SynchronizationTask} which synchronizes the specified {@link Player} .
 *
 * @author Kris
 * @author Dragonkk
 * @author Cjay0091
 */
public final class PlayerSynchronizationTask extends SynchronizationTask {

	private static final int[][] PACKED_LOCATION_UPDATE2 = new int[][]{{0, 3, 5}, {1, -1, 6}, {2, 4, 7}};

	private final Player player;
	private final PlayerUpdateInfo info;
	private final World world;
	private final ObjectSet<Player> limitedPlayers;

	private ObjectOpenHashSet<Player> added;

	public PlayerSynchronizationTask(Player player) {
		this.player = player;
		this.info = player.getUpdateInfo();
		this.world = player.getWorld();

		this.limitedPlayers = player.getLocalPlayerList();
		this.added = new ObjectOpenHashSet<>();
	}

	@Override
	public void run() {
		info.buffer = new GamePacketBuilder(79, PacketType.VARIABLE_SHORT);
		info.largeMaskBuffer = new GamePacketBuilder();
		limitedPlayers.clear();
		prefetch();
		processLocalPlayers(true);
		processLocalPlayers(false);
		processOutsidePlayers(true);
		processOutsidePlayers(false);
		info.buffer.putBytes(info.largeMaskBuffer);
		info.localIndexesCount = 0;
		info.externalIndexesCount = 0;
		for (var playerIndex = 1; playerIndex < 2048; playerIndex++) {
			info.activityFlags[playerIndex] >>= 1;
			if (info.localPlayers[playerIndex] == null) {
				info.externalIndexes[info.externalIndexesCount++] = playerIndex;
			} else {
				info.localIndexes[info.localIndexesCount++] = playerIndex;
			}
		}
		player.send(new PlayerSynchronizationMessage(info.buffer));
	}

	/**
	 * Prefetches a list of players closest to us when there are more than 255 players in the viewport.
	 * TODO: Convert the LocationMap to function at the chunk level. No reason to separate them.
	 */
	private void prefetch() {
		limitedPlayers.clear();

		// Process which players need to be added.
		final var position = player.getPosition();
		RegionRepository repository = world.getRegionRepository();
		Region current = repository.fromPosition(position);

		final var regions = current.getSurrounding();
		regions.add(current.getCoordinates());

		regions.stream().map(repository::get).forEach(region -> {
			var players = region.<Player>getEntities(EntityType.PLAYER);
			var iterator = players.iterator();
			while (iterator.hasNext()) {
				if (limitedPlayers.size() >= MAXIMUM_LOCAL_PLAYERS) {
					player.flagExcessivePlayers();
					break;
				}

				Player other = iterator.next();
				Position local = other.getPosition();

				if (player != other && other.isActive() && local
						.isWithinDistance(position, player.getViewingDistance())) {
					limitedPlayers.add(other);
				}
			}
		});
	}

	/**
	 * @param p           the player being removed.
	 * @param playerIndex the index of the player being removed. If the player needs to be removed and the index
	 *                    isn't -1, we also putBits the necessary information in the buffer.
	 * @return whether or not the requested player needs to be removed from the local players list.
	 */
	private boolean remove(@NotNull final Player p, final int playerIndex) {
		if (p == player) {
			return false;
		}
		if (!limitedPlayers.contains(p)) {
			if (playerIndex != -1) {
				info.buffer.putBits(1, 1);
				info.buffer.putBits(1, 0);
				info.buffer.putBits(2, 0);
				final var hash = p.getPosition().get18BitHash();
				final var previousHash = p.getOldPosition().get18BitHash();
				if (hash == previousHash) {
					info.buffer.putBits(1, 0);
				} else {
					info.buffer.putBits(1, 1);
					updatePositionMultiplier(previousHash, hash);
				}
				info.localPlayers[playerIndex] = null;
			}
			return true;
		}
		return false;
	}

	/**
	 * @param p           the player being added.
	 * @param playerIndex the index of the player being added. If the player needs to be added and the index isn't
	 *                    -1, we also putBits the necessary information in the buffer.
	 * @return whether or not the requested player needs to be added to the local players list.
	 */
	private boolean add(final Player p, final int playerIndex) {
		if (limitedPlayers.contains(p)) {
			if (playerIndex != -1) {
				info.buffer.putBits(1, 1);
				info.buffer.putBits(2, 0);
				final var hash = p.getPosition().get18BitHash();
				final var previousHash = p.getOldPosition().get18BitHash();
				if (hash == previousHash) {
					info.buffer.putBits(1, 0);
				} else {
					info.buffer.putBits(1, 1);
					updatePositionMultiplier(previousHash, hash);
				}
				info.buffer.putBits(13, p.getPosition().getX());
				info.buffer.putBits(13, p.getPosition().getY());

				final var blockSet = getBlockSet(p);
				final var updateMasks = blockSet.size() > 0;
				info.buffer.putBits(1, updateMasks ? 1 : 0);
				if (updateMasks) {
					appendUpdateBlock(p, blockSet, true);
				}
				info.localPlayers[p.getIndex()] = p;
				info.activityFlags[playerIndex] = (byte) (info.activityFlags[playerIndex] | 0x2);
			}
			return true;
		}
		return false;
	}

	/**
	 * Appends the position changes of the requested player. As of right now, the last if-block is never reached as
	 * the offsets can never exceed value 1.
	 *
	 * @param lastPosition    the last position multiplier transmitted to the client.
	 * @param currentPosition the current position multiplier.
	 */
	private void updatePositionMultiplier(final int lastPosition, final int currentPosition) {
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
			info.buffer.putBits(2, 1);
			info.buffer.putBits(2, planeOffset);
		} else if (Math.abs(xOffset) <= 1 && Math.abs(yOffset) <= 1) {
			info.buffer.putBits(2, 2);
			info.buffer.putBits(2, planeOffset);
			info.buffer.putBits(3,
					PACKED_LOCATION_UPDATE2[xOffset + 1][yOffset + 1]);// Utils.getMoveDirection(xOffset, yOffset));
		} else {
			info.buffer.putBits(2, 3);
			info.buffer.putBits(2, planeOffset);
			info.buffer.putBits(8, xOffset & 0xFF);
			info.buffer.putBits(8, yOffset & 0xFF);
		}
	}

	/**
	 * Processes the players outside of our viewport; either adds, updates or skips them if necessary to do so.
	 *
	 * @param inactivePlayers whether or not we loop the inactive or active players.
	 */
	private void processOutsidePlayers(final boolean inactivePlayers) {
		info.buffer.switchToBitAccess();

		var skip = 0;
		for (var i = 0; i < info.externalIndexesCount; i++) {
			final var playerIndex = info.externalIndexes[i];
			if (inactivePlayers == ((0x1 & info.activityFlags[playerIndex]) == 0)) {
				continue;
			}
			if (skip > 0) {
				skip--;
				info.activityFlags[playerIndex] = (byte) (info.activityFlags[playerIndex] | 0x2);
				continue;
			}
			final var p = world.getPlayerRepository().get(playerIndex);
			if (!add(p, playerIndex)) {
				final var hash = p == null ? 0 : p.getPosition().get18BitHash();
				final var previousHash = p == null ? 0 : p.getOldPosition().get18BitHash();
				if (hash != previousHash) {
					info.buffer.putBits(1, 1);
					updatePositionMultiplier(previousHash, hash);
				} else {
					info.buffer.putBits(1, 0);
					skip(skip += getSkippedExternalPlayers(i, inactivePlayers));
					info.activityFlags[playerIndex] = (byte) (info.activityFlags[playerIndex] | 0x2);
				}
			}
		}
		info.buffer.switchToByteAccess();

		if (skip != 0) {
			throw new IllegalStateException(inactivePlayers ? "NSN2" : "NSN3");
		}
	}

	/**
	 * @param index           the current index in the loop.
	 * @param inactivePlayers whether we check inactive or active players.
	 * @return the amount of external players we can skip.
	 */
	private int getSkippedExternalPlayers(final int index, final boolean inactivePlayers) {
		int skip = 0;
		for (var i = index + 1; i < info.externalIndexesCount; i++) {
			final var externalIndex = info.externalIndexes[i];
			if (inactivePlayers == ((0x1 & info.activityFlags[externalIndex]) == 0)) {
				continue;
			}
			final var externalPlayer = world.getPlayerRepository().get(externalIndex);
			final var externalHash = externalPlayer == null ? 0 : externalPlayer.getPosition().get18BitHash();
			final var previousExternalHash = externalPlayer == null ? 0 : externalPlayer.getOldPosition()
					.get18BitHash();
			if (add(externalPlayer, -1) || externalHash != previousExternalHash) {
				break;
			}
			skip++;
		}
		return skip;
	}

	/**
	 * Processes the players inside of our viewport; either removes, updates or skips them if necessary to do so.
	 *
	 * @param inactivePlayers whether or not we loop the inactive or active players.
	 */
	private void processLocalPlayers(final boolean inactivePlayers) {
		info.buffer.switchToBitAccess();

		var skip = 0;
		for (var i = 0; i < info.localIndexesCount; i++) {
			final var playerIndex = info.localIndexes[i];
			if (inactivePlayers == ((0x1 & info.activityFlags[playerIndex]) != 0)) {
				continue;
			}
			if (skip > 0) {
				skip--;
				info.activityFlags[playerIndex] = (byte) (info.activityFlags[playerIndex] | 0x2);
				continue;
			}
			final var p = info.localPlayers[playerIndex];

			if (!remove(p, playerIndex)) {
				final var directions = p.getDirections();
				final var blockSet = getBlockSet(p);
				var update = blockSet.size() > 0;
				if (update) {
					appendUpdateBlock(p, blockSet, false);
				}

				final var teleported = p.isTeleporting();
				if (teleported || directions.length > 0) {
					info.buffer.putBits(1, 1);
					info.buffer.putBits(1, update ? 1 : 0);
					info.buffer.putBits(2, 3);
					final var location = p.getPosition();
					final var lastLocation = p.getOldPosition();
					final var xOffset = location.getX() - lastLocation.getX();
					final var yOffset = location.getY() - lastLocation.getY();
					final var planeOffset = location.getHeight() - lastLocation.getHeight();
					if (Math.abs(xOffset) < 16 && Math.abs(yOffset) < 16) {
						info.buffer.putBits(1, 0);
						info.buffer.putBits(2, planeOffset & 0x3);
						info.buffer.putBits(5, xOffset & 0x1F);
						info.buffer.putBits(5, yOffset & 0x1F);
					} else {
						info.buffer.putBits(1, 1);
						info.buffer.putBits(2, planeOffset & 0x3);
						info.buffer.putBits(14, xOffset & 0x3FFF);
						info.buffer.putBits(14, yOffset & 0x3FFF);
					}
				} else if (update) {
					info.buffer.putBits(1, 1);
					info.buffer.putBits(1, 1);
					info.buffer.putBits(2, 0);
				} else {
					info.buffer.putBits(1, 0);
					skip(skip += getSkippedLocalPlayers(i, inactivePlayers));
					info.activityFlags[playerIndex] = (byte) (info.activityFlags[playerIndex] | 0x2);
				}
			}
		}
		info.buffer.switchToByteAccess();

		if (skip != 0) {
			throw new IllegalStateException(inactivePlayers ? "NSN0" : "NSN1");
		}
	}

	/**
	 * @param index           the current index in the loop.
	 * @param inactivePlayers whether we check inactive or active players.
	 * @return the amount of local players we can skip.
	 */
	private int getSkippedLocalPlayers(final int index, final boolean inactivePlayers) {
		int skip = 0;
		for (var i = index + 1; i < info.localIndexesCount; i++) {
			final var localIndex = info.localIndexes[i];
			if (inactivePlayers == ((0x1 & info.activityFlags[localIndex]) != 0)) {
				continue;
			}

			final var p = info.localPlayers[localIndex];
			if (remove(p, -1)) {
				break;
			}

			final var directions = p.getDirections();
			if (directions.length > 0) {
				break;
			}

			final var blockSet = getBlockSet(p);
			if (blockSet.size() > 0) {
				break;
			}
			skip++;
		}
		return skip;
	}

	/**
	 * Write the amount of players skipped into the buffer.
	 *
	 * @param count the amount of players skipped.
	 */
	private void skip(final int count) {
		if (count == 0) {
			info.buffer.putBits(2, 0);
		} else if (count < 32) {
			info.buffer.putBits(2, 1);
			info.buffer.putBits(5, count);
		} else if (count < 256) {
			info.buffer.putBits(2, 2);
			info.buffer.putBits(8, count);
		} else {
			info.buffer.putBits(2, 3);
			info.buffer.putBits(11, count);
		}
	}

	/**
	 * Appends the update block into the small mask buffer, and then into the large mask buffer.
	 *
	 * @param p        the player whose block is being written.
	 * @param blockSet
	 * @param added    whether or not the player was added to the viewport during this cycle.
	 */
	private void appendUpdateBlock(final Player p, SynchronizationBlockSet blockSet, final boolean added) {
		var flag = 0;

		if (blockSet.contains(HitUpdateBlock.class)) {
			flag |= 0x40;
		}
		if (blockSet.contains(GraphicBlock.class)) {
			flag |= 0x200;
		}
		if (blockSet.contains(TemporaryMovementTypeBlock.class)) {
			flag |= 0x1000;
		}
		if (blockSet.contains(ForceMovementBlock.class)) {
			flag |= 0x400;
		}
		if (blockSet.contains(ForceChatBlock.class)) {
			flag |= 0x20;
		}
		if (blockSet.contains(TurnToPositionBlock.class)) {
			flag |= 0x4;
		}
		if (blockSet.contains(AppearanceBlock.class)) {
			flag |= 0x1;
		}
		if (blockSet.contains(InteractingMobBlock.class)) {
			flag |= 0x2;
		}
		if (blockSet.contains(MovementTypeBlock.class)) {
			flag |= 0x800;
		}
		if (blockSet.contains(ChatBlock.class)) {
			flag |= 0x10;
		}
		if (blockSet.contains(TitleBlock.class)) {
			flag |= 0x4;
		}
		if (blockSet.contains(AnimationBlock.class)) {
			flag |= 0x80;
		}

		if (flag >= 0xFF) {
			flag |= 0x8;
		}

		info.largeMaskBuffer.put(DataType.BYTE, flag);

		if (flag >= 0xFF) {
			info.largeMaskBuffer.put(DataType.BYTE, flag >> 8);
		}

		if (blockSet.contains(HitUpdateBlock.class)) {
			putHitUpdateBlock(blockSet.get(HitUpdateBlock.class), info.largeMaskBuffer);
		}
		if (blockSet.contains(GraphicBlock.class)) {
			putGraphicBlock(blockSet.get(GraphicBlock.class), info.largeMaskBuffer);
		}
		if (blockSet.contains(TemporaryMovementTypeBlock.class)) {
			putTemporaryMovementTypeBlock(blockSet.get(TemporaryMovementTypeBlock.class), info.largeMaskBuffer);
		}
		if (blockSet.contains(ForceMovementBlock.class)) {
			putForceMovementBlock(blockSet.get(ForceMovementBlock.class), info.largeMaskBuffer);
		}
		if (blockSet.contains(ForceChatBlock.class)) {
			putForceChatBlock(blockSet.get(ForceChatBlock.class), info.largeMaskBuffer);
		}
		if (blockSet.contains(TurnToPositionBlock.class)) {
			putTurnToPositionBlock(blockSet.get(TurnToPositionBlock.class), info.largeMaskBuffer);
		}
		if (blockSet.contains(AppearanceBlock.class)) {
			putAppearanceBlock(blockSet.get(AppearanceBlock.class), info.largeMaskBuffer);
		}
		if (blockSet.contains(InteractingMobBlock.class)) {
			putInteractingMobBlock(blockSet.get(InteractingMobBlock.class), info.largeMaskBuffer);
		}
		if (blockSet.contains(MovementTypeBlock.class)) {
			putMovementTypeBlock(blockSet.get(MovementTypeBlock.class), info.largeMaskBuffer);
		}
		if (blockSet.contains(ChatBlock.class)) {
			putChatBlock(blockSet.get(ChatBlock.class), info.largeMaskBuffer);
		}
		if (blockSet.contains(TitleBlock.class)) {
			putTitleBlock(blockSet.get(TitleBlock.class), info.largeMaskBuffer);
		}
		if (blockSet.contains(AnimationBlock.class)) {
			putAnimationBlock(blockSet.get(AnimationBlock.class), info.largeMaskBuffer);
		}
	}

	/**
	 * Puts an Appearance block into the specified builder.
	 *
	 * @param block   The block.
	 * @param builder The builder.
	 */
	private static void putAppearanceBlock(AppearanceBlock block, GamePacketBuilder builder) {
		Appearance appearance = block.getAppearance();
		ByteBuf playerProperties = Unpooled.buffer();

		playerProperties.writeByte(appearance.getGender().toInteger());
		playerProperties.writeByte(block.isSkulled() ? 1 : -1);
		playerProperties.writeByte(block.getHeadIcon());

		if (block.appearingAsNpc()) {
			playerProperties.writeShort(0xFFFF);
			playerProperties.writeShort(block.getNpcId());
		} else {
			Inventory equipment = block.getEquipment();
			int[] style = appearance.getStyle();
			Item item, chest, helm;

			for (int slot = 0; slot < 4; slot++) {
				if ((item = equipment.get(slot)) != null) {
					playerProperties.writeShort(0x200 + item.getId());
				} else {
					playerProperties.writeByte(0);
				}
			}

			if ((chest = equipment.get(EquipmentConstants.CHEST)) != null) {
				playerProperties.writeShort(0x200 + chest.getId());
			} else {
				playerProperties.writeShort(0x100 + style[2]);
			}

			if ((item = equipment.get(EquipmentConstants.SHIELD)) != null) {
				playerProperties.writeShort(0x200 + item.getId());
			} else {
				playerProperties.writeByte(0);
			}

			if (chest != null) {
				EquipmentDefinition def = EquipmentDefinition.lookup(chest.getId());
				if (def != null && !def.isFullBody()) {
					playerProperties.writeShort(0x100 + style[3]);
				} else {
					playerProperties.writeByte(0);
				}
			} else {
				playerProperties.writeShort(0x100 + style[3]);
			}

			if ((item = equipment.get(EquipmentConstants.LEGS)) != null) {
				playerProperties.writeShort(0x200 + item.getId());
			} else {
				playerProperties.writeShort(0x100 + style[5]);
			}

			if ((helm = equipment.get(EquipmentConstants.HAT)) != null) {
				EquipmentDefinition def = EquipmentDefinition.lookup(helm.getId());
				if (def != null && !def.isFullHat() && !def.isFullMask()) {
					playerProperties.writeShort(0x100 + style[0]);
				} else {
					playerProperties.writeByte(0);
				}
			} else {
				playerProperties.writeShort(0x100 + style[0]);
			}

			if ((item = equipment.get(EquipmentConstants.HANDS)) != null) {
				playerProperties.writeShort(0x200 + item.getId());
			} else {
				playerProperties.writeShort(0x100 + style[4]);
			}

			if ((item = equipment.get(EquipmentConstants.FEET)) != null) {
				playerProperties.writeShort(0x200 + item.getId());
			} else {
				playerProperties.writeShort(0x100 + style[6]);
			}

			EquipmentDefinition def = null;
			if (helm != null) {
				def = EquipmentDefinition.lookup(helm.getId());
			}
			if (def != null && (def.isFullMask()) || appearance.getGender() == Gender.FEMALE) {
				playerProperties.writeByte(0);
			} else {
				playerProperties.writeShort(0x100 + style[1]);
			}
		}

		int[] colors = appearance.getColors();
		for (int color : colors) {
			playerProperties.writeByte(color);
		}

		playerProperties.writeShort(0x328); // stand
		playerProperties.writeShort(0x337); // stand turn
		playerProperties.writeShort(0x333); // walk
		playerProperties.writeShort(0x334); // turn 180
		playerProperties.writeShort(0x335); // turn 90 cw
		playerProperties.writeShort(0x336); // turn 90 ccw
		playerProperties.writeShort(0x338); // run

		BufferUtil.writeString(playerProperties, block.getName());
		playerProperties.writeByte(block.getCombatLevel());
		playerProperties.writeShort(0);//block.getSkillLevel());
		playerProperties.writeByte(0);

		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, playerProperties.writableBytes());
		for (int index = 0; index < playerProperties.writableBytes(); index++) {
			builder.put(DataType.BYTE, DataTransformation.ADD, playerProperties.getByte(index));
		}
	}

	/**
	 * Puts a chat block into the specified builder.
	 *
	 * @param block   The block.
	 * @param builder The builder.
	 */
	private static void putChatBlock(ChatBlock block, GamePacketBuilder builder) {
		byte[] bytes = block.getCompressedMessage();

		builder.put(DataType.SHORT, DataTransformation.ADD, block.getTextEffects() << 8 | block.getTextColor());
		builder.put(DataType.BYTE, block.getPrivilegeLevel().toInteger());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, block.getType().ordinal());
		builder.put(DataType.BYTE, bytes.length);
		builder.putBytes(bytes);
	}

	/**
	 * Puts a force chat block into the specified builder.
	 *
	 * @param block   The block.
	 * @param builder The builder.
	 */
	private static void putForceChatBlock(ForceChatBlock block, GamePacketBuilder builder) {
		builder.putString(block.getMessage());
	}

	/**
	 *
	 */
	private static void putTitleBlock(TitleBlock block, GamePacketBuilder builder) {
		var titles = block.getTitles();
		for (var positions : TitleBlock.TitlePosition.values()) {
			builder.putString(titles.getOrDefault(positions, ""));
		}
	}

	/**
	 * Puts a force movement block into the specified builder.
	 *
	 * @param block   The block.
	 * @param builder The builder.
	 */
	private static void putForceMovementBlock(ForceMovementBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getInitialX());
		builder.put(DataType.BYTE, DataTransformation.ADD, block.getInitialY());
		builder.put(DataType.BYTE, DataTransformation.ADD, block.getFinalX());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getFinalY());
		builder.put(DataType.SHORT, DataTransformation.ADD, block.getTravelDurationX());
		builder.put(DataType.SHORT, DataOrder.LITTLE, block.getTravelDurationY());
		builder.put(DataType.SHORT, block.getDirection().toInteger());
	}

	/**
	 * Puts a movement type block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putMovementTypeBlock(MovementTypeBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, DataTransformation.ADD, block.getMode().getClientValue());
	}

	/**
	 * Puts a temporary movement type block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putTemporaryMovementTypeBlock(TemporaryMovementTypeBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, DataTransformation.ADD, block.getMode().getClientValue());
	}

	/**
	 * Puts a graphic block into the specified builder.
	 *
	 * @param block   The block.
	 * @param builder The builder.
	 */
	private static void putGraphicBlock(GraphicBlock block, GamePacketBuilder builder) {
		Graphic graphic = block.getGraphic();
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, graphic.getId());
		builder.put(DataType.INT, DataOrder.MIDDLE,
				graphic.getHeight() << 16 & 0xFFFF0000 | graphic.getDelay() & 0x0000FFFF);
	}

	/**
	 * Puts a hit update block into the specified builder.
	 *
	 * @param block   The block.
	 * @param builder The builder.
	 */
	private static void putHitUpdateBlock(HitUpdateBlock block, GamePacketBuilder builder) {

	}

	/**
	 * Puts an interacting mob block into the specified builder.
	 *
	 * @param block   The block.
	 * @param builder The builder.
	 */
	private static void putInteractingMobBlock(InteractingMobBlock block, GamePacketBuilder builder) {
		builder.put(DataType.SHORT, DataTransformation.ADD, block.getIndex());
	}

	/**
	 * Puts a turn to position block into the specified builder.
	 *
	 * @param block   The block.
	 * @param builder The builder.
	 */
	private static void putTurnToPositionBlock(TurnToPositionBlock block, GamePacketBuilder builder) {
		var mobPosition = block.getMobPosition();
		var turnPosition = block.getTurnPosition();

		builder.put(DataType.SHORT, ((int) (Math.atan2(turnPosition.getX() - mobPosition.getX(),
				turnPosition.getY() - mobPosition.getY()) * 2607.5945876176133)) & 0x3fff);
	}

	/**
	 * Puts an Animation block into the specified builder.
	 *
	 * @param block   The block.
	 * @param builder The builder.
	 */
	private static void putAnimationBlock(AnimationBlock block, GamePacketBuilder builder) {
		Animation animation = block.getAnimation();
		builder.put(DataType.SHORT, animation.getId());
		builder.put(DataType.BYTE, animation.getDelay());
	}

	private SynchronizationBlockSet getBlockSet(Player p) {
		var blockSet = p.getBlockSet();
		if (!blockSet.contains(AppearanceBlock.class) && !hasCachedAppearance(player.getAppearanceTickets(),
				p.getIndex() - 1, p.getAppearanceTicket())) {
			blockSet = blockSet.clone();
			blockSet.add(SynchronizationBlock.createAppearanceBlock(p));
		}
		return blockSet;
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
}