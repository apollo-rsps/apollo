package org.apollo.game.model.area.update;

import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;

/**
 * An entity that can be sent as part of a grouped region update message.
 * <p>
 * Only {@link org.apollo.game.model.entity.Entity} extensions may implement this interface.
 * 
 * @author Major
 */
public interface GroupableEntity {

	/**
	 * Gets this Entity, as an {@link UpdateOperation} of a {@link Region}.
	 *
	 * @param region The Region.
	 * @param type The EntityUpdateType.
	 * @return The UpdateOperation.
	 */
	public UpdateOperation<?> toUpdateOperation(Region region, EntityUpdateType type);

}