package org.apollo.game.model.entity.obj;

import org.apollo.game.model.Position;
import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.update.ObjectUpdateOperation;
import org.apollo.game.model.def.ObjectDefinition;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.Player;

import com.google.common.base.MoreObjects;

/**
 * Represents an object in the game world.
 * 
 * @author Chris Fletcher
 * @author Major
 */
public abstract class GameObject extends Entity {

	/**
	 * The packed value that stores this object's id, type, and orientation.
	 */
	private final int packed;

	/**
	 * Creates the GameObject.
	 * 
	 * @param id The id of the GameObject
	 * @param position The {@link Position} of the GameObject.
	 * @param type The type of the GameObject.
	 * @param orientation The orientation of the GameObject.
	 */
	public GameObject(int id, Position position, int type, int orientation) {
		super(position);
		this.packed = id << 8 | (type & 0x3F) << 2 | orientation & 0x3;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GameObject) {
			GameObject other = (GameObject) obj;
			return position.equals(other.position) && packed == other.packed;
		}

		return false;
	}

	/**
	 * Gets the definition of this object.
	 * 
	 * @return The object's definition.
	 */
	public ObjectDefinition getDefinition() {
		return ObjectDefinition.lookup(getId());
	}

	/**
	 * Gets this object's id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return packed >>> 8;
	}

	/**
	 * Gets this object's orientation.
	 * 
	 * @return The orientation.
	 */
	public int getOrientation() {
		return packed & 0x3;
	}

	/**
	 * Gets this object's type.
	 * 
	 * @return The type.
	 */
	public int getType() {
		return (packed >> 2) & 0x3F;
	}

	@Override
	public int hashCode() {
		return packed;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", getId()).add("type", getType()).add("orientation", getOrientation()).toString();
	}

	@Override
	public ObjectUpdateOperation toUpdateOperation(Region region, EntityUpdateType operation) {
		return new ObjectUpdateOperation(region, operation, this);
	}

	/**
	 * Returns whether or not this GameObject can be seen by the specified {@link Player}.
	 * 
	 * @param player The Player.
	 * @return {@code true} if the Player can see this GameObject, {@code false} if not.
	 */
	public abstract boolean viewableBy(Player player);

}