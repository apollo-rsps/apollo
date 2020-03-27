package org.apollo.game.release.r181;

import org.apollo.game.message.impl.*;
import org.apollo.game.message.impl.encode.*;
import org.apollo.game.release.r181.decoders.*;
import org.apollo.game.release.r181.decoders.map.MapRebuildCompleteDecoder;
import org.apollo.game.release.r181.encoders.game.LogoutEncoder;
import org.apollo.game.release.r181.encoders.region.MobResetAnimsEncoder;
import org.apollo.game.release.r181.decoders.map.WalkMessageDecoder;
import org.apollo.game.release.r181.decoders.map.item.*;
import org.apollo.game.release.r181.decoders.map.npc.actions.*;
import org.apollo.game.release.r181.decoders.map.obj.*;
import org.apollo.game.release.r181.decoders.map.player.PlayerDesignMessageDecoder;
import org.apollo.game.release.r181.decoders.map.player.ReportAbuseMessageDecoder;
import org.apollo.game.release.r181.decoders.map.player.actions.*;
import org.apollo.game.release.r181.decoders.social.PrivacyOptionMessageDecoder;
import org.apollo.game.release.r181.decoders.social.PrivateChatMessageDecoder;
import org.apollo.game.release.r181.decoders.social.friends.AddFriendMessageDecoder;
import org.apollo.game.release.r181.decoders.social.friends.RemoveFriendMessageDecoder;
import org.apollo.game.release.r181.decoders.social.ignores.AddIgnoreMessageDecoder;
import org.apollo.game.release.r181.decoders.social.ignores.RemoveIgnoreMessageDecoder;
import org.apollo.game.release.r181.decoders.ui.ClosedInterfaceMessageDecoder;
import org.apollo.game.release.r181.decoders.ui.DisplayStatusMessageDecoder;
import org.apollo.game.release.r181.decoders.ui.EnteredAmountMessageDecoder;
import org.apollo.game.release.r181.decoders.ui.if1.*;
import org.apollo.game.release.r181.decoders.ui.if3.If3ActionMessageDecoder;
import org.apollo.game.release.r181.decoders.ui.opheld.*;
import org.apollo.game.release.r181.encoders.UpdateRunEnergyMessageEncoder;
import org.apollo.game.release.r181.encoders.UpdateSkillMessageEncoder;
import org.apollo.game.release.r181.encoders.UpdateWeightMessageEncoder;
import org.apollo.game.release.r181.encoders.game.ServerChatMessageEncoder;
import org.apollo.game.release.r181.encoders.npc.NpcSynchronizationMessageEncoder;
import org.apollo.game.release.r181.encoders.player.SetPlayerActionMessageEncoder;
import org.apollo.game.release.r181.encoders.region.RebuildNormalMessageEncoder;
import org.apollo.game.release.r181.encoders.region.SetMapFlagEncoder;
import org.apollo.game.release.r181.encoders.ui.*;
import org.apollo.game.release.r181.encoders.ui.container.UpdateContainerFullMessageEncoder;
import org.apollo.game.release.r181.encoders.ui.container.UpdateContainerPartialMessageEncoder;
import org.apollo.net.meta.PacketMetaDataGroup;
import org.apollo.net.release.Release;

public class Release181 extends Release {

	private static final int[] PACKET_LENGTHS = new int[256];

