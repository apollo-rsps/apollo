package org.apollo.game.model.entity;

import com.google.common.base.Preconditions;
import org.apollo.cache.map.Tile;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.update.GroupableEntity;
import org.apollo.game.model.area.update.ProjectileUpdateOperation;
import org.apollo.game.model.area.update.SpotAnimUpdateOperation;
import org.apollo.game.model.area.update.UpdateOperation;

import java.util.Objects;

/**
 * The type Spot anim.
 *
 * @author Khaled Abdeljaber
 */
public class SpotAnim extends Entity implements GroupableEntity {

	/**
	 * The graphic for this spot anim.
	 */
	private final Graphic graphic;

	/**
	 * Instantiates a new Spot anim.
	 *
	 * @param world    the world
	 * @param position the position
	 * @param graphic  the graphic
	 */
	public SpotAnim(World world, Position position, Graphic graphic) {
		super(world, position);
		this.graphic = graphic;
	}

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public int getId() {
		return graphic.getId();
	}

	/**
	 * Gets height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return graphic.getHeight();
	}

	/**
	 * Gets delay.
	 *
	 * @return the delay
	 */
	public int getDelay() {
		return graphic.getDelay();
	}


	@Override
	public UpdateOperation<?> toUpdateOperation(Region region, EntityUpdateType type) {
		Preconditions.checkArgument(type == EntityUpdateType.ADD, "Spotanims cannot be removed from the client");

		return new SpotAnimUpdateOperation(region, type, this);
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.SPOT_ANIM;
	}

	@Override
	public int getLength() {
		return 1;
	}

	@Override
	public int getWidth() {
		return 1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SpotAnim spotAnim = (SpotAnim) o;
		return graphic.equals(spotAnim.graphic);
	}

	@Override
	public int hashCode() {
		return Objects.hash(graphic);
	}

	/**
	 * Gets graphic.
	 *
	 * @return the graphic
	 */
	public Graphic getGraphic() {
		return graphic;
	}
}
