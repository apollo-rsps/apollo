package org.apollo.game.model.event.impl;

import org.apollo.game.model.Position;
import org.apollo.game.model.entity.Mob;
import org.apollo.game.model.event.Event;

/**
 * An {@link Event} created when a Mob's Position is being updated.
 *
 * @author Major
 */
public final class MobPositionUpdateEvent extends Event {

	/**
	 * The Mob whose position is being updated.
	 */
	private final Mob mob;

	/**
	 * The next Position of the Mob.
	 */
	private final Position next;

	/**
	 * Creates the MobPositionUpdateEvent.
	 *
	 * @param mob The {@link Mob} whose Position is being updated.
	 * @param next The next {@link Position} of the Mob.
	 */
	public MobPositionUpdateEvent(Mob mob, Position next) {
		this.mob = mob;
		this.next = next;
	}

	/**
	 * Gets the {@link Mob} being moved.
	 *
	 * @return The Mob.
	 */
	public Mob getMob() {
		return mob;
	}

	/**
	 * Gets the {@link Position} this {@link Mob} is being moved to.
	 *
	 * @return The Position.
	 */
	public Position getNext() {
		return next;
	}

}