package org.apollo.game.model.area.collision;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.apollo.game.model.Direction;
import org.apollo.game.model.entity.EntityType;

import java.util.Arrays;

/**
 * A 2-dimensional adjacency matrix containing tile collision data.
 *
 * @author Major
 */
public final class CollisionMatrix {

	/**
	 * Indicates that all types of traversal are allowed.
	 */
	private static final short ALL_ALLOWED = 0b00000000_00000000;

	/**
	 * Indicates that no types of traversal are allowed.
	 */
	private static final short ALL_BLOCKED = (short) 0b11111111_11111111;

	/**
	 * Indicates that projectiles may traverse this tile, but mobs may not.
	 */
	private static final short ALL_MOBS_BLOCKED = (short) 0b11111111_00000000;

	/**
	 * Creates an array of CollisionMatrix objects, all of the specified width and length.
	 *
	 * @param count The length of the array to create.
	 * @param width The width of each CollisionMatrix.
	 * @param length The length of each CollisionMatrix.
	 * @return The array of CollisionMatrix objects.
	 */
	public static CollisionMatrix[] createMatrices(int count, int width, int length) {
		CollisionMatrix[] matrices = new CollisionMatrix[count];
		Arrays.setAll(matrices, index -> new CollisionMatrix(width, length));
		return matrices;
	}

	/**
	 * The length of the matrix.
	 */
	private final int length;

	/**
	 * The collision matrix, as a {@code short} array.
	 */
	private final short[] matrix;

	/**
	 * The width of the matrix.
	 */
	private final int width;

	/**
	 * Creates the CollisionMatrix.
	 *
	 * @param width The width of the matrix.
	 * @param length The length of the matrix.
	 */
	public CollisionMatrix(int width, int length) {
		this.width = width;
		this.length = length;
		matrix = new short[width * length];
	}

	/**
	 * Returns whether or not <strong>all</strong> of the specified {@link CollisionFlag}s are set for the specified
	 * coordinate pair.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param flags The CollisionFlags.
	 * @return {@code true} iff all of the CollisionFlags are set.
	 */
	public boolean all(int x, int y, CollisionFlag... flags) {
		for (CollisionFlag flag : flags) {
			if (!flagged(x, y, flag)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns whether or not <strong>any</strong> of the specified {@link CollisionFlag}s are set for the specified
	 * coordinate pair.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param flags The CollisionFlags.
	 * @return {@code true} iff any of the CollisionFlags are set.
	 */
	public boolean any(int x, int y, CollisionFlag... flags) {
		for (CollisionFlag flag : flags) {
			if (flagged(x, y, flag)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Completely blocks the tile at the specified coordinate pair, while optionally allowing projectiles
	 * to pass through.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param impenetrable If projectiles should be permitted to traverse this tile.
	 */
	public void block(int x, int y, boolean impenetrable) {
		set(x, y, impenetrable ? ALL_BLOCKED : ALL_MOBS_BLOCKED);
	}

	/**
	 * Completely blocks the tile at the specified coordinate pair.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public void block(int x, int y) {
		block(x, y, true);
	}

	/**
	 * Clears (i.e. sets to {@code false}) the value of the specified {@link CollisionFlag} for the specified
	 * coordinate pair.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param flag The CollisionFlag.
	 */
	public void clear(int x, int y, CollisionFlag flag) {
		set(x, y, (short) (matrix[indexOf(x, y)] & ~flag.asShort()));
	}

	/**
	 * Adds an additional {@link CollisionFlag} for the specified coordinate pair.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param flag The CollisionFlag.
	 */
	public void flag(int x, int y, CollisionFlag flag) {
		matrix[indexOf(x, y)] |= flag.asShort();
	}

	/**
	 * Returns whether or not the specified {@link CollisionFlag} is set for the specified coordinate pair.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param flag The CollisionFlag.
	 * @return {@code true} iff the CollisionFlag is set.
	 */
	public boolean flagged(int x, int y, CollisionFlag flag) {
		return (get(x, y) & flag.asShort()) != 0;
	}

	/**
	 * Gets the value of the specified tile.
	 *
	 * @param x The x coordinate of the tile.
	 * @param y The y coordinate of the tile.
	 * @return The value.
	 */
	public int get(int x, int y) {
		return matrix[indexOf(x, y)] & 0xFFFF;
	}

	/**
	 * Resets the cell of the specified coordinate pair.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public void reset(int x, int y) {
		set(x, y, ALL_ALLOWED);
	}

	/**
	 * Resets all cells in this matrix.
	 */
	public void reset() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < width; y++) {
				reset(x, y);
			}
		}
	}

	/**
	 * Sets (i.e. sets to {@code true}) the value of the specified {@link CollisionFlag} for the specified coordinate
	 * pair.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param flag The CollisionFlag.
	 */
	public void set(int x, int y, CollisionFlag flag) {
		set(x, y, flag.asShort());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("width", width).add("length", length)
			.add("matrix", Arrays.toString(matrix)).toString();
	}

	/**
	 * Returns whether or not an Entity of the specified {@link EntityType type} cannot traverse the tile at the
	 * specified coordinate pair.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param entity The {@link EntityType}.
	 * @param direction The {@link Direction} the Entity is approaching from.
	 * @return {@code true} iff the tile at the specified coordinate pair is not traversable.
	 */
	public boolean untraversable(int x, int y, EntityType entity, Direction direction) {
		CollisionFlag[] flags = CollisionFlag.forType(entity);
		int northwest = 0, north = 1, northeast = 2, west = 3, east = 4, southwest = 5, south = 6, southeast = 7;

		switch (direction) {
			case NORTH_WEST:
				return flagged(x, y, flags[southeast]) || flagged(x, y, flags[south]) || flagged(x, y, flags[east]);
			case NORTH:
				return flagged(x, y, flags[south]);
			case NORTH_EAST:
				return flagged(x, y, flags[southwest]) || flagged(x, y, flags[south]) || flagged(x, y, flags[west]);
			case EAST:
				return flagged(x, y, flags[west]);
			case SOUTH_EAST:
				return flagged(x, y, flags[northwest]) || flagged(x, y, flags[north]) || flagged(x, y, flags[west]);
			case SOUTH:
				return flagged(x, y, flags[north]);
			case SOUTH_WEST:
				return flagged(x, y, flags[northeast]) || flagged(x, y, flags[north]) || flagged(x, y, flags[east]);
			case WEST:
				return flagged(x, y, flags[east]);
			default:
				throw new IllegalArgumentException("Unrecognised direction " + direction + ".");
		}
	}

	/**
	 * Gets the index in the matrix for the specified coordinate pair.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The index.
	 * @throws ArrayIndexOutOfBoundsException If the specified coordinate pair does not fit in this matrix.
	 */
	private int indexOf(int x, int y) {
		Preconditions.checkElementIndex(x, width, "X coordinate must be [0, " + width + "), received " + x + ".");
		Preconditions.checkElementIndex(y, length, "Y coordinate must be [0, " + length + "), received " + y + ".");
		return y * width + x;
	}

	/**
	 * Sets the appropriate index for the specified coordinate pair to the specified value.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param value The value.
	 */
	private void set(int x, int y, short value) {
		matrix[indexOf(x, y)] = value;
	}

}