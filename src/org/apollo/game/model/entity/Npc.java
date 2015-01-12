package org.apollo.game.model.entity;

import java.util.Arrays;
import java.util.Optional;

import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.Sector;
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
		this(position, NpcDefinition.lookup(id));
	}

	/**
	 * Creates a new npc with the specified {@link NpcDefinition} and {@link Position}.
	 * 
	 * @param position The position.
	 * @param definition The definition.
	 */
	public Npc(Position position, NpcDefinition definition) {
		super(position, definition);

		init();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Npc) {
			Npc other = (Npc) obj;
			return position.equals(other.position) && Arrays.equals(boundary, other.boundary) && getId() == other.getId();
		}

		return false;
	}

	/**
	 * Gets the boundary of this npc.
	 * 
	 * @return The boundary.
	 */
	public Position[] getBoundary() {
		return boundary.clone();
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
		return definition.get().getId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime * position.hashCode() + Arrays.hashCode(boundary);
		return prime * result + getId();
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
		this.boundary = boundary.clone();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", getId()).add("name", definition.get().getName()).toString();
	}

	/**
	 * Transforms this npc into the npc with the specified id.
	 * 
	 * @param id The id.
	 */
	public void transform(int id) {
		Preconditions.checkElementIndex(id, NpcDefinition.count(), "Id to transform to is out of bounds.");

		definition = Optional.of(NpcDefinition.lookup(id));
		blockSet.add(SynchronizationBlock.createTransformBlock(id));
	}

	/**
	 * Initialises this npc.
	 */
	private void init() {
		// This has to be here instead of in Mob#init because of ordering issues - the player cannot be added to the
		// sector until their credentials have been set, which is only done after the super constructors are called.
		Sector sector = World.getWorld().getSectorRepository().get(position.getSectorCoordinates());
		sector.addEntity(this);
	}

}