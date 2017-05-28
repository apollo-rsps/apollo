package org.apollo.game.action;

import org.apollo.game.model.entity.Mob;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * An action is a specialised {@link ScheduledTask} that is specific to a {@link Mob}.
 * <p>
 * <strong>ALL</strong> actions <strong>MUST</strong> implement the {@link #equals(Object)} method. This is to check if
 * two actions are identical: if they are, then the new action does not replace the old one (so spam/accidental clicking
 * won't cancel your action, and start another from scratch).
 *
 * @author Graham
 * @param <T> The type of mob.
 */
public abstract class Action<T extends Mob> extends ScheduledTask {

	/**
	 * The mob performing the action.
	 */
	protected final T mob;

	/**
	 * A flag indicating if this action is stopping.
	 */
	private boolean stopping = false;

	/**
	 * Creates a new action.
	 *
	 * @param delay The delay in pulses.
	 * @param immediate A flag indicating if the action should happen immediately.
	 * @param mob The mob performing the action.
	 */
	public Action(int delay, boolean immediate, T mob) {
		super(delay, immediate);
		this.mob = mob;
	}

	/**
	 * Gets the mob which performed the action.
	 *
	 * @return The mob.
	 */
	public final T getMob() {
		return mob;
	}

	@Override
	public void stop() {
		super.stop();
		if (!stopping) {
			stopping = true;
			mob.stopAction();
		}
	}

}