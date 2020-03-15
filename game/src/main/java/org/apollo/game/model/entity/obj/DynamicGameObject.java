package org.apollo.game.model.entity.obj;

import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@link GameObject} that is loaded dynamically, usually for specific Players.
 *
 * @author Major
 */
public final class DynamicGameObject extends GameObject {

	/**
	 * Creates a DynamicGameObject that is visible only to {@link Player}s specified later.
	 *
	 * @param world The {@link World} containing the DynamicGameObject.
	 * @param id The id of the DynamicGameObject
	 * @param position The {@link Position} of the DynamicGameObject.
	 * @param type The type of the DynamicGameObject.
	 * @param orientation The orientation of the DynamicGameObject.
	 * @return The DynamicGameObject.
	 */
	public static DynamicGameObject createLocal(World world, int id, Position position, int type, int orientation) {
		return new DynamicGameObject(world, id, position, type, orientation, false);
	}

	/**
	 * Creates a DynamicGameObject that is always visible.
	 *
	 * @param world The {@link World} containing the DynamicGameObject.
	 * @param id The id of the DynamicGameObject
	 * @param position The {@link Position} of the DynamicGameObject.
	 * @param type The type of the DynamicGameObject.
	 * @param orientation The orientation of the DynamicGameObject.
	 * @return The DynamicGameObject.
	 */
	public static DynamicGameObject createPublic(World world, int id, Position position, int type,
			int orientation) {
		return new DynamicGameObject(world, id, position, type, orientation, true);
	}

	/**
	 * The flag indicating whether or not this DynamicGameObject is visible to every player.
	 */
	private final boolean alwaysVisible;

	/**
	 * The Set of Player usernames that can view this DynamicGameObject.
	 */
	private final Set<String> players = new HashSet<>(); // TODO more appropriate type?

	/**
	 * Creates the DynamicGameObject.
	 *
	 * @param world The {@link World} containing the DynamicGameObject.
	 * @param id The id of the DynamicGameObject
	 * @param position The {@link Position} of the DynamicGameObject.
	 * @param type The type of the DynamicGameObject.
	 * @param orientation The orientation of the DynamicGameObject.
	 * @param alwaysVisible The flag indicates whether or not this DynamicGameObject is visible to every player.
	 */
	private DynamicGameObject(World world, int id, Position position, int type, int orientation,
			boolean alwaysVisible) {
		super(world, id, position, type, orientation);
		this.alwaysVisible = alwaysVisible;
	}

	/**
	 * Adds this DynamicGameObject to the view of the specified {@link Player}.
	 *
	 * @param player The Player.
	 * @return {@code true} if this DynamicGameObject was not already visible to the specified Player.
	 */
	public boolean addTo(Player player) {
		return players.add(player.getUsername());
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.DYNAMIC_OBJECT;
	}

	/**
	 * Removes this DynamicGameObject from the view of the specified {@link Player}.
	 *
	 * @param player The Player.
	 * @return {@code true} if this DynamicGameObject was visible to the specified Player.
	 */
	public boolean removeFrom(Player player) {
		return players.remove(player.getUsername());
	}

	@Override
	public boolean viewableBy(Player player, World world) {
		return alwaysVisible || players.contains(player.getUsername());
	}

}