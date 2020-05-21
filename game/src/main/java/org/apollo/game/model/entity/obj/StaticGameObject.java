package org.apollo.game.model.entity.obj;

import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.RegionCoordinates;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.Player;

/**
 * A {@link GameObject} that is a static part of the game world (i.e. is stored in the game resources).
 *
 * @author Major
 */
public final class StaticGameObject extends GameObject {

	/**
	 * Creates the StaticGameObject.
	 *
	 * @param world The {@link World} containing the StaticGameObject.
	 * @param id The id of the StaticGameObject
	 * @param position The {@link Position} of the StaticGameObject.
	 * @param type The type code of the StaticGameObject.
	 * @param orientation The orientation of the StaticGameObject.
	 */
	public StaticGameObject(World world, int id, Position position, int type, int orientation) {
		super(world, id, position, type, orientation);
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.STATIC_OBJECT;
	}

	@Override
	public boolean viewableBy(Player player, World world) {
		RegionRepository repository = world.getRegionRepository();
		RegionCoordinates coordinates = position.getRegionCoordinates();

		return repository.get(coordinates).contains(this);
	}

}