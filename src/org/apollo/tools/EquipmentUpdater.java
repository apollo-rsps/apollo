package org.apollo.tools;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.parser.ItemDefinitionParser;
import org.apollo.game.model.def.ItemDefinition;

/**
 * A tool for updating the equipment data.
 * @author Graham
 * @author Palidino76
 */
public final class EquipmentUpdater {

	/**
	 * The entry point of the application.
	 * @param args The command line arguments.
	 * @throws Exception if an error occurs.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage:");
			System.err.println("  java -cp ... org.apollo.tools.EquipmentUpdater [release]");
			return;
		}
		String release = args[0];

		DataOutputStream os = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("data/equipment-" + release + ".dat")));
		try {
			IndexedFileSystem fs = new IndexedFileSystem(new File("data/fs/" + release), true);
			try {
				ItemDefinitionParser parser = new ItemDefinitionParser(fs);
				ItemDefinition[] defs = parser.parse();
				ItemDefinition.init(defs);

				os.writeShort(defs.length);
				for (int id = 0; id < defs.length; id++) {
					ItemDefinition def = ItemDefinition.forId(id);
					int type = getWeaponType(def);
					os.writeByte(type);
					if (type != -1) {
						os.writeBoolean(isTwoHanded(def));
						os.writeBoolean(isFullBody(def));
						os.writeBoolean(isFullHat(def));
						os.writeBoolean(isFullMask(def));
						os.writeByte(getAttackRequirement(def));
						os.writeByte(getStrengthRequirement(def));
						os.writeByte(getDefenceRequirement(def));
						os.writeByte(getRangedRequirement(def));
						os.writeByte(getMagicRequirement(def));
					}
				}
			} finally {
				fs.close();
			}
		} finally {
			os.close();
		}
	}

	/**
	 * Checks if the item is two handed.
	 * @param def The item.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	private static boolean isTwoHanded(ItemDefinition def) {
		int id = def.getId();
		String name = def.getName();
		if (name == null)
			name = "null";
		if (id == 4212)
			return true;
		else if (id == 4214)
			return true;
		else if (name.endsWith("2h sword"))
			return true;
		else if (name.endsWith("longbow"))
			return true;
		else if (name.equals("Seercull"))
			return true;
		else if (name.endsWith("shortbow"))
			return true;
		else if (name.endsWith("Longbow"))
			return true;
		else if (name.endsWith("Shortbow"))
			return true;
		else if (name.endsWith("bow full"))
			return true;
		else if (name.endsWith("halberd"))
			return true;
		else if (name.equals("Granite maul"))
			return true;
		else if (name.equals("Karils crossbow"))
			return true;
		else if (name.equals("Torags hammers"))
			return true;
		else if (name.equals("Veracs flail"))
			return true;
		else if (name.equals("Dharoks greataxe"))
			return true;
		else if (name.equals("Guthans warspear"))
			return true;
		else if (name.equals("Tzhaar-ket-om"))
			return true;
		else if (name.endsWith("godsword"))
			return true;
		else if (name.equals("Saradomin sword"))
			return true;
		else
			return false;
	}

	/**
	 * Gets the ranged requirement.
	 * @param def The item.
	 * @return The required level.
	 */
	private static int getRangedRequirement(ItemDefinition def) {
		int id = def.getId();
		String name = def.getName();
		if (name == null)
			name = "null";
		if (id == 2499)
			return 50;
		if (id == 1135)
			return 40;
		if (id == 1099)
			return 40;
		if (id == 1065)
			return 40;
		if (id == 2501)
			return 60;
		if (id == 2503)
			return 70;
		if (id == 2487)
			return 50;
		if (id == 2489)
			return 60;
		if (id == 2495)
			return 60;
		if (id == 2491)
			return 70;
		if (id == 2493)
			return 50;
		if (id == 2505)
			return 60;
		if (id == 2507)
			return 70;
		if (id == 859)
			return 40;
		if (id == 861)
			return 40;
		if (id == 7370)
			return 40;
		if (id == 7372)
			return 40;
		if (id == 7378)
			return 40;
		if (id == 7380)
			return 40;
		if (id == 7374)
			return 50;
		if (id == 7376)
			return 50;
		if (id == 7382)
			return 50;
		if (id == 7384)
			return 50;
		if (name.equals("Coif"))
			return 20;
		if (name.startsWith("Studded chaps"))
			return 20;
		if (name.startsWith("Studded"))
			return 20;
		if (name.equals("Karils coif"))
			return 70;
		if (name.equals("Karils leathertop"))
			return 70;
		if (name.equals("Karils leatherskirt"))
			return 70;
		if (name.equals("Robin hood hat"))
			return 40;
		if (name.equals("Ranger boots"))
			return 40;
		if (name.equals("Crystal bow full"))
			return 70;
		if (name.equals("New crystal bow"))
			return 70;
		if (name.equals("Karils crossbow"))
			return 70;
		if (id == 2497)
			return 70;
		if (name.equals("Rune thrownaxe"))
			return 40;
		if (name.equals("Rune dart"))
			return 40;
		if (name.equals("Rune javelin"))
			return 40;
		if (name.equals("Rune knife"))
			return 40;
		if (name.equals("Adamant thrownaxe"))
			return 30;
		if (name.equals("Adamant dart"))
			return 30;
		if (name.equals("Adamant javelin"))
			return 30;
		if (name.equals("Adamant knife"))
			return 30;
		if (name.equals("Toktz-xil-ul"))
			return 60;
		if (name.equals("Seercull"))
			return 50;
		if (name.equals("Bolt rack"))
			return 70;
		if (name.equals("Rune arrow"))
			return 40;
		if (name.equals("Adamant arrow"))
			return 30;
		if (name.equals("Mithril arrow"))
			return 1;
		else
			return 1;
	}

