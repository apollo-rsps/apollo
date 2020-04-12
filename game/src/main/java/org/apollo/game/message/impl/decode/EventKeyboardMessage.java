package org.apollo.game.message.impl.decode;

import org.apollo.net.message.Message;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * The type Event keyboard message.
 *
 * @author Khaled Abdeljaber
 */
public class EventKeyboardMessage extends Message {

	/**
	 * The event that occurs on a keyboard press.
	 */
	public static class EventKeyboard {
		/**
		 * The key pressed.
		 */
		private int key;

		/**
		 * The duration in milliseconds.
		 */
		private Duration duration;

		/**
		 * Instantiates a new Event keyboard.
		 *  @param key      the key
		 * @param duration the duration
		 */
		public EventKeyboard(int key, Duration duration) {
			this.key = key;
			this.duration = duration;
		}

		/**
		 * Gets key.
		 *
		 * @return the key
		 */
		public int getKey() {
			return key;
		}

		/**
		 * Gets duration.
		 *
		 * @return the duration
		 */
		public Duration getDuration() {
			return duration;
		}
	}

	private final EventKeyboard[] events;

	/**
	 * Instantiates a new Event keyboard message.
	 *
	 * @param events the events
	 */
	public EventKeyboardMessage(EventKeyboard... events) {
		this.events = events;
	}

	/**
	 * Gets events.
	 *
	 * @return the events
	 */
	public EventKeyboard[] getEvents() {
		return events;
	}
}
