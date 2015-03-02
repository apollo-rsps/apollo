package org.apollo.game.model.area.collision;

import java.util.Arrays;

import org.apollo.game.model.Direction;
import org.apollo.game.model.entity.Entity.EntityType;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * A 2-dimensional adjacency matrix containing tile collision data.
 *
 * @author Major
 */
public final class CollisionMatrix {

	/**
	 * Indicates that all types of traversal are allowed.
	 */
	private static final byte ALL_ALLOWED = 0b0000_0000;

	/**
	 * Indicates that no types of traversal are allowed.
	 */
	private static final byte ALL_BLOCKED = (byte) 0b1111_1111;

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
	 * The collision matrix, as a {@code byte} array.
	 */
	private final byte[] matrix;

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
		matrix = new byte[width * length];
	}

	/**
	 * Returns whether or not <strong>all</strong> of the specified {@link CollisionFlag}s are set for the specified
	 * coordinate pair.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param flags The CollisionFlags.
	 * @return {@code true} if all of the CollisionFlags are set, otherwise {@code false}.
	 */
	public boolean all(int x, int y, CollisionFlag... flags) {
		return Arrays.stream(flags).allMatch(flag -> (get(x, y) & flag.asByte()) != 0);
	}

	/**
	 * Returns whether or not <strong>any</strong> of the specified {@link CollisionFlag}s are set for the specified
	 * coordinate pair.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param flags The CollisionFlags.
	 * @return {@code true} if any of the CollisionFlags are set, otherwise {@code false}.
	 */
	public boolean any(int x, int y, CollisionFlag... flags) {
		return Arrays.stream(flags).anyMatch(flag -> (get(x, y) & flag.asByte()) != 0);
	}

	/**
	 * Completely blocks the tile at the specified coordinate pair.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public void block(int x, int y) {
		set(x, y, ALL_BLOCKED);
	}

	/**
	 * Clears (i.e. sets to {@code false}) the value of the specified {@link CollisionFlag} for the specified coordinate
	 * pair.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param flag The CollisionFlag.
	 */
	public void clear(int x, int y, CollisionFlag flag) {
		set(x, y, (byte) ~flag.asByte());
	}

	/**
	 * Returns whether or not the specified {@link CollisionFlag} is set for the specified coordinate pair.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param flag The CollisionFlag.
	 * @return {@code true} if the CollisionFlag is set, {@code false} if not.
	 */
	public boolean flagged(int x, int y, CollisionFlag flag) {
		return (get(x, y) & flag.asByte()) != 0;
	}

	/**
	 * Gets the value of the specified tile.
	 * 
	 * @param x The x coordinate of the tile.
	 * @param y The y coordinate of the tile.
	 * @return The value.
	 */
	public int get(int x, int y) {
		return matrix[indexOf(x, y)] & 0xFF;
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
	 * Sets (i.e. sets to {@code true}) the value of the specified {@link CollisionFlag} for the specified coordinate
	 * pair.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param flag The CollisionFlag.
	 */
	public void set(int x, int y, CollisionFlag flag) {
		set(x, y, flag.asByte());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("width", width).add("length", length).add("matrix", Arrays.toString(matrix)).toString();
	}

	/**
	 * Returns whether or not an Entity of the specified {@link EntityType type} can traverse the tile at the specified
	 * coordinate pair.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param entity The {@link EntityType}.
	 * @param direction The {@link Direction} the Entity is approaching from.
	 * @return {@code true} if the tile at the specified coordinate pair is traversable, {@code false} if not.
	 */
	public boolean traversable(int x, int y, EntityType entity, Direction direction) {
		CollisionFlag[] flags = CollisionFlag.forType(entity);
		int north = 0, east = 1, south = 2, west = 3;

		switch (direction) {
			case NORTH_WEST:
				return any(x, y, flags[south], flags[east]);
			case NORTH:
				return flagged(x, y, flags[south]);
			case NORTH_EAST:
				return any(x, y, flags[south], flags[west]);
			case EAST:
				return flagged(x, y, flags[west]);
			case SOUTH_EAST:
				return any(x, y, flags[north], flags[west]);
			case SOUTH:
				return flagged(x, y, flags[north]);
			case SOUTH_WEST:
				return any(x, y, flags[north], flags[east]);
			case WEST:
				return flagged(x, y, flags[east]);
		}

		throw new IllegalArgumentException("Unrecognised direction " + direction + ".");
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
		int index = y * width + x;
		Preconditions.checkElementIndex(index, matrix.length, "Index out of bounds.");
		return index;
	}

	/**
	 * Sets the appropriate index for the specified coordinate pair to the specified value.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param value The value.
	 */
	private void set(int x, int y, byte value) {
		matrix[indexOf(x, y)] = value;
	}

}