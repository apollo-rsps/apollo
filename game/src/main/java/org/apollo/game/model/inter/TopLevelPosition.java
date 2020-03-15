package org.apollo.game.model.inter;

import org.apollo.cache.def.EnumDefinition;

public enum TopLevelPosition {
	CHATBOX(29, 162, ClientInterfaceType.OVERLAY), PRIVATE_CHAT(9, 163,
			ClientInterfaceType.OVERLAY), WILDERNESS_OVERLAY(4, ClientInterfaceType.OVERLAY), ORBS(28,
			ClientInterfaceType.OVERLAY), SKILLS_TAB(69, 320, ClientInterfaceType.OVERLAY), JOURNAL_TAB_HEADER(70, 629,
			ClientInterfaceType.OVERLAY), INVENTORY_TAB(71, 149, ClientInterfaceType.OVERLAY), EQUIPMENT_TAB(72, 387,
			ClientInterfaceType.OVERLAY), PRAYER_TAB(73, 541, ClientInterfaceType.OVERLAY), SPELLBOOK_TAB(74, 218,
			ClientInterfaceType.OVERLAY), ACCOUNT_MANAGEMENT(76, 109, ClientInterfaceType.OVERLAY), FRIENDS_TAB(77, 429,
			ClientInterfaceType.OVERLAY), LOGOUT_TAB(78, 182, ClientInterfaceType.OVERLAY), SETTINGS_TAB(79, 261,
			ClientInterfaceType.OVERLAY), EMOTE_TAB(80, 216, ClientInterfaceType.OVERLAY), MUSIC_TAB(81, 239,
			ClientInterfaceType.OVERLAY), CLAN_TAB(75, 7, ClientInterfaceType.OVERLAY), COMBAT_TAB(68, 593,
			ClientInterfaceType.OVERLAY), MODAL(13, ClientInterfaceType.MODAL), DIALOGUE(561,
			ClientInterfaceType.MODAL), MINIGAME_OVERLAY(6, ClientInterfaceType.OVERLAY), OVERLAY(3,
			ClientInterfaceType.OVERLAY), SINGLE_TAB(66, ClientInterfaceType.MODAL), WORLD_MAP(14,
			ClientInterfaceType.OVERLAY), UNKNOWN_OVERLAY(8, ClientInterfaceType.OVERLAY), XP_TRACKER(7,
			ClientInterfaceType.OVERLAY);

	private final int component, interfaceId;
	private final ClientInterfaceType interfaceType;

	TopLevelPosition(int component, int interfaceId, ClientInterfaceType interfaceType) {
		this.interfaceId = interfaceId;
		this.component = component;
		this.interfaceType = interfaceType;
	}

	TopLevelPosition(int component, ClientInterfaceType interfaceType) {
		this(component, -1, interfaceType);
	}

	public int getComponent(DisplayMode mode) {
		if (mode == DisplayMode.RESIZABLE) {
			return DisplayMode.RESIZABLE.getInterfaceId() << 16 | component;
		}
		return EnumDefinition.lookup(mode.getTopLevelEnum())
				.lookup(DisplayMode.RESIZABLE.getInterfaceId() << 16 | component, -1);
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public ClientInterfaceType getInterfaceType() {
		return interfaceType;
	}
}
