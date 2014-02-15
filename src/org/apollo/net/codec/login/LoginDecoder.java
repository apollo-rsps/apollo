package org.apollo.net.codec.login;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import net.burtleburtle.bob.rand.IsaacRandom;

import org.apollo.fs.FileSystemConstants;
import org.apollo.net.NetworkConstants;
import org.apollo.security.IsaacRandomPair;
import org.apollo.security.PlayerCredentials;
import org.apollo.util.BufferUtil;
import org.apollo.util.StatefulFrameDecoder;

/**
 * A {@link StatefulFrameDecoder} which decodes the login request frames.
 * 
 * @author Graham
 */
public final class LoginDecoder extends StatefulFrameDecoder<LoginDecoderState> {

	/**
	 * The secure random number generator.
	 */
	private static final SecureRandom random = new SecureRandom();

	/**
	 * The login packet length.
	 */
	private int loginLength;

	/**
	 * The reconnecting flag.
	 */
	private boolean reconnecting;

	/**
	 * The server-side session key.
	 */
	private long serverSeed;

	/**
	 * The username hash.
	 */
	private int usernameHash;

	/**
	 * Creates the login decoder with the default initial state.
	 */
	public LoginDecoder() {
		super(LoginDecoderState.LOGIN_HANDSHAKE, true);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out, LoginDecoderState state)
			throws Exception {
		switch (state) {
		case LOGIN_HANDSHAKE:
			decodeHandshake(ctx, in, out);
			break;
		case LOGIN_HEADER:
			decodeHeader(ctx, in, out);
			break;
		case LOGIN_PAYLOAD:
			decodePayload(ctx, in, out);
			break;
		default:
			throw new IllegalStateException("Invalid login decoder state.");
		}
	}

	/**
	 * Decodes in the handshake state.
	 * 
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws Exception If an error occurs.
	 */
	private void decodeHandshake(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
		if (buffer.isReadable()) {
			usernameHash = buffer.readUnsignedByte();
			serverSeed = random.nextLong();

			ByteBuf response = ctx.alloc().buffer(17);
			response.writeByte(LoginConstants.STATUS_EXCHANGE_DATA);
			response.writeLong(0);
			response.writeLong(serverSeed);
			ctx.channel().writeAndFlush(response);

			setState(LoginDecoderState.LOGIN_HEADER);
		}
	}

	/**
	 * Decodes in the header state.
	 * 
	 * @param ctx The channel handler context.
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @return The frame, or {@code null}.
	 * @throws IOException If the login type sent by the client is invalid.
	 */
	private void decodeHeader(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws IOException {
		if (buffer.readableBytes() >= 2) {
			int loginType = buffer.readUnsignedByte();

			if (loginType != LoginConstants.TYPE_STANDARD && loginType != LoginConstants.TYPE_RECONNECTION) {
				throw new IOException("Invalid login type.");
			}

			reconnecting = loginType == LoginConstants.TYPE_RECONNECTION;
			loginLength = buffer.readUnsignedByte();

			setState(LoginDecoderState.LOGIN_PAYLOAD);
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
	private void decodePayload(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
		if (buffer.readableBytes() >= loginLength) {
			ByteBuf payload = buffer.readBytes(loginLength);
			int clientVersion = 255 - payload.readUnsignedByte();

			int releaseNumber = payload.readUnsignedShort();

			int lowMemoryFlag = payload.readUnsignedByte();
			if (lowMemoryFlag != 0 && lowMemoryFlag != 1) {
				throw new Exception("Invalid value for low memory flag.");
			}

			boolean lowMemory = lowMemoryFlag == 1;

			int[] archiveCrcs = new int[FileSystemConstants.ARCHIVE_COUNT];
			for (int i = 0; i < 9; i++) {
				archiveCrcs[i] = payload.readInt();
			}

			int securePayloadLength = payload.readUnsignedByte();
			if (securePayloadLength != loginLength - 41) {
				throw new Exception("Secure payload length mismatch.");
			}

			ByteBuf securePayload = payload.readBytes(securePayloadLength);

			BigInteger bigInteger = new BigInteger(securePayload.array());
			bigInteger = bigInteger.modPow(NetworkConstants.RSA_EXPONENT, NetworkConstants.RSA_MODULUS);

			securePayload = Unpooled.wrappedBuffer(bigInteger.toByteArray());

			int secureId = securePayload.readUnsignedByte();
			if (secureId != 10) {
				throw new Exception("Invalid secure payload id.");
			}

			long clientSeed = securePayload.readLong();
			long reportedServerSeed = securePayload.readLong();
			if (reportedServerSeed != serverSeed) {
				throw new Exception("Server seed mismatch.");
			}

			int uid = securePayload.readInt();

			String username = BufferUtil.readString(securePayload);
			String password = BufferUtil.readString(securePayload);

			if (username.length() > 12 || password.length() > 20) {
				throw new Exception("Username or password too long.");
			}

			int[] seed = new int[4];
			seed[0] = (int) (clientSeed >> 32);
			seed[1] = (int) clientSeed;
			seed[2] = (int) (serverSeed >> 32);
			seed[3] = (int) serverSeed;

			IsaacRandom decodingRandom = new IsaacRandom(seed);
			for (int i = 0; i < seed.length; i++) {
				seed[i] += 50;
			}

			IsaacRandom encodingRandom = new IsaacRandom(seed);

			PlayerCredentials credentials = new PlayerCredentials(username, password, usernameHash, uid);
			IsaacRandomPair randomPair = new IsaacRandomPair(encodingRandom, decodingRandom);

			LoginRequest request = new LoginRequest(credentials, randomPair, reconnecting, lowMemory, releaseNumber,
					archiveCrcs, clientVersion);

			out.add(request);
			if (buffer.isReadable()) {
				out.add(buffer.readBytes(buffer.readableBytes()));
			}
		}
	}

}