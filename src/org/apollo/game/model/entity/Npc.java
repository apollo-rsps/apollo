package org.apollo.game.model.entity;

import org.apollo.game.model.Position;
import org.apollo.game.model.def.NpcDefinition;
import org.apollo.game.sync.block.SynchronizationBlock;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * An {@link Npc} is a {@link Mob} that is not being controlled by a player.
 * 
 * @author Major
 */
public final class Npc extends Mob {

	/**
	 * This npc's id.
	 */
	private int id;

	/**
	 * The positions representing the bounds (i.e. walking limits) of this npc.
	 */
	private Position[] boundary;

	/**
	 * Creates a new npc with the specified id and {@link Position}.
	 * 
	 * @param id The id.
	 * @param position The position.
	 */
	public Npc(int id, Position position) {
		this(NpcDefinition.lookup(id), position);
	}

	/**
	 * Creates a new npc with the specified {@link NpcDefinition} and {@link Position}.
	 * 
	 * @param definition The definition.
	 * @param position The position.
	 */
	public Npc(NpcDefinition definition, Position position) {
		super(position);
		this.definition = definition;
		this.id = definition.getId();
	}

	/**
	 * Gets the boundary of this npc.
	 * 
	 * @return The boundary.
	 */
	public Position[] getBoundary() {
		return boundary;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.NPC;
	}

	/**
	 * Gets the id of this npc.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Indicates whether or not this npc is bound to a specific set of coordinates.
	 * 
	 * @return {@code true} if the npc is bound, otherwise {@code false}.
	 */
	public boolean isBound() {
		return boundary == null;
	}

	/**
	 * Sets the boundary of this npc.
	 * 
	 * @param boundary The boundary.
	 */
	public void setBoundary(Position[] boundary) {
		Preconditions.checkArgument(boundary.length == 4, "Boundary count must be 4.");
		this.boundary = boundary;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", definition.getId()).add("name", definition.getName()).toString();
	}

	/**
	 * Transforms this npc into the npc with the specified id.
	 * 
	 * @param id The id.
	 */
	public void transform(int id) {
		Preconditions.checkArgument(id >= 0 && id < NpcDefinition.count(), "Id to transform to is out of bounds.");
		definition = NpcDefinition.lookup(this.id = id);
		blockSet.add(SynchronizationBlock.createTransformBlock(id));
	}

}