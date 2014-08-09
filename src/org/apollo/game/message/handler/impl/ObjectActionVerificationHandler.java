package org.apollo.game.message.handler.impl;

import java.util.List;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.ObjectActionMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.Sector;
import org.apollo.game.model.area.SectorRepository;
import org.apollo.game.model.def.ObjectDefinition;
import org.apollo.game.model.entity.Entity.EntityType;
import org.apollo.game.model.entity.GameObject;
import org.apollo.game.model.entity.Player;

/**
 * A verification {@link MessageHandler} for the {@link ObjectActionMessage}.
 * 
 * @author Major
 */
public final class ObjectActionVerificationHandler extends MessageHandler<ObjectActionMessage> {

	/**
	 * The world's sector repository.
	 */
	private final SectorRepository repository = World.getWorld().getSectorRepository();

	@Override
	public void handle(MessageHandlerContext ctx, Player player, ObjectActionMessage message) {
		int id = message.getId();
		if (id < 0 || id >= ObjectDefinition.count()) {
			ctx.breakHandlerChain();
			return;
		}

		Position position = message.getPosition();
		Sector sector = repository.fromPosition(position);
		List<GameObject> objects = sector.getEntities(position, EntityType.GAME_OBJECT);

		if (!containsObject(id, objects)) {
			ctx.breakHandlerChain();
			return;
		}

		if (!player.getPosition().isWithinDistance(position, 15)) {
			ctx.breakHandlerChain();
			return;
		}

		ObjectDefinition definition = ObjectDefinition.lookup(id);
		if (message.getOption() >= definition.getMenuActions().length) {
			ctx.breakHandlerChain();
			return;
		}
	}

	/**
	 * Indicates whether or not the {@link List} of {@link GameObject}s contains the object with the specified id.
	 * 
	 * @param id The id of the object.
	 * @param objects The list of objects.
	 * @return {@code true} if the list does contain the object with the specified id, otherwise {@code false}.
	 */
	private boolean containsObject(int id, List<GameObject> objects) {
		for (GameObject object : objects) {
			if (object.getId() == id) {
				return true;
			}
		}
		return false;
	}

}