package org.apollo.net.codec.game;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import org.apollo.game.event.Event;
import org.apollo.net.release.EventDecoder;
import org.apollo.net.release.Release;

/**
 * A {@link MessageToMessageDecoder} that decodes {@link GamePacket}s into {@link Event}s.
 * 
 * @author Graham
 */
public final class GameEventDecoder extends MessageToMessageDecoder<GamePacket> {

    /**
     * The current release.
     */
    private final Release release;

    /**
     * Creates the game event decoder with the specified release.
     * 
     * @param release The release.
     */
    public GameEventDecoder(Release release) {
	this.release = release;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, GamePacket packet, List<Object> out) {
	EventDecoder<?> decoder = release.getEventDecoder(packet.getOpcode());
	if (decoder != null) {
	    out.add(decoder.decode(packet));
	} else {
	    System.out.println("Unidentified packet received - opcode: " + packet.getOpcode() + ".");
	}
    }

}