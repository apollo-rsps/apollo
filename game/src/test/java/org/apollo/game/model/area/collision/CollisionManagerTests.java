package org.apollo.game.model.area.collision;

import org.apollo.cache.def.ObjectDefinition;
import org.apollo.cache.map.MapObject;
import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.obj.ObjectType;
import org.apollo.game.model.entity.obj.StaticGameObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Tests for the {@link CollisionManager}.
 */
public final class CollisionManagerTests {

	/**
	 * The id of a simple wall object.
	 */
	private static final int WALL = 0;

	/**
	 * The id of a square 2x2 solid object.
	 */
	private static final int SQUARE_OBJECT = 1;

	/**
	 * Setup some simple object definitions to use in collision tests.
	 */
	@BeforeClass
	public static void setupObjectDefinitions() {
		ObjectDefinition wall = new ObjectDefinition(WALL);

		ObjectDefinition squareObject = new ObjectDefinition(SQUARE_OBJECT);
		squareObject.setLength(2);
		squareObject.setWidth(2);

		ObjectDefinition.init(new ObjectDefinition[]{
			wall, squareObject
		});
	}

	/**
	 * Tests that a wall is untraversable from the front and back, for the facing direction and opposite facing direction
	 * respectively. For a simple grid, with a wall on 0,1 we end up with a collision matrix looking like:
	 * <pre>
	 * (0,2) |---------|
	 *       |         |
	 *       |         |
	 *       |   xxx   |
	 * (0,1) |---------|
	 *       |   xxx   |
	 *       |         |
	 *       |         |
	 * (0,0) |---------|
	 *       |         |
	 *       |         |
	 *       |         |
	 *       |---------|
	 * </pre>
	 * <p>
	 * Where {@code xxx} denotes that you cannot walk into that tile from that direction (in this case you south on
	 * (0,2) and north on (0,1)). For the grid above you can't walk north into {@code (0, 2)} because {@code (0, 2)} is
	 * untraversable from the south, and as expectred you can't walk south into {@code (0,1)} because ({@code (0, 1)} is
	 * untraversable from the north.
	 */
	@Test
	public void wall() {
		Position front = new Position(0, 1, 0);
		Position back = front.step(1, Direction.NORTH);

		CollisionManager collisionManager = createCollisionManager(
			createMapObject(WALL, front, ObjectType.LENGTHWISE_WALL, Direction.NORTH)
		);

		assertUntraversable(collisionManager, front, Direction.NORTH);
		assertUntraversable(collisionManager, back, Direction.SOUTH);
		assertUntraversable(collisionManager, front, Direction.NORTH_EAST);
		assertUntraversable(collisionManager, back, Direction.SOUTH_EAST);
	}


	/**
	 * Tests that a corner triangular wall is untraversable from the sides that it blocks.  Corners are much like walls,
	 * with the only difference being their orientation.  Instead of {@link Direction#WNES}, they have diagonal directions.
	 * With a corner wall at (0, 1) facing to the north-east we end up with a grid looking like this:
	 * <p>
	 * <pre>
	 * (0,2) |---------|---------|
	 *       |         |         |
	 *       |         |         |
	 *       |         |xxx      |
	 * (0,1) |---------|---------|
	 *       |      xxx|         |
	 *       |         |         |
	 *       |         |         |
	 * (0,0) |---------|---------|
	 * 	   (1,0)     (2,0)     (3,0)
	 * </pre>
	 * <p>
	 * Where you can walk north and east through the tile the corner occupies, as well as south and west through the
	 * adjacent tile, but not north-east or south-west.
	 */
	@Test
	public void cornerWall() {
		Position front = new Position(0, 1, 0);
		Position back = front.step(1, Direction.NORTH_EAST);

		CollisionManager collisionManager = createCollisionManager(
			createMapObject(WALL, front, ObjectType.TRIANGULAR_CORNER, Direction.NORTH_EAST)
		);

		assertTraversable(collisionManager, front, Direction.NORTH, Direction.EAST);
		assertUntraversable(collisionManager, front, Direction.NORTH_EAST);
		assertTraversable(collisionManager, back, Direction.SOUTH, Direction.WEST);
		assertUntraversable(collisionManager, back, Direction.SOUTH_WEST);
	}

	/**
	 * Tests that a corner rectangle wall is untraversable from the sides that it blocks.  Corners are much like walls,
	 * with the only difference being their orientation.  Instead of {@link Direction#WNES}, they have diagonal directions.
	 * With a corner wall at (0, 1) facing to the north-east we end up with a grid looking like this:
	 * <p>
	 * <pre>
	 * (0,2) |---------|---------|
	 *       |         |         |
	 *       |         |         |
	 *       |         |xxx      |
	 * (0,1) |---------|---------|
	 *       |      xxx|         |
	 *       |         |         |
	 *       |         |         |
	 * (0,0) |---------|---------|
	 * 	   (1,0)     (2,0)     (3,0)
	 * </pre>
	 * <p>
	 * Where you can walk north and east through the tile the corner occupies, as well as south and west through the
	 * adjacent tile, but not north-east or south-west.
	 */
	@Test
	public void rectangleWall() {
		Position front = new Position(0, 1, 0);
		Position back = front.step(1, Direction.NORTH_EAST);

		CollisionManager collisionManager = createCollisionManager(
			createMapObject(WALL, front, ObjectType.RECTANGULAR_CORNER, Direction.NORTH_EAST)
		);

		assertTraversable(collisionManager, front, Direction.NORTH, Direction.EAST);
		assertUntraversable(collisionManager, front, Direction.NORTH_EAST);
		assertTraversable(collisionManager, back, Direction.SOUTH, Direction.WEST);
		assertUntraversable(collisionManager, back, Direction.SOUTH_WEST);
	}

