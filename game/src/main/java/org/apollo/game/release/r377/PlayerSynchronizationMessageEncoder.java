package org.apollo.game.release.r377;

import org.apollo.cache.def.EquipmentDefinition;
import org.apollo.game.message.impl.PlayerSynchronizationMessage;
import org.apollo.game.model.*;
import org.apollo.game.model.entity.EquipmentConstants;
import org.apollo.game.model.entity.AnimationMap;
import org.apollo.game.model.entity.setting.Gender;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.sync.block.*;
import org.apollo.game.sync.seg.*;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link PlayerSynchronizationMessage}.
 *
 * @author Graham
 * @author Major
 */
public final class PlayerSynchronizationMessageEncoder extends MessageEncoder<PlayerSynchronizationMessage> {

	@Override
	public GamePacket encode(PlayerSynchronizationMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(90, PacketType.VARIABLE_SHORT);
		builder.switchToBitAccess();

		GamePacketBuilder blockBuilder = new GamePacketBuilder();

		putMovementUpdate(message.getSegment(), message, builder);
		putBlocks(message.getSegment(), blockBuilder);

		builder.putBits(8, message.getLocalPlayers());

		for (SynchronizationSegment segment : message.getSegments()) {
			SegmentType type = segment.getType();
			if (type == SegmentType.REMOVE_MOB) {
				putRemovePlayerUpdate(builder);
			} else if (type == SegmentType.ADD_MOB) {
				putAddPlayerUpdate((AddPlayerSegment) segment, message, builder);
				putBlocks(segment, blockBuilder);
			} else {
				putMovementUpdate(segment, message, builder);
				putBlocks(segment, blockBuilder);
			}
		}

		if (blockBuilder.getLength() > 0) {
			builder.putBits(11, 2047);
			builder.switchToByteAccess();
			builder.putRawBuilder(blockBuilder);
		} else {
			builder.switchToByteAccess();
		}

		return builder.toGamePacket();
	}

	/**
	 * Puts an add player update.
	 *
	 * @param seg The segment.
	 * @param message The message.
	 * @param builder The builder.
	 */
	private static void putAddPlayerUpdate(AddPlayerSegment seg, PlayerSynchronizationMessage message, GamePacketBuilder builder) {
		boolean updateRequired = seg.getBlockSet().size() > 0;
		Position player = message.getPosition();
		Position other = seg.getPosition();
		builder.putBits(11, seg.getIndex());
		builder.putBits(5, other.getX() - player.getX());
		builder.putBits(1, updateRequired ? 1 : 0);
		builder.putBits(1, 1); // discard walking queue?
		builder.putBits(5, other.getY() - player.getY());
	}

