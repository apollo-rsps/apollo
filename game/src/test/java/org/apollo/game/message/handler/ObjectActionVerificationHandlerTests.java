package org.apollo.game.message.handler;

import org.apollo.cache.def.ItemDefinition;
import org.apollo.cache.def.ObjectDefinition;
import org.apollo.game.message.impl.ObjectActionMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.obj.StaticGameObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({World.class, Player.class, ObjectDefinition.class, RegionRepository.class, Region.class})
public final class ObjectActionVerificationHandlerTests {

	@Before
	public void setupTestObjectDefinitions() {
		mockStatic(ObjectDefinition.class);
		when(ObjectDefinition.count()).thenReturn(4152);
	}

	@Test
	public void terminateIfOutOfRange() throws Exception {
		Position playerPosition = new Position(3200, 3200);
		Position objectPosition = new Position(3200, 3216);

		World world = mock(World.class);
		Region region = mock(Region.class);
		RegionRepository regionRepository = mock(RegionRepository.class);
		Player player = mock(Player.class);

		Set<Entity> entitySet = new HashSet<>();
		entitySet.add(new StaticGameObject(world, 4151, objectPosition, 0, 0));

		when(world.getRegionRepository()).thenReturn(regionRepository);
		when(regionRepository.fromPosition(objectPosition)).thenReturn(region);
		when(player.getPosition()).thenReturn(playerPosition);
		when(region.getEntities(objectPosition, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT))
				.thenReturn(entitySet);

		ObjectActionMessage objectActionMessage = new ObjectActionMessage(1, 4151, objectPosition);
		ObjectActionVerificationHandler objectActionVerificationHandler = new ObjectActionVerificationHandler(world);

		objectActionVerificationHandler.handle(player, objectActionMessage);

		assertTrue("ObjectVerificationHandler: message not terminated when out of range!", objectActionMessage.terminated());
	}

	@Test
	public void terminateIfNoObject() throws Exception {
		Position playerPosition = new Position(3200, 3200);
		Position objectPosition = new Position(3200, 3201);

		World world = mock(World.class);
		Region region = mock(Region.class);
		RegionRepository regionRepository = mock(RegionRepository.class);
		Player player = mock(Player.class);

		Set<Entity> entitySet = new HashSet<>();

		when(world.getRegionRepository()).thenReturn(regionRepository);
		when(regionRepository.fromPosition(objectPosition)).thenReturn(region);
		when(player.getPosition()).thenReturn(playerPosition);
		when(region.getEntities(objectPosition, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT))
				.thenReturn(entitySet);

		ObjectActionMessage objectActionMessage = new ObjectActionMessage(1, 4151, objectPosition);
		ObjectActionVerificationHandler objectActionVerificationHandler = new ObjectActionVerificationHandler(world);

		objectActionVerificationHandler.handle(player, objectActionMessage);

		assertTrue("ObjectVerificationHandler: message not terminated when no object exists!", objectActionMessage.terminated());
	}
}