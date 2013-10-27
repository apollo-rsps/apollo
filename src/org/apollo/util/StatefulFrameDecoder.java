package org.apollo.util;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import org.jboss.netty.handler.codec.replay.VoidEnum;

/**
 * A stateful implementation of a {@link FrameDecoder} which may be
 * extended and used by other classes. The current state is tracked by this
 * class and is a user-specified enumeration.
 *
 * The state may be changed by calling the
 * {@link StatefulFrameDecoder#setState(Enum)} method.
 *
 * The current state is supplied as a parameter in the
 * {@link StatefulFrameDecoder#decode(ChannelHandlerContext, Channel, ChannelBuffer, Enum)}
 * and
 * {@link StatefulFrameDecoder#decodeLast(ChannelHandlerContext, Channel, ChannelBuffer, Enum)}
 * methods.
 *
 * This class is not thread safe: it is recommended that the state is only set
 * in the decode methods overriden.
 *
 * {@code null} states are not permitted. This means you cannot use
 * {@link VoidEnum} like used in a {@link ReplayingDecoder}. If you do not need
 * state management, the {@link FrameDecoder} class should be used instead.
 * @author Graham
 * @param <T> The state enumeration.
 */
public abstract class StatefulFrameDecoder<T extends Enum<T>> extends FrameDecoder {

	/**
	 * The current state.
	 */
	private T state;

	/**
	 * Creates the stateful frame decoder with the specified initial state.
	 * @param state The initial state.
	 * @throws NullPointerException if the state is {@code null}.
	 */
	public StatefulFrameDecoder(T state) {
		this(state, false);
	}

	/**
	 * Creates the stateful frame decoder with the specified initial state and
	 * unwrap flag.
	 * @param state The initial state.
	 * @param unwrap The unwrap flag.
	 * @throws NullPointerException if the state is {@code null}.
	 */
	public StatefulFrameDecoder(T state, boolean unwrap) {
		super(unwrap);
		setState(state);
	}

	@Override
	protected final Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		return decode(ctx, channel, buffer, state);
	}

	@Override
	protected final Object decodeLast(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		return decodeLast(ctx, channel, buffer, state);
	}

	/**
	 * Sets a new state.
	 * @param state The new state.
	 * @throws NullPointerException if the state is {@code null}.
	 */
	public final void setState(T state) {
		if (state == null) {
			throw new NullPointerException("state");
		}
		this.state = state;
	}

	/**
	 * Decodes the received packets into a frame.
	 * @param ctx The current context of this handler.
	 * @param channel The channel.
	 * @param buffer The cumulative buffer, which may contain zero or more
	 * bytes.
	 * @param state The current state. The state may be changed by calling
	 * {@link #setState(Enum)}.
	 * @return The decoded frame, or {@code null} if not enough data was
	 * received.
	 * @throws Exception if an error occurs during decoding.
	 */
	protected abstract Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, T state) throws Exception;

	/**
	 * Decodes remaining data before the channel is closed into a frame. You
	 * may override this method, but it is not required. If you do not,
	 * remaining data will be discarded!
	 * @param ctx The current context of this handler.
	 * @param channel The channel.
	 * @param buffer The cumulative buffer, which may contain zero or more
	 * bytes.
	 * @param state The current state. The state may be changed by calling
	 * {@link #setState(Enum)}.
	 * @return The decoded frame, or {@code null} if not enough data was
	 * received.
	 * @throws Exception if an error occurs during decoding.
	 */
	protected Object decodeLast(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer, T state) throws Exception {
		return null;
	}

}
