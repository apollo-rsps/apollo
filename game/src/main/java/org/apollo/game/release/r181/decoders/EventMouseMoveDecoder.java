package org.apollo.game.release.r181.decoders;

import org.apollo.game.message.impl.decode.EventMouseMoveMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Khaled Abdeljaber
 */
public class EventMouseMoveDecoder extends MessageDecoder<EventMouseMoveMessage> {
	@Override
	public EventMouseMoveMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int length = packet.getLength();

		int count1 = (int) reader.getUnsigned(DataType.BYTE);
		int count2 = (int) reader.getUnsigned(DataType.BYTE);

		int dt = 0;
		int dx = 0xFFFF;
		int dy = 0xFFFF;

		if (length == Short.BYTES) {
			int packed = (int) reader.getSigned(DataType.BYTE);
			dy = packed & 0x3F;
			dx = (packed >> 6) & 0x3F;
			dt = (packed >> 12) & 0xF;
		} else if (length == Byte.BYTES + Short.BYTES) {
			dt = (int) reader.getSigned(DataType.BYTE);
			int packed = (int) reader.getSigned(DataType.SHORT);
			dy = packed & 0xFF;
			dx = (packed >> 8) & 0xFF;
		} else if (length == Byte.BYTES + Integer.BYTES) {
			dt = (int) reader.getSigned(DataType.BYTE);
			int packed = (int) reader.getSigned(DataType.INT);
			dx = packed & 0xFF;
			dy = (packed >> 16) & 0xFF;
		} else if (length == Short.BYTES + Integer.BYTES) {
			dt = (int) reader.getSigned(DataType.SHORT);
			int packed = (int) reader.getSigned(DataType.INT);
			dx = packed & 0xFF;
			dy = (packed >> 16) & 0xFF;
		} else {
			//throw new IllegalStateException("Length of " + length + " condition has been accessed.");
		}

		//System.out.println(count1 + " " + " " + count2 + " " + dt + " " + dx + " " + dy);


		/**
		 * 				if (dt < 8 && dx >= -32 && dx <= 31 && dy >= -32 && dy <= 31) {
		 * 						dx += 32;
		 * 						dy += 32;
		 *
		 * 						frame.payload.writeShort(dy + (dx << 6) + (dt << 12));
		 *            } else if (dt < 32 && dx >= -128 && dx <= 127 && dy >= -128 && dy <= 127) {
		 * 						dx += 128;
		 * 						dy += 128;
		 *
		 * 						frame.payload.writeByte(dt + 128);
		 * 						frame.payload.writeShort(dy + (dx << 8));
		 *                    } else if (dt < 32) {
		 * 						frame.payload.writeByte(192 + dt);
		 *
		 * 						if (-1 == x || -1 == y) {
		 * 							frame.payload.writeInt(-2147483648);
		 *                        } else {
		 * 							frame.payload.writeInt(x | y << 16);
		 *                        }
		 *                    } else {
		 * 						frame.payload.writeShort((dt & 0x1fff) + 57344);
		 * 						if (-1 == x || -1 == y) {
		 * 							frame.payload.writeInt(-2147483648);
		 *                        } else {
		 * 							frame.payload.writeInt(x | y << 16);
		 *                        }
		 *                    }
		 */

		return new EventMouseMoveMessage(dx, dy, dt);
	}
}
