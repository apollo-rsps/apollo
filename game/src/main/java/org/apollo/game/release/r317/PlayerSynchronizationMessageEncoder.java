package org.apollo.game.release.r317;

import org.apollo.cache.def.EquipmentDefinition;
import org.apollo.game.message.impl.PlayerSynchronizationMessage;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Appearance;
import org.apollo.game.model.Direction;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Item;
import org.apollo.game.model.Position;
import org.apollo.game.model.entity.EquipmentConstants;
import org.apollo.game.model.entity.AnimationMap;
import org.apollo.game.model.entity.setting.Gender;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.sync.block.AnimationBlock;
import org.apollo.game.sync.block.AppearanceBlock;
import org.apollo.game.sync.block.ChatBlock;
import org.apollo.game.sync.block.ForceChatBlock;
import org.apollo.game.sync.block.ForceMovementBlock;
import org.apollo.game.sync.block.GraphicBlock;
import org.apollo.game.sync.block.HitUpdateBlock;
import org.apollo.game.sync.block.InteractingMobBlock;
import org.apollo.game.sync.block.SecondaryHitUpdateBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.game.sync.block.TurnToPositionBlock;
import org.apollo.game.sync.seg.AddPlayerSegment;
import org.apollo.game.sync.seg.MovementSegment;
import org.apollo.game.sync.seg.SegmentType;
import org.apollo.game.sync.seg.SynchronizationSegment;
import org.apollo.game.sync.seg.TeleportSegment;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
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
		GamePacketBuilder builder = new GamePacketBuilder(81, PacketType.VARIABLE_SHORT);
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
		builder.putBits(1, updateRequired ? 1 : 0);
		builder.putBits(1, 1); // discard walking queue?
		builder.putBits(5, other.getY() - player.getY());
		builder.putBits(5, other.getX() - player.getX());
	}

	/**
	 * Puts an animation block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putAnimationBlock(AnimationBlock block, GamePacketBuilder builder) {
		Animation animation = block.getAnimation();
		builder.put(DataType.SHORT, DataOrder.LITTLE, animation.getId());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, animation.getDelay());
	}

	/**
	 * Puts an appearance block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putAppearanceBlock(AppearanceBlock block, GamePacketBuilder builder) {
		Appearance appearance = block.getAppearance();
		GamePacketBuilder playerProperties = new GamePacketBuilder();

		playerProperties.put(DataType.BYTE, appearance.getGender().toInteger());
		playerProperties.put(DataType.BYTE, 0);

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

		builder.put(DataType.BYTE, DataTransformation.NEGATE, playerProperties.getLength());

		builder.putRawBuilder(playerProperties);
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

			if (blockSet.contains(ForceMovementBlock.class)) {
				mask |= 0x400;
			}
			if (blockSet.contains(GraphicBlock.class)) {
				mask |= 0x100;
			}
			if (blockSet.contains(AnimationBlock.class)) {
				mask |= 0x8;
			}
			if (blockSet.contains(ForceChatBlock.class)) {
				mask |= 0x4;
			}
			if (blockSet.contains(ChatBlock.class)) {
				mask |= 0x80;
			}
			if (blockSet.contains(InteractingMobBlock.class)) {
				mask |= 0x1;
			}
			if (blockSet.contains(AppearanceBlock.class)) {
				mask |= 0x10;
			}
			if (blockSet.contains(TurnToPositionBlock.class)) {
				mask |= 0x2;
			}
			if (blockSet.contains(HitUpdateBlock.class)) {
				mask |= 0x20;
			}
			if (blockSet.contains(SecondaryHitUpdateBlock.class)) {
				mask |= 0x200;
			}

			if (mask >= 0x100) {
				mask |= 0x40;
				builder.put(DataType.SHORT, DataOrder.LITTLE, mask);
			} else {
				builder.put(DataType.BYTE, mask);
			}

			if (blockSet.contains(ForceMovementBlock.class)) {
				putForceMovementBlock(blockSet.get(ForceMovementBlock.class), builder);
			}
			if (blockSet.contains(GraphicBlock.class)) {
				putGraphicBlock(blockSet.get(GraphicBlock.class), builder);
			}
			if (blockSet.contains(AnimationBlock.class)) {
				putAnimationBlock(blockSet.get(AnimationBlock.class), builder);
			}
			if (blockSet.contains(ForceChatBlock.class)) {
				putForceChatBlock(blockSet.get(ForceChatBlock.class), builder);
			}
			if (blockSet.contains(ChatBlock.class)) {
				putChatBlock(blockSet.get(ChatBlock.class), builder);
			}
			if (blockSet.contains(InteractingMobBlock.class)) {
				putInteractingMobBlock(blockSet.get(InteractingMobBlock.class), builder);
			}
			if (blockSet.contains(AppearanceBlock.class)) {
				putAppearanceBlock(blockSet.get(AppearanceBlock.class), builder);
			}
			if (blockSet.contains(TurnToPositionBlock.class)) {
				putTurnToPositionBlock(blockSet.get(TurnToPositionBlock.class), builder);
			}
			if (blockSet.contains(HitUpdateBlock.class)) {
				putHitUpdateBlock(blockSet.get(HitUpdateBlock.class), builder);
			}
			if (blockSet.contains(SecondaryHitUpdateBlock.class)) {
				putSecondHitUpdateBlock(blockSet.get(SecondaryHitUpdateBlock.class), builder);
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
		builder.put(DataType.SHORT, DataOrder.LITTLE, block.getTextColor() << 8 | block.getTextEffects());
		builder.put(DataType.BYTE, block.getPrivilegeLevel().toInteger());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, bytes.length);
		builder.putBytesReverse(bytes);
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
	 * Puts a force movement block in the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putForceMovementBlock(ForceMovementBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getInitialX());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getInitialY());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getFinalX());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getFinalY());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, block.getTravelDurationX());
		builder.put(DataType.SHORT, DataTransformation.ADD, block.getTravelDurationY());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getDirection().toInteger());
	}

	/**
	 * Puts a graphic block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putGraphicBlock(GraphicBlock block, GamePacketBuilder builder) {
		Graphic graphic = block.getGraphic();
		builder.put(DataType.SHORT, DataOrder.LITTLE, graphic.getId());
		builder.put(DataType.INT, graphic.getHeight() << 16 | graphic.getDelay() & 0xFFFF);
	}

	/**
	 * Puts a hit update block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putHitUpdateBlock(HitUpdateBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, block.getDamage());
		builder.put(DataType.BYTE, DataTransformation.ADD, block.getType());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, block.getCurrentHealth());
		builder.put(DataType.BYTE, block.getMaximumHealth());
	}

	/**
	 * Puts an interacting mob block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putInteractingMobBlock(InteractingMobBlock block, GamePacketBuilder builder) {
		builder.put(DataType.SHORT, DataOrder.LITTLE, block.getIndex());
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
			Position position = ((TeleportSegment) seg).getDestination();
			builder.putBits(1, 1);
			builder.putBits(2, 3);
			builder.putBits(2, position.getHeight());
			builder.putBits(1, message.hasRegionChanged() ? 0 : 1);
			builder.putBits(1, updateRequired ? 1 : 0);
			builder.putBits(7, position.getLocalY(message.getLastKnownRegion()));
			builder.putBits(7, position.getLocalX(message.getLastKnownRegion()));
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
	 * Puts a Second Hit Update block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putSecondHitUpdateBlock(SecondaryHitUpdateBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, block.getDamage());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getType());
		builder.put(DataType.BYTE, block.getCurrentHealth());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, block.getMaximumHealth());
	}

	/**
	 * Puts a Turn To Position block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putTurnToPositionBlock(TurnToPositionBlock block, GamePacketBuilder builder) {
		Position pos = block.getPosition();
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, pos.getX() * 2 + 1);
		builder.put(DataType.SHORT, DataOrder.LITTLE, pos.getY() * 2 + 1);
	}

}