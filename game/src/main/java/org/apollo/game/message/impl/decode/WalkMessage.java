package org.apollo.game.message.impl.decode;

import com.google.common.base.Preconditions;
import org.apollo.game.model.Position;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client to request that the player walks somewhere.
 *
 * @author Graham
 */
public final class WalkMessage extends Message {

	/**
	 * The running flag.
	 */
	private final boolean run;

	/**
	 * The steps.
	 */
	private int x, y;

	/**
	 * Creates the message.
	 *
	 * @param x   The absolute x-axis position.
	 * @param y   The absolute y-axis position.
	 * @param run The run flag.
	 */
	public WalkMessage(int x, int y, boolean run) {
		this.x = x;
		this.y = y;
		this.run = run;
	}


	/**
	 * Gets x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Checks if the steps should be ran (ctrl+click).
	 *
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean isRunning() {
		return run;
	}

}