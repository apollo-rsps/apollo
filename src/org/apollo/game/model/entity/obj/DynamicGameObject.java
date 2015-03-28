package org.apollo.game.model.entity.obj;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;

/**
 * A {@link GameObject} that is loaded dynamically, usually for specific Players.
 *
 * @author Major
 */
public final class DynamicGameObject extends GameObject {

	/**
	 * A {@link WeakReference} for {@link Player}s.
	 */
	private static final class WeakPlayerReference extends WeakReference<Player> {

		/**
		 * Creates the WeakPlayerReference.
		 *
		 * @param player The Player wrapped in this {@link WeakReference}.
		 */
		public WeakPlayerReference(Player player) {
			super(player);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof WeakPlayerReference) {
				WeakPlayerReference other = (WeakPlayerReference) obj; // TODO need to have a reference queue

				Player player = get();
				return player != null && player.equals(other.get());
			}

			return false;
		}

		@Override
		public int hashCode() {
			Player player = get();
			return (player == null) ? 0 : player.hashCode();
		}

	}

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
	public static DynamicGameObject createPublic(World world, int id, Position position, int type, int orientation) {
		return new DynamicGameObject(world, id, position, type, orientation, true);
	}

	/**
	 * The flag indicating whether or not this DynamicGameObject is visible to every player.
	 */
	private final boolean alwaysVisible;

	/**
	 * The Set of Players that can view this DynamicGameObject.
	 */
	private final Set<WeakPlayerReference> players = new HashSet<>();

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
	private DynamicGameObject(World world, int id, Position position, int type, int orientation, boolean alwaysVisible) {
		super(world, id, position, type, orientation);
		this.alwaysVisible = alwaysVisible;
	}

	/**
	 * Adds this DynamicGameObject to the view of the specified {@link Player}.
	 * 
	 * @param player The Player.
	 * @return {@code true} if this GameObject was not already visible to the specified Player.
	 */
	public boolean addTo(Player player) {
		WeakPlayerReference reference = new WeakPlayerReference(player);
		return players.add(reference);
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.DYNAMIC_OBJECT;
	}

	/**
	 * Removes this DynamicGameObject from the view of the specified {@link Player}.
	 * 
	 * @param player The Player.
	 * @return {@code true} if this GameObject was visible to the specified Player.
	 */
	public boolean removeFrom(Player player) {
		WeakPlayerReference reference = new WeakPlayerReference(player);
		return players.remove(reference);
	}

	@Override
	public boolean viewableBy(Player player, World world) {
		return alwaysVisible || players.contains(new WeakPlayerReference(player));
	}

}