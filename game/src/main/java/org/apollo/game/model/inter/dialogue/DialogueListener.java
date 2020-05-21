package org.apollo.game.model.inter.dialogue;

import org.apollo.game.message.handler.MessageHandlerChain;
import org.apollo.game.model.inter.InterfaceListener;

/**
 * An {@link InterfaceListener} that listens for dialogue-specific message (e.g. clicking buttons).
 *
 * @author Chris Fletcher
 */
public interface DialogueListener extends InterfaceListener {

	/**
	 * Called when the player has clicked the specified button.
	 * <p>
	 * Note that this method is invoked when any button is clicked whilst the dialogue is opened. In case the button is
	 * not being handled by this listener, simply return {@code false} to allow further processing of the message.
	 * </p>
	 *
	 * @param button The button interface id.
	 * @return {@code true} if the {@link MessageHandlerChain} should be broken, {@code false} if it should be
	 *         continued.
	 */
	public boolean buttonClicked(int button);

	/**
	 * Called when the player has clicked the "Click here to continue" button on a chatting dialogue.
	 */
	public void continued();

}