package org.apollo.net.codec.game;

import net.burtleburtle.bob.rand.IsaacRandom;

import org.apollo.net.meta.PacketMetaData;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.Release;
import org.apollo.util.StatefulFrameDecoder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * A {@link StatefulFrameDecoder} which decodes game packets.
 * @author Graham
 */
public final class GamePacketDecoder extends StatefulFrameDecoder<GameDecoderState> {

	/**
	 * The current release.
	 */
	private final Release release;

	/**
	 * The random number generator.
	 */
	private final IsaacRandom random;

	/**
	 * The current opcode.
	 */
	private int opcode;

	/**
	 * The packet type.
	 */
	private PacketType type;

	/**
	 * The current length.
	 */
	private int length;

	/**
	 * Creates the {@link GamePacketDecoder}.
	 * @param random The random number generator.
	 * @param release The current release.
	 */
	public GamePacketDecoder(IsaacRandom random, Release release) {
		super(GameDecoderState.GAME_OPCODE);
		this.random = random;
		this.release = release;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, GameDecoderState state) throws Exception {
		switch (state) {
		case GAME_OPCODE:
			return decodeOpcode(ctx, channel, buffer);
		case GAME_LENGTH:
			return decodeLength(ctx, channel, buffer);
		case GAME_PAYLOAD:
			return decodePayload(ctx, channel, buffer);
		default:
			throw new Exception("Invalid game decoder state");
		}
	}

	/**
	 * Decodes in the opcode state.
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception if an error occurs.
	 */
	private Object decodeOpcode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readable()) {
			int encryptedOpcode = buffer.readUnsignedByte();
			opcode = (encryptedOpcode - random.nextInt()) & 0xFF;

			PacketMetaData metaData = release.getIncomingPacketMetaData(opcode);
			if (metaData == null) {
				throw new Exception("Illegal opcode: " + opcode);
			}


			type = metaData.getType();
			switch (type) {
			case FIXED:
				length = metaData.getLength();
				if (length == 0) {
					return decodeZeroLengthPacket(ctx, channel, buffer);
				} else {
					setState(GameDecoderState.GAME_PAYLOAD);
				}
				break;
			case VARIABLE_BYTE:
				setState(GameDecoderState.GAME_LENGTH);
				break;
			default:
				throw new Exception("Illegal packet type: " + type);
			}
		}
		return null;
	}

	/**
	 * Decodes in the length state.
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception if an error occurs.
	 */
	private Object decodeLength(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readable()) {
			length = buffer.readUnsignedByte();
			if (length == 0) {
				return decodeZeroLengthPacket(ctx, channel, buffer);
			} else {
				setState(GameDecoderState.GAME_PAYLOAD);
			}
		}
		return null;
	}

	/**
	 * Decodes in the payload state.
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception if an error occurs.
	 */
	private Object decodePayload(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() >= length) {
			ChannelBuffer payload = buffer.readBytes(length);
			setState(GameDecoderState.GAME_OPCODE);
			return new GamePacket(opcode, type, payload);
		}
		return null;
	}

	/**
	 * Decodes a zero length packet. This hackery is required as Netty will
	 * throw an exception if we return a frame but have read nothing!
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception if an error occurs.
	 */
	private Object decodeZeroLengthPacket(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		ChannelBuffer payload = ChannelBuffers.buffer(0);
		setState(GameDecoderState.GAME_OPCODE);
		return new GamePacket(opcode, type, payload);
	}

}
