package def;

import org.apollo.cache.def.EquipmentDefinition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.ws.RespectBinding;
import java.util.ArrayList;

/**
 * Test class for the EquipmentDefinition class
 *
 * @author KGALLET
 */
public class EquipmentDefinitionTest {

	protected EquipmentDefinition equipmentAttack;
	protected EquipmentDefinition equipmentDefence;
	protected EquipmentDefinition equipmentStrength;
	protected EquipmentDefinition[] definitionsArrayTest = new EquipmentDefinition[3];

	@Before
	public void createEquipment(){
		equipmentAttack = new EquipmentDefinition(0);
		equipmentDefence = new EquipmentDefinition(1);
		equipmentStrength = new EquipmentDefinition(2);

		definitionsArrayTest[0] = equipmentAttack;
		definitionsArrayTest[1] = equipmentDefence;
		definitionsArrayTest[2] = equipmentStrength;
	}

	@Test
	public void countAndInitTest(){
		this.createEquipment();
		Assert.assertEquals(0,equipmentAttack.count());
		equipmentAttack.getDefinitions().put(0,equipmentAttack);
		Assert.assertEquals(1,equipmentAttack.count());

		equipmentAttack.init(definitionsArrayTest);
		Assert.assertEquals(3,equipmentAttack.count());
	}

	@Test
	public void settersAndGettersTest(){
		equipmentAttack.setFlags(true,true,true,true);
		Assert.assertEquals(true,equipmentAttack.isFullBody());
		Assert.assertEquals(true,equipmentAttack.isFullHat());
		Assert.assertEquals(true,equipmentAttack.isFullMask());
		Assert.assertEquals(true,equipmentAttack.isTwoHanded());

		equipmentAttack.setLevels(2,2,2,2,2,2,2);
		Assert.assertEquals(2,equipmentAttack.getAttackLevel());
		Assert.assertEquals(2,equipmentAttack.getStrengthLevel());
		Assert.assertEquals(2,equipmentAttack.getDefenceLevel());
		Assert.assertEquals(2,equipmentAttack.getRangedLevel());
		Assert.assertEquals(2,equipmentAttack.getHitpointsLevel());
		Assert.assertEquals(2,equipmentAttack.getMagicLevel());
		Assert.assertEquals(2,equipmentAttack.getPrayerLevel());

		equipmentAttack.setSlot(5);
		Assert.assertEquals(5,equipmentAttack.getSlot());
	}


}
