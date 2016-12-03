package org.apollo.game.release.r317;

import org.apollo.game.message.impl.ReportAbuseMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

public final class ReportAbuseMessageDecoder extends MessageDecoder<ReportAbuseMessage> {

	@Override
	public ReportAbuseMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		long reported = reader.getSigned(DataType.LONG);
		int rule = (int) reader.getUnsigned(DataType.BYTE);
		boolean mute = reader.getUnsigned(DataType.BYTE) == 1;
		return new ReportAbuseMessage(reported, rule, mute);
	}

}
