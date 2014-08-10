package org.apollo.game.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apollo.game.action.Action;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Direction;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.Sector;
import org.apollo.game.model.area.SectorCoordinates;
import org.apollo.game.model.area.SectorRepository;
import org.apollo.game.model.def.NpcDefinition;
import org.apollo.game.model.entity.attr.Attribute;
import org.apollo.game.model.entity.attr.AttributeMap;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.Inventory.StackMode;
import org.apollo.game.model.inv.InventoryConstants;
import org.apollo.game.scheduling.impl.SkillNormalizationTask;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;

/**
 * A {@link Mob} is a living entity in the world, such as a player or NPC.
 * 
 * @author Graham
 * @author Major
 */
public abstract class Mob extends Entity {

	/**
	 * This mob's current action.
	 */
	private transient Action<?> action;

	/**
	 * The position this mob is facing towards.
	 */
	private transient Position facingPosition = position;

	/**
	 * This mob's first movement direction.
	 */
	private transient Direction firstDirection = Direction.NONE;

	/**
	 * This mob's list of local npcs.
	 */
	private final transient List<Npc> localNpcs = new ArrayList<>();

	/**
	 * This mob's list of local players.
	 */
	private final transient List<Player> localPlayers = new ArrayList<>();

	/**
	 * This mob's second movement direction.
	 */
	private transient Direction secondDirection = Direction.NONE;

	/**
	 * Indicates whether this mob is currently teleporting or not.
	 */
	private transient boolean teleporting = false;

	/**
	 * The attribute map of this entity.
	 */
	protected final AttributeMap attributes = new AttributeMap();

	/**
	 * This mob's set of synchronization blocks.
	 */
	protected transient SynchronizationBlockSet blockSet = new SynchronizationBlockSet();

	/**
	 * This mob's npc definition. A player only uses this if they are appearing as an npc.
	 */
	protected NpcDefinition definition;

	/**
	 * This mob's equipment.
	 */
	protected final Inventory equipment = new Inventory(InventoryConstants.EQUIPMENT_CAPACITY, StackMode.STACK_ALWAYS);

	/**
	 * The index of this mob.
	 */
	protected int index = -1;

	/**
	 * The mob this mob is interacting with.
	 */
	protected Mob interactingMob;

	/**
	 * This mob's inventory.
	 */
	protected final Inventory inventory = new Inventory(InventoryConstants.INVENTORY_CAPACITY);

	/**
	 * This mob's skill set.
	 */
	protected final SkillSet skillSet = new SkillSet();

	/**
	 * This mob's walking queue.
	 */
	protected final transient WalkingQueue walkingQueue = new WalkingQueue(this);

	/**
	 * Creates a new mob with the specified initial {@link Position}.
	 * 
	 * @param position The initial position.
	 */
	public Mob(Position position) {
		super(position);
		init();
	}

	/**
	 * Deals damage to this mob.
	 * 
	 * @param damage The damage dealt.
	 * @param type The type of damage.
	 * @param secondary If the damage should be dealt as a secondary hit.
	 */
	public final void damage(int damage, int type, boolean secondary) {
		Skill hitpoints = skillSet.getSkill(Skill.HITPOINTS);
		int current = Math.max(hitpoints.getCurrentLevel() - damage, 0), maximum = hitpoints.getMaximumLevel();

		blockSet.add(SynchronizationBlock.createHitUpdateBlock(damage, type, current, maximum, secondary));
		skillSet.setSkill(Skill.HITPOINTS, new Skill(hitpoints.getExperience(), current, maximum));
	}

	/**
	 * Gets the value of the attribute with the specified name.
	 * 
	 * @param name The name of the attribute.
	 * @return The value of the attribute.
	 */
	public final Attribute<?> getAttribute(String name) {
		return attributes.getAttribute(name);
	}

	/**
	 * Gets a shallow copy of the attributes of this entity, as a {@link Map}.
	 * 
	 * @return The map of attributes.
	 */
	public final Map<String, Attribute<?>> getAttributes() {
		return attributes.getAttributes();
	}

