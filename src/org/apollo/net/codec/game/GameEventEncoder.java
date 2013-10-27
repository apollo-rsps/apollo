package org.apollo.net.codec.game;

import org.apollo.game.event.Event;
import org.apollo.net.release.EventEncoder;
import org.apollo.net.release.Release;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * A {@link OneToOneEncoder} which encodes {@link Event}s into
 * {@link GamePacket}s.
 * @author Graham
 */
public final class GameEventEncoder extends OneToOneEncoder {

	/**
	 * The current release.
	 */
	private final Release release;

	/**
	 * Creates the game event encoder with the specified release.
	 * @param release The release.
	 */
	public GameEventEncoder(Release release) {
		this.release = release;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel c, Object msg) throws Exception {
		if (msg instanceof Event) {
			Event event = (Event) msg;
			EventEncoder<Event> encoder = (EventEncoder<Event>) release.getEventEncoder(event.getClass());
			if (encoder != null) {
				return encoder.encode(event);
			}
			return null;
		}
		return msg;
	}

}
