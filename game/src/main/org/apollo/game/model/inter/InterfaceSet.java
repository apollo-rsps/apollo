package org.apollo.game.model.inter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apollo.game.message.handler.MessageHandlerChain;
import org.apollo.game.message.impl.CloseInterfaceMessage;
import org.apollo.game.message.impl.EnterAmountMessage;
import org.apollo.game.message.impl.OpenDialogueInterfaceMessage;
import org.apollo.game.message.impl.OpenDialogueOverlayMessage;
import org.apollo.game.message.impl.OpenInterfaceMessage;
import org.apollo.game.message.impl.OpenInterfaceSidebarMessage;
import org.apollo.game.message.impl.OpenOverlayMessage;
import org.apollo.game.message.impl.OpenSidebarMessage;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.event.impl.CloseInterfacesEvent;
import org.apollo.game.model.inter.dialogue.DialogueListener;
import org.apollo.game.model.inv.InventoryListener;

/**
 * Represents the set of interfaces the player has open.
 * <p>
 * This class manages all six distinct types of interface (the last two are not present on 317 servers).
 * <p>
 * <ul>
 * <li><strong>Windows:</strong> Interfaces such as the bank, the wilderness warning screen, the trade screen,
 * etc.</li>
 * <li><strong>Overlays:</strong> Displayed in the same place as windows, but don't prevent a player from moving e.g.
 * the wilderness level indicator.</li>
 * <li><strong>Dialogues:</strong> Interfaces displayed over the chat box.</li>
 * <li><strong>Sidebars:</strong> Interfaces displayed over the inventory area.</li>
 * <li><strong>Fullscreen windows:</strong> A window displayed over the whole screen e.g. the 377 welcome screen.</li>
 * <li><strong>Fullscreen background:</strong> Interfaces displayed behind the fullscreen window, typically a blank,
 * black screen.</li>
 * </ul>
 *
 * @author Graham
 */
public final class InterfaceSet {

	/**
	 * A map of open interfaces.
	 */
	private final Map<InterfaceType, Integer> interfaces = new HashMap<>();

	/**
	 * The player whose interfaces are being managed.
	 */
	private final Player player;

	/**
	 * The current enter amount listener.
	 */
	private Optional<EnterAmountListener> amountListener = Optional.empty();

	/**
	 * The current chat box dialogue listener.
	 */
	private Optional<DialogueListener> dialogueListener = Optional.empty();

	/**
	 * The current listener.
	 */
	private Optional<InterfaceListener> listener = Optional.empty();

	/**
	 * Creates an interface set.
	 *
	 * @param player The player.
	 */
	public InterfaceSet(Player player) {
		this.player = player;
	}

	/**
	 * Called when the player has clicked the specified button. Notifies the current dialogue listener.
	 *
	 * @param button The button.
	 * @return {@code true} if the {@link MessageHandlerChain} should be broken.
	 */
	public boolean buttonClicked(int button) {
		return dialogueListener.isPresent() && dialogueListener.get().buttonClicked(button);
	}

	/**
	 * Closes the current open interface(s).
	 */
	public void close() {
		if (interfaces.size() != 0) {
			CloseInterfacesEvent event = new CloseInterfacesEvent(player);

			if (player.getWorld().submit(event)) {
				closeAndNotify();
				player.send(new CloseInterfaceMessage());
			}
		}
	}

	/**
	 * Checks if this interface sets contains the specified interface.
	 *
	 * @param id The interface's id.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean contains(int id) {
		return interfaces.containsValue(id);
	}

	/**
	 * Checks if this interface set contains the specified interface type.
	 *
	 * @param type The interface's type.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean contains(InterfaceType type) {
		return interfaces.containsKey(type);
	}

	/**
	 * Called when the player has clicked the "Click here to continue" button on a dialogue.
	 */
	public void continueRequested() {
		if (dialogueListener.isPresent()) {
			dialogueListener.get().continued();
		}
	}

	/**
	 * Called when the client has entered the specified amount. Notifies the current listener.
	 *
	 * @param amount The amount.
	 */
	public void enteredAmount(int amount) {
		if (amountListener.isPresent()) {
			amountListener.get().amountEntered(amount);
			amountListener = Optional.empty();
		}
	}

	/**
	 * Sent by the client when it has closed an interface.
	 */
	public void interfaceClosed() {
		closeAndNotify();
	}

