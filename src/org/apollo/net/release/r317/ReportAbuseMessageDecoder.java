package org.apollo.net.release.r317;

import org.apollo.game.message.impl.ReportAbuseMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;
import org.apollo.util.NameUtil;

/**
 * A {@link MessageDecoder} for the {@link ReportAbuseMessage}.
 * 
 * @author Lmctruck30
 */
public class ReportAbuseMessageDecoder extends MessageDecoder<ReportAbuseMessage> {

	@Override
	public ReportAbuseMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		String username = NameUtil.decodeBase37(reader.getSigned(DataType.LONG));
		byte rule = (byte) reader.getUnsigned(DataType.BYTE);
		boolean mute = reader.getUnsigned(DataType.BYTE) == 1;
		return new ReportAbuseMessage(username, rule, mute);
	}

}