	/**
	 * Tests that the tiles occupied by a 2x2 square object are not traversable.  When an interactable object is added
	 * to a collision update, all tiles spanning its with and length from its origin position will be marked as completely
	 * blocked off, which with a 2x2 object at (1,1) produces a grid like this:
	 * <pre>
	 * (0,3) |---------|---------|---------|---------|
	 *       |         |xxxxxxxxx|xxxxxxxxx|         |
	 *       |         |x       x|x       x|         |
	 *       |         |xxxxxxxxx|xxxxxxxxx|         |
	 * (0,2) |---------|---------|---------|---------|
	 *       |         |xxxxxxxxx|xxxxxxxxx|         |
	 *       |         |x       x|x       x|         |
	 *       |         |xxxxxxxxx|xxxxxxxxx|         |
	 * (0,1) |---------|---------|---------|---------|
	 *       |         |         |         |         |
	 *       |         |         |         |         |
	 *       |         |         |         |         |
	 * (0,0) |---------|---------|---------|---------|
	 * 	   (0,0)     (1,0)     (2,0)     (3,0)     (4,0)
	 * </pre>
	 * <p>
	 * Where every tile that is occupied by the object is untraversable in every direction.
	 */
	@Test
	public void object() {
		Position origin = new Position(1, 1, 0);

		CollisionManager collisionManager = createCollisionManager(
			createMapObject(SQUARE_OBJECT, origin, ObjectType.INTERACTABLE, Direction.NORTH)
		);

		Position west = origin.step(1, Direction.WEST);
		Position east = origin.step(2, Direction.EAST);
		Position northEast = origin.step(2, Direction.NORTH).step(1, Direction.EAST);
		Position southEast = origin.step(1, Direction.SOUTH_EAST);

		assertUntraversable(collisionManager, west, Direction.EAST);
		assertUntraversable(collisionManager, east, Direction.WEST);
		assertUntraversable(collisionManager, northEast, Direction.SOUTH_EAST, Direction.SOUTH_WEST);
		assertUntraversable(collisionManager, southEast, Direction.NORTH_EAST, Direction.NORTH_WEST);
	}

	/**
	 * Helper function for creating {@code org.apollo.cache} {@link MapObject}s using data structures from
	 * {@code org.apollo.game}.
	 *
	 * @param id The id of the object.  Must be one of the {@link ObjectDefinition}s defined above.
	 * @param position The position of the object.
	 * @param type The object type.
	 * @param direction The orientation of the object.
	 * @return A new {@link MapObject}.
	 */
	private static MapObject createMapObject(int id, Position position, ObjectType type, Direction direction) {
		return new MapObject(id, position.getX(), position.getY(), position.getHeight(), type.getValue(),
			direction.toOrientationInteger());
	}

	/**
	 * Sets up dependencies for and creates a stub {@link CollisionManager}, then builds the collision matrices
	 * using the {@code objects} given.
	 *
	 * @param objects The objects to build collision matrices from.
	 * @return A new {@link CollisionManager} with a valid {@link RegionRepository} and every {@link CollisionMatrix}
	 * built.
	 */
	private static CollisionManager createCollisionManager(MapObject... objects) {
		World world = new World();
		RegionRepository regions = world.getRegionRepository();
		CollisionManager collisionManager = world.getCollisionManager();

		for (MapObject object : objects) {
			// treat local coordinates as absolute for simplicity
			int x = object.getLocalX(), y = object.getLocalY();
			int height = object.getHeight();

			Position position = new Position(x, y, height);
			Region region = regions.fromPosition(position);
			region.addEntity(new StaticGameObject(world, object.getId(), position, object.getType(),
				object.getOrientation()), false);
		}

		collisionManager.build(false);
		return collisionManager;
	}

	/**
	 * Helper test assertion that a position is untraversable in a given direction.
	 *
	 * @param collisionManager The {@link CollisionManager} to check.
	 * @param position The {@link Position}.
	 * @param directions The {@link Direction}s to assert.
	 */
	private static void assertUntraversable(CollisionManager collisionManager, Position position, Direction... directions) {
		for (Direction direction : directions) {
			boolean traversable = collisionManager.traversable(position, EntityType.NPC, direction);
			String message = String.format("Can walk %s from tile at (%d,%d), should not be able to",
				direction.toString(), position.getX(), position.getY());

			Assert.assertFalse(message, traversable);
		}
	}

	/**
	 * Helper test assertion that a position is traversable in a given direction.
	 *
	 * @param collisionManager The {@link CollisionManager} to check.
	 * @param position The {@link Position}.
	 * @param directions The {@link Direction}s to assert.
	 */
	private static void assertTraversable(CollisionManager collisionManager, Position position, Direction... directions) {
		for (Direction direction : directions) {
			boolean traversable = collisionManager.traversable(position, EntityType.NPC, direction);
			String message = String.format("Cannot walk %s from tile at (%d,%d), should be able to",
				direction.toString(), position.getX(), position.getY());

			Assert.assertTrue(message, traversable);
		}
	}
}
