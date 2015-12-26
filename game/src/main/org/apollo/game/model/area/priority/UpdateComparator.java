package org.apollo.game.model.area.priority;

import java.util.Comparator;

import org.apollo.game.model.entity.Mob;
import org.apollo.game.model.entity.Npc;
import org.apollo.game.model.entity.Player;

import com.google.common.base.Preconditions;

/**
 * A {@link Comparator} implementation that will prioritize based on
 * characteristics common to {@link Player}s and {@link Npc}s. Both must extend
 * this class in order to provide their own specific prioritizing
 * characteristics.
 * 
 * @author lare96
 *
 * @param <T> The type of {@link Mob} being prioritized with this
 *        {@code Comparator}.
 */
public abstract class UpdateComparator<T extends Mob> implements Comparator<T> {

	/**
	 * The priority value of the characteristic for distance.
	 */
	protected static final int DISTANCE_PRIORITY = 1;

	/**
	 * The priority value of the characteristic for players on the friends list.
	 */
	protected static final int FRIENDS_PRIORITY = 2;

	/**
	 * The priority value of the characteristic for staff members.
	 */
	protected static final int STAFF_PRIORITY = 3;

	/**
	 * The priority value of the characteristic for players in combat.
	 */
	protected static final int COMBAT_PRIORITY = 4;

	/**
	 * The player that is operating this {@code Comparator}.
	 */
	private final Player player;

	/**
	 * The prioritizing value for the first player.
	 */
	private int compareOne;

	/**
	 * The prioritizing value for the second player.
	 */
	private int compareTwo;

	/**
	 * Creates a new {@link UpdateComparator}.
	 *
	 * @param player The player that is operating this {@code Comparator}.
	 */
	protected UpdateComparator(Player player) {
		this.player = player;
	}

	@Override
	public final int compare(T o1, T o2) {
		// TODO Compare which entity is in combat with the player here, be sure
		// to use the COMBAT_PRIORITY value.
		compareDistance(o1, o2);
		andCompare(o1, o2);
		return -Integer.compare(compareOne, compareTwo);
	}

	/**
	 * Allows the implementing class to set the prioritizing values to be
	 * compared with {@code Integer.compare(int, int)}.
	 * 
	 * @param o1 The first player to compare.
	 * @param o2 The second player to compare.
	 */
	public abstract void andCompare(T o1, T o2);

	/**
	 * Compares the {@link Position}s of {@code o1} and {@code o2} to the {@link Player}
	 * being updated, to determine who is closer.
	 * 
	 * @param o1 The first player to compare.
	 * @param o2 The second player to compare.
	 */
	private void compareDistance(T o1, T o2) {
		int firstX = Math.abs(o1.getPosition().getX() - player.getPosition().getX());
		int secondX = Math.abs(o2.getPosition().getX() - player.getPosition().getX());

		int firstY = Math.abs(o1.getPosition().getY() - player.getPosition().getY());
		int secondY = Math.abs(o2.getPosition().getY() - player.getPosition().getY());

		int firstDistance = Math.max(firstX, firstY);
		int secondDistance = Math.max(secondX, secondY);

		if (Math.min(firstDistance, secondDistance) == firstDistance) {
			setCompareOne(DISTANCE_PRIORITY);
		} else {
			setCompareTwo(DISTANCE_PRIORITY);
		}
	}

	/**
	 * A function that sets the prioritizing value of the first {@link Player}
	 * being compared. If the value being set is less than the current
	 * prioritizing value, the value being set will be discarded.
	 * 
	 * @param value The new value to set, cannot be below {@code 1}.
	 */
	public final void setCompareOne(int value) {
		Preconditions.checkArgument(value > 0, "value <= 0");
		if (value <= compareOne) {
			return;
		}
		compareOne = value;
	}

	/**
	 * A function that sets the prioritizing value of the second {@link Player}
	 * being compared. If the value being set is less than the current
	 * prioritizing value, the value being set will be discarded.
	 * 
	 * @param value The new value to set, cannot be below {@code 1}.
	 */
	public final void setCompareTwo(int value) {
		Preconditions.checkArgument(value > 0, "value <= 0");
		if (value <= compareTwo) {
			return;
		}
		compareTwo = value;
	}

	/**
	 * Gets the player that is operating this {@code Comparator}.
	 * 
	 * @return the player that is being updated.
	 */
	public final Player getPlayer() {
		return player;
	}
}
