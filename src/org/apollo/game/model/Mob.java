package org.apollo.game.model;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.action.Action;
import org.apollo.game.model.Inventory.StackMode;
import org.apollo.game.model.def.NpcDefinition;
import org.apollo.game.scheduling.impl.SkillNormalizationTask;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.util.MobRepository;

/**
 * A {@link Mob} is a living entity in the world, such as a player or NPC.
 * 
 * @author Graham
 */
public abstract class Mob extends Entity {

	/**
	 * This mob's current action.
	 */
	private Action<?> action;

	/**
	 * This mob's set of {@link SynchronizationBlock}s.
	 */
	protected SynchronizationBlockSet blockSet = new SynchronizationBlockSet();

	/**
	 * This mob's {@link NpcDefinition). A {@link Player} only uses this if they are appearing as an npc in-game.
	 */
	protected NpcDefinition definition;

	/**
	 * This mob's equipment.
	 */
	private final Inventory equipment = new Inventory(InventoryConstants.EQUIPMENT_CAPACITY, StackMode.STACK_ALWAYS);

	/**
	 * This mob's first movement {@link Direction}.
	 */
	private Direction firstDirection = Direction.NONE;

	/**
	 * The index of this mob in the {@link MobRepository} it belongs to.
	 */
	private int index = -1;

	/**
	 * This mob's inventory.
	 */
	private final Inventory inventory = new Inventory(InventoryConstants.INVENTORY_CAPACITY);

	/**
	 * This mob's {@link List} of local npcs.
	 */
	private final List<Npc> localNpcs = new ArrayList<Npc>();

	/**
	 * This mob's {@link List} of local players.
	 */
	private final List<Player> localPlayers = new ArrayList<Player>();

	/**
	 * This mob's second movement direction.
	 */
	private Direction secondDirection = Direction.NONE;

	/**
	 * This mob's skill set.
	 */
	private final SkillSet skillSet = new SkillSet();

	/**
	 * Indicates whether this mob is currently teleporting or not.
	 */
	private boolean teleporting = false;

	/**
	 * This mob's walking queue.
	 */
	private final WalkingQueue walkingQueue = new WalkingQueue(this);

	/**
	 * Creates a new mob with the specified initial {@link Position}.
	 * 
	 * @param position The initial position.
	 */
	public Mob(Position position) {
		this.position = position;
		init();
	}

	/**
	 * Gets the {@link SynchronizationBlockSet}.
	 * 
	 * @return The block set.
	 */
	public SynchronizationBlockSet getBlockSet() {
		return blockSet;
	}

	/**
	 * Deals damage to this mob.
	 * 
	 * @param damage The damage dealt.
	 * @param type The type of damage.
	 * @param secondary Whether this should be dealt as a secondary hit or not.
	 */
	public void damage(int damage, int type, boolean secondary) {
		Skill hitpoints = skillSet.getSkill(Skill.HITPOINTS);
		blockSet.add(SynchronizationBlock.createHitUpdateBlock(damage, type, hitpoints.getCurrentLevel(),
				hitpoints.getMaximumLevel(), secondary));
	}

	/**
	 * Gets the directions as an array.
	 * 
	 * @return A zero, one or two element array containing the directions (in order).
	 */
	public Direction[] getDirections() {
		if (firstDirection != Direction.NONE) {
			return secondDirection == Direction.NONE ? new Direction[] { firstDirection } : new Direction[] {
					firstDirection, secondDirection };
		}
		return Direction.EMPTY_DIRECTION_ARRAY;
	}

	/**
	 * Gets the mob's equipment.
	 * 
	 * @return The mob's equipment.
	 */
	public Inventory getEquipment() {
		return equipment;
	}

	/**
	 * Gets the first {@link Direction}.
	 * 
	 * @return The direction.
	 */
	public Direction getFirstDirection() {
		return firstDirection;
	}

	/**
	 * Gets the index of this mob.
	 * 
	 * @return The index.
	 */
	public int getIndex() {
		synchronized (this) {
			return index;
		}
	}

	/**
	 * Gets the mob's inventory.
	 * 
	 * @return The inventory.
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Gets the local npc {@link List}.
	 * 
	 * @return The list.
	 */
	public List<Npc> getLocalNpcList() {
		return localNpcs;
	}

	/**
	 * Gets the local player {@link List}.
	 * 
	 * @return The list.
	 */
	public List<Player> getLocalPlayerList() {
		return localPlayers;
	}

