package org.apollo.game.model.event;

/**
 * An {@link Event} that wraps another {@link Event}.
 *
 * This is a workaround for JRuby issue <a href="https://github.com/jruby/jruby/issues/2359">2359</a>. This class
 * should <strong>not</strong> be used in Java.
 *
 * @author Major
 */
public final class ProxyEvent extends Event {

	/**
	 * The Event created by a Ruby plugin.
	 */
	private final Event event;

	/**
	 * The name of the Ruby Event.
	 */
	private final String name;

	/**
	 * Creates the ProxyEvent.
	 *
	 * @param name The name of the {@link Event} defined in Ruby.
	 * @param event The Event created by a Ruby plugin.
	 */
	public ProxyEvent(String name, Event event) {
		this.name = name;
		this.event = event;
	}

	/**
	 * Gets the name of the {@link Event} defined in Ruby.
	 *
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the {@link Event} created in a Ruby plugin.
	 *
	 * @return The Ruby {@link Event}.
	 */
	public Event getRuby() {
		return event;
	}

}
