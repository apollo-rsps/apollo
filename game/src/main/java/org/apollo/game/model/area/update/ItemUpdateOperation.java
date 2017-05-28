package org.apollo.game.model.area.update;

import org.apollo.game.message.impl.RegionUpdateMessage;
import org.apollo.game.message.impl.RemoveTileItemMessage;
import org.apollo.game.message.impl.SendPublicTileItemMessage;
import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.entity.GroundItem;

/**
 * A {@link UpdateOperation} for {@link GroundItem}s.
 *
 * @author Major
 */
public final class ItemUpdateOperation extends UpdateOperation<GroundItem> {

	/**
	 * Creates the ItemUpdateOperation.
	 *
	 * @param region The {@link Region} the type occurred in. Must not be {@code null}.
	 * @param type The {@link EntityUpdateType}. Must not be {@code null}.
	 * @param item The modified {@link GroundItem}. Must not be {@code null}.
	 */
	public ItemUpdateOperation(Region region, EntityUpdateType type, GroundItem item) {
		super(region, type, item);
	}

	@Override
	protected RegionUpdateMessage add(int offset) {
		return new SendPublicTileItemMessage(entity.getItem(), entity.getOwnerIndex(), offset);
	}

	@Override
	protected RegionUpdateMessage remove(int offset) {
		return new RemoveTileItemMessage(entity.getItem(), offset);
	}

}