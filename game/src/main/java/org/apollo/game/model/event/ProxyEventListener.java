package org.apollo.game.model.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An {@link EventListener} for {@link ProxyEvent}s.
 *
 * This is a workaround for JRuby issue <a href="https://github.com/jruby/jruby/issues/2359">2359</a>.
 *
 * @author Major
 */
public final class ProxyEventListener implements EventListener<ProxyEvent> {

	/**
	 * The {@link Map} from Event names to {@link List}s of {@link EventListener}s.
	 */
	private final Map<String, List<EventListener<Event>>> listeners = new HashMap<>();

	/**
	 * Registers an {@link EventListener} to this proxy. This method is <strong>not</strong> type-safe, and must only
	 * be called from ruby.
	 *
	 * @param name The name of the Event. Must not be {@code null}.
	 * @param listener The {@link EventListener} to add. Must not be {@code null}.
	 */
	public void add(String name, EventListener<Event> listener) {
		List<EventListener<Event>> listeners = this.listeners.computeIfAbsent(name, n -> new ArrayList<>(2));
		listeners.add(listener);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void handle(ProxyEvent event) {
		List<EventListener<Event>> chain = listeners.get(event.getName());

		if (chain != null) {
			for (EventListener<Event> listener : chain) {
				Event ruby = event.getRuby();
				listener.handle(ruby);

				if (ruby.terminated()) {
					event.terminate();
					break;
				}
			}
		}
	}

}