	/**
	 * Gets this mob's {@link SynchronizationBlockSet}.
	 * 
	 * @return The block set.
	 */
	public final SynchronizationBlockSet getBlockSet() {
		return blockSet;
	}

	/**
	 * Gets this mob's {@link NpcDefinition}.
	 * 
	 * @param definition The definition.
	 */
	public final NpcDefinition getDefinition() {
		return definition;
	}

	/**
	 * Gets this mob's movement {@link Direction}s, as an array.
	 * 
	 * @return A zero, one or two element array containing the directions (in order).
	 */
	public final Direction[] getDirections() {
		if (firstDirection != Direction.NONE) {
			return secondDirection == Direction.NONE ? new Direction[] { firstDirection } : new Direction[] {
					firstDirection, secondDirection };
		}
		return Direction.EMPTY_DIRECTION_ARRAY;
	}

	/**
	 * Gets this mob's equipment.
	 * 
	 * @return The mob's equipment.
	 */
	public final Inventory getEquipment() {
		return equipment;
	}

	/**
	 * Gets the {@link Position} this mob is facing towards.
	 * 
	 * @return The position.
	 */
	public final Position getFacingPosition() {
		return facingPosition;
	}

	/**
	 * Gets the first {@link Direction}.
	 * 
	 * @return The direction.
	 */
	public final Direction getFirstDirection() {
		return firstDirection;
	}

	/**
	 * Gets the index of this mob.
	 * 
	 * @return The index.
	 */
	public final int getIndex() {
		synchronized (this) {
			return index;
		}
	}

	/**
	 * Gets the mob this mob is interacting with.
	 * 
	 * @return The mob.
	 */
	public final Mob getInteractingMob() {
		return interactingMob;
	}

	/**
	 * Gets this mob's inventory.
	 * 
	 * @return The inventory.
	 */
	public final Inventory getInventory() {
		return inventory;
	}

	/**
	 * Gets this mob's local npc {@link List}.
	 * 
	 * @return The list.
	 */
	public final List<Npc> getLocalNpcList() {
		return localNpcs;
	}

	/**
	 * Gets this mob' local player {@link List}.
	 * 
	 * @return The list.
	 */
	public final List<Player> getLocalPlayerList() {
		return localPlayers;
	}

	/**
	 * Gets this mob's second movement {@link Direction}.
	 * 
	 * @return The direction.
	 */
	public final Direction getSecondDirection() {
		return secondDirection;
	}

	/**
	 * Gets this mob's {@link SkillSet}.
	 * 
	 * @return The skill set.
	 */
	public final SkillSet getSkillSet() {
		return skillSet;
	}

	/**
	 * Gets this mob's {@link WalkingQueue}.
	 * 
	 * @return The walking queue.
	 */
	public final WalkingQueue getWalkingQueue() {
		return walkingQueue;
	}

	/**
	 * Checks if this mob is active.
	 * 
	 * @return {@code true} if the mob is active, {@code false} if not.
	 */
	public final boolean isActive() {
		return index != -1;
	}

	/**
	 * Checks if this mob is currently teleporting.
	 * 
	 * @return {@code true} if so, {@code false} if not.
	 */
	public final boolean isTeleporting() {
		return teleporting;
	}

	/**
	 * Makes this mob perform the specified {@link Animation}.
	 * 
	 * @param animation The animation.
	 */
	public final void playAnimation(Animation animation) {
		blockSet.add(SynchronizationBlock.createAnimationBlock(animation));
	}

	/**
	 * Makes this mob perform the specified {@link Graphic}.
	 * 
	 * @param graphic The graphic.
	 */
	public final void playGraphic(Graphic graphic) {
		blockSet.add(SynchronizationBlock.createGraphicBlock(graphic));
	}

	/**
	 * Resets this mob's block set.
	 */
	public final void resetBlockSet() {
		blockSet = new SynchronizationBlockSet();
	}

	/**
	 * Resets the mob this mob is interacting with.
	 */
	public final void resetInteractingMob() {
		interactingMob = null;
		blockSet.add(SynchronizationBlock.createInteractingMobBlock(65535));
	}

