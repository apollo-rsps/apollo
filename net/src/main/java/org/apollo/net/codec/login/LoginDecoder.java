package org.apollo.net.codec.login;

import com.google.common.net.InetAddresses;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apollo.net.NetworkConstants;
import org.apollo.util.BufferUtil;
import org.apollo.util.StatefulFrameDecoder;
import org.apollo.util.security.IsaacRandom;
import org.apollo.util.security.IsaacRandomPair;
import org.apollo.util.security.PlayerCredentials;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.List;
import java.util.logging.Logger;

/**
 * A {@link StatefulFrameDecoder} which decodes the login request frames.
 *
 * @author Graham
 */
public final class LoginDecoder extends StatefulFrameDecoder<LoginDecoderState> {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(LoginDecoder.class.getName());

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
		super(LoginDecoderState.LOGIN_HEADER);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out, LoginDecoderState state) {
		switch (state) {
			case LOGIN_HEADER:
				decodeHeader(ctx, in, out);
				break;
			case LOGIN_PAYLOAD:
				decodePayload(ctx, in, out);
				break;
			default:
				throw new IllegalStateException("Invalid login decoder state: " + state);
		}
	}

	/**
	 * Decodes in the header state.
	 *
	 * @param ctx    The channel handler context.
	 * @param buffer The buffer.
	 * @param out    The {@link List} of objects to pass forward through the pipeline.
	 */
	private void decodeHeader(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
		if (buffer.readableBytes() >= Byte.BYTES + Integer.BYTES + Integer.BYTES) {
			int type = buffer.readUnsignedByte();

			if (type != LoginConstants.TYPE_STANDARD && type != LoginConstants.TYPE_RECONNECTION) {
				logger.fine("Failed to decode login header.");
				writeResponseCode(ctx, LoginConstants.STATUS_LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			loginLength = buffer.readUnsignedShort();
			reconnecting = type == LoginConstants.TYPE_RECONNECTION;

			setState(LoginDecoderState.LOGIN_PAYLOAD);
		}
	}

	/**
	 * Decodes in the payload state.
	 *
	 * @param ctx    The channel handler context.
	 * @param buffer The buffer.
	 * @param out    The {@link List} of objects to pass forward through the pipeline.
	 */
	private void decodePayload(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
		if (buffer.readableBytes() >= loginLength) {
			ByteBuf payload = buffer.readBytes(loginLength);

			long release = payload.readUnsignedInt();
			long version = payload.readUnsignedInt();

			int memoryStatus = payload.readUnsignedByte();
			if (memoryStatus != 0 && memoryStatus != 1) {
				logger.fine("Login memoryStatus (" + memoryStatus + ") not in expected range of [0, 1].");
				writeResponseCode(ctx, LoginConstants.STATUS_LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			boolean lowMemory = memoryStatus == 1;


			int length = payload.readUnsignedByte();
			if (length != loginLength - 41) {
				logger.fine("Login packet unexpected length (" + length + ")");
				writeResponseCode(ctx, LoginConstants.STATUS_LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			ByteBuf secure = payload.readBytes(length);

			BigInteger value = new BigInteger(secure.array());
			value = value.modPow(NetworkConstants.RSA_EXPONENT, NetworkConstants.RSA_MODULUS);
			secure = Unpooled.wrappedBuffer(value.toByteArray());

			int id = secure.readUnsignedByte();
			if (id != 1) {
				logger.fine("Unable to read id from secure payload.");
				writeResponseCode(ctx, LoginConstants.STATUS_LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			int[] rounds = new int[4];
			for (int index = 0; index < rounds.length; index++) {
				rounds[index] = secure.readInt();
			}

			long clientSeed = secure.readLong();

			int[] seed = new int[4];
			seed[0] = (int) (clientSeed >> 32);
			seed[1] = (int) clientSeed;
			seed[2] = (int) (serverSeed >> 32);
			seed[3] = (int) serverSeed;

			long reportedSeed = secure.readLong();
			if (reportedSeed != serverSeed) {
				logger.fine("Reported seed differed from server seed.");
				writeResponseCode(ctx, LoginConstants.STATUS_LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			int uid = secure.readInt();
			String username = BufferUtil.readString(secure);
			String password = BufferUtil.readString(secure);
			InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
			String hostAddress = InetAddresses.toAddrString(socketAddress.getAddress());

			if (password.length() < 6 || password.length() > 20 || username.isEmpty() || username.length() > 12) {
				logger.fine("Username ('" + username + "') or password did not pass validation.");
				writeResponseCode(ctx, LoginConstants.STATUS_INVALID_CREDENTIALS);
				return;
			}

			int[] crcs = new int[25];
			for (int index = 0; index < 9; index++) {
				crcs[index] = payload.readInt();
			}


			IsaacRandom decodingRandom = new IsaacRandom(seed);
			for (int index = 0; index < seed.length; index++) {
				seed[index] += 50;
			}

			IsaacRandom encodingRandom = new IsaacRandom(seed);

			PlayerCredentials credentials = new PlayerCredentials(username, password, usernameHash, uid, hostAddress);
			IsaacRandomPair randomPair = new IsaacRandomPair(encodingRandom, decodingRandom);

			out.add(new LoginRequest(credentials, randomPair, reconnecting, lowMemory, release, crcs, version));
		}
	}

	/**
	 * Writes a response code to the client and closes the current channel.
	 *
	 * @param ctx      The context of the channel handler.
	 * @param response The response code to write.
	 */
	private void writeResponseCode(ChannelHandlerContext ctx, int response) {
		ByteBuf buffer = ctx.alloc().buffer(Byte.BYTES);
		buffer.writeByte(response);
		ctx.writeAndFlush(buffer).addListener(ChannelFutureListener.CLOSE);
	}

}