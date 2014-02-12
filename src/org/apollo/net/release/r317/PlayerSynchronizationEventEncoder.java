package org.apollo.net.release.r317;

import org.apollo.game.event.impl.PlayerSynchronizationEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Appearance;
import org.apollo.game.model.Direction;
import org.apollo.game.model.EquipmentConstants;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Position;
import org.apollo.game.model.def.EquipmentDefinition;
import org.apollo.game.model.settings.Gender;
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
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link PlayerSynchronizationEvent}.
 * 
 * @author Graham
 * @author Major
 */
public final class PlayerSynchronizationEventEncoder extends EventEncoder<PlayerSynchronizationEvent> {

	@Override
	public GamePacket encode(PlayerSynchronizationEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(81, PacketType.VARIABLE_SHORT);
		builder.switchToBitAccess();

		GamePacketBuilder blockBuilder = new GamePacketBuilder();

		putMovementUpdate(event.getSegment(), event, builder);
		putBlocks(event.getSegment(), blockBuilder);

		builder.putBits(8, event.getLocalPlayers());

		for (SynchronizationSegment segment : event.getSegments()) {
			SegmentType type = segment.getType();
			if (type == SegmentType.REMOVE_MOB) {
				putRemovePlayerUpdate(builder);
			} else if (type == SegmentType.ADD_MOB) {
				putAddPlayerUpdate((AddPlayerSegment) segment, event, builder);
				putBlocks(segment, blockBuilder);
			} else {
				putMovementUpdate(segment, event, builder);
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
	 * @param event The event.
	 * @param builder The builder.
	 */
	private void putAddPlayerUpdate(AddPlayerSegment seg, PlayerSynchronizationEvent event, GamePacketBuilder builder) {
		boolean updateRequired = seg.getBlockSet().size() > 0;
		Position player = event.getPosition();
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
	 * @param blockBuilder The builder.
	 */
	private void putAnimationBlock(AnimationBlock block, GamePacketBuilder blockBuilder) {
		Animation animation = block.getAnimation();
		blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, animation.getId());
		blockBuilder.put(DataType.BYTE, DataTransformation.NEGATE, animation.getDelay());
	}

	/**
	 * Puts an appearance block into the specified builder.
	 * 
	 * @param block The block.
	 * @param blockBuilder The builder.
	 */
	private void putAppearanceBlock(AppearanceBlock block, GamePacketBuilder blockBuilder) {
		Appearance appearance = block.getAppearance();
		GamePacketBuilder playerProperties = new GamePacketBuilder();

		playerProperties.put(DataType.BYTE, appearance.getGender().toInteger());
		playerProperties.put(DataType.BYTE, block.getPrayerIcon());

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
			if (def != null && (def.isFullHat() || def.isFullMask()) || appearance.getGender() == Gender.FEMALE) {
				playerProperties.put(DataType.BYTE, 0);
			} else {
				playerProperties.put(DataType.SHORT, 0x100 + style[1]);
			}
		}

		int[] colors = appearance.getColors();
		for (int color : colors) {
			playerProperties.put(DataType.BYTE, color);
		}

		playerProperties.put(DataType.SHORT, 0x328); // stand
		playerProperties.put(DataType.SHORT, 0x337); // stand turn
		playerProperties.put(DataType.SHORT, 0x333); // walk
		playerProperties.put(DataType.SHORT, 0x334); // turn 180
		playerProperties.put(DataType.SHORT, 0x335); // turn 90 cw
		playerProperties.put(DataType.SHORT, 0x336); // turn 90 ccw
		playerProperties.put(DataType.SHORT, 0x338); // run

		playerProperties.put(DataType.LONG, block.getName());
		playerProperties.put(DataType.BYTE, block.getCombatLevel());
		playerProperties.put(DataType.SHORT, block.getSkillLevel());

		blockBuilder.put(DataType.BYTE, DataTransformation.NEGATE, playerProperties.getLength());

		blockBuilder.putRawBuilder(playerProperties);
	}

	/**
	 * Puts the blocks for the specified segment.
	 * 
	 * @param segment The segment.
	 * @param blockBuilder The block builder.
	 */
	private void putBlocks(SynchronizationSegment segment, GamePacketBuilder blockBuilder) {
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
				blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, mask);
			} else {
				blockBuilder.put(DataType.BYTE, mask);
			}

			if (blockSet.contains(ForceMovementBlock.class)) {
				putForceMovementBlock(blockSet.get(ForceMovementBlock.class), blockBuilder);
			}
			if (blockSet.contains(GraphicBlock.class)) {
				putGraphicBlock(blockSet.get(GraphicBlock.class), blockBuilder);
			}
			if (blockSet.contains(AnimationBlock.class)) {
				putAnimationBlock(blockSet.get(AnimationBlock.class), blockBuilder);
			}
			if (blockSet.contains(ForceChatBlock.class)) {
				putForceChatBlock(blockSet.get(ForceChatBlock.class), blockBuilder);
			}
			if (blockSet.contains(ChatBlock.class)) {
				putChatBlock(blockSet.get(ChatBlock.class), blockBuilder);
			}
			if (blockSet.contains(InteractingMobBlock.class)) {
				putInteractingMobBlock(blockSet.get(InteractingMobBlock.class), blockBuilder);
			}
			if (blockSet.contains(AppearanceBlock.class)) {
				putAppearanceBlock(blockSet.get(AppearanceBlock.class), blockBuilder);
			}
			if (blockSet.contains(TurnToPositionBlock.class)) {
				putTurnToPositionBlock(blockSet.get(TurnToPositionBlock.class), blockBuilder);
			}
			if (blockSet.contains(HitUpdateBlock.class)) {
				putHitUpdateBlock(blockSet.get(HitUpdateBlock.class), blockBuilder);
			}
			if (blockSet.contains(SecondaryHitUpdateBlock.class)) {
				putSecondHitUpdateBlock(blockSet.get(SecondaryHitUpdateBlock.class), blockBuilder);
			}
		}
	}

	/**
	 * Puts a chat block into the specified builder.
	 * 
	 * @param block The block.
	 * @param builder The builder.
	 */
	private void putChatBlock(ChatBlock block, GamePacketBuilder builder) {
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
	private void putForceChatBlock(ForceChatBlock block, GamePacketBuilder builder) {
		builder.putString(block.getMessage());
	}

	/**
	 * Puts a force movement block in the specified builder.
	 * 
	 * @param forceMovementBlock The block.
	 * @param builder The builder.
	 */
	private void putForceMovementBlock(ForceMovementBlock block, GamePacketBuilder builder) {
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
	private void putGraphicBlock(GraphicBlock block, GamePacketBuilder builder) {
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
	private void putHitUpdateBlock(HitUpdateBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, block.getDamage());
		builder.put(DataType.BYTE, DataTransformation.ADD, block.getType());
		builder.put(DataType.BYTE, block.getCurrentHealth());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, block.getMaximumHealth());
	}

	/**
	 * Puts an interacting mob block into the specified builder.
	 * 
	 * @param block The block.
	 * @param builder The builder.
	 */
	private void putInteractingMobBlock(InteractingMobBlock block, GamePacketBuilder builder) {
		builder.put(DataType.SHORT, DataOrder.LITTLE, block.getInteractingMobIndex());
	}

	/**
	 * Puts a movement update for the specified segment.
	 * 
	 * @param seg The segment.
	 * @param event The event.
	 * @param builder The builder.
	 */
	private void putMovementUpdate(SynchronizationSegment seg, PlayerSynchronizationEvent event,
			GamePacketBuilder builder) {
		boolean updateRequired = seg.getBlockSet().size() > 0;
		if (seg.getType() == SegmentType.TELEPORT) {
			Position position = ((TeleportSegment) seg).getDestination();
			builder.putBits(1, 1);
			builder.putBits(2, 3);
			builder.putBits(2, position.getHeight());
			builder.putBits(1, event.hasRegionChanged() ? 0 : 1);
			builder.putBits(1, updateRequired ? 1 : 0);
			builder.putBits(7, position.getLocalY(event.getLastKnownRegion()));
			builder.putBits(7, position.getLocalX(event.getLastKnownRegion()));
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
	private void putRemovePlayerUpdate(GamePacketBuilder builder) {
		builder.putBits(1, 1);
		builder.putBits(2, 3);
	}

	/**
	 * Puts a Second Hit Update block into the specified builder.
	 * 
	 * @param block The block.
	 * @param builder The builder.
	 */
	private void putSecondHitUpdateBlock(SecondaryHitUpdateBlock block, GamePacketBuilder builder) {
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
	private void putTurnToPositionBlock(TurnToPositionBlock block, GamePacketBuilder builder) {
		Position pos = block.getPosition();
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, pos.getX() * 2 + 1);
		builder.put(DataType.SHORT, DataOrder.LITTLE, pos.getY() * 2 + 1);
	}

}