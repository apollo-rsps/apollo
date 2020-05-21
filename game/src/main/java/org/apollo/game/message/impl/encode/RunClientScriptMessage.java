package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * @author Khaled Abdeljaber
 */
public class RunClientScriptMessage extends Message {

	private final int id;
	private final Object[] params;

	/**
	 * Instantiates a new Run client script message.
	 *
	 * @param id     the id
	 * @param params the params
	 */
	public RunClientScriptMessage(int id, Object... params) {
		this.id = id;
		this.params = params;
	}

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get params object [ ].
	 *
	 * @return the object [ ]
	 */
	public Object[] getParams() {
		return params;
	}
}
