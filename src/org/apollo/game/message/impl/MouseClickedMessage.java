package org.apollo.game.message.impl;

import org.apollo.game.message.Message;

/**
 * A {@link Message} sent by the client to indicate when the mouse button/s have been clicked. This can be used in
 * combination with {@link org.apollo.game.message.impl.FocusUpdateMessage} to work out if the player is clicking
 * whilst the client is closed
 *
 * @author Stuart
 */
public final class MouseClickedMessage extends Message {

    /**
     * Time in milliseconds since the last click
     */
    private final long lastClickedDelay;
    /**
     * Right mouse button flag
     */
    private final boolean rightMouseButton;
    /**
     * The y position of the cursor
     */
    private final int x;
    /**
     * The x position of the cursor
     */
    private final int y;

    /**
     * Creates a new mouse clicked message
     *
     * @param lastClickedDelay  The delay since the last click
     * @param rightMouseButton Whether or not the right mouse button was used
     * @param x                 The x cursor position when clicked
     * @param y                 The y cursor position when clicked
     */
    public MouseClickedMessage(long lastClickedDelay, boolean rightMouseButton, int x, int y) {
        this.lastClickedDelay = lastClickedDelay;
        this.rightMouseButton = rightMouseButton;
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the time in milliseconds since the last click
     *
     * @return The time in milliseconds since the last click
     */
    public long getLastClickedDelay() {
        return lastClickedDelay;
    }

    /**
     * Gets whether the right mouse button was used or not
     *
     * @return If the mouse right button was used to click
     */
    public boolean usingRightMouseButton() {
        return rightMouseButton;
    }

    /**
     * Gets the x position of the cursor
     *
     * @return The x position of the cursor when clicked
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y position of the cursor
     *
     * @return The y position of the cursor when clicked
     */
    public int getY() {
        return y;
    }

}
