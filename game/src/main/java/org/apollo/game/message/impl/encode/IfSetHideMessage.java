package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that changes the state of a hidden widget component (e.g. the special attack bar
 * on the weapon tab).
 *
 * @author Khaled Abdeljaber
 */
public final class IfSetHideMessage extends Message {

	/**
	 * The component id.
	 */
	private final int packedInterface;

	/**
	 * Visible flag.
	 */
	private final boolean visible;

	/**
	 * Creates the interface component state message.
	 *
	 * @param interfaceId The interface id.
	 * @param componentId The compononent id.
	 * @param visible     The flag for showing or hiding the component.
	 */
	public IfSetHideMessage(int interfaceId, int componentId, boolean visible) {
		this.packedInterface = interfaceId << 16 | componentId;
		this.visible = visible;
	}

	/**
	 * Gets the packed interface.
	 *
	 * @return The packed interface.
	 */
	public int getPackedInterface() {
		return packedInterface;
	}

	/**
	 * Gets the visible flag.
	 *
	 * @return {@code true} if the component has been set to visible, {@code false} if not.
	 */
	public boolean isVisible() {
		return visible;
	}

}