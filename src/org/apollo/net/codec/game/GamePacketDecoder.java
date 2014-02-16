package org.apollo.net.codec.game;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.List;

import net.burtleburtle.bob.rand.IsaacRandom;

import org.apollo.net.meta.PacketMetaData;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.Release;
import org.apollo.util.StatefulFrameDecoder;

/**
 * A {@link StatefulFrameDecoder} which decodes game packets.
 * 
 * @author Graham
 */
public final class GamePacketDecoder extends StatefulFrameDecoder<GameDecoderState> {

	/**
	 * The current length.
	 */
	private int length;

	/**
	 * The current opcode.
	 */
	private int opcode;

	/**
	 * The random number generator.
	 */
	private final IsaacRandom random;

	/**
	 * The current release.
	 */
	private final Release release;

	/**
	 * The packet type.
	 */
	private PacketType type;

	/**
	 * Creates the {@link GamePacketDecoder}.
	 * 
	 * @param random The random number generator.
	 * @param release The current release.
	 */
	public GamePacketDecoder(IsaacRandom random, Release release) {
		super(GameDecoderState.GAME_OPCODE);
		this.random = random;
		this.release = release;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out, GameDecoderState state)
			throws IOException {
		switch (state) {
		case GAME_OPCODE:
			decodeOpcode(ctx, in, out);
			break;
		case GAME_LENGTH:
			decodeLength(ctx, in, out);
			break;
		case GAME_PAYLOAD:
			decodePayload(ctx, in, out);
			break;
		default:
			throw new IllegalStateException("Invalid game decoder state.");
		}
	}

	/**
	 * Decodes in the length state.
	 * 
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception If an error occurs.
	 */
	private Object decodeLength(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
		if (buffer.isReadable()) {
			length = buffer.readUnsignedByte();
			if (length != 0) {
				setState(GameDecoderState.GAME_PAYLOAD);
			}
		}
		return null;
	}

	/**
	 * Decodes in the opcode state.
	 * 
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws IOException If a received opcode or packet type is illegal.
	 */
	private void decodeOpcode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws IOException {
		if (buffer.isReadable()) {
			int encryptedOpcode = buffer.readUnsignedByte();
			opcode = encryptedOpcode - random.nextInt() & 0xFF;

			PacketMetaData metaData = release.getIncomingPacketMetaData(opcode);
			if (metaData == null) {
				throw new IOException("Illegal opcode: " + opcode + ".");
			}

			type = metaData.getType();
			switch (type) {
			case FIXED:
				length = metaData.getLength();
				if (length != 0) {
					setState(GameDecoderState.GAME_PAYLOAD);
				}
				break;
			case VARIABLE_BYTE:
				setState(GameDecoderState.GAME_LENGTH);
				break;
			default:
				throw new IOException("Illegal packet type: " + type + ".");
			}
		}
	}

	/**
	 * Decodes in the payload state.
	 * 
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception If an error occurs.
	 */
	private void decodePayload(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
		if (buffer.readableBytes() >= length) {
			ByteBuf payload = buffer.readBytes(length);
			setState(GameDecoderState.GAME_OPCODE);
			out.add(new GamePacket(opcode, type, payload));
		}
	}

}