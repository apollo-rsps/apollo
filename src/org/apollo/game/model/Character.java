package org.apollo.game.model;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.action.Action;
import org.apollo.game.event.Event;
import org.apollo.game.event.impl.ServerMessageEvent;
import org.apollo.game.model.Inventory.StackMode;
import org.apollo.game.scheduling.impl.SkillNormalizationTask;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.util.CharacterRepository;

/**
 * A {@link Character} is a living creature in the world, such as a player or
 * NPC.
 * @author Graham
 */
public abstract class Character {

	/**
	 * The index of this character in the {@link CharacterRepository} it
	 * belongs to.
	 */
	private int index = -1;

	/**
	 * Teleportation flag.
	 */
	private boolean teleporting = false;

	/**
	 * The walking queue.
	 */
	private final WalkingQueue walkingQueue = new WalkingQueue(this);

	/**
	 * The first direction.
	 */
	private Direction firstDirection = Direction.NONE;

	/**
	 * The second direction.
	 */
	private Direction secondDirection = Direction.NONE;

	/**
	 * The current position of this character.
	 */
	private Position position;

	/**
	 * A list of local players.
	 */
	private final List<Player> localPlayers = new ArrayList<Player>(); // TODO make a specialized collection?

	/**
	 * A set of {@link SynchronizationBlock}s.
	 */
	private SynchronizationBlockSet blockSet = new SynchronizationBlockSet();

	/**
	 * The character's current action.
	 */
	private Action<?> action; // TODO

	/**
	 * The character's inventory.
	 */
	private final Inventory inventory = new Inventory(InventoryConstants.INVENTORY_CAPACITY);

	/**
	 * The character's equipment.
	 */
	private final Inventory equipment = new Inventory(InventoryConstants.EQUIPMENT_CAPACITY, StackMode.STACK_ALWAYS);

	/**
	 * The character's bank.
	 */
	private final Inventory bank = new Inventory(InventoryConstants.BANK_CAPACITY, StackMode.STACK_ALWAYS);

	/**
	 * The character's skill set.
	 */
	private final SkillSet skillSet = new SkillSet();

	/**
	 * Creates a new character with the specified initial position.
	 * @param position The initial position of this character.
	 */
	public Character(Position position) {
		this.position = position;
		init();
	}

	/**
	 * Initialises this character.
	 */
	private void init() {
		World.getWorld().schedule(new SkillNormalizationTask(this));
	}

	/**
	 * Gets the character's inventory.
	 * @return The character's inventory.
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Gets the character's equipment.
	 * @return The character's equipment.
	 */
	public Inventory getEquipment() {
		return equipment;
	}

	/**
	 * Gets the character's bank.
	 * @return The character's bank.
	 */
	public Inventory getBank() {
		return bank;
	}

	/**
	 * Gets the local player list.
	 * @return The local player list.
	 */
	public List<Player> getLocalPlayerList() {
		return localPlayers;
	}

	/**
	 * Checks if this player is currently teleporting.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isTeleporting() {
		return teleporting;
	}

	/**
	 * Sets the teleporting flag.
	 * @param teleporting {@code true} if the player is teleporting,
	 * {@code false} if not.
	 */
	public void setTeleporting(boolean teleporting) {
		this.teleporting = teleporting;
	}

	/**
	 * Gets the walking queue.
	 * @return The walking queue.
	 */
	public WalkingQueue getWalkingQueue() {
		return walkingQueue;
	}

	/**
	 * Sets the next directions for this character.
	 * @param first The first direction.
	 * @param second The second direction.
	 */
	public void setDirections(Direction first, Direction second) {
		this.firstDirection = first;
		this.secondDirection = second;
	}

	/**
	 * Gets the first direction.
	 * @return The first direction.
	 */
	public Direction getFirstDirection() {
		return firstDirection;
	}

	/**
	 * Gets the second direction.
	 * @return The second direction.
	 */
	public Direction getSecondDirection() {
		return secondDirection;
	}