	static {
		PACKET_LENGTHS[0] = 8;
		PACKET_LENGTHS[1] = 3;
		PACKET_LENGTHS[2] = 14;
		PACKET_LENGTHS[3] = 7;
		PACKET_LENGTHS[4] = -1;
		PACKET_LENGTHS[5] = 7;
		PACKET_LENGTHS[6] = 7;
		PACKET_LENGTHS[7] = 8;
		PACKET_LENGTHS[8] = -1;
		PACKET_LENGTHS[9] = 4;
		PACKET_LENGTHS[10] = 7;
		PACKET_LENGTHS[11] = 10;
		PACKET_LENGTHS[12] = 13;
		PACKET_LENGTHS[13] = -1;
		PACKET_LENGTHS[14] = 8;
		PACKET_LENGTHS[15] = 3;
		PACKET_LENGTHS[16] = -2;
		PACKET_LENGTHS[17] = 4;
		PACKET_LENGTHS[18] = 15;
		PACKET_LENGTHS[19] = 8;
		PACKET_LENGTHS[20] = 0;
		PACKET_LENGTHS[21] = 8;
		PACKET_LENGTHS[22] = 0;
		PACKET_LENGTHS[23] = 11;
		PACKET_LENGTHS[24] = 2;
		PACKET_LENGTHS[25] = -2;
		PACKET_LENGTHS[26] = 8;
		PACKET_LENGTHS[27] = 3;
		PACKET_LENGTHS[28] = -1;
		PACKET_LENGTHS[29] = 7;
		PACKET_LENGTHS[30] = 2;
		PACKET_LENGTHS[31] = 3;
		PACKET_LENGTHS[32] = 8;
		PACKET_LENGTHS[33] = 3;
		PACKET_LENGTHS[34] = -1;
		PACKET_LENGTHS[35] = 5;
		PACKET_LENGTHS[36] = 2;
		PACKET_LENGTHS[37] = 0;
		PACKET_LENGTHS[38] = -1;
		PACKET_LENGTHS[39] = 4;
		PACKET_LENGTHS[40] = 8;
		PACKET_LENGTHS[41] = 6;
		PACKET_LENGTHS[42] = 2;
		PACKET_LENGTHS[43] = -1;
		PACKET_LENGTHS[44] = 15;
		PACKET_LENGTHS[45] = 4;
		PACKET_LENGTHS[46] = 8;
		PACKET_LENGTHS[47] = 3;
		PACKET_LENGTHS[48] = 8;
		PACKET_LENGTHS[49] = 0;
		PACKET_LENGTHS[50] = 3;
		PACKET_LENGTHS[51] = 7;
		PACKET_LENGTHS[52] = -1;
		PACKET_LENGTHS[53] = 13;
		PACKET_LENGTHS[54] = -1;
		PACKET_LENGTHS[55] = 9;
		PACKET_LENGTHS[56] = 3;
		PACKET_LENGTHS[57] = 16;
		PACKET_LENGTHS[58] = 8;
		PACKET_LENGTHS[59] = 3;
		PACKET_LENGTHS[60] = -1;
		PACKET_LENGTHS[61] = 9;
		PACKET_LENGTHS[62] = 3;
		PACKET_LENGTHS[63] = 16;
		PACKET_LENGTHS[64] = 8;
		PACKET_LENGTHS[65] = 8;
		PACKET_LENGTHS[66] = 8;
		PACKET_LENGTHS[67] = -2;
		PACKET_LENGTHS[68] = 8;
		PACKET_LENGTHS[69] = 4;
		PACKET_LENGTHS[70] = 6;
		PACKET_LENGTHS[71] = 3;
		PACKET_LENGTHS[72] = 8;
		PACKET_LENGTHS[73] = 1;
		PACKET_LENGTHS[74] = 7;
		PACKET_LENGTHS[75] = 3;
		PACKET_LENGTHS[76] = 0;
		PACKET_LENGTHS[77] = -1;
		PACKET_LENGTHS[78] = -1;
		PACKET_LENGTHS[79] = 7;
		PACKET_LENGTHS[80] = 9;
		PACKET_LENGTHS[81] = 11;
		PACKET_LENGTHS[82] = 16;
		PACKET_LENGTHS[83] = 3;
		PACKET_LENGTHS[84] = 8;
		PACKET_LENGTHS[85] = 8;
		PACKET_LENGTHS[86] = 9;
		PACKET_LENGTHS[87] = 8;
		PACKET_LENGTHS[88] = -1;
		PACKET_LENGTHS[89] = 7;
		PACKET_LENGTHS[90] = -1;
		PACKET_LENGTHS[91] = -1;
		PACKET_LENGTHS[92] = 13;
		PACKET_LENGTHS[93] = 3;
		PACKET_LENGTHS[94] = 7;
		PACKET_LENGTHS[95] = -1;
		PACKET_LENGTHS[96] = -1;
		PACKET_LENGTHS[97] = -1;
		PACKET_LENGTHS[98] = 8;
		PACKET_LENGTHS[99] = 7;
	}

	/**
	 * Creates the release.
	 */
	public Release181() {
		super(181, PacketMetaDataGroup.createFromArray(PACKET_LENGTHS));
		init();
	}

