package org.apollo.game.model.entity;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.update.GroupableEntity;
import org.apollo.game.model.area.update.ProjectileUpdateOperation;

/**
 * A projectile fired through the game world, such as an arrow.
 *
 * @author Major
 */
public final class Projectile extends Entity implements GroupableEntity {

	/**
	 * A builder for {@link Projectile}s.
	 */
	public static final class ProjectileBuilder {

		/**
		 * The time, in ticks, before the projectile is fired.
		 */
		private int delay;

		/**
		 * The ending {@link Position} of the Projectile.
		 */
		private Position destination;

		/**
		 * The ending height of the projectile. Defaults to the most commonly used ending height (for arrows and combat
		 * magic).
		 */
		private int endHeight = 40;

		/**
		 * The id of the graphic played as the Projectile proceeds.
		 */
		private int graphic;

		/**
		 * The time, in ticks, that the Projectile is present.
		 */
		private int lifetime;

		/**
		 * The pitch of the Projectile as it climbs. Defaults to the most commonly used pitch (for arrows and combat
		 * magic).
		 */
		private int pitch = 15;

		/**
		 * The starting {@link Position} of the Projectile.
		 */
		private Position source;

		/**
		 * The starting height of the projectile, relative to the height of the tile the Projectile starts on. Defaults
		 * to the most commonly used starting height (for arrows and combat magic).
		 */
		private int startHeight = 50;

		/**
		 * The index of the target Mob (which must be 0 if the Projectile does not target a Mob).
		 */
		private int target;

		/**
		 * The offset from the center of the {@code source} tile in world units. Defaults to 1/2 of
		 * a tile (or a {@link Mob} with a size of 1).
		 */
		private int offset = 64;

		/**
		 * The {@link World} the projectile will be built in.
		 */
		private final World world;

		/**
		 * Creates a new ProjectileBuilder.
		 *
		 * @param world The {@link World} the projectile will be built in.
         */
		public ProjectileBuilder(World world) {
			this.world = world;
		}

		/**
		 * Builds this ProjectileBuilder into a {@link Projectile}.
		 *
		 * @return The built {@link Projectile}. Will never be {@code null}.
		 * @throws IllegalArgumentException If {@code lifetime} is not positive.
		 * @throws NullPointerException If the source or destination {@link Position}s are {@code null}.
		 */
		public Projectile build() {
			Preconditions.checkNotNull(source, "Projectile must have a source.");
			Preconditions.checkNotNull(destination, "Projectile must have a destination.");
			Preconditions.checkArgument(lifetime > 0, "Lifetime must be positive.");

			return new Projectile(world, source, delay, destination, endHeight, graphic, lifetime, pitch,
				startHeight, target, offset);
		}

		/**
		 * Sets the delay before the Projectile is fired.
		 *
		 * @param delay The delay, in ticks. Must not be negative.
		 * @return This {@link ProjectileBuilder}, for chaining. Will never be {@code null}.
		 */
		public ProjectileBuilder delay(int delay) {
			this.delay = delay;
			return this;
		}

		/**
		 * Sets the destination {@link Position} of the {@link Projectile}.
		 *
		 * @param destination The destination {@link Position}. Must not be {@code null}.
		 * @return This {@link ProjectileBuilder}, for chaining. Will never be {@code null}.
		 */
		public ProjectileBuilder destination(Position destination) {
			this.destination = destination;
			return this;
		}

		/**
		 * Sets the end height of the {@link Projectile}. Note that this is the height in 'world units',
		 * <strong>not</strong> the height as described in {@link Position}s.
		 *
		 * @param height The end height of the {@link Projectile}. Must be {@code 0 <= height <= 255}.
		 * @return This {@link ProjectileBuilder}, for chaining. Will never be {@code null}.
		 */
		public ProjectileBuilder endHeight(int height) {
			this.endHeight = height;
			return this;
		}

