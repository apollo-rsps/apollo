package org.apollo.game.model.entity;

import org.apollo.cache.def.NpcDefinition;
import org.apollo.game.action.Action;
import org.apollo.game.model.*;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.entity.attr.Attribute;
import org.apollo.game.model.entity.attr.AttributeMap;
import org.apollo.game.model.event.impl.MobPositionUpdateEvent;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.Inventory.StackMode;
import org.apollo.game.model.inv.InventoryConstants;
import org.apollo.game.scheduling.impl.SkillNormalizationTask;
import org.apollo.game.sync.block.InteractingMobBlock;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A living entity in the world, such as a player or npc.
 *
 * @author Graham
 * @author Major
 */
public abstract class Mob extends Entity {

	/**
	 * The attribute map of this mob.
	 */
	protected final AttributeMap attributes = new AttributeMap();

	/**
	 * This mob's equipment.
	 */
	protected final Inventory equipment = new Inventory(InventoryConstants.EQUIPMENT_CAPACITY, StackMode.STACK_ALWAYS);

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
	protected final WalkingQueue walkingQueue = new WalkingQueue(this);

	/**
	 * This mob's list of local npcs.
	 */
	private final List<Npc> localNpcs = new ArrayList<>();

	/**
	 * This mob's list of local players.
	 */
	private final List<Player> localPlayers = new ArrayList<>();

	/**
	 * This mob's set of synchronization blocks.
	 */
	protected SynchronizationBlockSet blockSet = new SynchronizationBlockSet();

	/**
	 * This mob's npc definition. A player only uses this if they are appearing as an npc.
	 */
	protected Optional<NpcDefinition> definition;

	/**
	 * The index of this mob.
	 */
	protected int index = -1;

	/**
	 * The mob this mob is interacting with.
	 */
	protected Mob interactingMob;

	/**
	 * This mob's current action.
	 */
	private Action<?> action;

	/**
	 * The position this mob is facing towards.
	 */
	private Position facingPosition = position;

	/**
	 * This mob's first movement direction.
	 */
	private Direction firstDirection = Direction.NONE;

	/**
	 * The last facing direction of this mob.
	 */
	private Direction lastDirection = Direction.NORTH;

	/**
	 * This mob's second movement direction.
	 */
	private Direction secondDirection = Direction.NONE;

	/**
	 * Indicates whether this mob is currently teleporting or not.
	 */
	private boolean teleporting;

	/**
	 * Creates the Mob.
	 *
	 * @param world The {@link World} containing the Mob
	 * @param position The {@link Position} of the Mob.
	 * @param definition The {@link NpcDefinition} of the Mob.
	 */
	protected Mob(World world, Position position, NpcDefinition definition) {
		super(world, position);
		this.definition = Optional.ofNullable(definition);

		init();
	}

	/**
	 * Creates the Mob.
	 *
	 * @param world The {@link World} containing the Mob
	 * @param position The {@link Position} of the Mob.
	 */
	protected Mob(World world, Position position) {
		this(world, position, null);
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
		return attributes.get(name);
	}

	/**
	 * Gets a shallow copy of the attributes of this mob, as a {@link Map}.
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
	 * @return The npc definition.
	 */
	public final NpcDefinition getDefinition() {
		return definition.get();
	}

	/**
	 * Gets this mob's movement {@link Direction}s, as an array.
	 *
	 * @return A zero, one or two element array containing the directions (in order).
	 */
	public final Direction[] getDirections() {
		if (firstDirection != Direction.NONE) {
			return secondDirection == Direction.NONE ? new Direction[]{firstDirection}
				: new Direction[]{firstDirection, secondDirection};
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
	 * Gets the current action, if any, of this mob.
	 *
	 * @return The action.
	 */
	public final Action<?> getAction() {
		return action;
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
	 * Returns this mobs interacting index.
	 *
	 * @return The interaction index of this mob.
	 */
	public int getInteractionIndex() {
		return index;
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
	 * Gets the last facing direction of this mob.
	 *
	 * @return The last direction this mob was facing.
	 */
	public Direction getLastDirection() {
		return lastDirection;
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
	 * Gets this mob's local player {@link List}.
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

	@Override
	public int getLength() {
		return definition.map(NpcDefinition::getSize).orElse(1);
	}

	@Override
	public int getWidth() {
		return definition.map(NpcDefinition::getSize).orElse(1);
	}

	/**
	 * Check whether this mob has a current active {@link Action}.
	 *
	 * @return {@code true} if this mob has a non-null {@link Action}.
	 */
	public final boolean hasAction() {
		return action != null;
	}

	/**
	 * Returns whether or not this mob has an {@link NpcDefinition}.
	 *
	 * @return {@code true} if this mob has an npc definition, {@code false} if not.
	 */
	public final boolean hasNpcDefinition() {
		return definition.isPresent();
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
		blockSet.add(SynchronizationBlock.createInteractingMobBlock(InteractingMobBlock.RESET_INDEX));
	}

	/**
	 * Sets the value of the attribute with the specified name.
	 *
	 * @param name The name of the attribute.
	 * @param value The attribute.
	 */
	public final void setAttribute(String name, Attribute<?> value) {
		attributes.set(name, value);
	}

	/**
	 * Sets this mob's {@link NpcDefinition}.
	 *
	 * @param definition The definition. Must not be {@code null}.
	 * @throws NullPointerException If the specified definition is {@code null}.
	 */
	public final void setDefinition(NpcDefinition definition) {
		this.definition = Optional.of(definition);
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
		blockSet.add(SynchronizationBlock.createInteractingMobBlock(mob.getInteractionIndex()));
	}

	/**
	 * Set the last direction this mob was facing.
	 *
	 * @param lastDirection The direction to set.
	 */
	public void setLastDirection(Direction lastDirection) {
		this.lastDirection = lastDirection;
	}

	/**
	 * Sets the {@link Position} of this mob.
	 * <p>
	 * This method may be intercepted using a {@link MobPositionUpdateEvent}, which can be terminated like any
	 * other. Plugins that intercept this Event <strong>must</strong> be cautious, because movement will not be
	 * possible (even through mechanisms such as teleporting) if the Event is terminated.
	 *
	 * @param position The Position.
	 */
	public final void setPosition(Position position) {
		if (!position.equals(this.position) && world.submit(new MobPositionUpdateEvent(this, position))) {
			Position old = this.position;
			RegionRepository repository = world.getRegionRepository();
			Region current = repository.fromPosition(old), next = repository.fromPosition(position);

			current.removeEntity(this);
			this.position = position; // addEntity relies on the position being updated, so do that first.

			next.addEntity(this);
		}
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
	 * Gets the number of tiles this mob occupies.
	 *
	 * @return The number of tiles this mob occupies.
	 */
	public int size() {
		return definition.map(NpcDefinition::getSize).orElse(1);
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

		this.action = action;
		return world.schedule(action);
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
		setPosition(position);
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
		facingPosition = position;
		blockSet.add(SynchronizationBlock.createTurnToPositionBlock(position));
	}

	/**
	 * Initialises this mob.
	 */
	private void init() {
		world.schedule(new SkillNormalizationTask(this));
	}


}