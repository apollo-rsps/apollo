package org.apollo.game.model.entity;

import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.area.collision.CollisionManager;
import org.apollo.util.security.PlayerCredentials;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({World.class, CollisionManager.class})
public class WalkingQueueTests {
	private static final Position START_POSITION = new Position(1, 1);
	private static final Position END_WALK_POSITION = new Position(2, 1);
	private static final Position END_RUN_POSITION = new Position(3, 1);

	private World createFakeWorld(boolean traversable) {
		World world = spy(World.class);
		CollisionManager collisionManager = mock(CollisionManager.class);
		when(collisionManager.traversable(any(), any(), any())).thenReturn(traversable);
		when(world.getCollisionManager()).thenReturn(collisionManager);

		return world;
	}

	private Player createFakePlayer(World world) {
		PlayerCredentials credentials = new PlayerCredentials("test", "test", -1, -1, "0.0.0.0");
		Player player = new Player(world, credentials, START_POSITION);

		Region region = world.getRegionRepository().fromPosition(START_POSITION);
		region.addEntity(player);

		return player;
	}

	@Test
	public void doesntMoveWhenTileIsBlocked()  {
		World world = createFakeWorld(false);
		Player player = createFakePlayer(world);

		WalkingQueue walkingQueue = new WalkingQueue(player);
		walkingQueue.addStep(END_WALK_POSITION);
		walkingQueue.pulse();

		assertEquals(START_POSITION, player.getPosition());
	}

	@Test
	public void movesOneTileWhenWalking() throws Exception {
		World world = createFakeWorld(true);
		Player player = createFakePlayer(world);

		WalkingQueue walkingQueue = new WalkingQueue(player);
		walkingQueue.addStep(END_WALK_POSITION);
		walkingQueue.pulse();

		assertEquals(END_WALK_POSITION, player.getPosition());
	}

	@Test
	public void movesTwoTilesWhenRunning() throws Exception {
		World world = createFakeWorld(true);
		Player player = createFakePlayer(world);

		WalkingQueue walkingQueue = new WalkingQueue(player);
		walkingQueue.setRunning(true);
		walkingQueue.addStep(END_RUN_POSITION);
		walkingQueue.pulse();

		assertEquals(END_RUN_POSITION, player.getPosition());
	}
}