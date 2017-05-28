package org.apollo.game.release.r377;

import org.apollo.game.message.impl.NpcSynchronizationMessage;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Direction;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Position;
import org.apollo.game.sync.block.AnimationBlock;
import org.apollo.game.sync.block.ForceChatBlock;
import org.apollo.game.sync.block.GraphicBlock;
import org.apollo.game.sync.block.HitUpdateBlock;
import org.apollo.game.sync.block.InteractingMobBlock;
import org.apollo.game.sync.block.SecondaryHitUpdateBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.game.sync.block.TransformBlock;
import org.apollo.game.sync.block.TurnToPositionBlock;
import org.apollo.game.sync.seg.AddNpcSegment;
import org.apollo.game.sync.seg.MovementSegment;
import org.apollo.game.sync.seg.SegmentType;
import org.apollo.game.sync.seg.SynchronizationSegment;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link NpcSynchronizationMessage}.
 *
 * @author Major
 */
public final class NpcSynchronizationMessageEncoder extends MessageEncoder<NpcSynchronizationMessage> {

	@Override
	public GamePacket encode(NpcSynchronizationMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(71, PacketType.VARIABLE_SHORT);
		builder.switchToBitAccess();

		GamePacketBuilder blockBuilder = new GamePacketBuilder();
		builder.putBits(8, message.getLocalNpcCount());

		for (SynchronizationSegment segment : message.getSegments()) {
			SegmentType type = segment.getType();
			if (type == SegmentType.REMOVE_MOB) {
				putRemoveNpcUpdate(builder);
			} else if (type == SegmentType.ADD_MOB) {
				putAddNpcUpdate((AddNpcSegment) segment, message, builder);
				putBlocks(segment, blockBuilder);
			} else {
				putMovementUpdate(segment, message, builder);
				putBlocks(segment, blockBuilder);
			}
		}

		if (blockBuilder.getLength() > 0) {
			builder.putBits(14, 16383);
			builder.switchToByteAccess();
			builder.putRawBuilder(blockBuilder);
		} else {
			builder.switchToByteAccess();
		}

		return builder.toGamePacket();
	}

	/**
	 * Puts an add npc update.
	 *
	 * @param seg The segment.
	 * @param message The message.
	 * @param builder The builder.
	 */
	private static void putAddNpcUpdate(AddNpcSegment seg, NpcSynchronizationMessage message, GamePacketBuilder builder) {
		boolean updateRequired = seg.getBlockSet().size() > 0;
		Position npc = message.getPosition();
		Position other = seg.getPosition();
		builder.putBits(14, seg.getIndex());
		builder.putBits(1, updateRequired ? 1 : 0);
		builder.putBits(5, other.getY() - npc.getY());
		builder.putBits(5, other.getX() - npc.getX());
		builder.putBits(1, 0); // discard walking queue
		builder.putBits(13, seg.getNpcId());
	}

	/**
	 * Puts an animation block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putAnimationBlock(AnimationBlock block, GamePacketBuilder builder) {
		Animation animation = block.getAnimation();
		builder.put(DataType.SHORT, animation.getId());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, animation.getDelay());
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

			if (blockSet.contains(TransformBlock.class)) {
				mask |= 0x1;
			}

			if (blockSet.contains(InteractingMobBlock.class)) {
				mask |= 0x40;
			}

			if (blockSet.contains(HitUpdateBlock.class)) {
				mask |= 0x80;
			}

			if (blockSet.contains(GraphicBlock.class)) {
				mask |= 0x4;
			}

			if (blockSet.contains(ForceChatBlock.class)) {
				mask |= 0x20;
			}

			if (blockSet.contains(TurnToPositionBlock.class)) {
				mask |= 0x8;
			}

			if (blockSet.contains(AnimationBlock.class)) {
				mask |= 0x2;
			}

			if (blockSet.contains(SecondaryHitUpdateBlock.class)) {
				mask |= 0x10;
			}

			builder.put(DataType.BYTE, mask);

			if (blockSet.contains(TransformBlock.class)) {
				putTransformBlock(blockSet.get(TransformBlock.class), builder);
			}

			if (blockSet.contains(InteractingMobBlock.class)) {
				putInteractingMobBlock(blockSet.get(InteractingMobBlock.class), builder);
			}

			if (blockSet.contains(HitUpdateBlock.class)) {
				putHitUpdateBlock(blockSet.get(HitUpdateBlock.class), builder);
			}

			if (blockSet.contains(GraphicBlock.class)) {
				putGraphicBlock(blockSet.get(GraphicBlock.class), builder);
			}

			if (blockSet.contains(ForceChatBlock.class)) {
				putForceChatBlock(blockSet.get(ForceChatBlock.class), builder);
			}

			if (blockSet.contains(TurnToPositionBlock.class)) {
				putTurnToPositionBlock(blockSet.get(TurnToPositionBlock.class), builder);
			}

			if (blockSet.contains(AnimationBlock.class)) {
				putAnimationBlock(blockSet.get(AnimationBlock.class), builder);
			}

			if (blockSet.contains(SecondaryHitUpdateBlock.class)) {
				putSecondHitUpdateBlock(blockSet.get(SecondaryHitUpdateBlock.class), builder);
			}
		}
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
	 * Puts a graphic block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putGraphicBlock(GraphicBlock block, GamePacketBuilder builder) {
		Graphic graphic = block.getGraphic();
		builder.put(DataType.SHORT, graphic.getId());
		builder.put(DataType.INT, graphic.getHeight() << 16 | graphic.getDelay() & 0xFFFF);
	}

	/**
	 * Puts a hit update block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putHitUpdateBlock(HitUpdateBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, DataTransformation.ADD, block.getDamage());
		builder.put(DataType.BYTE, DataTransformation.ADD, block.getType());
		builder.put(DataType.BYTE, block.getCurrentHealth());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getMaximumHealth());
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
	private static void putMovementUpdate(SynchronizationSegment seg, NpcSynchronizationMessage message, GamePacketBuilder builder) {
		boolean updateRequired = seg.getBlockSet().size() > 0;
		if (seg.getType() == SegmentType.RUN) {
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
	 * Puts a remove mob update.
	 *
	 * @param builder The builder.
	 */
	private static void putRemoveNpcUpdate(GamePacketBuilder builder) {
		builder.putBits(1, 1);
		builder.putBits(2, 3);
	}

	/**
	 * Puts a second hit update block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putSecondHitUpdateBlock(SecondaryHitUpdateBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getDamage());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getType());
		builder.put(DataType.BYTE, block.getCurrentHealth());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, block.getMaximumHealth());
	}

	/**
	 * Puts a transform block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putTransformBlock(TransformBlock block, GamePacketBuilder builder) {
		builder.put(DataType.SHORT, DataTransformation.ADD, block.getId());
	}

	/**
	 * Puts a turn to position block into the specified builder.
	 *
	 * @param block The block.
	 * @param builder The builder.
	 */
	private static void putTurnToPositionBlock(TurnToPositionBlock block, GamePacketBuilder builder) {
		Position position = block.getPosition();
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, position.getX() * 2 + 1);
		builder.put(DataType.SHORT, DataOrder.LITTLE, position.getY() * 2 + 1);
	}

}