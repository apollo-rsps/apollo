package org.apollo.game.model.area.update;

import org.apollo.game.message.impl.encode.AreaSoundMessage;
import org.apollo.game.message.impl.encode.RegionUpdateMessage;
import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.entity.AreaSound;
import org.apollo.game.model.entity.SpotAnim;

/**
 * @author Khaled Abdeljaber
 */
public class AreaSoundUpdateOperation extends UpdateOperation<AreaSound> {

	/**
	 * Creates the UpdateOperation.
	 *
	 * @param region The region in which the UpdateOperation occurred. Must not be {@code null}.
	 * @param type   The type of {@link EntityUpdateType}. Must not be {@code null}.
	 * @param entity The {@link Entity} being added or removed. Must not be {@code null}.
	 */
	public AreaSoundUpdateOperation(Region region, EntityUpdateType type, AreaSound entity) {
		super(region, type, entity);
	}

	@Override
	protected RegionUpdateMessage add(int offset) {
		return new AreaSoundMessage(entity, offset);
	}

	@Override
	protected RegionUpdateMessage remove(int offset) {
		throw new IllegalStateException("Area sounds cannot be removed.");
	}
}

