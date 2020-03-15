package org.apollo.game.message.impl.encode;

import org.apollo.game.model.inter.ClientInterfaceType;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that opens an interface.
 *
 * @author Graham
 */
public final class IfOpenSubMessage extends Message {

	/**
	 * The parent interface.
	 */
	private final int parentComponent;

	/**
	 * The type of the interface.
	 */
	private final ClientInterfaceType type;

	/**
	 * The interface id.
	 */
	private final int id;

	/**
	 * Creates the message with the specified interface id.
	 *
	 * @param parentComponent the parent component
	 * @param type            the type
	 * @param id              the id
	 */
	public IfOpenSubMessage(int parentComponent, ClientInterfaceType type, int id) {
		this.parentComponent = parentComponent;
		this.type = type;
		this.id = id;
	}

	/**
	 * Gets the interface id.
	 *
	 * @return The interface id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets parent component.
	 *
	 * @return the parent component
	 */
	public int getParentComponent() {
		return parentComponent;
	}

	/**
	 * Gets type.
	 *
	 * @return the type
	 */
	public ClientInterfaceType getType() {
		return type;
	}
}