	/**
	 * Gets the magic requirement.
	 * @param def The item.
	 * @return The required level.
	 */
	private static int getMagicRequirement(ItemDefinition def) {
		String name = def.getName();
		if (name == null)
			name = "null";
		if (name.equals("Mystic hat"))
			return 40;
		if (name.equals("Mystic robe top"))
			return 40;
		if (name.equals("Mystic robe bottom"))
			return 40;
		if (name.equals("Mystic gloves"))
			return 40;
		if (name.equals("Mystic boots"))
			return 40;
		if (name.equals("Slayer's staff"))
			return 50;
		if (name.equals("Enchanted hat"))
			return 40;
		if (name.equals("Enchanted top"))
			return 40;
		if (name.equals("Enchanted robe"))
			return 40;
		if (name.equals("Splitbark helm"))
			return 40;
		if (name.equals("Splitbark body"))
			return 40;
		if (name.equals("Splitbark gauntlets"))
			return 40;
		if (name.equals("Splitbark legs"))
			return 40;
		if (name.equals("Splitbark greaves"))
			return 40;
		if (name.equals("Infinity gloves"))
			return 50;
		if (name.equals("Infinity hat"))
			return 50;
		if (name.equals("Infinity top"))
			return 50;
		if (name.equals("Infinity bottoms"))
			return 50;
		if (name.equals("Infinity boots"))
			return 50;
		if (name.equals("Ahrims hood"))
			return 70;
		if (name.equals("Ahrims robetop"))
			return 70;
		if (name.equals("Ahrims robeskirt"))
			return 70;
		if (name.equals("Ahrims staff"))
			return 70;
		if (name.equals("Saradomin cape"))
			return 60;
		if (name.equals("Saradomin staff"))
			return 60;
		if (name.equals("Zamorak cape"))
			return 60;
		if (name.equals("Zamorak staff"))
			return 60;
		if (name.equals("Guthix cape"))
			return 60;
		if (name.equals("Guthix staff"))
			return 60;
		if (name.equals("mud staff"))
			return 30;
		if (name.equals("Fire battlestaff"))
			return 30;
		return 1;
	}

	/**
	 * Gets the strength requirement.
	 * @param def The item.
	 * @return The required level.
	 */
	private static int getStrengthRequirement(ItemDefinition def) {
		String name = def.getName();
		if (name == null)
			name = "null";
		if (name.equals("Torags hammers"))
			return 70;
		if (name.equals("Dharoks greataxe"))
		   return 70;
		if (name.equals("Granite maul"))
		   return 50;
		if (name.equals("Granite legs"))
		   return 99;
		if (name.equals("Tzhaar-ket-om"))
		   return 60;
		if (name.equals("Granite shield"))
		   return 50;
	   return 1;
	}

