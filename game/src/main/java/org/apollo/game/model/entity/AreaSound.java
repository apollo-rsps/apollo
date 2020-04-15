package org.apollo.game.model.entity;

import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.update.AreaSoundUpdateOperation;
import org.apollo.game.model.area.update.GroupableEntity;
import org.apollo.game.model.area.update.UpdateOperation;

import java.util.Objects;

/**
 * The type Area sound.
 *
 * @author Khaled Abdeljaber
 */
public class AreaSound extends Entity implements GroupableEntity {

	/**
	 * The sound.
	 */
	private final Sound sound;

	/**
	 * The radius the sound encompasses.
	 */
	private final int area;

	/**
	 * Instantiates a new Area sound.
	 *
	 * @param world    the world
	 * @param position the position
	 * @param sound    the sound
	 * @param area     the area
	 */
	AreaSound(World world, Position position, Sound sound, int area) {
		super(world, position);
		this.position = position;
		this.sound = sound;
		this.area = area;
	}

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public int getId() {
		return sound.getId();
	}

	/**
	 * Gets volume.
	 *
	 * @return the volume
	 */
	public int getVolume() {
		return sound.getVolume();
	}

	/**
	 * Gets delay.
	 *
	 * @return the delay
	 */
	public int getDelay() {
		return sound.getDelay();
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.AREA_SOUND;
	}

	@Override
	public int getLength() {
		return area;
	}

	@Override
	public int getWidth() {
		return area;
	}

	@Override
	public UpdateOperation<?> toUpdateOperation(Region region, EntityUpdateType type) {
		return new AreaSoundUpdateOperation(region, type, this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AreaSound areaSound = (AreaSound) o;
		return area == areaSound.area && sound.equals(areaSound.sound);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sound, area);
	}
}