	/**
	 * Sets the value of the attribute with the specified name.
	 * 
	 * @param name The name of the attribute.
	 * @param value The attribute.
	 */
	public final void setAttribute(String name, Attribute<?> value) {
		attributes.setAttribute(name, value);
	}

	/**
	 * Sets this mob's {@link NpcDefinition}.
	 * 
	 * @param definition The definition.
	 */
	public final void setDefinition(NpcDefinition definition) {
		this.definition = definition;
	}

	/**
	 * Sets the next movement {@link Direction}s for this mob.
	 * 
	 * @param first The first direction.
	 * @param second The second direction.
	 */
	public final void setDirections(Direction first, Direction second) {
		firstDirection = first;
		secondDirection = second;
	}

	/**
	 * Sets the index of this mob.
	 * 
	 * @param index The index.
	 */
	public final void setIndex(int index) {
		synchronized (this) {
			this.index = index;
		}
	}

	/**
	 * Updates this mob's interacting mob.
	 * 
	 * @param mob The mob.
	 */
	public final void setInteractingMob(Mob mob) {
		interactingMob = mob;
		blockSet.add(SynchronizationBlock.createInteractingMobBlock(mob.index));
	}

	/**
	 * Sets the {@link Position} of this mob.
	 * 
	 * @param position The position.
	 */
	public final void setPosition(Position position) {
		SectorRepository repository = World.getWorld().getSectorRepository();
		Sector newSector = repository.fromPosition(position);

		if (SectorCoordinates.fromPosition(this.position) != SectorCoordinates.fromPosition(position)) {
			Sector oldSector = repository.fromPosition(this.position);
			oldSector.removeEntity(this);
		} else {
			newSector.removeEntity(this);
		}

		this.position = position;
		newSector.addEntity(this);
	}

	/**
	 * Sets whether this mob is teleporting or not.
	 * 
	 * @param teleporting {@code true} if the mob is teleporting, {@code false} if not.
	 */
	public final void setTeleporting(boolean teleporting) {
		this.teleporting = teleporting;
	}

	/**
	 * Forces this mob to shout a message. Only messages said by a player can be shown in the chat box.
	 * 
	 * @param message The message.
	 * @param chatOnly If the message should only be shown in the player's chat box, or above their head too.
	 */
	public void shout(String message, boolean chatOnly) {
		blockSet.add(SynchronizationBlock.createForceChatBlock(message));
	}

	/**
	 * Starts a new action, stopping the current one if it exists.
	 * 
	 * @param action The new action.
	 * @return A flag indicating if the action was started.
	 */
	public final boolean startAction(Action<?> action) {
		if (this.action != null) {
			if (this.action.equals(action)) {
				return false;
			}
			stopAction();
		}
		return World.getWorld().schedule(this.action = action);
	}

	/**
	 * Stops this mob's current action.
	 */
	public final void stopAction() {
		if (action != null) {
			action.stop();
			action = null;
		}
	}

	/**
	 * Stops this mob's current {@link Animation}.
	 */
	public final void stopAnimation() {
		playAnimation(Animation.STOP_ANIMATION);
	}

	/**
	 * Stops this mob's current {@link Graphic}.
	 */
	public final void stopGraphic() {
		playGraphic(Graphic.STOP_GRAPHIC);
	}

	/**
	 * Teleports this mob to the specified {@link Position}, setting the appropriate flags and clearing the walking
	 * queue.
	 * 
	 * @param position The position.
	 */
	public void teleport(Position position) {
		this.position = position;
		teleporting = true;
		walkingQueue.clear();
		stopAction();
	}

	/**
	 * Turns this mob to face the specified {@link Position}.
	 * 
	 * @param position The position to face.
	 */
	public final void turnTo(Position position) {
		blockSet.add(SynchronizationBlock.createTurnToPositionBlock(this.facingPosition = position));
	}

	/**
	 * Initialises this mob.
	 */
	private void init() {
		World.getWorld().schedule(new SkillNormalizationTask(this));
	}

}