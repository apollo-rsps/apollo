package org.apollo.game.message.impl.decode;

import org.apollo.game.model.inter.DisplayMode;
import org.apollo.net.message.Message;

public class DisplayStatusMessage extends Message {

	private final DisplayMode mode;
	private final int height, width;

	public DisplayStatusMessage(DisplayMode mode, int height, int width) {
		this.mode = mode;
		this.height = height;
		this.width = width;
	}

	public DisplayMode getMode() {
		return mode;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
}