	/**
	 * Opens a dialogue interface.
	 *
	 * @param listener The {@link DialogueListener}.
	 * @param dialogueId The dialogue id.
	 */
	public void openDialogue(DialogueListener listener, int dialogueId) {
		closeAndNotify();

		dialogueListener = Optional.ofNullable(listener);
		this.listener = Optional.ofNullable(listener);

		interfaces.put(InterfaceType.DIALOGUE, dialogueId);
		player.send(new OpenDialogueInterfaceMessage(dialogueId));
	}

	/**
	 * Opens a dialogue.
	 *
	 * @param dialogueId The dialogue id.
	 */
	public void openDialogue(int dialogueId) {
		openDialogue(null, dialogueId);
	}

	/**
	 * Opens a dialogue overlay interface.
	 *
	 * @param listener The {@link DialogueListener}.
	 * @param dialogueId The dialogue id.
	 */
	public void openDialogueOverlay(DialogueListener listener, int dialogueId) {
		closeAndNotify();

		dialogueListener = Optional.ofNullable(listener);
		this.listener = Optional.ofNullable(listener);

		interfaces.put(InterfaceType.DIALOGUE, dialogueId);
		player.send(new OpenDialogueOverlayMessage(dialogueId));
	}

	/**
	 * Opens a dialogue overlay.
	 *
	 * @param dialogueId The dialogue id.
	 */
	public void openDialogueOverlay(int dialogueId) {
		openDialogueOverlay(null, dialogueId);
	}

	/**
	 * Opens the enter amount dialogue.
	 *
	 * @param listener The enter amount listener.
	 */
	public void openEnterAmountDialogue(EnterAmountListener listener) {
		amountListener = Optional.of(listener);
		player.send(new EnterAmountMessage());
	}

	/**
	 * Opens an overlay interface.
	 *
	 * @param overlay The overlay id.
	 */
	public void openOverlay(int overlay) {
		interfaces.put(InterfaceType.OVERLAY, overlay);
		player.send(new OpenOverlayMessage(overlay));
	}

	/**
	 * Opens an sidebar interface.
	 *
	 * @param sidebar The sidebar id.
	 */
	public void openSidebar(int sidebar) {
		closeAndNotify();
		interfaces.put(InterfaceType.SIDEBAR, sidebar);

		player.send(new OpenSidebarMessage(sidebar));
	}

	/**
	 * Opens an sidebar interface with the specified {@link InventoryListener}.
	 *
	 * @param listener The listener.
	 * @param sidebar The sidebar id.
	 */
	public void openSidebar(InterfaceListener listener, int sidebar) {
		closeAndNotify();
		this.listener = Optional.ofNullable(listener);
		interfaces.put(InterfaceType.SIDEBAR, sidebar);

		player.send(new OpenSidebarMessage(sidebar));
	}

	/**
	 * Opens a window.
	 *
	 * @param windowId The window's id.
	 */
	public void openWindow(int windowId) {
		openWindow(null, windowId);
	}

	/**
	 * Opens a window with the specified listener.
	 *
	 * @param listener The listener for this interface.
	 * @param windowId The window's id.
	 */
	public void openWindow(InterfaceListener listener, int windowId) {
		closeAndNotify();
		this.listener = Optional.ofNullable(listener);

		interfaces.put(InterfaceType.WINDOW, windowId);
		player.send(new OpenInterfaceMessage(windowId));
	}

	/**
	 * Opens a window and inventory sidebar.
	 *
	 * @param windowId The window's id.
	 * @param sidebarId The sidebar's id.
	 */
	public void openWindowWithSidebar(int windowId, int sidebarId) {
		openWindowWithSidebar(null, windowId, sidebarId);
	}

	/**
	 * Opens a window and inventory sidebar with the specified listener.
	 *
	 * @param listener The listener for this interface.
	 * @param windowId The window's id.
	 * @param sidebarId The sidebar's id.
	 */
	public void openWindowWithSidebar(InterfaceListener listener, int windowId, int sidebarId) {
		closeAndNotify();
		this.listener = Optional.ofNullable(listener);

		interfaces.put(InterfaceType.WINDOW, windowId);
		interfaces.put(InterfaceType.SIDEBAR, sidebarId);

		player.send(new OpenInterfaceSidebarMessage(windowId, sidebarId));
	}

	/**
	 * Gets the size of the interface set.
	 *
	 * @return The size.
	 */
	public int size() {
		return interfaces.size();
	}

	/**
	 * An internal method for closing the interface, notifying the listener if appropriate, but not sending any
	 * messages.
	 */
	private void closeAndNotify() {
		amountListener = Optional.empty();
		dialogueListener = Optional.empty();

		interfaces.clear();
		if (listener.isPresent()) {
			listener.get().interfaceClosed();
			listener = Optional.empty();
		}
	}

}