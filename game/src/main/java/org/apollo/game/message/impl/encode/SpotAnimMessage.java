package org.apollo.game.message.impl.encode;

import org.apollo.game.model.entity.SpotAnim;

import java.util.Objects;

/**
 * The type Spot anim message.
 *
 * @author Khaled Abdeljaber
 */
public class SpotAnimMessage extends RegionUpdateMessage {

	/**
	 * The graphic.
	 */
	private final SpotAnim spotAnim;

	/**
	 * The position offset.
	 */
	private final int positionOffset;

	/**
	 * Instantiates a new Spot anim message.
	 *
	 * @param spotAnim           the anim
	 * @param positionOffset the position offset
	 */
	public SpotAnimMessage(SpotAnim spotAnim, int positionOffset) {
		this.spotAnim = spotAnim;
		this.positionOffset = positionOffset;
	}

	/**
	 * Gets anim.
	 *
	 * @return the anim
	 */
	public SpotAnim getSpotAnim() {
		return spotAnim;
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
		return 3;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SpotAnimMessage that = (SpotAnimMessage) o;
		return positionOffset == that.positionOffset && spotAnim.equals(that.spotAnim);
	}

	@Override
	public int hashCode() {
		return Objects.hash(spotAnim, positionOffset);
	}
}
