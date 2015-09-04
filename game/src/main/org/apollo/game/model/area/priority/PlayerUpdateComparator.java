package org.apollo.game.model.area.priority;

import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.setting.PrivilegeLevel;

/**
 * An {@link UpdateComparator} implementation that will prioritize based on
 * characteristics specific to {@link Player}s.
 * 
 * @author lare96
 */
public final class PlayerUpdateComparator extends UpdateComparator<Player> {

	/**
	 * Creates a new {@link PlayerUpdateComparator}.
	 *
	 * @param player The player that is operating this comparator.
	 */
	public PlayerUpdateComparator(Player player) {
		super(player);
	}

	@Override
	public void andCompare(Player o1, Player o2) {
		compareStaff(o1, o2);
		compareFriends(o1, o2);
	}

	/**
	 * Compares the two {@link Player}s {@code o1}, and {@code o2} based on if
	 * they are staff members or not.
	 * 
	 * @param o1 The first player to compare.
	 * @param o2 The second player to compare.
	 */
	private void compareStaff(Player o1, Player o2) {
		if (PrivilegeLevel.STAFF.contains(o1.getPrivilegeLevel())) {
			setCompareOne(STAFF_PRIORITY);
		}
		if (PrivilegeLevel.STAFF.contains(o2.getPrivilegeLevel())) {
			setCompareTwo(STAFF_PRIORITY);
		}
	}

	/**
	 * Compares the two {@link Player}s {@code o1}, and {@code o2} based on if
	 * the player being updated has them as a friend.
	 * 
	 * @param o1 The first player to compare.
	 * @param o2 The second player to compare.
	 */
	private void compareFriends(Player o1, Player o2) {
		// XXX Currently the friends list is stored as a
		// list which stores the usernames of the players in String format.
		// There has to be a faster solution, possibly using a LinkedHashSet
		// and storing the usernames as a long instead for O(1) here?

		// XXX Would a check need to be made if the other player also has
		// the player being updated added as a friend?

		Player player = getPlayer();
		if (player.getFriendUsernames().contains(o1.getCredentials().getUsername())) {
			setCompareOne(FRIENDS_PRIORITY);
		}
		if (player.getFriendUsernames().contains(o2.getCredentials().getUsername())) {
			setCompareTwo(FRIENDS_PRIORITY);
		}
	}
}