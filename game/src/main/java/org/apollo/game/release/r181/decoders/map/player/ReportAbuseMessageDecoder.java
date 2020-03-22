package org.apollo.game.release.r181.decoders.map.player;

import org.apollo.game.message.impl.ReportAbuseMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link ReportAbuseMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class ReportAbuseMessageDecoder extends MessageDecoder<ReportAbuseMessage> {

	@Override
	public ReportAbuseMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int length = (int) reader.getUnsigned(DataType.BYTE); // goes name + 1 + 2 so assuming the length of the body of the msg?
		String name = reader.getString();
		int rule = (int) reader.getUnsigned(DataType.BYTE);
		boolean mute = reader.getUnsigned(DataType.BYTE) == 1;

		return new ReportAbuseMessage(name, rule, mute);
	}

}
