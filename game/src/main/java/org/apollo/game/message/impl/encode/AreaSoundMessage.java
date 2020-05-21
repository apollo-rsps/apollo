package org.apollo.game.message.impl.encode;

import org.apollo.game.model.entity.AreaSound;

import java.util.Objects;

/**
 * The type Area sound message.
 *
 * @author Khaled Abdeljaber
 */
public class AreaSoundMessage extends RegionUpdateMessage {

	/**
	 * The sound.
	 */
	private final AreaSound sound;

	/**
	 * The position offset.
	 */
	private final int positionOffset;

	/**
	 * Instantiates a new Area sound message.
	 *
	 * @param sound          the sound
	 * @param positionOffset the position offset
	 */
	public AreaSoundMessage(AreaSound sound, int positionOffset) {
		this.sound = sound;
		this.positionOffset = positionOffset;
	}

	/**
	 * Gets sound.
	 *
	 * @return the sound
	 */
	public AreaSound getSound() {
		return sound;
	}

	/**
	 * Gets position offset.
	 *
	 * @return the position offset
	 */
	public int getPositionOffset() {
		return positionOffset;
	}

	@Override
	public int priority() {
		return 5;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AreaSoundMessage that = (AreaSoundMessage) o;
		return positionOffset == that.positionOffset && sound.equals(that.sound);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sound, positionOffset);
	}
}
