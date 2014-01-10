package org.apollo.game.model;

/**
 * Represents the rules of the game.
 *
 * @author Kyle Stevenson
 */
public enum GameRule {

	/**
	 * When multiple people log into an account or when someone attempts to trade/sell their account.
	 */
	ACCOUNT_SHARING_TRADING(5),

	/**
	 * Advertising or spamming a website.
	 */
	ADVERTISING_WEBSITE(10),

	/**
	 * Asking other players for personal details such as their phone number or Facebook.
	 */
	ASKING_FOR_PERSONAL_DETAILS(12),

	/**
	 * Knowingly abusing a bug for personal gain.
	 */
	BUG_ABUSE(3),

	/**
	 * Encouraging other players to break the rules of the game.
	 */
	ENCOURAGING_OTHERS_TO_BREAK_RULES(8),

	/**
	 * Attempting to scam another user of their items.
	 */
	ITEM_SCAMMING(1),

	/**
	 * Impersonating a staff member.
	 */
	JAGEX_STAFF_IMPERSONATION(4),

	/**
	 * Use of macroing tools or bots.
	 */
	MACROING(6),

	/**
	 * Misusing customer support.
	 */
	MISUSE_OF_CUSTOMER_SUPPORT(9),

	/**
	 * One human logging into multiple accounts at once.
	 */
	MULTIPLE_LOGGING_IN(7),

	/**
	 * Offensive language such as racial slurs or fowl language.
	 */
	OFFENSIVE_LANGUAGE(0),

	/**
	 * Attempting to scam another user of their password.
	 */
	PASSWORD_SCAMMING(2),

	/**
	 * Trading in-game currency or items for real world currency or items.
	 */
	REAL_WORLD_ITEM_TRADING(11);


	/**
	 * The in game rule number offset by -1.
	 */
	private final int ruleNumber;

	/**
	 * Creates the game rule.
	 *
	 * @param ruleNumber The rule number.
	 */
	private GameRule(int ruleNumber) {
		this.ruleNumber = ruleNumber;
	}

	/**
	 * Gets the game rule for the specified numerical value.
	 *
	 * @param value The game rule index.
	 * @return The game rule.
	 * @throws IllegalArgumentException If the numerical value is invalid.
	 */
	public static GameRule valueOf(int value) {
		for (GameRule gameRule : values()) {
			if (gameRule.toInteger() == value) {
				return gameRule;
			}
		}

		throw new IllegalArgumentException("Invalid game rule value specified");
	}

	/**
	 * Converts this privacy state to an integer.
	 *
	 * @return The numerical value used by the client.
	 */
	public int toInteger() {
		return ruleNumber;
	}

}