package org.apollo.game.release.r377;

import org.apollo.game.message.impl.ReportAbuseMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;
import org.apollo.util.NameUtil;

/**
 * A {@link MessageDecoder} for the {@link ReportAbuseMessage}.
 *
 * @author Major
 */
public final class ReportAbuseMessageDecoder extends MessageDecoder<ReportAbuseMessage> {

	@Override
	public ReportAbuseMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		String name = NameUtil.decodeBase37(reader.getSigned(DataType.LONG));
		int rule = (int) reader.getUnsigned(DataType.BYTE);
		boolean mute = reader.getUnsigned(DataType.BYTE) == 1;

		return new ReportAbuseMessage(name, rule, mute);
	}

}
