package org.apollo.game.message.handler;

import org.apollo.cache.def.ItemDefinition;
import org.apollo.cache.def.ObjectDefinition;
import org.apollo.game.message.impl.ItemOnObjectMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.obj.StaticGameObject;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SynchronizationInventoryListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Player.class, World.class, Region.class, RegionRepository.class, ObjectDefinition.class,
		ItemDefinition.class})
public final class ItemOnObjectVerificationHandlerTests {

	@Before
	public void setupTestItemDefinitions() {
		mockStatic(ItemDefinition.class);
		when(ItemDefinition.lookup(4151)).thenReturn(new ItemDefinition(4151));

		mockStatic(ObjectDefinition.class);
		when(ObjectDefinition.count()).thenReturn(2);
	}


	@Test
	public void terminateIfInvalidItem() throws Exception {
		Position playerPosition = new Position(3200, 3200);
		Position objectPosition = new Position(3200, 3216);

		World world = mock(World.class);
		Region region = mock(Region.class);
		RegionRepository regionRepository = mock(RegionRepository.class);
		Player player = mock(Player.class);

		Set<Entity> entitySet = new HashSet<>();
		entitySet.add(new StaticGameObject(world, 4151, objectPosition, 0, 0));
		Inventory inventory = new Inventory(28);

		when(player.getInventory()).thenReturn(inventory);
		when(world.getRegionRepository()).thenReturn(regionRepository);
		when(regionRepository.fromPosition(objectPosition)).thenReturn(region);
		when(player.getPosition()).thenReturn(playerPosition);
		when(region.getEntities(objectPosition, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT))
				.thenReturn(entitySet);

		ItemOnObjectMessage itemOnObjectMessage = new ItemOnObjectMessage(SynchronizationInventoryListener.INVENTORY_ID, 4151, 1,
				1, objectPosition.getX(), objectPosition.getY());
		ItemOnObjectVerificationHandler itemOnObjectVerificationHandler = new ItemOnObjectVerificationHandler(world);

		itemOnObjectVerificationHandler.handle(player, itemOnObjectMessage);

		assertTrue("ObjectVerificationHandler: message not terminated valid item given!", itemOnObjectMessage.terminated());
	}

	@Test
	public void terminateIfInvalidSlot() throws Exception {
		Position playerPosition = new Position(3200, 3200);
		Position objectPosition = new Position(3200, 3200);

		World world = mock(World.class);
		Region region = mock(Region.class);
		RegionRepository regionRepository = mock(RegionRepository.class);
		Player player = mock(Player.class);

		Set<Entity> entitySet = new HashSet<>();
		entitySet.add(new StaticGameObject(world, 4151, objectPosition, 0, 0));
		Inventory inventory = new Inventory(28);

		when(player.getInventory()).thenReturn(inventory);
		when(world.getRegionRepository()).thenReturn(regionRepository);
		when(regionRepository.fromPosition(objectPosition)).thenReturn(region);
		when(player.getPosition()).thenReturn(playerPosition);
		when(region.getEntities(objectPosition, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT))
				.thenReturn(entitySet);

		ItemOnObjectMessage itemOnObjectMessage = new ItemOnObjectMessage(SynchronizationInventoryListener.INVENTORY_ID, 4151, 30,
				1, objectPosition.getX(), objectPosition.getY());
		ItemOnObjectVerificationHandler itemOnObjectVerificationHandler = new ItemOnObjectVerificationHandler(world);

		itemOnObjectVerificationHandler.handle(player, itemOnObjectMessage);

		assertTrue("ObjectVerificationHandler: message not terminated when no valid slot given!", itemOnObjectMessage.terminated());
	}

	@Test
	public void terminateIfObjectOutOfRange() throws Exception {
		Position playerPosition = new Position(3200, 3200);
		Position objectPosition = new Position(3200, 3200);

		World world = mock(World.class);
		Region region = mock(Region.class);
		RegionRepository regionRepository = mock(RegionRepository.class);
		Player player = mock(Player.class);

		Set<Entity> entitySet = new HashSet<>();
		entitySet.add(new StaticGameObject(world, 4151, objectPosition, 0, 0));
		Inventory inventory = new Inventory(28);
		inventory.set(1, new Item(4151, 1));

		when(player.getInventory()).thenReturn(inventory);
		when(world.getRegionRepository()).thenReturn(regionRepository);
		when(regionRepository.fromPosition(objectPosition)).thenReturn(region);
		when(player.getPosition()).thenReturn(playerPosition);
		when(region.getEntities(objectPosition, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT))
				.thenReturn(entitySet);

		ItemOnObjectMessage itemOnObjectMessage = new ItemOnObjectMessage(SynchronizationInventoryListener.INVENTORY_ID, 4151, 1,
				1, objectPosition.getX(), objectPosition.getY());
		ItemOnObjectVerificationHandler itemOnObjectVerificationHandler = new ItemOnObjectVerificationHandler(world);

		itemOnObjectVerificationHandler.handle(player, itemOnObjectMessage);

		assertTrue("ObjectVerificationHandler: message not terminated when object out of range!", itemOnObjectMessage.terminated());
	}
}