	/**
	 * Gets the attack requirement.
	 * @param def The item.
	 * @return The required level.
	 */
	private static int getAttackRequirement(ItemDefinition def) {
		String name = def.getName();
		if (name == null)
			name = "null";
		if (name.equals("Black sword"))
			return 10;
		if (name.equals("Black dagger"))
			return 10;
		if (name.equals("Black spear"))
			return 10;
		if (name.equals("Black longsword"))
			return 10;
		if (name.equals("Black scimitar"))
			return 10;
		if (name.equals("Black axe"))
			return 10;
		if (name.equals("Black battleaxe"))
			return 10;
		if (name.equals("Black mace"))
			return 10;
		if (name.equals("Black halberd"))
			return 10;
		if (name.equals("Mithril sword"))
			return 20;
		if (name.equals("Mithril dagger"))
			return 20;
		if (name.equals("Mithril spear"))
			return 20;
		if (name.equals("Mihril longsword"))
			return 20;
		if (name.equals("Mithril scimitar"))
			return 20;
		if (name.equals("Mithril axe"))
			return 20;
		if (name.equals("Mithril battleaxe"))
			return 20;
		if (name.equals("Mithril mace"))
			return 20;
		if (name.equals("Mithril halberd"))
			return 20;
		if (name.equals("Adamant sword"))
			return 30;
		if (name.equals("Adamant dagger"))
			return 30;
		if (name.equals("Adamant spear"))
			return 30;
		if (name.equals("Adamant longsword"))
			return 30;
		if (name.equals("Adamant scimitar"))
			return 30;
		if (name.equals("Adamant axe"))
			return 30;
		if (name.equals("Adamant battleaxe"))
			return 30;
		if (name.equals("Adamant mace"))
			return 30;
		if (name.equals("Adamant halberd"))
			return 30;
		if (name.equals("Rune sword"))
			return 40;
		if (name.equals("Rune dagger"))
			return 40;
		if (name.equals("Rune spear"))
			return 40;
		if (name.equals("Rune longsword"))
			return 40;
		if (name.equals("Rune scimitar"))
			return 40;
		if (name.equals("Rune axe"))
			return 40;
		if (name.equals("Rune battleaxe"))
			return 40;
		if (name.equals("Rune mace"))
			return 40;
		if (name.equals("Rune halberd"))
			return 40;
		if (name.equals("Dragon sword"))
			return 60;
		if (name.equals("Dragon dagger(s)"))
			return 60;
		if (name.equals("Dragon dagger"))
			return 60;
		if (name.startsWith("Dragon spear"))
			return 60;
		if (name.equals("Dragon longsword"))
			return 60;
		if (name.equals("Dragon scimitar"))
			return 60;
		if (name.equals("Dragon axe"))
			return 60;
		if (name.equals("Dragon battleaxe"))
			return 60;
		if (name.equals("Dragon mace"))
			return 60;
		if (name.equals("Dragon halberd"))
			return 60;
		if (name.equals("Abyssal whip"))
			return 70;
		if (name.equals("Veracs flail"))
			return 70;
		if (name.equals("Torags hammers"))
			return 70;
		if (name.equals("Dharoks greataxe"))
			return 70;
		if (name.equals("Guthans warspear"))
			return 70;
		if (name.equals("Ahrims staff"))
			return 70;
		if (name.equals("Granite maul"))
			return 50;
		if (name.equals("Toktz-xil-ak"))
			return 60;
		if (name.equals("Tzhaar-ket-em"))
			return 60;
		if (name.equals("Toktz-xil-ek"))
			return 60;
		if (name.equals("Granite legs"))
			return 99;
		if (name.equals("Mud staff"))
			return 30;
		if (name.equals("Armadyl godsword"))
			return 75;
		if (name.equals("Bandos godsword"))
			return 75;
		if (name.equals("Saradomin godsword"))
			return 75;
		if (name.equals("Zamorak godsword"))
			return 75;
		if (name.equals("Lava battlestaff"))
			return 30;
		if (name.equals("Toktz-mej-tal"))
			return 60;
		if (name.equals("Ancient staff"))
			return 50;
		return 1;
	}

