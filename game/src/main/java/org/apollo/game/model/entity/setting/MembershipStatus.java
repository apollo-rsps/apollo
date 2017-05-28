package org.apollo.game.model.entity.setting;

import java.util.Arrays;
import java.util.Optional;

/**
 * The membership status of a Player.
 *
 * @author Major
 */
public enum MembershipStatus {

	/**
	 * The free membership status.
	 */
	FREE(0),

	/**
	 * The paid membership status.
	 */
	PAID(1);

	/**
	 * Gets the MembershipStatus with the specified value.
	 *
	 * @param value The integer value of the MembershipStatus.
	 * @return The MembershipStatus.
	 * @throws IllegalArgumentException If no MembershipStatus with the specified the value exists.
	 */
	public static MembershipStatus valueOf(int value) {
		Optional<MembershipStatus> optional = Arrays.stream(values()).filter(status -> status.value == value).findAny();
		return optional.orElseThrow(() -> new IllegalArgumentException("Illegal membership status value."));
	}

	/**
	 * The integer value of this MembershipStatus.
	 */
	private final int value;

	/**
	 * Creates the MembershipStatus.
	 *
	 * @param value The integer value.
	 */
	private MembershipStatus(int value) {
		this.value = value;
	}

	/**
	 * Gets the value of this MembershipStatus.
	 *
	 * @return The value.
	 */
	public int getValue() {
		return value;
	}

}