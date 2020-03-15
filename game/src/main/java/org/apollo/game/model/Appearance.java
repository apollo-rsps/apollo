package org.apollo.game.model;

import com.google.common.base.Preconditions;
import org.apollo.game.model.entity.setting.Gender;

/**
 * Represents the appearance of a player.
 *
 * @author Graham
 */
public final class Appearance {

	/**
	 * The default appearance.
	 */
	public static final Appearance DEFAULT_APPEARANCE = new Appearance(Gender.MALE, new int[] { 0, 10, 18, 26, 33, 36,
			42 }, new int[5]);

	/**
	 * The array of clothing/skin colors.
	 */
	private final int[] colors;

	/**
	 * The player's gender.
	 */
	private final Gender gender;

	/**
	 * The array of clothing/characteristic styles.
	 */
	private final int[] style;

	/**
	 * Creates the appearance with the specified gender, style and colors.
	 *
	 * @param gender The gender.
	 * @param style The style.
	 * @param colors The colors.
	 */
	public Appearance(Gender gender, int[] style, int[] colors) {
		if (gender == null || style == null || colors == null) {
			throw new NullPointerException("No arguments can be null.");
		}
		Preconditions.checkArgument(style.length == 7, "Style array must have 7 exactly elements.");
		Preconditions.checkArgument(colors.length == 5, "Colors array must have exactly 5 elements.");

		this.gender = gender;
		this.style = style;
		this.colors = colors;
	}

	/**
	 * Gets the player's colors.
	 *
	 * @return The player's colors.
	 */
	public int[] getColors() {
		return colors;
	}

	/**
	 * Gets the gender of the player.
	 *
	 * @return The gender of the player.
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * Gets the player's styles.
	 *
	 * @return The player's styles.
	 */
	public int[] getStyle() {
		// 0 = head 1 = chin/beard 2 = chest 3 = arms 4 = hands 5 = legs 6 = feet
		return style;
	}

	/**
	 * Checks if the player is female.
	 *
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isFemale() {
		return gender == Gender.FEMALE;
	}

	/**
	 * Checks if the player is male.
	 *
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isMale() {
		return gender == Gender.MALE;
	}

}