	/**
	 * Gets the defence requirement.
	 * @param def The item.
	 * @return The required level.
	 */
	private static int getDefenceRequirement(ItemDefinition def) {
		int id = def.getId();
		String name = def.getName();
		if (name == null)
			name = "null";
		if (name.equals("Rune boots"))
			return 40;
		if (id == 2499)
			return 40;
		if (id == 4123)
			return 5;
		if (id == 4125)
			return 10;
		if (id == 4127)
			return 20;
		if (id == 4129)
			return 30;
		if (id == 7990)
			return 60;
		if (id == 2501)
			return 40;
		if (id == 1131)
			return 10;
		if (id == 2503)
			return 40;
		if (id == 1135)
			return 40;
		if (id == 7462)
			return 42;
		if (id == 7461)
			return 42;
		if (id == 7460)
			return 42;
		if (id == 7459)
			return 20;
		if (id == 7458)
			return 1;
		if (id == 7457)
			return 1;
		if (id == 7456)
			return 1;
		if (name.equals("White med helm"))
			return 10;
		if (name.equals("White chainbody"))
			return 10;
		if (name.startsWith("White full helm"))
			return 10;
		if (name.startsWith("White platebody"))
			return 10;
		if (name.startsWith("White plateskirt"))
			return 10;
		if (name.startsWith("White platelegs"))
			return 10;
		if (name.startsWith("White kiteshield"))
			return 10;
		if (name.startsWith("White sq shield"))
			return 10;
		if (name.startsWith("Studded chaps"))
			return 1;
		if (name.startsWith("Studded"))
			return 20;
		if (name.startsWith("Black kiteshield(h)"))
			return 10;
		if (name.startsWith("Rune kiteshield(h)"))
			return 40;
		if (name.equals("Black med helm"))
			return 10;
		if (name.equals("Black chainbody"))
			return 10;
		if (name.startsWith("Black full helm"))
			return 10;
		if (name.startsWith("Black platebody"))
			return 10;
		if (name.startsWith("Black plateskirt"))
			return 10;
		if (name.startsWith("Black platelegs"))
			return 10;
		if (name.startsWith("Black kiteshield"))
			return 10;
		if (name.startsWith("Black sq shield"))
			return 10;
		if (name.equals("Mithril med helm"))
			return 20;
		if (name.equals("Mithril chainbody"))
			return 20;
		if (name.startsWith("Mithril full helm"))
			return 20;
		if (name.startsWith("Mithril platebody"))
			return 20;
		if (name.startsWith("Mithril plateskirt"))
			return 20;
		if (name.startsWith("Mithril platelegs"))
			return 20;
		if (name.startsWith("Mithril kiteshield"))
			return 20;
		if (name.startsWith("Mithril sq shield"))
			return 20;
		if (name.equals("Adamant med helm"))
			return 30;
		if (name.equals("Adamant chainbody"))
			return 30;
		if (name.startsWith("Adamant full helm"))
			return 30;
		if (name.startsWith("Adamant platebody"))
			return 30;
		if (name.startsWith("Adamant plateskirt"))
			return 30;
		if (name.startsWith("Adamant platelegs"))
			return 30;
		if (name.startsWith("Adamant kiteshield"))
			return 30;
		if (name.startsWith("Adamant sq shield"))
			return 30;
		if (name.startsWith("Adam full helm"))
			return 30;
		if (name.startsWith("Adam platebody"))
			return 30;
		if (name.startsWith("Adam plateskirt"))
			return 30;
		if (name.startsWith("Adam platelegs"))
			return 30;
		if (name.startsWith("Adam kiteshield"))
			return 30;
		if (name.startsWith("Adam kiteshield(h)"))
			return 30;
		if (name.startsWith("D-hide body(g)"))
			return 40;
		if (name.startsWith("D-hide body(t)"))
			return 40;
		if (name.equals("Dragon sq shield"))
			return 60;
		if (name.equals("Dragon med helm"))
			return 60;
		if (name.equals("Dragon chainbody"))
			return 60;
		if (name.equals("Dragon plateskirt"))
			return 60;
		if (name.equals("Dragon platelegs"))
			return 60;
		if (name.equals("Dragon sq shield"))
			return 60;
		if (name.equals("Rune med helm"))
			return 40;
		if (name.equals("Rune chainbody"))
			return 40;
		if (name.startsWith("Rune full helm"))
			return 40;
		if (name.startsWith("Rune platebody"))
			return 40;
		if (name.startsWith("Rune plateskirt"))
			return 40;
		if (name.startsWith("Rune platelegs"))
			return 40;
		if (name.startsWith("Rune kiteshield"))
			return 40;
		if (name.startsWith("Zamorak full helm"))
			return 40;
		if (name.startsWith("Zamorak platebody"))
			return 40;
		if (name.startsWith("Zamorak plateskirt"))
			return 40;
		if (name.startsWith("Zamorak platelegs"))
			return 40;
		if (name.startsWith("Zamorak kiteshield"))
			return 40;
		if (name.startsWith("Guthix full helm"))
			return 40;
		if (name.startsWith("Guthix platebody"))
			return 40;
		if (name.startsWith("Guthix plateskirt"))
			return 40;
		if (name.startsWith("Guthix platelegs"))
			return 40;
		if (name.startsWith("Guthix kiteshield"))
			return 40;
		if (name.startsWith("Saradomin full"))
			return 40;
		if (name.startsWith("Saradomrangedin plate"))
			return 40;
		if (name.startsWith("Saradomin plateskirt"))
			return 40;
		if (name.startsWith("Saradomin legs"))
			return 40;
		if (name.startsWith("Zamorak kiteshield"))
			return 40;
		if (name.startsWith("Rune sq shield"))
			return 40;
		if (name.equals("Gilded full helm"))
			return 40;
		if (name.equals("Gilded platebody"))
			return 40;
		if (name.equals("Gilded plateskirt"))
			return 40;
		if (name.equals("Gilded platelegs"))
			return 40;
		if (name.equals("Gilded kiteshield"))
			return 40;
		if (name.equals("Fighter torso"))
			return 40;
		if (name.equals("Granite legs"))
			return 99;
		if (name.equals("Toktz-ket-xil"))
			return 60;
		if (name.equals("Dharoks helm"))
			return 70;
		if (name.equals("Dharoks platebody"))
			return 70;
		if (name.equals("Dharoks platelegs"))
			return 70;
		if (name.equals("Guthans helm"))
			return 70;
		if (name.equals("Guthans platebody"))
			return 70;
		if (name.equals("Guthans chainskirt"))
			return 70;
		if (name.equals("Torags helm"))
			return 70;
		if (name.equals("Torags platebody"))
			return 70;
		if (name.equals("Torags platelegs"))
			return 70;
		if (name.equals("Veracs helm"))
			return 70;
		if (name.equals("Veracs brassard"))
			return 70;
		if (name.equals("Veracs plateskirt"))
			return 70;
		if (name.equals("Ahrims hood"))
			return 70;
		if (name.equals("Ahrims robetop"))
			return 70;
		if (name.equals("Ahrims robeskirt"))
			return 70;
		if (name.equals("Karils coif"))
			return 70;
		if (name.equals("Karils leathertop"))
			return 70;
		if (name.equals("Karils leatherskirt"))
			return 70;
		if (name.equals("Granite shield"))
			return 50;
		if (name.equals("New crystal shield"))
			return 70;
		if (name.equals("Archer helm"))
			return 45;
		if (name.equals("Berserker helm"))
			return 45;
		if (name.equals("Warrior helm"))
			return 45;
		if (name.equals("Farseer helm"))
			return 45;
		if (name.equals("Initiate helm"))
			return 20;
		if (name.equals("Initiate platemail"))
			return 20;
		if (name.equals("Initiate platelegs"))
			return 20;
		if (name.equals("Dragonhide body"))
			return 40;
		if (name.equals("Mystic hat"))
			return 20;
		if (name.equals("Mystic robe top"))
			return 20;
		if (name.equals("Mystic robe bottom"))
			return 20;
		if (name.equals("Mystic gloves"))
			return 20;
		if (name.equals("Mystic boots"))
			return 20;
		if (name.equals("Enchanted hat"))
			return 20;
		if (name.equals("Enchanted top"))
			return 20;
		if (name.equals("Enchanted robe"))
			return 20;
		if (name.equals("Splitbark helm"))
			return 40;
		if (name.equals("Splitbark body"))
			return 40;
		if (name.equals("Splitbark gauntlets"))
			return 40;
		if (name.equals("Splitbark legs"))
			return 40;
		if (name.equals("Splitbark greaves"))
			return 40;
		if (name.equals("Infinity gloves"))
			return 25;
		if (name.equals("Infinity hat"))
			return 25;
		if (name.equals("Infinity top"))
			return 25;
		if (name.equals("Infinity bottoms"))
			return 25;
		if (name.equals("Infinity boots"))
			return 25;
		return 1;
	}