		/**
		 * Sets the id of the graphic played by the {@link Projectile}.
		 *
		 * @param graphic The graphic id. Must not be negative.
		 * @return This {@link ProjectileBuilder}, for chaining. Will never be {@code null}.
		 */
		public ProjectileBuilder graphic(int graphic) {
			this.graphic = graphic;
			return this;
		}

		/**
		 * Sets the amount of time, in ticks, that the {@link Projectile} lasts in the game world.
		 *
		 * @param lifetime The lifetime of the {@link Projectile}, in ticks. Must not be negative.
		 * @return This {@link ProjectileBuilder}, for chaining. Will never be {@code null}.
		 */
		public ProjectileBuilder lifetime(int lifetime) {
			this.lifetime = lifetime;
			return this;
		}

		/**
		 * Sets the pitch of the {@link Projectile}.
		 *
		 * @param pitch The pitch.
		 * @return This {@link ProjectileBuilder}, for chaining. Will never be {@code null}.
		 */
		public ProjectileBuilder pitch(int pitch) {
			this.pitch = pitch;
			return this;
		}

		/**
		 * Sets the {@code offset} of the {@link Projectile} from the source tile. in 'world units'.
		 *
		 * @param offset The offset from the source tile in world units.
         * @return This {@link ProjectileBuilder}, for chaining. Will never be {@code null}.
         */
		public ProjectileBuilder offset(int offset) {
			this.offset = offset;
			return this;
		}

		/**
		 * Sets the source {@link Position} of the {@link Projectile}.
		 *
		 * @param source The source {@link Projectile}. Must not be {@code null}.
		 * @return This {@link ProjectileBuilder}, for chaining. Will never be {@code null}.
		 */
		public ProjectileBuilder source(Position source) {
			this.source = source;
			return this;
		}

		/**
		 * Sets the starting height of the {@link Projectile}, relative to the height of the origin tile (so a value
		 * of 0 fires the {@link Projectile} from the exact height of the origin tile). Note that this is the height
		 * in 'world units', <strong>not</strong> the height as described in {@link Position}s.
		 *
		 * @param height The start height of the {@link Projectile}. Must be {@code 0 <= height <= 255}.
		 * @return This {@link ProjectileBuilder}, for chaining. Will never be {@code null}.
		 */
		public ProjectileBuilder startHeight(int height) {
			this.startHeight = height;
			return this;
		}

		/**
		 * Sets the target of the {@link Projectile}.
		 *
		 * @param mob The {@link Mob} the {@link Projectile} is targeting. Must not be {@code null}.
		 * @return This {@link ProjectileBuilder}, for chaining. Will never be {@code null}.
		 */
		public ProjectileBuilder target(Mob mob) {
			int mobIndex = mob.getIndex() + 1;

			destination = mob.getPosition();
			target = mob.getEntityType() == EntityType.NPC ? mobIndex : -mobIndex;
			offset = mob.size() * 64;
			return this;
		}

	}

	/**
	 * Creates a new {@link ProjectileBuilder}.
	 *
	 * @param world The {@link World} the Projectile will be created in. Must not be {@code null}.
	 * @return The {@link ProjectileBuilder}. Will never be {@code null}.
	 */
	public static ProjectileBuilder builder(World world) {
		return new ProjectileBuilder(world);
	}

	/**
	 * The time, in ticks, before the projectile is fired.
	 */
	private final int delay;

	/**
	 * The ending {@link Position} of the Projectile
	 */
	private final Position destination;

	/**
	 * The ending height of the projectile.
	 */
	private final int endHeight;

	/**
	 * The id of the graphic played as the Projectile proceeds.
	 */
	private final int graphic;

	/**
	 * The time, in ticks, that the Projectile is present.
	 */
	private final int lifetime;

	/**
	 * The pitch of the Projectile as it climbs.
	 */
	private final int pitch;

	/**
	 * The offset of the projectile from the origin in world units.
	 */
	private final int offset;

	/**
	 * The starting height of the projectile, relative to the height of the tile the Projectile starts on.
	 */
	private final int startHeight;

