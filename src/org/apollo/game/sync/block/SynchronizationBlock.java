package org.apollo.game.sync.block;

import org.apollo.game.event.impl.ChatEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Direction;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Position;
import org.apollo.game.model.Player;
import org.apollo.game.sync.seg.SynchronizationSegment;

/**
 * A synchronization block is part of a {@link SynchronizationSegment}. A segment can have up to one block of each type.
 * <p>
 * This class also has static factory methods for creating {@link SynchronizationBlock}s.
 * 
 * @author Graham
 */
public abstract class SynchronizationBlock {

	/**
	 * Creates an animation block with the specified animation.
	 * 
	 * @param animation The animation.
	 * @return The animation block.
	 */
	public static SynchronizationBlock createAnimationBlock(Animation animation) {
		return new AnimationBlock(animation);
	}

	/**
	 * Creates an appearance block for the specified player.
	 * 
	 * @param player The player.
	 * @return The appearance block.
	 */
	public static SynchronizationBlock createAppearanceBlock(Player player) {
		return new AppearanceBlock(player.getEncodedName(), player.getAppearance(), player.getSkillSet()
				.getCombatLevel(), 0, player.getEquipment(), player.getPrayerIcon(), player.getHeadIcon(),
				player.getNpcDefinition() == null ? -1 : player.getNpcDefinition().getId());
	}

	/**
	 * Creates a chat block for the specified player.
	 * 
	 * @param player The player.
	 * @param chatEvent The chat event.
	 * @return The chat block.
	 */
	public static SynchronizationBlock createChatBlock(Player player, ChatEvent chatEvent) {
		return new ChatBlock(player.getPrivilegeLevel(), chatEvent);
	}

	/**
	 * Creates a new force chat block with the specified message.
	 * 
	 * @param message The message.
	 * @return The force chat block.
	 */
	public static SynchronizationBlock createForceChatBlock(String message) {
		return new ForceChatBlock(message);
	}

	/**
	 * Creates a new force movement block with the specified parameters.
	 * 
	 * @param initialPosition The initial position of the player.
	 * @param finalPosition The final position of the player.
	 * @param travelDurationX The duration motion along the X axis will occur.
	 * @param travelDurationY The duration motion along the Y axis will occur.
	 * @param direction The direction the player will face.
	 * @return The force movement block.
	 */
	public static SynchronizationBlock createForceMovementBlock(Position initialPosition, Position finalPosition,
			int travelDurationX, int travelDurationY, Direction direction) {
		return new ForceMovementBlock(initialPosition, finalPosition, travelDurationX, travelDurationY, direction);
	}

	/**
	 * Creates a graphic block with the specified graphic.
	 * 
	 * @param graphic The graphic.
	 * @return The graphic block.
	 */
	public static SynchronizationBlock createGraphicBlock(Graphic graphic) {
		return new GraphicBlock(graphic);
	}

	/**
	 * Creates an interacting character block with the specified character index.
	 * 
	 * @param index The index of the interacting character.
	 * @return The interacting character block.
	 */
	public static SynchronizationBlock createInteractingCharacterBlock(int index) {
		return new InteractingCharacterBlock(index);
	}

	/**
	 * Creates a turn to position block with the specified position.
	 * 
	 * @param position The position.
	 * @return The turn to position block.
	 */
	public static SynchronizationBlock createTurnToPositionBlock(Position position) {
		return new TurnToPositionBlock(position);
	}

}