	/**
	 * Gets the weapon type.
	 * @param def The item.
	 * @return The weapon type, or {@code -1} if it is not a weapon.
	 */
	private static int getWeaponType(ItemDefinition def) {
		String name = def.getName();
		if (name == null)
			name = "null";
		for (int i = 0; i < EquipmentConstants.CAPES.length; i++) {
			if (name.contains(EquipmentConstants.CAPES[i]))
				return 1;
		}
		for (int i = 0; i < EquipmentConstants.HATS.length; i++) {
			if (name.contains(EquipmentConstants.HATS[i]))
				return 0;
		}
		for (int i = 0; i < EquipmentConstants.BOOTS.length; i++) {
			if (name.endsWith(EquipmentConstants.BOOTS[i]) || name.startsWith(EquipmentConstants.BOOTS[i]))
				return 10;
		}
		for (int i = 0; i < EquipmentConstants.GLOVES.length; i++) {
			if (name.endsWith(EquipmentConstants.GLOVES[i]) || name.startsWith(EquipmentConstants.GLOVES[i]))
				return 9;
		}
		for (int i = 0; i < EquipmentConstants.SHIELDS.length; i++) {
			if (name.contains(EquipmentConstants.SHIELDS[i]))
				return 5;
		}
		for (int i = 0; i < EquipmentConstants.AMULETS.length; i++) {
			if (name.endsWith(EquipmentConstants.AMULETS[i]) || name.startsWith(EquipmentConstants.AMULETS[i]))
				return 2;
		}
		for (int i = 0; i < EquipmentConstants.ARROWS.length; i++) {
			if (name.endsWith(EquipmentConstants.ARROWS[i]) || name.startsWith(EquipmentConstants.ARROWS[i]))
				return 13;
		}
		for (int i = 0; i < EquipmentConstants.RINGS.length; i++) {
			if (name.endsWith(EquipmentConstants.RINGS[i]) || name.startsWith(EquipmentConstants.RINGS[i]))
				return 12;
		}
		for (int i = 0; i < EquipmentConstants.BODY.length; i++) {
			if (name.contains(EquipmentConstants.BODY[i]))
				return 4;
		}
		for (int i = 0; i < EquipmentConstants.LEGS.length; i++) {
			if (name.contains(EquipmentConstants.LEGS[i]))
				return 7;
		}
		for (int i = 0; i < EquipmentConstants.WEAPONS.length; i++) {
			if (name.endsWith(EquipmentConstants.WEAPONS[i]) || name.startsWith(EquipmentConstants.WEAPONS[i]))
				return 3;
		}
		return -1;
	}

	/**
	 * Checks if the item is a full body item.
	 * @param def The item.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	private static boolean isFullBody(ItemDefinition def) {
		String name = def.getName();
		if (name == null)
			name = "null";
		for (int i = 0; i < EquipmentConstants.FULL_BODIES.length; i++) {
			if (name.contains(EquipmentConstants.FULL_BODIES[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the item is a full hat item.
	 * @param def The item.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	private static boolean isFullHat(ItemDefinition def) {
		String name = def.getName();
		if (name == null)
			name = "null";
		for (int i = 0; i < EquipmentConstants.FULL_HATS.length; i++) {
			if (name.endsWith(EquipmentConstants.FULL_HATS[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the item is a full mask item.
	 * @param def The item.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	private static boolean isFullMask(ItemDefinition def) {
		String name = def.getName();
		if (name == null)
			name = "null";
		for (int i = 0; i < EquipmentConstants.FULL_MASKS.length; i++) {
			if (name.endsWith(EquipmentConstants.FULL_MASKS[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private EquipmentUpdater() {

	}

}
