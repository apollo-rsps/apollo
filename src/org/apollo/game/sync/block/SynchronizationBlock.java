package org.apollo.game.sync.block;

import org.apollo.game.event.impl.ChatEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
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
	 * Creates an appearance block for the specified player.
	 * 
	 * @param player The player.
	 * @return The appearance block.
	 */
	public static SynchronizationBlock createAppearanceBlock(Player player) {
		return new AppearanceBlock(player.getEncodedName(), player.getAppearance(), player.getSkillSet()
				.getCombatLevel(), 0, player.getEquipment());
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
	 * Creates an animation block with the specified animation.
	 * 
	 * @param animation The animation.
	 * @return The animation block.
	 */
	public static SynchronizationBlock createAnimationBlock(Animation animation) {
		return new AnimationBlock(animation);
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
	 * Creates a turn to position block with the specified position.
	 * 
	 * @param position The position.
	 * @return The turn to position block.
	 */
	public static SynchronizationBlock createTurnToPositionBlock(Position position) {
		return new TurnToPositionBlock(position);
	}

}
