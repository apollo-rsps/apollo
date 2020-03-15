package org.apollo.game.model.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.apollo.cache.def.NpcDefinition;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.sync.block.SynchronizationBlock;

import java.util.Optional;

/**
 * A {@link Mob} that is not controlled by a player.
 *
 * @author Major
 */
public final class Npc extends Mob {

	/**
	 * The Positions representing the boundaries (i.e. walking limits) of this Npc.
	 */
	private Optional<Position[]> boundaries;

	/**
	 * Creates the Npc.
	 *
	 * @param world The {@link World} containing the Npc.
	 * @param id The id.
	 * @param position The position.
	 */
	public Npc(World world, int id, Position position) {
		this(world, position, NpcDefinition.lookup(id), null);
	}

	/**
	 * Creates the Npc.
	 *
	 * @param world The {@link World} containing the Npc.
	 * @param position The Position.
	 * @param definition The NpcDefinition.
	 * @param boundaries The boundary Positions.
	 */
	public Npc(World world, Position position, NpcDefinition definition, Position[] boundaries) {
		super(world, position, definition);

		this.boundaries = Optional.ofNullable(boundaries);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Npc) {
			Npc other = (Npc) obj;
			return index == other.index && getId() == other.getId();
		}

		return false;
	}

	/**
	 * Gets the boundaries of this Npc.
	 *
	 * @return The boundaries.
	 */
	public Optional<Position[]> getBoundaries() {
		return boundaries.isPresent() ? Optional.of(boundaries.get().clone()) : Optional.empty();
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.NPC;
	}

	/**
	 * Gets the id of this Npc.
	 *
	 * @return The id.
	 */
	public int getId() {
		return definition.get().getId();
	}

	/**
	 * Returns whether or not this Npc has boundaries.
	 *
	 * @return {@code true} if this Npc has boundaries, {@code false} if not.
	 */
	public boolean hasBoundaries() {
		return boundaries.isPresent();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime * index + getId();
	}

	/**
	 * Sets the boundaries of this Npc.
	 *
	 * @param boundaries The boundaries.
	 */
	public void setBoundaries(Position[] boundaries) {
		Preconditions.checkArgument(boundaries.length == 2, "Boundary count must be 2.");
		this.boundaries = Optional.of(boundaries.clone());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", getId()).add("name", definition.get().getName()).toString();
	}

	/**
	 * Transforms this Npc into the Npc with the specified id.
	 *
	 * @param id The id.
	 */
	public void transform(int id) {
		Preconditions.checkElementIndex(id, NpcDefinition.count(), "Id to transform to is out of bounds.");

		definition = Optional.of(NpcDefinition.lookup(id));
		blockSet.add(SynchronizationBlock.createTransformBlock(id));
	}

}