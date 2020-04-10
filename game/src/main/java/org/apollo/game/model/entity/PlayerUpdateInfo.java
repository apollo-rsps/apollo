package org.apollo.game.model.entity;

import org.apollo.net.codec.game.GamePacketBuilder;

/**
 * @author Khaled Abdeljaber
 */
public class PlayerUpdateInfo {

	private byte[] skipped = new byte[2048];
	private int[] externalPositions = new int[2048];

	public PlayerUpdateInfo() {

	}

	public byte getSkip(int index) {
		return skipped[index];
	}

	public void setSkip(int index, byte value) {
		skipped[index] = value;
	}

	public void setExternalPositions(int index, int value) {
		externalPositions[index] = value;
	}

	public int getExternalPosition(int index) {
		return externalPositions[index];
	}
}
