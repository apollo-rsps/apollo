package org.apollo.game.message.impl.decode;

import org.apollo.net.message.Message;

import java.time.Duration;

/**
 * The type Event keyboard message.
 *
 * @author Khaled Abdeljaber
 */
public class KeyboardEventMessage extends Message {

	/**
	 * The event that occurs on a keyboard press.
	 */
	public static class KeyboardEvent {
		/**
		 * The key pressed.
		 */
		private int key;

		/**
		 * The duration in milliseconds.
		 */
		private long duration;

		/**
		 * Instantiates a new Event keyboard.
		 *  @param key      the key
		 * @param duration the duration
		 */
		public KeyboardEvent(int key, Duration duration) {
			this.key = key;
			this.duration = duration.toMillis();
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
		public long getDuration() {
			return duration;
		}
	}

	private final KeyboardEvent[] events;

	/**
	 * Instantiates a new Event keyboard message.
	 *
	 * @param events the events
	 */
	public KeyboardEventMessage(KeyboardEvent... events) {
		this.events = events;
	}

	/**
	 * Gets events.
	 *
	 * @return the events
	 */
	public KeyboardEvent[] getEvents() {
		return events;
	}
}
