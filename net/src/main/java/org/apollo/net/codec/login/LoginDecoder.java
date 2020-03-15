package org.apollo.net.codec.login;

import com.google.common.net.InetAddresses;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apollo.net.NetworkConstants;
import org.apollo.util.BufferUtil;
import org.apollo.util.StatefulFrameDecoder;
import org.apollo.util.XteaUtil;
import org.apollo.util.security.IsaacRandom;
import org.apollo.util.security.IsaacRandomPair;
import org.apollo.util.security.PlayerCredentials;
import org.apollo.util.security.UserStats;

import java.math.BigInteger;
import java.net.InetSocketAddress;
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
		if (!buffer.isReadable(Byte.BYTES + Short.BYTES)) {
			return;
		}

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

	/**
	 * Decodes in the payload state.
	 *
	 * @param ctx    The channel handler context.
	 * @param buffer The buffer.
	 * @param out    The {@link List} of objects to pass forward through the pipeline.
	 */
	private void decodePayload(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
		if (!buffer.isReadable(loginLength)) {
			return;
		}

		final var payload = buffer.readBytes(loginLength);
		final var release = payload.readUnsignedInt();
		final var version = payload.readUnsignedInt();
		final var clientType = payload.readUnsignedByte() & 0x3; // 0 desktop, 1 android, 2 ios

		var secure = payload.readBytes(payload.readUnsignedShort());
		final var rsa = new BigInteger(
				ByteBufUtil.getBytes(secure, secure.readerIndex(), secure.readableBytes(), false))
				.modPow(NetworkConstants.RSA_EXPONENT, NetworkConstants.RSA_MODULUS);
		secure = Unpooled.wrappedBuffer(rsa.toByteArray());

		final var secureCheck = secure.readUnsignedByte();
		if (secureCheck != 1) {
			logger.fine("Login secureCheck (" + secureCheck + ") is not expected value of 1.");
			writeResponseCode(ctx, LoginConstants.STATUS_LOGIN_SERVER_REJECTED_SESSION);
			return;
		}

		final var seed = new int[4];
		for (int index = 0; index < seed.length; index++) {
			seed[index] = secure.readInt();
		}

		final var serverSessionKey = secure.readLong();

		final short authType;
		final int authCode;
		final String password;
		final var previousSeed = new int[4];

		if (reconnecting) {
			for (int index = 0; index < previousSeed.length; index++) {
				previousSeed[index] = secure.readInt();
			}

			authType = -1;
			authCode = -1;
			password = "";
		} else {
			authType = secure.readUnsignedByte();
			if (authType == 1) { // Authenticator Related = 1
				authCode = secure.readInt();
			} else if (authType == 0 || authType == 2) {//Authenticator Related = 2, TrustedPC = 0
				authCode = secure.readUnsignedMedium();
				secure.skipBytes(Byte.BYTES);
			} else { //Regular Login
				authCode = secure.readInt();
			}

			secure.skipBytes(Byte.BYTES);
			password = BufferUtil.readString(secure);
		}

		XteaUtil.decipher(payload, payload.readerIndex(), loginLength, seed);

		final var username = BufferUtil.readString(payload);
		if (password.length() < 4 || password.length() > 20 || username.isEmpty() || username.length() > 12) {
			logger.fine("Username ('" + username + "') or password did not pass validation.");
			writeResponseCode(ctx, LoginConstants.STATUS_INVALID_CREDENTIALS);
			return;
		}

		final var clientProperties = payload.readUnsignedByte();
		final var lowMemory = (clientProperties & 0x1) == 1;
		final var displayMode = clientProperties >> 1;
		final var frameWidth = payload.readShort();
		final var frameHeight = payload.readShort();

		final var randFileContents = new byte[24];
		payload.readBytes(randFileContents);

		final var areaKey = BufferUtil.readString(payload); // TODO this is known.
		payload.readInt(); // Affiliate

		final var stats = new UserStats(payload);
		if (!stats.isValid()) {
			logger.fine("User statistics version mismatched.");
			writeResponseCode(ctx, LoginConstants.STATUS_GAME_UPDATED);
			return;
		}

		var test2 = payload.readUnsignedByte(); // The client type again?
		var test = payload.readInt(); // This is always zero.

		System.out.println(payload.readerIndex() + " " + loginLength);
		int[] crcs = new int[21];
		for (int index = 0; index < crcs.length; index++) {
			crcs[index] = payload.readInt();
		}

		IsaacRandom decodingRandom = new IsaacRandom(seed);
		for (int index = 0; index < seed.length; index++) {
			seed[index] += 50;
		}
		IsaacRandom encodingRandom = new IsaacRandom(seed);

		InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		String hostAddress = InetAddresses.toAddrString(socketAddress.getAddress());

		PlayerCredentials credentials = new PlayerCredentials(username, password, usernameHash, stats, hostAddress);
		IsaacRandomPair randomPair = new IsaacRandomPair(encodingRandom, decodingRandom);

		out.add(new LoginRequest(credentials, randomPair, reconnecting, lowMemory, release, crcs, version, displayMode));
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