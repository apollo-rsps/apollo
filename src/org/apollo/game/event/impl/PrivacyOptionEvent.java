package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Player;

/**
 * An {@link Event} sent by the client or server to specify the options at the bottom of the game screen relating
 * to the chat and trade privacy options.
 * <br />
 * See http://rswiki.moparisthebest.com/index.php?title=317:Privacy_options
 *
 * @author Kyle Stevenson
 *         Date: 12/24/13
 *         Time: 1:38 AM
 */
public class PrivacyOptionEvent extends Event {
    /**
     * Public chat setting
     */
    private final int publicChat;

    /**
     * Private chat setting
     */
    private final int privateChat;

    /**
     * Trade/compete setting
     */
    private final int tradeCompete;

    /**
     * Creates a privacy option event.
     *
     * @param publicChat The byte value of the public chat privacy option.
     * @param privateChat The byte value of the private chat privacy option.
     * @param tradeCompete The byte value of the trade/compete privacy option.
     */
    public PrivacyOptionEvent(final int publicChat, final int privateChat, final int tradeCompete) {
        this.publicChat = publicChat & 0xFF;
        this.privateChat = privateChat & 0xFF;
        this.tradeCompete = tradeCompete & 0xFF;
    }

    /**
     * Public chat unsigned byte value.
     *
     * @return The public chat unsigned byte.
     */
    public int getPublicChat() {
        return publicChat;
    }

    /**
     * Public chat privacy option.
     *
     * @return The public chat privacy option.
     */
    public Player.PrivacyOption getPrivacyPublicChat() {
        return Player.PrivacyOption.valueOf(publicChat);
    }

    /**
     * Private chat unsigned byte value.
     *
     * @return The private chat unsigned byte.
     */
    public int getPrivateChat() {
        return privateChat;
    }

    /**
     * Private chat privacy option.
     *
     * @return The private chat privacy option.
     */
    public Player.PrivacyOption getPrivacyPrivateChat() {
        return Player.PrivacyOption.valueOf(privateChat);
    }

    /**
     * Trade/compete unsigned byte value.
     *
     * @return The trade/compete unsigned byte.
     */
    public int getTradeCompete() {
        return tradeCompete;
    }

    /**
     * Trade/compete privacy option.
     *
     * @return The trade/compete privacy option.
     */
    public Player.PrivacyOption getPrivacyTradeCompete() {
        return Player.PrivacyOption.valueOf(tradeCompete);
    }
}
