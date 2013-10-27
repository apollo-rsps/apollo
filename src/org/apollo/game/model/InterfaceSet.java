package org.apollo.game.model;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.event.impl.CloseInterfaceEvent;
import org.apollo.game.event.impl.EnterAmountEvent;
import org.apollo.game.event.impl.OpenInterfaceEvent;
import org.apollo.game.event.impl.OpenInterfaceSidebarEvent;
import org.apollo.game.model.inter.EnterAmountListener;
import org.apollo.game.model.inter.InterfaceListener;

/**
 * Represents the set of interfaces the player has open.
 * <p>
 * This class manages all six distinct types of interface (the last two are not
 * present on 317 servers).
 * <p>
 * <ul>
 *   <li><strong>Windows:</strong> the ones people mostly associate with the
 *   word interfaces. Things like your bank, the wildy warning screen, the
 *   trade screen, etc.</li>
 *   <li><strong>Overlays:</strong> display in the same place as windows, but
 *   don't prevent you from moving. For example, the wilderness level
 *   indicator.</li>
 *   <li><strong>Dialogues:</strong> interfaces which are displayed over the
 *   chat box.</li>
 *   <li><strong>Sidebars:</strong> an interface which displays over the
 *   inventory area.</li>
 *   <li><strong>Fullscreen windows:</strong> a window which displays over the
 *   whole screen e.g. the 377 welcome screen.</li>
 *   <li><strong>Fullscreen background:</strong> an interface displayed behind
 *   the fullscreen window, typically a blank, black screen.</li>
 * </ul>
 * @author Graham
 */
public final class InterfaceSet {

	/**
	 * The player whose interfaces are being managed.
	 */
	private final Player player; // TODO: maybe switch to a listener system like the inventory?

	// TODO: maybe store the current inventory tab ids here??
	/**
	 * A map of open interfaces.
	 */
	private Map<InterfaceType, Integer> interfaces = new HashMap<InterfaceType, Integer>();

	/**
	 * The current listener.
	 */
	private InterfaceListener listener;

	/**
	 * The current enter amount listener.
	 */
	private EnterAmountListener amountListener;

	/**
	 * Creates an interface set.
	 * @param player The player.
	 */
	public InterfaceSet(Player player) {
		this.player = player;
	}

	/**
	 * Closes the current open interface(s).
	 */
	public void close() {
		closeAndNotify();

		player.send(new CloseInterfaceEvent());
	}

	/**
	 * Sent by the client when it has closed an interface.
	 */
	public void interfaceClosed() {
		closeAndNotify();
	}

	/**
	 * Opens the enter amount dialog.
	 * @param listener The enter amount listener.
	 */
	public void openEnterAmountDialog(EnterAmountListener listener) {
		this.amountListener = listener;

		player.send(new EnterAmountEvent());
	}

	/**
	 * Opens a window and inventory sidebar.
	 * @param windowId The window's id.
	 * @param sidebarId The sidebar's id.
	 */
	public void openWindowWithSidebar(int windowId, int sidebarId) {
		openWindowWithSidebar(null, windowId, sidebarId);
	}

	/**
	 * Opens a window and inventory sidebar with the specified listener.
	 * @param listener The listener for this interface.
	 * @param windowId The window's id.
	 * @param sidebarId The sidebar's id.
	 */
	public void openWindowWithSidebar(InterfaceListener listener, int windowId, int sidebarId) {
		closeAndNotify();
		this.listener = listener;

		interfaces.put(InterfaceType.WINDOW, windowId);
		interfaces.put(InterfaceType.SIDEBAR, sidebarId);

		player.send(new OpenInterfaceSidebarEvent(windowId, sidebarId));
	}

	/**
	 * Opens a window.
	 * @param windowId The window's id.
	 */
	public void openWindow(int windowId) {
		openWindow(null, windowId);
	}

	/**
	 * Opens a window with the specified listener.
	 * @param listener The listener for this interface.
	 * @param windowId The window's id.
	 */
	public void openWindow(InterfaceListener listener, int windowId) {
		closeAndNotify();
		this.listener = listener;

		interfaces.put(InterfaceType.WINDOW, windowId);

		player.send(new OpenInterfaceEvent(windowId));
	}

	/**
	 * Checks if this interface sets contains the specified interface.
	 * @param id The interface's id.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean contains(int id) {
		return interfaces.containsValue(id);
	}

	/**
	 * An internal method for closing the interface, notifying the listener if
	 * appropriate, but not sending any events.
	 */
	private void closeAndNotify() {
		amountListener = null; // TODO should we notify??

		interfaces.clear();
		if (listener != null) {
			listener.interfaceClosed();
			listener = null;
		}
	}

	/**
	 * Called when the client has entered the specified amount. Notifies the
	 * current listener.
	 * @param amount The amount.
	 */
	public void enteredAmount(int amount) {
		if (amountListener != null) {
			amountListener.amountEntered(amount);
			amountListener = null;
		}
	}

}
