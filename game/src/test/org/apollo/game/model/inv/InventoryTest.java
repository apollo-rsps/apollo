package org.apollo.game.model.inv;

import org.apollo.cache.def.ItemDefinition;
import org.apollo.cache.def.ObjectDefinition;
import org.apollo.game.model.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ObjectDefinition.class,ItemDefinition.class})

public class InventoryTest {

	Inventory inventory;
	Inventory stackNeverInventory;
	Inventory stackableItemInventory;

	@Before
	public void setup() {
		mockStatic(ItemDefinition.class);
		inventory = new Inventory(28, Inventory.StackMode.STACK_ALWAYS);
		stackNeverInventory = new Inventory(13, Inventory.StackMode.STACK_NEVER);
		stackableItemInventory = new Inventory(15, Inventory.StackMode.STACK_STACKABLE_ITEMS);
	}

	@Test
	public void addTest(){
		inventory.set(1, new Item(4151, 1));
		assertTrue(inventory.contains(4151));
	}

	@Test
	public void clearTest(){
		inventory.set(1, new Item(4151, 1));
		assertTrue(inventory.contains(4151));
		inventory.clear();
		assertTrue(!(inventory.contains(4551)));
	}

	@Test
	public void getAmountTest(){
		assertEquals(inventory.getAmount(5000),0);
		inventory.set(1, new Item(5000,3));
		assertEquals(inventory.getAmount(5000),3);
		inventory.add(new Item(5000,3));
		assertEquals(inventory.getAmount(5000),6);
	}

	@Test
	public void removeTest(){
		ItemDefinition definition;

		inventory.set(1, new Item(5000,3));
		inventory.remove(5000,2);
		assertEquals(inventory.getAmount(5000),1);

		stackNeverInventory.set(1, new Item(1234));
		stackNeverInventory.remove(1234);
		assertEquals(stackNeverInventory.getAmount(1234),0);

		/*TODO test when the inventory is an STACK_STACKABLE_ITEMS inventory */
	}

}