	/**
	 * Puts an Animation block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putAnimationBlock(AnimationBlock block, GamePacketBuilder builder) {
		Animation animation = block.getAnimation();
		builder.put(DataType.SHORT, animation.getId());
		builder.put(DataType.BYTE, DataTransformation.ADD, animation.getDelay());
	}

	/**
	 * Puts an Appearance block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putAppearanceBlock(AppearanceBlock block, GamePacketBuilder builder) {
		Appearance appearance = block.getAppearance();
		GamePacketBuilder playerProperties = new GamePacketBuilder();

		playerProperties.put(DataType.BYTE, appearance.getGender().toInteger());
		playerProperties.put(DataType.BYTE, block.isSkulled() ? 1 : -1);
		playerProperties.put(DataType.BYTE, block.getHeadIcon());

		if (block.appearingAsNpc()) {
			playerProperties.put(DataType.BYTE, 255);
			playerProperties.put(DataType.BYTE, 255);
			playerProperties.put(DataType.SHORT, block.getNpcId());
		} else {
			Inventory equipment = block.getEquipment();
			int[] style = appearance.getStyle();
			Item item, chest, helm;

			for (int slot = 0; slot < 4; slot++) {
				if ((item = equipment.get(slot)) != null) {
					playerProperties.put(DataType.SHORT, 0x200 + item.getId());
				} else {
					playerProperties.put(DataType.BYTE, 0);
				}
			}

			if ((chest = equipment.get(EquipmentConstants.CHEST)) != null) {
				playerProperties.put(DataType.SHORT, 0x200 + chest.getId());
			} else {
				playerProperties.put(DataType.SHORT, 0x100 + style[2]);
			}

			if ((item = equipment.get(EquipmentConstants.SHIELD)) != null) {
				playerProperties.put(DataType.SHORT, 0x200 + item.getId());
			} else {
				playerProperties.put(DataType.BYTE, 0);
			}

			if (chest != null) {
				EquipmentDefinition def = EquipmentDefinition.lookup(chest.getId());
				if (def != null && !def.isFullBody()) {
					playerProperties.put(DataType.SHORT, 0x100 + style[3]);
				} else {
					playerProperties.put(DataType.BYTE, 0);
				}
			} else {
				playerProperties.put(DataType.SHORT, 0x100 + style[3]);
			}

			if ((item = equipment.get(EquipmentConstants.LEGS)) != null) {
				playerProperties.put(DataType.SHORT, 0x200 + item.getId());
			} else {
				playerProperties.put(DataType.SHORT, 0x100 + style[5]);
			}

			if ((helm = equipment.get(EquipmentConstants.HAT)) != null) {
				EquipmentDefinition def = EquipmentDefinition.lookup(helm.getId());
				if (def != null && !def.isFullHat() && !def.isFullMask()) {
					playerProperties.put(DataType.SHORT, 0x100 + style[0]);
				} else {
					playerProperties.put(DataType.BYTE, 0);
				}
			} else {
				playerProperties.put(DataType.SHORT, 0x100 + style[0]);
			}

			if ((item = equipment.get(EquipmentConstants.HANDS)) != null) {
				playerProperties.put(DataType.SHORT, 0x200 + item.getId());
			} else {
				playerProperties.put(DataType.SHORT, 0x100 + style[4]);
			}

			if ((item = equipment.get(EquipmentConstants.FEET)) != null) {
				playerProperties.put(DataType.SHORT, 0x200 + item.getId());
			} else {
				playerProperties.put(DataType.SHORT, 0x100 + style[6]);
			}

			EquipmentDefinition def = null;
			if (helm != null) {
				def = EquipmentDefinition.lookup(helm.getId());
			}
			if (def != null && (def.isFullMask()) || appearance.getGender() == Gender.FEMALE) {
				playerProperties.put(DataType.BYTE, 0);
			} else {
				playerProperties.put(DataType.SHORT, 0x100 + style[1]);
			}
		}

		int[] colors = appearance.getColors();
		for (int color : colors) {
			playerProperties.put(DataType.BYTE, color);
		}

		AnimationMap animations = block.getAnimations();
		playerProperties.put(DataType.SHORT, animations.getStand()); // stand
		playerProperties.put(DataType.SHORT, animations.getIdleTurn()); // stand turn
		playerProperties.put(DataType.SHORT, animations.getWalking()); // walk
		playerProperties.put(DataType.SHORT, animations.getTurnAround()); // turn 180
		playerProperties.put(DataType.SHORT, animations.getTurnRight()); // turn 90 cw
		playerProperties.put(DataType.SHORT, animations.getTurnLeft()); // turn 90 ccw
		playerProperties.put(DataType.SHORT, animations.getRunning()); // run

		playerProperties.put(DataType.LONG, block.getName());
		playerProperties.put(DataType.BYTE, block.getCombatLevel());
		playerProperties.put(DataType.SHORT, block.getSkillLevel());

		builder.put(DataType.BYTE, playerProperties.getLength());
		builder.putRawBuilderReverse(playerProperties);
	}

	/**
	 * Puts the blocks for the specified segment.
	 *
	 * @param segment The segment.
	 * @param builder The block builder.
	 */
	private static void putBlocks(SynchronizationSegment segment, GamePacketBuilder builder) {
		SynchronizationBlockSet blockSet = segment.getBlockSet();
		if (blockSet.size() > 0) {
			int mask = 0;

			if (blockSet.contains(AnimationBlock.class)) {
				mask |= 0x8;
			}
			if (blockSet.contains(ForceChatBlock.class)) {
				mask |= 0x10;
			}
			if (blockSet.contains(ForceMovementBlock.class)) {
				mask |= 0x100;
			}
			if (blockSet.contains(InteractingMobBlock.class)) {
				mask |= 0x1;
			}
			if (blockSet.contains(TurnToPositionBlock.class)) {
				mask |= 0x2;
			}
			if (blockSet.contains(GraphicBlock.class)) {
				mask |= 0x200;
			}
			if (blockSet.contains(AppearanceBlock.class)) {
				mask |= 0x4;
			}
			if (blockSet.contains(SecondaryHitUpdateBlock.class)) {
				mask |= 0x400;
			}
			if (blockSet.contains(ChatBlock.class)) {
				mask |= 0x40;
			}
			if (blockSet.contains(HitUpdateBlock.class)) {
				mask |= 0x80;
			}

			if (mask >= 0x100) {
				mask |= 0x20;
				builder.put(DataType.SHORT, DataOrder.LITTLE, mask);
			} else {
				builder.put(DataType.BYTE, mask);
			}

			if (blockSet.contains(AnimationBlock.class)) {
				putAnimationBlock(blockSet.get(AnimationBlock.class), builder);
			}
			if (blockSet.contains(ForceChatBlock.class)) {
				putForceChatBlock(blockSet.get(ForceChatBlock.class), builder);
			}
			if (blockSet.contains(ForceMovementBlock.class)) {
				putForceMovementBlock(blockSet.get(ForceMovementBlock.class), builder);
			}
			if (blockSet.contains(InteractingMobBlock.class)) {
				putInteractingMobBlock(blockSet.get(InteractingMobBlock.class), builder);
			}
			if (blockSet.contains(TurnToPositionBlock.class)) {
				putTurnToPositionBlock(blockSet.get(TurnToPositionBlock.class), builder);
			}
			if (blockSet.contains(GraphicBlock.class)) {
				putGraphicBlock(blockSet.get(GraphicBlock.class), builder);
			}
			if (blockSet.contains(AppearanceBlock.class)) {
				putAppearanceBlock(blockSet.get(AppearanceBlock.class), builder);
			}
			if (blockSet.contains(SecondaryHitUpdateBlock.class)) {
				putSecondHitUpdateBlock(blockSet.get(SecondaryHitUpdateBlock.class), builder);
			}
			if (blockSet.contains(ChatBlock.class)) {
				putChatBlock(blockSet.get(ChatBlock.class), builder);
			}
			if (blockSet.contains(HitUpdateBlock.class)) {
				putHitUpdateBlock(blockSet.get(HitUpdateBlock.class), builder);
			}
		}
	}

