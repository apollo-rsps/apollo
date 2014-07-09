package org.apollo.game.event.handler.impl;

import java.util.List;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ObjectActionEvent;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.Sector;
import org.apollo.game.model.def.ObjectDefinition;
import org.apollo.game.model.entity.Entity.EntityType;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.obj.GameObject;

/**
 * A verification {@link EventHandler} for the {@link ObjectActionEvent}.
 * 
 * @author Major
 */
public final class ObjectActionVerificationHandler extends EventHandler<ObjectActionEvent> {

    @Override
    public void handle(EventHandlerContext ctx, Player player, ObjectActionEvent event) {
	int id = event.getId();
	if (id < 0 || id >= ObjectDefinition.count()) {
	    ctx.breakHandlerChain();
	    return;
	}

	Position position = event.getPosition();
	Sector sector = World.getWorld().getSectorRepository().fromPosition(position);
	List<GameObject> objects = sector.getEntities(position, EntityType.GAME_OBJECT);

	if (!containsObject(id, objects)) {
	    ctx.breakHandlerChain();
	    return;
	}

	if (!player.getPosition().isWithinDistance(position, 15)) {
	    ctx.breakHandlerChain();
	    return;
	}

	// TODO is this right?
	if (event.getOption() >= ObjectDefinition.lookup(id).getMenuActions().length) {
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