	/**
	 * The index of the target Mob (which must be 0 if the Projectile does not target a Mob).
	 */
	private final int target;

	/**
	 * Creates the Projectile.
	 *
	 * @param delay The time, in ticks, before the projectile is fired. Must not be negative.
	 * @param destination The ending {@link Position} of the Projectile. Must not be {@code null}.
	 * @param endHeight The ending height of the projectile. Must be [0, 255].
	 * @param graphic The id of the graphic played as the Projectile proceeds. Must not be negative.
	 * @param lifetime The time, in ticks, that the Projectile is present. Must be positive.
	 * @param pitch The pitch of the Projectile as it climbs.
	 * @param source The starting {@link Position} of the Projectile. Must not be {@code null}.
	 * @param startHeight The starting height of the projectile, relative to the height of the {@code source} tile.
	 * @param target The index of the target Mob (or 0 if the Projectile is not targeting a Mob).
	 */
	public Projectile(World world, Position source, int delay, Position destination, int endHeight, int graphic, int lifetime,
					  int pitch, int startHeight, int target, int offset) {
		super(world, source);
		this.delay = delay;
		this.destination = destination;
		this.endHeight = endHeight;
		this.graphic = graphic;
		this.lifetime = lifetime;
		this.pitch = pitch;
		this.startHeight = startHeight;
		this.target = target;
		this.offset = offset;
	}

	/**
	 * Gets the delay before this Projectile is fired, in ticks.
	 *
	 * @return The delay.
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Gets the destination {@link Position} of this Projectile.
	 *
	 * @return The destination {@link Position}. Will never be {@code null}.
	 */
	public Position getDestination() {
		return destination;
	}

	/**
	 * Gets the ending height of this Projectile.
	 *
	 * @return The ending height.
	 */
	public int getEndHeight() {
		return endHeight;
	}

	/**
	 * Gets the id of the graphic played by this Projectile.
	 *
	 * @return The graphic id.
	 */
	public int getGraphic() {
		return graphic;
	}

	/**
	 * Gets the time, in ticks, that this Projectile lasts in the game world.
	 *
	 * @return The lifetime of this Projectile.
	 */
	public int getLifetime() {
		return lifetime;
	}

	/**
	 * Gets the offset of this Projectile.
	 *
	 * @return The offset of this Projectile.
     */
	public int getOffset() {
		return offset;
	}

	/**
	 * Gets the pitch of this Projectile.
	 *
	 * @return The pitch.
	 */
	public int getPitch() {
		return pitch;
	}

	/**
	 * Gets the starting height of this Projectile, relative to the origin tile.
	 *
	 * @return The starting height.
	 */
	public int getStartHeight() {
		return startHeight;
	}

	/**
	 * Gets the index of the {@link Mob} this Projectile is targeting, or 0 if this Projectile is not targeting a
	 * {@link Mob}.
	 *
	 * @return The index.
	 */
	public int getTarget() {
		return target;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Projectile) {
			Projectile other = (Projectile) obj;

			return position.equals(other.position)
				&& destination.equals(other.destination)
				&& delay == other.delay
				&& lifetime == other.lifetime
				&& target == other.target
				&& startHeight == other.startHeight
				&& endHeight == other.endHeight
				&& pitch == other.pitch
				&& offset == other.offset
				&& graphic == other.graphic;
		}

		return false;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.PROJECTILE;
	}

	@Override
	public int getLength() {
		return 1;
	}

	@Override
	public int getWidth() {
		return 1;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(position, destination, delay, lifetime, target, startHeight,
			endHeight, pitch, offset, graphic);
	}

	@Override
	public ProjectileUpdateOperation toUpdateOperation(Region region, EntityUpdateType type) {
		Preconditions.checkArgument(type == EntityUpdateType.ADD, "Projectiles cannot be removed from the client");

		return new ProjectileUpdateOperation(region, type, this);
	}

}