	/**
	 * Gets the directions as an array.
	 * @return A zero, one or two element array containing the directions (in
	 * order).
	 */
	public Direction[] getDirections() {
		if (firstDirection != Direction.NONE) {
			if (secondDirection != Direction.NONE) {
				return new Direction[] { firstDirection, secondDirection };
			} else {
				return new Direction[] { firstDirection };
			}
		} else {
			return Direction.EMPTY_DIRECTION_ARRAY;
		}
	}

	/**
	 * Gets the position of this character.
	 * @return The position of this character.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Sets the position of this character.
	 * @param position The position of this character.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * Checks if this character is active.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isActive() {
		return index != -1;
	}

	/**
	 * Gets the index of this character.
	 * @return The index of this character.
	 */
	public int getIndex() {
		synchronized (this) {
			return index;
		}
	}

	/**
	 * Sets the index of this character.
	 * @param index The index of this character.
	 */
	public void setIndex(int index) {
		synchronized (this) {
			this.index = index;
		}
	}

	/**
	 * Gets the {@link SynchronizationBlockSet}.
	 * @return The block set.
	 */
	public SynchronizationBlockSet getBlockSet() {
		return blockSet;
	}

	/**
	 * Resets the block set.
	 */
	public void resetBlockSet() {
		blockSet = new SynchronizationBlockSet();
	}

	/**
	 * Sends an {@link Event} to either:
	 * <ul>
	 *   <li>The client if this {@link Character} is a {@link Player}.</li>
	 *   <li>The AI routines if this {@link Character} is an NPC</li>
	 * </ul>
	 * @param event The event.
	 */
	public abstract void send(Event event);

	/**
	 * Teleports this character to the specified position, setting the
	 * appropriate flags and clearing the walking queue.
	 * @param position The position.
	 */
	public void teleport(Position position) {
		this.teleporting = true;
		this.position = position;
		this.walkingQueue.clear();
		this.stopAction(); // TODO do it on any movement is a must.. walking queue perhaps?
	}

	/**
	 * Plays the specified animation.
	 * @param animation The animation.
	 */
	public void playAnimation(Animation animation) {
		blockSet.add(SynchronizationBlock.createAnimationBlock(animation));
	}

	/**
	 * Stops the current animation.
	 */
	public void stopAnimation() {
		playAnimation(Animation.STOP_ANIMATION);
	}

	/**
	 * Plays the specified graphic.
	 * @param graphic The graphic.
	 */
	public void playGraphic(Graphic graphic) {
		blockSet.add(SynchronizationBlock.createGraphicBlock(graphic));
	}

	/**
	 * Stops the current graphic.
	 */
	public void stopGraphic() {
		playGraphic(Graphic.STOP_GRAPHIC);
	}

	/**
	 * Gets the character's skill set.
	 * @return The character's skill set.
	 */
	public SkillSet getSkillSet() {
		return skillSet;
	}

	/**
	 * Starts a new action, stopping the current one if it exists.
	 * @param action The new action.
	 * @return A flag indicating if the action was started.
	 */
	public boolean startAction(Action<?> action) {
		if (this.action != null) {
			if (this.action.equals(action)) {
				return false;
			}
			stopAction();
		}
		this.action = action;
		World.getWorld().schedule(action);
		return true; // TODO maybe this should be incorporated into the action class itself?
	}

	/**
	 * Stops the current action.
	 */
	public void stopAction() {
		if (action != null) {
			action.stop();
			action = null;
		}
	}

	/**
	 * Turns the character to face the specified position.
	 * @param position The position to face.
	 */
	public void turnTo(Position position) {
		blockSet.add(SynchronizationBlock.createTurnToPositionBlock(position));
	}

	/**
	 * Sends a message to the character.
	 * @param message The message.
	 */
	public void sendMessage(String message) {
		send(new ServerMessageEvent(message));
	}

}
