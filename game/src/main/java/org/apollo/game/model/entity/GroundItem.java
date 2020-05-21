package org.apollo.game.model.entity;

import org.apollo.game.model.Item;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.update.GroupableEntity;
import org.apollo.game.model.area.update.ItemUpdateOperation;
import org.apollo.game.model.area.update.UpdateOperation;

/**
 * An {@link Item} displayed on the ground.
 *
 * @author Major
 */
public final class GroundItem extends Entity implements GroupableEntity {

	/**
	 * Creates a new GroundItem.
	 *
	 * @param world The {@link World} containing the GroundItem.
	 * @param position The {@link Position} of the Item.
	 * @param item The Item displayed on the ground.
	 * @return The GroundItem.
	 */
	public static GroundItem create(World world, Position position, Item item) {
		return new GroundItem(world, position, item, -1);
	}

	/**
	 * Creates a new dropped GroundItem.
	 *
	 * @param world The {@link World} containing the GroundItem.
	 * @param position The {@link Position} of the Item.
	 * @param item The Item displayed on the ground.
	 * @param owner The the {@link Player} who dropped this GroundItem.
	 * @return The GroundItem.
	 */
	public static GroundItem dropped(World world, Position position, Item item, Player owner) {
		return new GroundItem(world, position, item, owner.getIndex());
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
	 * Creates the GroundItem.
	 *
	 * @param world The {@link World} containing the GroundItem.
	 * @param position The {@link Position} of the GroundItem.
	 * @param item The Item displayed on the ground.
	 * @param index The index of the {@link Player} who dropped this GroundItem.
	 */
	private GroundItem(World world, Position position, Item item, int index) {
		super(world, position);
		this.item = item;
		this.index = index;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GroundItem) {
			GroundItem other = (GroundItem) obj;
			return position.equals(other.position);
		}

		return false;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.GROUND_ITEM;
	}

	@Override
	public int getLength() {
		return 1;
	}

	@Override
	public int getWidth() {
		return 1;
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

	@Override
	public int hashCode() {
		return position.hashCode() * 31 + item.hashCode();
	}

	@Override
	public UpdateOperation<GroundItem> toUpdateOperation(Region region, EntityUpdateType operation) {
		return new ItemUpdateOperation(region, operation, this);
	}

}