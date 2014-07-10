package org.apollo.net.codec.game;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import org.apollo.game.event.Event;
import org.apollo.net.release.EventEncoder;
import org.apollo.net.release.Release;

/**
 * A {@link MessageToMessageEncoder} which encodes {@link Event}s into {@link GamePacket}s.
 * 
 * @author Graham
 */
public final class GameEventEncoder extends MessageToMessageEncoder<Event> {

    /**
     * The current release.
     */
    private final Release release;

    /**
     * Creates the game event encoder with the specified release.
     * 
     * @param release The release.
     */
    public GameEventEncoder(Release release) {
	this.release = release;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void encode(ChannelHandlerContext ctx, Event event, List<Object> out) {
	EventEncoder<Event> encoder = (EventEncoder<Event>) release.getEventEncoder(event.getClass());
	if (encoder != null) {
	    out.add(encoder.encode(event));
	}
    }

}