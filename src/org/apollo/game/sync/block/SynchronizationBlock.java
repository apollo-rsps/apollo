package org.apollo.game.sync.block;

import org.apollo.game.event.impl.ChatEvent;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Direction;
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
	 * @param initialPosition The initial {@link Position} of the player.
	 * @param finalPosition The final {@link Position} of the player
	 * @param travelDurationX The length of time (in game pulses) the player's movement along the X axis will last.
	 * @param travelDurationY The length of time (in game pulses) the player's movement along the Y axis will last.
	 * @param direction The direction the player should move.
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
	 * Creates a new hit or secondary hit update block
	 * 
	 * @param damage The damage dealt by the hit.
	 * @param type The type of hit.
	 * @param currentHealth The current health of the mob.
	 * @param maximumHealth The maximum health of the mob.
	 * @param secondary If the block is a secondary hit or not.
	 * @return The hit update block.
	 */
	public static SynchronizationBlock createHitUpdateBlock(int damage, int type, int currentHealth, int maximumHealth,
			boolean secondary) {
		return secondary ? new SecondaryHitUpdateBlock(damage, type, currentHealth, maximumHealth)
				: new HitUpdateBlock(damage, type, currentHealth, maximumHealth);
	}

	/**
	 * Creates an interacting mob block with the specified index.
	 * 
	 * @param index The index of the mob being interacted with.
	 * @return The interacting mob block.
	 */
	public static SynchronizationBlock createInteractingMobBlock(int index) {
		return new InteractingMobBlock(index);
	}

	/**
	 * Creates a transform block with the specified id.
	 * 
	 * @param id The id.
	 * @return The transform block.
	 */
	public static SynchronizationBlock createTransformBlock(int id) {
		return new TransformBlock(id);
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