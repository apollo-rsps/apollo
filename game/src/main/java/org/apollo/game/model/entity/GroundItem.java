package org.apollo.game.model.entity;

import org.apollo.game.model.Item;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.update.GroupableEntity;
import org.apollo.game.model.area.update.ItemUpdateOperation;
import org.apollo.game.model.area.update.UpdateOperation;

import java.util.Objects;

/**
 * An {@link Item} displayed on the ground.
 *
 * @author Major
 */
public final class GroundItem extends Entity implements GroupableEntity {

	/**
	 * Creates a new <strong>global</strong> GroundItem.
	 *
	 * @param world The {@link World} containing the GroundItem.
	 * @param position The {@link Position} of the Item.
	 * @param item The Item displayed on the ground.
	 * @return The GroundItem.
	 */
	public static GroundItem createGlobal(World world, Position position, Item item) {
		return new GroundItem(world, position, item, -1, true);
	}

	/**
	 * Creates a new <strong>non-global</strong> dropped GroundItem.
	 *
	 * @param world The {@link World} containing the GroundItem.
	 * @param position The {@link Position} of the Item.
	 * @param item The Item displayed on the ground.
	 * @param owner The the {@link Player} who dropped this GroundItem.
	 * @return The GroundItem.
	 */
	public static GroundItem dropped(World world, Position position, Item item, Player owner) {
		return new GroundItem(world, position, item, owner.getIndex(), false);
	}

	/**
	 * The index of the Player who dropped this GroundItem.
	 */
	private final int index;

	/**
	 * The Item displayed on the ground.
	 */
	private final Item item;

	/**
	 * Represents the global state of this GroundItem.
	 */
	private boolean global;

	/**
	 * Represents the availability state of this GroundItem.
	 */
	private boolean available = true;

	/**
	 * Creates the GroundItem.
	 *
	 * @param world The {@link World} containing the GroundItem.
	 * @param position The {@link Position} of the GroundItem.
	 * @param item The Item displayed on the ground.
	 * @param index The index of the {@link Player} who dropped this GroundItem.
	 * @param global The global state of this GroundItem.
	 */
	private GroundItem(World world, Position position, Item item, int index, boolean global) {
		super(world, position);
		this.item = item;
		this.index = index;
		this.global = global;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GroundItem) {
			GroundItem other = (GroundItem) obj;
			return item.equals(other.item) && position.equals(other.position) && global == other.global;
		}

		return false;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.GROUND_ITEM;
	}

	/**
	 * Gets the {@link Item} displayed on the ground.
	 *
	 * @return The Item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Gets the index of the {@link Player} who dropped this GroundItem, or {@code -1} if this GroundItem was not
	 * dropped by a Player.
	 *
	 * @return The index.
	 */
	public int getOwnerIndex() {
		return index;
	}

	/**
	 * Gets the global state of this GroundItem.
	 *
	 * @return {@code true} iff this GroundItem is global.
	 */
	public boolean isGlobal() {
		return global;
	}

	/**
	 * Globalizes this GroundItem.
	 *
	 * @throws IllegalStateException If this GroundItem is already global.
	 */
	public void globalize() {
		if (global) {
			throw new IllegalStateException("Ground item state has already been set to global.");
		}
		global = true;
	}

	/**
	 * Gets the availability of this GroundItem.
	 *
	 * @return {@code true} iff this GroundItem is available to be picked up.
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * Sets whether or not this GroundItem is available.
	 *
	 * @param available {@code true} if this GroundItem is available to be picked up, otherwise {@code false}.
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}

	@Override
	public int hashCode() {
		return Objects.hash(item, position, global);
	}

	@Override
	public UpdateOperation<GroundItem> toUpdateOperation(Region region, EntityUpdateType operation) {
		return new ItemUpdateOperation(region, operation, this);
	}

}