package org.apollo.game.model.entity;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;

import org.apollo.cache.def.NpcDefinition;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(World.class)
public class NpcTest {

	Npc npc1;
	Npc npc2;
	NpcDefinition npcDefs;
	Position boundaries[];
	@Before
	public void setup(){
		World world= mock(World.class);
		NpcDefinition npcDef1=new NpcDefinition(0);
		NpcDefinition npcDef2=new NpcDefinition(1);
		npcDefs.init(new NpcDefinition[]{npcDef1, npcDef2});
		npc1=new Npc(world,0,new Position(0,0));
		boundaries = new Position[]{new Position(0, 0), new Position(0, 1)};
		npc2=new Npc(world,new Position(0,0),npcDef2,boundaries);
	}

	@Test
	public void equalsTest(){
		assertFalse(npc1.equals(npc2));
		assertTrue(npc1.equals(npc1));
	}


	@Test
	public void getEntityTypeTest(){
		assertEquals(npc1.getEntityType(),EntityType.NPC);
	}

	@Test
	public void getIdTest(){
		assertEquals(npc1.getId(),0);
		assertEquals(npc2.getId(),1);
	}

	@Test
	public void transformTest(){
		assertEquals(npc1.getId(),0);
		npc1.transform(1);
		assertEquals(npc1.getId(),1);
	}

}