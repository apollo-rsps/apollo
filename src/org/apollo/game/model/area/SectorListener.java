package org.apollo.game.model.area;

import org.apollo.game.model.entity.Entity;

/**
 * A class that should be implemented by listeners that execute actions when an entity is added or removed from the
 * sector.
 * 
 * @author Major
 */
@FunctionalInterface
public interface SectorListener {

	/**
	 * Executes the action for this listener.
	 * 
	 * @param sector The {@link Sector} that was updated.
	 * @param entity The affected {@link Entity}.
	 * @param operation The type of {@link SectorOperation}.
	 */
	public abstract void execute(Sector sector, Entity entity, SectorOperation operation);

}