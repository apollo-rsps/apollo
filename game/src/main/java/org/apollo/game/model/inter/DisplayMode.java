package org.apollo.game.model.inter;

public enum DisplayMode {
	FIXED(548, 1129),
	RESIZABLE(161, 1130),
	SIDE_PANELS(164, 1131),
	FULL_SCREEN(165, 1132),
	MOBILE(601, 1745);

	private final int interfaceId;
	private final int topLevelEnum;

	DisplayMode(int interfaceId, int topLevelEnum) {
		this.interfaceId = interfaceId;
		this.topLevelEnum = topLevelEnum;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public int getTopLevelEnum() {
		return topLevelEnum;
	}
}
