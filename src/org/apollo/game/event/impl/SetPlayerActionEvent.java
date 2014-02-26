package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent by the client to add an action to the menu when a player right-clicks another.
 * 
 * @author Major
 */
public final class SetPlayerActionEvent extends Event {

	/**
	 * This action's text (e.g. "Follow").
	 */
	private final String text;

	/**
	 * The menu slot this action will occupy.
	 */
	private final int slot;

	/**
	 * Whether or not this action is the primary action.
	 */
	private final boolean primaryAction;

	/**
	 * Creates the set player action event.
	 * 
	 * @param text The action text.
	 * @param slot The menu slot.
	 */
	public SetPlayerActionEvent(String text, int slot) {
		this(text, slot, false);
	}

	/**
	 * Creates the set player action event.
	 * 
	 * @param text The action text.
	 * @param slot The menu slot.
	 * @param primaryInteraction Whether or not the action is the primary action.
	 */
	public SetPlayerActionEvent(String text, int slot, boolean primaryInteraction) {
		this.text = text;
		this.slot = slot;
		this.primaryAction = primaryInteraction;
	}

	/**
	 * Gets the action text.
	 * 
	 * @return The text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Gets the menu slot this action occupies.
	 * 
	 * @return The slot.
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * Whether or not this action is the primary one (i.e. should be displayed when the player hovers over the other
	 * player).
	 * 
	 * @return {@code true} if this action is the primary action, {@code false} if not.
	 */
	public boolean isPrimaryAction() {
		return primaryAction;
	}

}