	private void init() {
		/**
		 * Server
		 */

		register(LogoutMessage.class, new LogoutEncoder());

		register(SetPlayerActionMessage.class, new SetPlayerActionMessageEncoder());
		register(SetMapFlagMessage.class, new SetMapFlagEncoder());

		register(MobResetAnimsMessage.class, new MobResetAnimsEncoder());

		register(IfOpenTopMessage.class, new IfOpenTopMessageEncoder());
		register(IfOpenSubMessage.class, new IfOpenSubMessageEncoder());
		register(IfMoveSubMessage.class, new IfMoveSubMessageEncoder());
		register(IfSetEventMessage.class, new IfSetEventMessageEncoder());
		register(IfClearItemsMessage.class, new IfClearItemsEncoder());
		register(IfSetAngleMessage.class, new IfSetAngleEncoder());
		register(IfSetAnimMessage.class, new IfSetAnimEncoder());
		register(IfSetColourMessage.class, new IfSetColourEncoder());
		register(IfSetHideMessage.class, new IfSetHiddenEncoder());
		register(IfSetItemMessage.class, new IfSetItemEncoder());
		register(IfSetModelMessage.class, new IfSetModelEncoder());
		register(IfSetModelRotateMessage.class, new IfSetModelRotateEncoder());
		register(IfSetNpcHeadMessage.class, new IfSetNpcHeadEncoder());
		register(IfSetPlayerHeadMessage.class, new IfSetPlayerHeadEncoder());
		register(IfSetPositionMessage.class, new IfSetPositionEncoder());
		register(IfSetScrollPosMessage.class, new IfSetScrollPosEncoder());
		register(IfSetTextMessage.class, new IfSetTextEncoder());
		register(RunClientScriptMessage.class, new RunClientScriptEncoder());

		register(UpdateInventoryPartialMessage.class, new UpdateContainerPartialMessageEncoder());
		register(UpdateInventoryFullMessage.class, new UpdateContainerFullMessageEncoder());

		register(UpdateWeightMessage.class, new UpdateWeightMessageEncoder());
		register(UpdateSkillMessage.class, new UpdateSkillMessageEncoder());
		register(UpdateRunEnergyMessage.class, new UpdateRunEnergyMessageEncoder());

		register(RebuildNormalMessage.class, new RebuildNormalMessageEncoder());
		register(NpcSynchronizationMessage.class, new NpcSynchronizationMessageEncoder());

		register(ConfigMessage.class, new ConfigMessageEncoder());
		register(ServerChatMessage.class, new ServerChatMessageEncoder());

		/**
		 * Client
		 */

		/**
		 * Friends List
		 */
		register(88, new AddFriendMessageDecoder());
		register(54, new RemoveFriendMessageDecoder());

		/**
		 * Ignores List
		 */
		register(90, new AddIgnoreMessageDecoder());
		register(28, new RemoveIgnoreMessageDecoder());

		/**
		 * Private Message
		 */
		register(25, new PrivateChatMessageDecoder());
		register(15, new PrivacyOptionMessageDecoder());

		/**
		 * Movement
		 */
		register(52, new WalkMessageDecoder());
		register(96, new WalkMessageDecoder());

		/**
		 * Camera
		 */
		register(39, new ArrowKeyMessageDecoder());

		/**
		 * Interface
		 */
		register(35, new DisplayStatusMessageDecoder());
		register(20, new ClosedInterfaceMessageDecoder());

		final var if3 = new int[]{68, 21, 48, 19, 40, 66, 85, 14, 84, 0};
		for (int index = 0; index < if3.length; index++) {
			register(if3[index], new If3ActionMessageDecoder(index + 1));
		}

		register(46, new FirstIf1ActionMessageDecoder());
		register(26, new SecondIf1ActionMessageDecoder());
		register(65, new ThirdIf1ActionMessageDecoder());
		register(64, new FourthIf1ActionMessageDecoder());
		register(32, new FifthIf1ActionMessageDecoder());

		register(87, new FirstOpHeldMessageDecoder());
		register(98, new SecondOpHeldMessageDecoder());
		register(72, new ThirdOpHeldMessageDecoder());
		register(7, new FourthOpHeldMessageDecoder());
		register(58, new FifthOpHeldMessageDecoder());
		register(24, new SixthOpHeldMessageDecoder());


		register(5, new FirstOpItemMessageDecoder());
		register(74, new SecondOpItemMessageDecoder());
		register(6, new ThirdOpItemMessageDecoder());
		register(29, new FourthOpItemMessageDecoder());
		register(51, new FifthOpItemMessageDecoder());

		/**
		 * Resume Inputs
		 */
		register(17, new EnteredAmountMessageDecoder());

		/**
		 * Player
		 */
		register(47, new FirstPlayerActionMessageDecoder());
		register(56, new SecondPlayerActionMessageDecoder());
		register(62, new ThirdPlayerActionMessageDecoder());
		register(27, new FourthPlayerActionMessageDecoder());
		register(83, new FifthPlayerActionMessageDecoder());
		register(93, new SixthPlayerActionMessageDecoder());
		register(75, new SeventhPlayerActionMessageDecoder());
		register(50, new EightPlayerActionMessageDecoder());

		register(12, new PlayerDesignMessageDecoder());

		/**
		 * NPC
		 */
		register(71, new FirstNpcActionMessageDecoder());
		register(1, new SecondNpcActionMessageDecoder());
		register(33, new ThirdNpcActionMessageDecoder());
		register(59, new FourthNpcActionMessageDecoder());
		register(31, new FifthNpcActionMessageDecoder());
		register(30, new SixthNpcActionMessageDecoder());

		/**
		 * Objects
		 */
		register(10, new FirstObjectActionMessageDecoder());
		register(79, new SecondObjectActionMessageDecoder());
		register(89, new ThirdObjectActionMessageDecoder());
		register(3, new FourthObjectActionMessageDecoder());
		register(94, new FifthObjectActionMessageDecoder());
		register(36, new SixthObjectActionMessageDecoder());

		/**
		 * Floor Items
		 */

		register(38, new ReportAbuseMessageDecoder());
		register(73, new FocusUpdateMessageDecoder());
		register(60, new CommandMessageDecoder());
		register(41, new MouseClickedMessageDecoder());
		register(22, new KeepAliveMessageDecoder());

		/**
		 * Map
		 */
		register(76, new MapRebuildCompleteDecoder());

		/**
		 * Misc
		 */
		{
			//TODO these if they are relevant.
			register(34, new SpamPacketMessageDecoder()); // register(34, new EventMouseMoveDecoder());
			register(4, new SpamPacketMessageDecoder()); // register(34, new LoginInfoMessageDecoder());
		}
		register(49, new KeepAliveMessageDecoder());


		//TODO register completed client prot here.
	}
}
