package org.apollo.net.release.r377;

import java.util.Map;

import org.apollo.game.message.impl.GroupedRegionUpdateMessage;
import org.apollo.game.message.impl.RegionUpdateMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

/**
 * A {@link MessageEncoder} for the {@link GroupedRegionUpdateMessage}.
 *
 * @author Major
 */
public final class GroupedRegionUpdateMessageEncoder extends MessageEncoder<GroupedRegionUpdateMessage> {

	/**
	 * The Map of RegionUpdateMessages to MessageEncoders.
	 */
	private final Map<Class<? extends RegionUpdateMessage>, MessageEncoder<? extends RegionUpdateMessage>> encoders;

	/**
	 * Creates the GroupedRegionUpdateMessageEncoder.
	 *
	 * @param encoders The Map of RegionUpdateMessages to MessageEncoders.
	 */
	public GroupedRegionUpdateMessageEncoder(Map<Class<? extends RegionUpdateMessage>, MessageEncoder<? extends RegionUpdateMessage>> encoders) {
		this.encoders = ImmutableMap.copyOf(encoders);
	}

	@Override
	public GamePacket encode(GroupedRegionUpdateMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(183, PacketType.VARIABLE_SHORT);
		Position base = message.getLastKnownRegion(), region = message.getRegionPosition();

		builder.put(DataType.BYTE, region.getLocalX(base));
		builder.put(DataType.BYTE, DataTransformation.ADD, region.getLocalY(base));

		for (RegionUpdateMessage update : message.getMessages()) {
			@SuppressWarnings("unchecked")
			MessageEncoder<RegionUpdateMessage> encoder = (MessageEncoder<RegionUpdateMessage>) encoders.get(update);

			Preconditions.checkState(encoder != null, update.getClass() + " does not have a registered encoder in GroupedRegionUpdateMessageEncoder.");

			GamePacket packet = encoder.encode(update);
			builder.put(DataType.BYTE, packet.getOpcode());
			builder.putBytes(packet.getPayload());
		}

		return builder.toGamePacket();
	}

}