package org.apollo.game.message.handler;

import org.apollo.cache.def.ObjectDefinition;
import org.apollo.game.message.impl.ObjectActionMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.obj.GameObject;

import java.util.List;
import java.util.Set;

/**
 * A verification {@link MessageHandler} for the {@link ObjectActionMessage}.
 *
 * @author Major
 */
public final class ObjectActionVerificationHandler extends MessageHandler<ObjectActionMessage> {

	/**
	 * Indicates whether or not the {@link List} of {@link GameObject}s contains the object with the specified id.
	 *
	 * @param id The id of the object.
	 * @param objects The list of objects.
	 * @return {@code true} if the list does contain the object with the specified id, otherwise {@code false}.
	 */
	public static boolean containsObject(int id, Set<GameObject> objects) {
		return objects.stream().anyMatch(object -> object.getId() == id);
	}

	/**
	 * Creates the ObjectActionVerificationHandler.
	 *
	 * @param world The {@link World} the {@link ObjectActionMessage} occurred in.
	 */
	public ObjectActionVerificationHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, ObjectActionMessage message) {
		int id = message.getId();
		if (id < 0 || id >= ObjectDefinition.count()) {
			message.terminate();
			return;
		}

		Position position = message.getPosition();
		Region region = world.getRegionRepository().fromPosition(position);
		Set<GameObject> objects = region.getEntities(position, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT);

		if (!player.getPosition().isWithinDistance(position, 15) || !containsObject(id, objects)) {
			message.terminate();
			return;
		}

		ObjectDefinition definition = ObjectDefinition.lookup(id);
		if (message.getOption() >= definition.getMenuActions().length) {
			message.terminate();
			return;
		}
	}

}