	/**
	 * Puts a chat block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putChatBlock(ChatBlock block, GamePacketBuilder builder) {
		byte[] bytes = block.getCompressedMessage();
		builder.put(DataType.SHORT, DataOrder.LITTLE, block.getTextEffects() << 8 | block.getTextColor());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, block.getPrivilegeLevel().toInteger());
		builder.put(DataType.BYTE, DataTransformation.ADD, bytes.length);
		builder.putBytes(DataTransformation.ADD, bytes);
	}

	/**
	 * Puts a force chat block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putForceChatBlock(ForceChatBlock block, GamePacketBuilder builder) {
		builder.putString(block.getMessage());
	}

	/**
	 * Puts a force movement block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putForceMovementBlock(ForceMovementBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, DataTransformation.ADD, block.getInitialX());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, block.getInitialY());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getFinalX());
		builder.put(DataType.BYTE, block.getFinalY());
		builder.put(DataType.SHORT, block.getTravelDurationX());
		builder.put(DataType.SHORT, DataTransformation.ADD, block.getTravelDurationY());
		builder.put(DataType.BYTE, block.getDirection().toInteger());
	}

	/**
	 * Puts a graphic block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putGraphicBlock(GraphicBlock block, GamePacketBuilder builder) {
		Graphic graphic = block.getGraphic();
		builder.put(DataType.SHORT, DataTransformation.ADD, graphic.getId());
		builder.put(DataType.INT, DataOrder.MIDDLE, graphic.getHeight() << 16 & 0xFFFF0000 | graphic.getDelay() & 0x0000FFFF);
	}

	/**
	 * Puts a hit update block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putHitUpdateBlock(HitUpdateBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getDamage());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, block.getType());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getCurrentHealth());
		builder.put(DataType.BYTE, block.getMaximumHealth());
	}

	/**
	 * Puts an interacting mob block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putInteractingMobBlock(InteractingMobBlock block, GamePacketBuilder builder) {
		builder.put(DataType.SHORT, DataTransformation.ADD, block.getIndex());
	}

	/**
	 * Puts a movement update for the specified segment.
	 *
	 * @param seg The segment.
	 * @param message The message.
	 * @param builder The builder.
	 */
	private static void putMovementUpdate(SynchronizationSegment seg, PlayerSynchronizationMessage message, GamePacketBuilder builder) {
		boolean updateRequired = seg.getBlockSet().size() > 0;
		if (seg.getType() == SegmentType.TELEPORT) {
			Position pos = ((TeleportSegment) seg).getDestination();
			builder.putBits(1, 1);
			builder.putBits(2, 3);
			builder.putBits(1, message.hasRegionChanged() ? 0 : 1);
			builder.putBits(2, pos.getHeight());
			builder.putBits(7, pos.getLocalY(message.getLastKnownRegion()));
			builder.putBits(7, pos.getLocalX(message.getLastKnownRegion()));
			builder.putBits(1, updateRequired ? 1 : 0);
		} else if (seg.getType() == SegmentType.RUN) {
			Direction[] directions = ((MovementSegment) seg).getDirections();
			builder.putBits(1, 1);
			builder.putBits(2, 2);
			builder.putBits(3, directions[0].toInteger());
			builder.putBits(3, directions[1].toInteger());
			builder.putBits(1, updateRequired ? 1 : 0);
		} else if (seg.getType() == SegmentType.WALK) {
			Direction[] directions = ((MovementSegment) seg).getDirections();
			builder.putBits(1, 1);
			builder.putBits(2, 1);
			builder.putBits(3, directions[0].toInteger());
			builder.putBits(1, updateRequired ? 1 : 0);
		} else {
			if (updateRequired) {
				builder.putBits(1, 1);
				builder.putBits(2, 0);
			} else {
				builder.putBits(1, 0);
			}
		}
	}

	/**
	 * Puts a remove player update.
	 *
	 * @param builder The builder.
	 */
	private static void putRemovePlayerUpdate(GamePacketBuilder builder) {
		builder.putBits(1, 1);
		builder.putBits(2, 3);
	}

	/**
	 * Puts a secondary hit update block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putSecondHitUpdateBlock(SecondaryHitUpdateBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, DataTransformation.ADD, block.getDamage());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getType());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, block.getCurrentHealth());
		builder.put(DataType.BYTE, block.getMaximumHealth());
	}

	/**
	 * Puts a turn to position block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putTurnToPositionBlock(TurnToPositionBlock block, GamePacketBuilder builder) {
		Position position = block.getPosition();
		builder.put(DataType.SHORT, position.getX() * 2 + 1);
		builder.put(DataType.SHORT, position.getY() * 2 + 1);
	}

}