	/**
	 * Gets this mob's {@link NpcDefinition}.
	 * 
	 * @param definition The definition.
	 */
	public NpcDefinition getNpcDefinition() {
		return definition;
	}

	/**
	 * Gets the second {@link Direction}.
	 * 
	 * @return The direction.
	 */
	public Direction getSecondDirection() {
		return secondDirection;
	}

	/**
	 * Gets this mob's {@link SkillSet}.
	 * 
	 * @return The skill set.
	 */
	public SkillSet getSkillSet() {
		return skillSet;
	}

	/**
	 * Gets the {@link WalkingQueue}.
	 * 
	 * @return The walking queue.
	 */
	public WalkingQueue getWalkingQueue() {
		return walkingQueue;
	}

	/**
	 * Initialises this mob.
	 */
	private void init() {
		World.getWorld().schedule(new SkillNormalizationTask(this));
	}

	/**
	 * Checks if this mob is active.
	 * 
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isActive() {
		return index != -1;
	}

	/**
	 * Checks if this mob is currently teleporting.
	 * 
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isTeleporting() {
		return teleporting;
	}

	/**
	 * Plays the specified {@link Animation}.
	 * 
	 * @param animation The animation.
	 */
	public void playAnimation(Animation animation) {
		blockSet.add(SynchronizationBlock.createAnimationBlock(animation));
	}

	/**
	 * Plays the specified {@link Graphic}.
	 * 
	 * @param graphic The graphic.
	 */
	public void playGraphic(Graphic graphic) {
		blockSet.add(SynchronizationBlock.createGraphicBlock(graphic));
	}

	/**
	 * Resets the block set.
	 */
	public void resetBlockSet() {
		blockSet = new SynchronizationBlockSet();
	}

	/**
	 * Sets this mob's {@link NpcDefinition}.
	 * 
	 * @param definition The definition.
	 */
	public void setDefinition(NpcDefinition definition) {
		this.definition = definition;
	}

	/**
	 * Sets the next directions for this mob.
	 * 
	 * @param first The first direction.
	 * @param second The second direction.
	 */
	public void setDirections(Direction first, Direction second) {
		firstDirection = first;
		secondDirection = second;
	}

	/**
	 * Sets the index of this mob.
	 * 
	 * @param index The index.
	 */
	public void setIndex(int index) {
		synchronized (this) {
			this.index = index;
		}
	}

	/**
	 * Sets the position of this mob.
	 * 
	 * @param position The position.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * Forces this mob to shout a message. Note that messages can only be shown in the chat box if they are said by a
	 * player.
	 * 
	 * @param message The message.
	 * @param chatBox If the message should be shown in the player's chat box.
	 */
	public void shout(String message, boolean chatBox) {
		blockSet.add(SynchronizationBlock.createForceChatBlock(message));
	}

	/**
	 * Sets whether this mob is teleporting or not.
	 * 
	 * @param teleporting {@code true} if the mob is teleporting, {@code false} if not.
	 */
	public void setTeleporting(boolean teleporting) {
		this.teleporting = teleporting;
	}

	/**
	 * Starts a new action, stopping the current one if it exists.
	 * 
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
		return World.getWorld().schedule(action);
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
	 * Stops the current {@link Animation}.
	 */
	public void stopAnimation() {
		playAnimation(Animation.STOP_ANIMATION);
	}

	/**
	 * Stops the current {@link Graphic}.
	 */
	public void stopGraphic() {
		playGraphic(Graphic.STOP_GRAPHIC);
	}

	/**
	 * Teleports this mob to the specified {@link Position}, setting the appropriate flags and clearing the walking
	 * queue.
	 * 
	 * @param position The position.
	 */
	public void teleport(Position position) {
		teleporting = true;
		this.position = position;
		walkingQueue.clear();
		stopAction(); // TODO do it on any movement is a must... walking queue perhaps?
	}

	/**
	 * Turns the mob to face the specified {@link Position}.
	 * 
	 * @param position The position to face.
	 */
	public void turnTo(Position position) {
		blockSet.add(SynchronizationBlock.createTurnToPositionBlock(position));
	}

	/**
	 * Updates this mob's interacting mob.
	 * 
	 * @param index The index of the interacting mob.
	 */
	public void updateInteractingMob(int index) {
		blockSet.add(SynchronizationBlock.createInteractingMobBlock(index));
	}

}