package org.apollo.net.release.r317;

import org.apollo.game.event.impl.NpcSynchronizationEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Direction;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Position;
import org.apollo.game.sync.block.AnimationBlock;
import org.apollo.game.sync.block.ForceChatBlock;
import org.apollo.game.sync.block.GraphicBlock;
import org.apollo.game.sync.block.HitUpdateBlock;
import org.apollo.game.sync.block.InteractingCharacterBlock;
import org.apollo.game.sync.block.SecondHitUpdateBlock;
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
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link NpcSynchronizationEvent}.
 * 
 * @author Major
 */
public class NpcSynchronizationEventEncoder extends EventEncoder<NpcSynchronizationEvent> {

	@Override
	public GamePacket encode(NpcSynchronizationEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(65, PacketType.VARIABLE_SHORT);
		builder.switchToBitAccess();

		GamePacketBuilder blockBuilder = new GamePacketBuilder();

		builder.putBits(8, event.getLocalNpcCount());

		for (SynchronizationSegment segment : event.getSegments()) {
			SegmentType type = segment.getType();
			if (type == SegmentType.REMOVE_CHARACTER) {
				putRemoveCharacterUpdate(builder);
			} else if (type == SegmentType.ADD_CHARACTER) {
				putAddNpcUpdate((AddNpcSegment) segment, event, builder);
				putBlocks(segment, blockBuilder);
			} else {
				putMovementUpdate(segment, event, builder);
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
	 * Puts an add character update.
	 * 
	 * @param seg The segment.
	 * @param event The event.
	 * @param builder The builder.
	 */
	private void putAddNpcUpdate(AddNpcSegment seg, NpcSynchronizationEvent event, GamePacketBuilder builder) {
		boolean updateRequired = seg.getBlockSet().size() > 0;
		Position npc = event.getPosition();
		Position other = seg.getPosition();
		builder.putBits(14, seg.getIndex());
		builder.putBits(5, other.getY() - npc.getY());
		builder.putBits(5, other.getX() - npc.getX());
		builder.putBits(1, 0); // discard walking queue
		builder.putBits(12, seg.getNpcId());
		builder.putBits(1, updateRequired ? 1 : 0);
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
		blockBuilder.put(DataType.BYTE, animation.getDelay());
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

			if (blockSet.contains(AnimationBlock.class)) {
				mask |= 0x10;
			}

			if (blockSet.contains(HitUpdateBlock.class)) {
				mask |= 0x8;
			}

			if (blockSet.contains(GraphicBlock.class)) {
				mask |= 0x80;
			}

			if (blockSet.contains(InteractingCharacterBlock.class)) {
				mask |= 0x20;
			}

			if (blockSet.contains(ForceChatBlock.class)) {
				mask |= 0x1;
			}

			if (blockSet.contains(SecondHitUpdateBlock.class)) {
				mask |= 0x40;
			}

			if (blockSet.contains(TransformBlock.class)) {
				mask |= 0x2;
			}

			if (blockSet.contains(TurnToPositionBlock.class)) {
				mask |= 0x4;
			}

			blockBuilder.put(DataType.BYTE, mask);

			if (blockSet.contains(AnimationBlock.class)) {
				putAnimationBlock(blockSet.get(AnimationBlock.class), blockBuilder);
			}

			if (blockSet.contains(HitUpdateBlock.class)) {
				putHitUpdateBlock(blockSet.get(HitUpdateBlock.class), blockBuilder);
			}

			if (blockSet.contains(GraphicBlock.class)) {
				putGraphicBlock(blockSet.get(GraphicBlock.class), blockBuilder);
			}

			if (blockSet.contains(InteractingCharacterBlock.class)) {
				putInteractingCharacterBlock(blockSet.get(InteractingCharacterBlock.class), blockBuilder);
			}

			if (blockSet.contains(ForceChatBlock.class)) {
				putForceChatBlock(blockSet.get(ForceChatBlock.class), blockBuilder);
			}

			if (blockSet.contains(SecondHitUpdateBlock.class)) {
				putSecondHitUpdateBlock(blockSet.get(SecondHitUpdateBlock.class), blockBuilder);
			}

			if (blockSet.contains(TransformBlock.class)) {
				putTransformBlock(blockSet.get(TransformBlock.class), blockBuilder);
			}

			if (blockSet.contains(TurnToPositionBlock.class)) {
				putTurnToPositionBlock(blockSet.get(TurnToPositionBlock.class), blockBuilder);
			}
		}
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
	 * Puts a graphic block into the specified builder.
	 * 
	 * @param block The block.
	 * @param blockBuilder The builder.
	 */
	private void putGraphicBlock(GraphicBlock block, GamePacketBuilder blockBuilder) {
		Graphic graphic = block.getGraphic();
		blockBuilder.put(DataType.SHORT, graphic.getId());
		blockBuilder.put(DataType.INT, graphic.getDelay());
	}

	/**
	 * Puts a hit update block into the specified builder.
	 * 
	 * @param block The block.
	 * @param builder The builder.
	 */
	private void putHitUpdateBlock(HitUpdateBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, DataTransformation.ADD, block.getDamage());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, block.getType());
		builder.put(DataType.BYTE, DataTransformation.ADD, block.getCurrentHealth());
		builder.put(DataType.BYTE, block.getMaximumHealth());
	}

	/**
	 * Puts an interacting character block into the specified builder.
	 * 
	 * @param block The block.
	 * @param builder The builder.
	 */
	private void putInteractingCharacterBlock(InteractingCharacterBlock block, GamePacketBuilder builder) {
		builder.put(DataType.SHORT, block.getInteractingCharacterIndex());
	}

	/**
	 * Puts a movement update for the specified segment.
	 * 
	 * @param segment The segment.
	 * @param event The event.
	 * @param builder The builder.
	 */
	private void putMovementUpdate(SynchronizationSegment segment, NpcSynchronizationEvent event,
			GamePacketBuilder builder) {
		boolean updateRequired = segment.getBlockSet().size() > 0;
		if (segment.getType() == SegmentType.RUN) {
			Direction[] directions = ((MovementSegment) segment).getDirections();
			builder.putBits(1, 1);
			builder.putBits(2, 2);
			builder.putBits(3, directions[0].toInteger());
			builder.putBits(3, directions[1].toInteger());
			builder.putBits(1, updateRequired ? 1 : 0);
		} else if (segment.getType() == SegmentType.WALK) {
			Direction[] directions = ((MovementSegment) segment).getDirections();
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
	 * Puts a remove character update.
	 * 
	 * @param builder The builder.
	 */
	private void putRemoveCharacterUpdate(GamePacketBuilder builder) {
		builder.putBits(1, 1);
		builder.putBits(2, 3);
	}

	/**
	 * Puts a second hit update block into the specified builder.
	 * 
	 * @param block The block.
	 * @param builder The builder.
	 */
	private void putSecondHitUpdateBlock(SecondHitUpdateBlock block, GamePacketBuilder builder) {
		builder.put(DataType.BYTE, DataTransformation.NEGATE, block.getDamage());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getType());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, block.getCurrentHealth());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, block.getMaximumHealth());
	}

	/**
	 * Puts a transform block into the specified builder.
	 * 
	 * @param block The block.
	 * @param builder The builder.
	 */
	private void putTransformBlock(TransformBlock block, GamePacketBuilder builder) {
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, block.getId());
	}

	/**
	 * Puts a turn to position block into the specified builder.
	 * 
	 * @param block The block.
	 * @param blockBuilder The builder.
	 */
	private void putTurnToPositionBlock(TurnToPositionBlock block, GamePacketBuilder blockBuilder) {
		Position pos = block.getPosition();
		blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, pos.getX() * 2 + 1);
		blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, pos.getY() * 2 + 1);
	}

}