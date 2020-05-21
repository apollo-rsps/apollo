package org.apollo.game.model.area.update;

import org.apollo.game.message.impl.encode.RegionUpdateMessage;
import org.apollo.game.message.impl.encode.SpotAnimMessage;
import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.entity.SpotAnim;
import org.apollo.game.model.entity.obj.GameObject;

/**
 * @author Khaled Abdeljaber
 */
public class SpotAnimUpdateOperation extends UpdateOperation<SpotAnim> {

	/**
	 * Creates the UpdateOperation.
	 *
	 * @param region The region in which the UpdateOperation occurred. Must not be {@code null}.
	 * @param type   The type of {@link EntityUpdateType}. Must not be {@code null}.
	 * @param entity The {@link Entity} being added or removed. Must not be {@code null}.
	 */
	public SpotAnimUpdateOperation(Region region, EntityUpdateType type, SpotAnim entity) {
		super(region, type, entity);
	}

	@Override
	protected RegionUpdateMessage add(int offset) {
		return new SpotAnimMessage(entity, offset);
	}

	@Override
	protected RegionUpdateMessage remove(int offset) {
		throw new IllegalStateException("SpotAnims cannot be removed.");
	}
}
