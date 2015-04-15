package org.apollo.game.model.inter;

/**
 * A listener for the enter amount dialog.
 *
 * @author Graham
 */
@FunctionalInterface
public interface EnterAmountListener {

	/**
	 * Called when the player enters the specified amount.
	 *
	 * @param amount The amount.
	 */
	public void amountEntered(int amount);

}